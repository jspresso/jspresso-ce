/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.gate.GateHelper;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IModelGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Abstract base class for action factories.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual action class created.
 * @param <F>
 *          the actual component class the created actions are installed in.
 * @param <G>
 *          the actual icon class the created actions support.
 */
public abstract class AbstractActionFactory<E, F, G> implements
    IActionFactory<E, F> {

  private IIconFactory<G>      iconFactory;
  private ITranslationProvider translationProvider;

  /**
   * Creates and attach the necessary action gates.
   * 
   * @param action
   *          the displayable Jspresso action.
   * @param actionHandler
   *          the action handler.
   * @param modelDescriptor
   *          the model descriptor of the view.
   * @param viewConnector
   *          the view connector.
   * @param uiAction
   *          the created ui specific action.
   */
  protected void attachActionGates(IDisplayableAction action,
      IActionHandler actionHandler, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector, E uiAction) {
    if (action.getActionabilityGates() != null) {
      Collection<IGate> clonedGates = new HashSet<IGate>();
      for (IGate gate : action.getActionabilityGates()) {
        final IGate clonedGate = gate.clone();
        if (clonedGate instanceof ISubjectAware) {
          ((ISubjectAware) clonedGate).setSubject(actionHandler.getSubject());
        }
        if (clonedGate instanceof IModelGate) {
          if (modelDescriptor instanceof IComponentDescriptorProvider<?>) {
            viewConnector.addValueChangeListener(new IValueChangeListener() {

              public void valueChange(ValueChangeEvent evt) {
                ((IModelGate) clonedGate).setModel(evt.getNewValue());
              }
            });
          } else if (modelDescriptor instanceof ICollectionPropertyDescriptor<?>) {
            ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector().addValueChangeListener(
                    new IValueChangeListener() {

                      public void valueChange(ValueChangeEvent evt) {
                        ICollectionConnector collectionConnector = (ICollectionConnector) evt
                            .getSource();
                        if (collectionConnector.getModelConnector() != null) {
                          ((IModelGate) clonedGate)
                              .setModel(collectionConnector.getModelProvider()
                                  .getModel());
                        } else {
                          ((IModelGate) clonedGate).setModel(null);
                        }
                      }
                    });
          }
        }
        clonedGates.add(clonedGate);
      }
      new GatesListener(uiAction, clonedGates);
    }
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<G> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private final class GatesListener implements PropertyChangeListener {

    private E                 action;
    private Collection<IGate> gates;

    /**
     * Constructs a new <code>GatesListener</code> instance.
     * 
     * @param action
     *          the action to (de)activate based on gates state.
     * @param gates
     *          the gates that determine action state.
     */
    public GatesListener(E action, Collection<IGate> gates) {
      this.action = action;
      this.gates = gates;
      setActionEnabled(action, GateHelper.areGatesOpen(gates));
      for (IGate gate : gates) {
        gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, this);
      }
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(
        @SuppressWarnings("unused") PropertyChangeEvent evt) {
      setActionEnabled(action, GateHelper.areGatesOpen(gates));
    }
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  protected IIconFactory<G> getIconFactory() {
    return iconFactory;
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * Creates the initial action context.
   * 
   * @param actionHandler
   *          the action handler.
   * @param modelDescriptor
   *          the model descriptor.
   * @param sourceComponent
   *          the source component.
   * @param viewConnector
   *          the view connector.
   * @param actionCommand
   *          the action command.
   * @param actionWidget
   *          the widget this action was triggered from.
   * @return the initial action context.
   */
  public Map<String, Object> createActionContext(IActionHandler actionHandler,
      IModelDescriptor modelDescriptor, F sourceComponent,
      IValueConnector viewConnector, String actionCommand, F actionWidget) {
    Map<String, Object> actionContext = actionHandler.createEmptyContext();
    actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR, modelDescriptor);
    actionContext.put(ActionContextConstants.SOURCE_COMPONENT, sourceComponent);
    actionContext.put(ActionContextConstants.VIEW_CONNECTOR, viewConnector);
    if (viewConnector instanceof ICollectionConnectorProvider
        && ((ICollectionConnectorProvider) viewConnector)
            .getCollectionConnector() != null) {
      actionContext.put(ActionContextConstants.SELECTED_INDICES,
          ((ICollectionConnectorProvider) viewConnector)
              .getCollectionConnector().getSelectedIndices());
    }
    actionContext.put(ActionContextConstants.ACTION_COMMAND, actionCommand);
    actionContext.put(ActionContextConstants.ACTION_WIDGET, actionWidget);
    return actionContext;
  }
}
