/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.async;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.action.Asynchronous;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.action.Transactional;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;
import org.jspresso.framework.util.bean.SingleWeakPropertyChangeSupport;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * A specialized thread dedicated to executing asynchronous actions. It is able
 * to record the action progress.
 *
 * @author Vincent Vandenschrick
 */
public class AsyncActionExecutor extends Thread implements IPropertyChangeCapable {

  private IAction                   action;
  private Map<String, Object>       context;
  private Map<String, Object>       progressionData;
  private AbstractBackendController slaveBackendController;

  private Date   startedTimestamp;
  private Date   endedTimestamp;
  private double progress;
  private String i18nName;
  private String status;

  private boolean stoppable   = false;
  private boolean cancellable = false;

  private static final String EXCEPTION_KEY            = "EXCEPTION_KEY";
  private static final String COMPLETED_CONTROLLER_KEY = "COMPLETED_CONTROLLER_KEY";

  private transient SinglePropertyChangeSupport     propertyChangeSupport;
  private transient SingleWeakPropertyChangeSupport weakPropertyChangeSupport;
  private transient List<PropertyChangeEvent>       delayedEvents;

  /**
   * Constructs a new {@code AsyncActionExecutor} instance.
   *
   * @param action
   *     the action to execute asynchronously.
   * @param context
   *     the action context.
   * @param group
   *     the thread group.
   * @param slaveBackendController
   *     the slave backend controller used to execute the action.
   */
  public AsyncActionExecutor(IAction action, Map<String, Object> context, ThreadGroup group,
                             AbstractBackendController slaveBackendController) {
    super(group, /* "Jspresso Asynchronous Action Runner " + */
        action.getClass().getSimpleName() + "[" + slaveBackendController.getApplicationSession().getId() + "]["
            + slaveBackendController.getApplicationSession().getUsername() + "]");
    this.action = action;
    this.context = context;
    this.slaveBackendController = slaveBackendController;
    this.progressionData = new HashMap<>();

    this.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (new HashSet<>(Arrays.asList("startedTimestamp", "endedTimestamp", "progress")).contains(propertyName)) {
          Long estimatedRemainingTime = getEstimatedRemainingTime();
          Long totalDuration = getTotalDuration();
          firePropertyChange("estimatedRemainingTime", null, estimatedRemainingTime);
          firePropertyChange("totalDuration", null, totalDuration);
        }
      }
    });
  }

  /**
   * Runs the action.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void run() {
    setStartedTimestamp(new Date());
    // The following will not store the slave backend controller in the HTTP
    // session since we are in a new thread.
    BackendControllerHolder.setThreadBackendController(slaveBackendController);

    // Clone the context to ensure the outer one is not modified.
    final Map<String, Object> slaveContext = new HashMap<>();
    slaveContext.putAll(context);
    // Ensure the action will be executing in the context of the slave
    // backend controller.
    slaveContext.putAll(slaveBackendController.getInitialActionContext());
    // Make sure front controller cannot be referenced.
    slaveContext.remove(ActionContextConstants.FRONT_CONTROLLER);

    try {
      if (action.getClass().getAnnotation(Asynchronous.class).autoMergeBackEntities()) {
        slaveBackendController.recordUowMergedEntities();
      }
      if (action.getClass().isAnnotationPresent(Transactional.class)) {
        slaveBackendController.executeTransactionally(action, slaveContext);
      } else {
        action.execute(slaveBackendController, slaveContext);
      }
      if (action.getClass().getAnnotation(Asynchronous.class).autoMergeBackEntities()) {
        Map<String, Object> mergeContext = new HashMap<>();
        mergeContext.put(COMPLETED_CONTROLLER_KEY, slaveBackendController);
        slaveBackendController.executeLater(new BackendAction() {
          @Override
          public boolean execute(IActionHandler actionHandler, Map<String, Object> innerContext) {
            AbstractBackendController completedController = (AbstractBackendController) innerContext.get(
                COMPLETED_CONTROLLER_KEY);
            AbstractBackendController mainController = (AbstractBackendController) getBackendController(innerContext);
            mainController.merge(completedController.getRecordedUowMergedEntitiesAndClear(), EMergeMode.MERGE_LAZY);
            return super.execute(actionHandler, innerContext);
          }
        }, mergeContext);
      }
    } catch (RuntimeException ex) {
      if (action.getClass().getAnnotation(Asynchronous.class).pushRuntimeExceptions()) {
        Map<String, Object> exceptionContext = new HashMap<>();
        exceptionContext.put(EXCEPTION_KEY, ex);
        slaveBackendController.executeLater(new BackendAction() {
          @Override
          public boolean execute(IActionHandler actionHandler, Map<String, Object> innerContext) {
            throw (RuntimeException) innerContext.get(EXCEPTION_KEY);
          }
        }, exceptionContext);
        status = "exception";
      }
    } finally {
      setEndedTimestamp(new Date());
      slaveBackendController.cleanupRequestResources();
      slaveBackendController.stop();
      BackendControllerHolder.setThreadBackendController(null);
      action = null;
      context = null;
      slaveBackendController = null;
    }
  }

  /**
   * Gets the progress.
   *
   * @return the progress.
   */
  public double getProgress() {
    return progress;
  }

  /**
   * Sets the progress.
   *
   * @param progress
   *     the progress to set.
   */
  public void setProgress(double progress) {
    double oldProgress = this.progress;
    this.progress = progress;
    firePropertyChange("progress", oldProgress, this.progress);
  }

  /**
   * Gets the startedTimestamp.
   *
   * @return the startedTimestamp.
   */
  public Date getStartedTimestamp() {
    return this.startedTimestamp;
  }

  /**
   * Sets started timestamp.
   *
   * @param startedTimestamp
   *     the started timestamp
   */
  public void setStartedTimestamp(Date startedTimestamp) {
    Date oldStartedTimestamp = this.startedTimestamp;
    this.startedTimestamp = startedTimestamp;
    firePropertyChange("startedTimestamp", oldStartedTimestamp, this.startedTimestamp);
  }

  /**
   * Gets ended timestamp.
   *
   * @return the ended timestamp
   */
  public Date getEndedTimestamp() {
    return endedTimestamp;
  }

  /**
   * Sets ended timestamp.
   *
   * @param endedTimestamp
   *     the ended timestamp
   */
  public void setEndedTimestamp(Date endedTimestamp) {
    Date oldEndedTimestamp = this.endedTimestamp;
    this.endedTimestamp = endedTimestamp;
    firePropertyChange("endedTimestamp", oldEndedTimestamp, this.endedTimestamp);
  }

  /**
   * Sets i18n name.
   *
   * @param i18nName
   *     the 18n name
   */
  public void setI18nName(String i18nName) {
    this.i18nName = i18nName;
  }

  /**
   * Gets i18n name.
   *
   * @return the i18n name if not null, otherwise return's thread's name.
   */
  public String getI18nName() {

    if (i18nName != null) {
      return i18nName;
    }

    return getName();
  }

  /**
   * Sets i18n status.
   *
   * @param status
   *     the i18n status
   */
  public void setStatus(String status) {
    String oldStatus = this.status;
    this.status = status;
    firePropertyChange("status", oldStatus, this.status);
  }

  /**
   * Gets i18n status.
   *
   * @return the i18n status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Is stoppable boolean.
   *
   * @return the boolean
   */
  public boolean isStoppable() {
    return stoppable;
  }

  /**
   * Sets stoppable.
   *
   * @param stoppable
   *     the stoppable
   */
  public void setStoppable(boolean stoppable) {
    boolean oldStoppable = this.stoppable;
    this.stoppable = stoppable;
    firePropertyChange("stoppable", oldStoppable, this.stoppable);
  }

  /**
   * Is cancellable boolean.
   *
   * @return the boolean
   */
  public boolean isCancellable() {
    return cancellable;
  }

  /**
   * Sets cancellable.
   *
   * @param cancellable
   *     the cancellable
   */
  public void setCancellable(boolean cancellable) {
    boolean oldCancellable = this.cancellable;
    this.cancellable = cancellable;
    firePropertyChange("cancellable", oldCancellable, this.cancellable);
  }

  /**
   * Gets progression data.
   *
   * @return the progression data
   */
  public Map<String, Object> getProgressionData() {
    return progressionData;
  }

  /**
   * Gets estimated remaining time asn string.
   *
   * @return the estimated remaining time
   */
  public Long getEstimatedRemainingTime() {

    Date startedTimestamp = getStartedTimestamp();
    if (startedTimestamp == null) {
      return null;
    }

    Date endedTimestamp = getEndedTimestamp();
    if (endedTimestamp != null) {
      return null;
    }

    double progress = getProgress();
    if (progress < 0.05d) {
      return null;
    }

    long elapsed = new Date().getTime() - startedTimestamp.getTime();
    double eta = elapsed * (1.0d - progress) / progress;
    if (eta < 1) {
      return null;
    }

    return new Double(eta).longValue();
  }

  /**
   * Gets estimated remaining time asn string.
   *
   * @return the estimated remaining time
   */
  public Long getTotalDuration() {

    Date startedTimestamp = getStartedTimestamp();
    if (startedTimestamp == null) {
      return null;
    }

    Date endedTimestamp = getEndedTimestamp();
    if (endedTimestamp == null) {
      return null;
    }

    long elapsed = endedTimestamp.getTime() - startedTimestamp.getTime();
    if (elapsed < 1) {
      return null;
    }

    return elapsed;
  }


  private synchronized void initializePropertyChangeSupportIfNeeded() {
    if (propertyChangeSupport == null) {
      this.propertyChangeSupport = new SinglePropertyChangeSupport(this);
    }
  }

  private synchronized void initializeWeakPropertyChangeSupportIfNeeded() {
    if (weakPropertyChangeSupport == null) {
      this.weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(PropertyChangeListener listener) {
    initializeWeakPropertyChangeSupportIfNeeded();
    weakPropertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    initializeWeakPropertyChangeSupportIfNeeded();
    weakPropertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AsyncActionExecutor clone() {
    try {
      AsyncActionExecutor clonedBean = (AsyncActionExecutor) super.clone();
      clonedBean.propertyChangeSupport = null;
      clonedBean.weakPropertyChangeSupport = null;
      clonedBean.delayedEvents = null;
      return clonedBean;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Performs property change firing.
   *
   * @param evt
   *     evt
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
   */

  protected void firePropertyChange(PropertyChangeEvent evt) {
    Object oldValue = evt.getOldValue();
    Object newValue = evt.getNewValue();
    if (oldValue == null && newValue == null || oldValue != null && oldValue.equals(newValue)) {
      return;
    }
    if (delayedEvents != null) {
      delayedEvents.add(evt);
    } else {
      if (propertyChangeSupport != null) {
        propertyChangeSupport.firePropertyChange(evt);
      }
      if (weakPropertyChangeSupport != null) {
        weakPropertyChangeSupport.firePropertyChange(evt);
      }
    }
  }

  /**
   * Performs property change firing.
   *
   * @param propertyName
   *     propertyName
   * @param oldValue
   *     oldValue
   * @param newValue
   *     newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, * java.lang.Object, java.lang.Object)
   */
  @Override
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Retrieves listeners.
   *
   * @return all of the {@code PropertyChangeListeners} added or an empty array
   * if no listeners have been added
   *
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  @Override
  public PropertyChangeListener[] getPropertyChangeListeners() {
    ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      for (PropertyChangeListener pcl : propertyChangeSupport.getPropertyChangeListeners()) {
        // do not add single property change listeners
        if (!(pcl instanceof PropertyChangeListenerProxy)) {
          listeners.add(pcl);
        }
      }
    }
    if (weakPropertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(weakPropertyChangeSupport.getPropertyChangeListeners()));
    }
    return listeners.toArray(new PropertyChangeListener[0]);
  }

  /**
   * Retrieves listeners.
   *
   * @param propertyName
   *     propertyName
   * @return all of the {@code PropertyChangeListeners} associated with the
   * named property. If no such listeners have been added, or if
   * {@code propertyName} is null, an empty array is returned.
   *
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(String)
   */
  @Override
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(propertyChangeSupport.getPropertyChangeListeners(propertyName)));
    }
    if (weakPropertyChangeSupport != null) {
      listeners.addAll(Arrays.asList(weakPropertyChangeSupport.getPropertyChangeListeners(propertyName)));
    }
    return listeners.toArray(new PropertyChangeListener[0]);
  }

  /**
   * Tests whether there are listeners for the property.
   *
   * @param propertyName
   *     propertyName
   * @return true if there are one or more listeners for the given property.
   *
   * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
   */
  @Override
  public boolean hasListeners(String propertyName) {
    if (propertyChangeSupport != null && propertyChangeSupport.hasListeners(propertyName)) {
      return true;
    }
    return weakPropertyChangeSupport != null && weakPropertyChangeSupport.hasListeners(propertyName);
  }

  /**
   * Delays events propagation by buffering them. When events are unblocked,
   * they get fired in the order they were recorded. {@inheritDoc}
   */
  @Override
  public boolean blockEvents() {
    if (delayedEvents == null) {
      delayedEvents = new ArrayList<>();
      return true;
    }
    return false;
  }

  /**
   * Unblocks event propagation. All events that were buffered are fired.
   * {@inheritDoc}
   */
  @Override
  public void releaseEvents() {
    if (delayedEvents != null) {
      List<PropertyChangeEvent> delayedEventsCopy = new ArrayList<>(delayedEvents);
      delayedEvents = null;
      for (PropertyChangeEvent evt : delayedEventsCopy) {
        firePropertyChange(evt);
      }
    }
  }
}
