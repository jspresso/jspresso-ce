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
package org.jspresso.framework.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.action.IActionHandlerAware;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.IModelChangeListener;
import org.jspresso.framework.model.ModelChangeEvent;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityHandlerAware;
import org.jspresso.framework.security.ISubjectAware;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.gate.GateHelper;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IModelGate;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Abstract base class for action factories.
 *
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

  private IIconFactory<G> iconFactory;

  private boolean liveDebugUI = false;

  /**
   * {@inheritDoc}
   */
  @Override
  public E createAction(IAction action, IActionHandler actionHandler,
      IView<F> view, Locale locale) {
    if (action == null) {
      return null;
    }
    return createAction(action, null, actionHandler, view, locale);
  }

  /**
   * Creates the initial action context.
   *
   * @param actionHandler
   *          the action handler.
   * @param view
   *          the view.
   * @param viewConnector
   *          the view connector.
   * @param actionCommand
   *          the action command.
   * @param actionWidget
   *          the widget this action was triggered from.
   * @return the initial action context.
   */
  @Override
  public Map<String, Object> createActionContext(IActionHandler actionHandler,
      IView<F> view, IValueConnector viewConnector, String actionCommand,
      F actionWidget) {
    Map<String, Object> actionContext = new HashMap<>();

    IModelDescriptor modelDescriptor = null;
    F sourceComponent = null;
    if (view != null) {
      if (view.getDescriptor() != null) {
        modelDescriptor = view.getDescriptor().getModelDescriptor();
      }
      sourceComponent = view.getPeer();
    }
    IValueConnector refinedViewConnector = viewConnector;
    if (modelDescriptor instanceof ICollectionDescriptor<?>) {
      refinedViewConnector = ((ICollectionConnectorProvider) viewConnector)
          .getCollectionConnector();
    }
    actionContext.put(ActionContextConstants.VIEW, view);
    actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR, modelDescriptor);
    actionContext.put(ActionContextConstants.SOURCE_COMPONENT, sourceComponent);
    actionContext.put(ActionContextConstants.VIEW_CONNECTOR,
        refinedViewConnector);
    actionContext.put(ActionContextConstants.ACTION_COMMAND, actionCommand);
    actionContext.put(ActionContextConstants.ACTION_WIDGET, actionWidget);
    return actionContext;
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
   * Creates and attach the necessary action gates.
   *
   * @param action
   *          the displayable Jspresso action.
   * @param actionHandler
   *          the action handler.
   * @param view
   *          the view.
   * @param uiAction
   *          the created ui specific action.
   */

  protected void attachActionGates(IDisplayableAction action,
      IActionHandler actionHandler, IView<F> view, E uiAction) {
    try {
      actionHandler.pushToSecurityContext(action);
      IModelDescriptor modelDescriptor = null;
      IValueConnector viewConnector = null;
      if (view != null) {
        if (view.getDescriptor() != null) {
          modelDescriptor = view.getDescriptor().getModelDescriptor();
        }
        viewConnector = view.getConnector();
      }
      Collection<IGate> actionabilityGates = action.getActionabilityGates();
      if (actionabilityGates != null) {
        Collection<IGate> clonedGates = new HashSet<>();
        for (IGate gate : actionabilityGates) {
          if (!(gate instanceof ISecurable)
              || actionHandler.isAccessGranted((ISecurable) gate)) {
            final IGate clonedGate = gate.clone();
            applyGateDependencyInjection(clonedGate, actionHandler);
            if (clonedGate instanceof IModelGate && viewConnector != null) {
              if (((IModelGate) clonedGate).isCollectionBased()) {
                if (/*
                     * modelDescriptor instanceof
                     * ICollectionPropertyDescriptor<?>
                     */viewConnector instanceof ICollectionConnectorProvider) {
                  ((IModelGate) clonedGate).setModel(null);
                  // tracks children connectors selection
                  ((ICollectionConnectorProvider) viewConnector)
                      .getCollectionConnector().addSelectionChangeListener(
                          new ISelectionChangeListener() {

                            @Override
                            public void selectionChange(SelectionChangeEvent evt) {
                              ICollectionConnector collConnector = (ICollectionConnector) evt
                                  .getSource();
                              int[] newSelection = evt.getNewSelection();
                              assignCollectionBasedGateModel(clonedGate,
                                  collConnector, newSelection);
                            }
                          });
                  // tracks selected children model change
                  ((ICollectionConnectorProvider) viewConnector)
                      .getCollectionConnector().addValueChangeListener(
                          new IValueChangeListener() {

                            @Override
                            public void valueChange(ValueChangeEvent evt) {
                              ICollectionConnector collConnector = (ICollectionConnector) evt
                                  .getSource();
                              int[] newSelection = collConnector
                                  .getSelectedIndices();
                              assignCollectionBasedGateModel(clonedGate,
                                  collConnector, newSelection);
                            }
                          });
                  // to respect init state
                  assignCollectionBasedGateModel(clonedGate,
                      ((ICollectionConnectorProvider) viewConnector)
                          .getCollectionConnector(),
                      ((ICollectionConnectorProvider) viewConnector)
                          .getCollectionConnector().getSelectedIndices());
                } else if (viewConnector instanceof IItemSelectable) {
                  ((IModelGate) clonedGate).setModel(null);
                  ((IItemSelectable) viewConnector)
                      .addItemSelectionListener(new IItemSelectionListener() {

                        @Override
                        public void selectedItemChange(ItemSelectionEvent event) {
                          Object selectedItem = event.getSelectedItem();
                          if (selectedItem == event.getSource()) {
                            return;
                          }
                          assignCollectionBasedGateModel(clonedGate,
                              selectedItem);
                        }
                      });
                  // to respect init state
                  assignCollectionBasedGateModel(clonedGate,
                      ((IItemSelectable) viewConnector).getSelectedItem());
                } else {
                  bindSimpleGateModel(clonedGate, viewConnector,
                      modelDescriptor);
                }
              } else {
                bindSimpleGateModel(clonedGate, viewConnector, modelDescriptor);
              }
            }
            clonedGates.add(clonedGate);
          }
        }
        new GatesListener(uiAction, clonedGates);
      }
    } finally {
      actionHandler.restoreLastSecurityContextSnapshot();
    }
  }

  /**
   * Performs dependency injection on a gate.
   *
   * @param gate
   *          the gate.
   * @param actionHandler
   *          the action handler.
   */
  protected void applyGateDependencyInjection(IGate gate,
      IActionHandler actionHandler) {
    if (gate instanceof ISecurityHandlerAware) {
      ((ISecurityHandlerAware) gate).setSecurityHandler(actionHandler);
    }
    if (gate instanceof IActionHandlerAware) {
      ((IActionHandlerAware) gate).setActionHandler(actionHandler);
    }
    if (gate instanceof ISubjectAware) {
      ((ISubjectAware) gate).setSubject(actionHandler.getSubject());
    }
  }

  private void bindSimpleGateModel(final IGate gate,
      IValueConnector viewConnector, IModelDescriptor modelDescriptor) {
    if (modelDescriptor instanceof IPropertyDescriptor) {
      // Binds to the model provider
      if (viewConnector.getModelConnector() != null
          && viewConnector.getModelConnector().getModelProvider() != null) {
        ((IModelGate) gate).setModel(viewConnector.getModelConnector()
            .getModelProvider().getModel());
        // the following disables table cell editors in swing.
        // } else {
        // ((IModelGate) gate).setModel(null);
      }
      final IModelChangeListener modelChangeListener = new IModelChangeListener() {

        @Override
        public void modelChange(ModelChangeEvent evt) {
          ((IModelGate) gate).setModel(evt.getNewValue());
        }
      };
      viewConnector.addPropertyChangeListener(
          IValueConnector.MODEL_CONNECTOR_PROPERTY,
          new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
              IValueConnector oldModelConnector = (IValueConnector) evt
                  .getOldValue();
              IValueConnector newModelConnector = (IValueConnector) evt
                  .getNewValue();
              if (oldModelConnector != null) {
                oldModelConnector.getModelProvider().removeModelChangeListener(
                    modelChangeListener);
              }
              if (newModelConnector != null
                  && newModelConnector.getModelProvider() != null) {
                ((IModelGate) gate).setModel(newModelConnector
                    .getModelProvider().getModel());
                newModelConnector.getModelProvider().addModelChangeListener(
                    modelChangeListener);
                // the following disables table cell editors in swing.
                // } else {
                // ((IModelGate) gate).setModel(null);
              }
            }
          });
    } else {
      // simply binds to the value.
      ((IModelGate) gate).setModel(viewConnector.getConnectorValue());
      viewConnector.addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          ((IModelGate) gate).setModel(evt.getNewValue());
        }
      });
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
   * Is live debug UI structure.
   *
   * @return the boolean
   */
  protected boolean isLiveDebugUI() {
    return liveDebugUI;
  }

  /**
   * Sets live debug UI structure.
   *
   * @param liveDebugUI the live debug uI structure
   */
  public void setLiveDebugUI(boolean liveDebugUI) {
    this.liveDebugUI = liveDebugUI;
  }

  private void assignCollectionBasedGateModel(final IGate gate,
      ICollectionConnector collConnector, int... selectedIndices) {
    Set<Object> selectedModels = null;
    if (selectedIndices != null && selectedIndices.length > 0) {
      selectedModels = new HashSet<>();
      for (int selectedIndice : selectedIndices) {
        IValueConnector childConnector = collConnector
            .getChildConnector(selectedIndice);
        if (childConnector != null) {
          selectedModels.add(childConnector.getConnectorValue());
        }
      }
    }
    ((IModelGate) gate).setModel(selectedModels);
  }

  private void assignCollectionBasedGateModel(final IGate gate,
      Object selectedItem) {
    if (selectedItem != null) {
      if (selectedItem instanceof IValueConnector) {
        Object connectorValue = ((IValueConnector) selectedItem)
            .getConnectorValue();
        if (connectorValue != null) {
          ((IModelGate) gate).setModel(Collections.singleton(connectorValue));
        } else {
          ((IModelGate) gate).setModel(null);
        }
      } else {
        ((IModelGate) gate).setModel(Collections.singleton(selectedItem));
      }
    } else {
      ((IModelGate) gate).setModel(null);
    }
  }

  /**
   * Complete description with live debug uI.
   *
   * @param action the action
   * @param i18nDescription the i 18 n description
   * @return the completed action description
   */
  protected String completeDescriptionWithLiveDebugUI(IAction action, String i18nDescription) {
    if (isLiveDebugUI()) {
      if (i18nDescription == null) {
        i18nDescription = "";
      } else {
        i18nDescription = i18nDescription + " ";
      }
      i18nDescription = i18nDescription + "(Action PermId -> [" + action.getPermId() + "])";
    }
    return i18nDescription;
  }

  private final class GatesListener implements PropertyChangeListener {

    private final E                 action;
    private final Collection<IGate> gates;

    /**
     * Constructs a new {@code GatesListener} instance.
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
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      setActionEnabled(action, GateHelper.areGatesOpen(gates));
    }
  }
}
