/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.BackendControllerHolder;

/**
 * A specialized thread dedicated to executing asynchronous actions. It is able
 * to record the action progress.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AsyncActionExecutor extends Thread {

  private IAction                   action;
  private Map<String, Object>       context;
  private AbstractBackendController slaveBackendController;

  private Date                      startedTimestamp;
  private double                    progress;

  /**
   * Constructs a new {@code AsyncActionExecutor} instance.
   *
   * @param action
   *          the action to execute asynchronously.
   * @param context
   *          the action context.
   * @param group
   *          the thread group.
   * @param slaveBackendController
   *          the slave backend controller used to execute the action.
   */
  public AsyncActionExecutor(IAction action, Map<String, Object> context,
      ThreadGroup group, AbstractBackendController slaveBackendController) {
    super(group, /* "Jspresso Asynchronous Action Runner " + */
    action.getClass().getSimpleName() + "["
        + slaveBackendController.getApplicationSession().getId() + "]["
        + slaveBackendController.getApplicationSession().getUsername() + "]");
    this.action = action;
    this.context = context;
    this.slaveBackendController = slaveBackendController;
  }

  /**
   * Runs the action.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void run() {
    startedTimestamp = new Date();
    // The following ill not store the slave backend controller in the HTTP
    // session since we are in a new thread.
    BackendControllerHolder.setThreadBackendController(slaveBackendController);

    // Clone the context to ensure the outer one is not modified.
    final Map<String, Object> slaveContext = new HashMap<String, Object>();
    slaveContext.putAll(context);
    // Ensure the action will be executing in the context of the slave
    // backend controller.
    slaveContext.putAll(slaveBackendController.getInitialActionContext());
    // Make sure front controller cannot be referenced.
    slaveContext.remove(ActionContextConstants.FRONT_CONTROLLER);

    try {
      if (action.isTransactional()) {
        slaveBackendController.executeTransactionally(action, slaveContext);
      } else {
        action.execute(slaveBackendController, slaveContext);
      }
    } finally {
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
   *          the progress to set.
   */
  public void setProgress(double progress) {
    this.progress = progress;
  }

  /**
   * Gets the startedTimestamp.
   * 
   * @return the startedTimestamp.
   */
  public Date getStartedTimestamp() {
    return startedTimestamp;
  }
}
