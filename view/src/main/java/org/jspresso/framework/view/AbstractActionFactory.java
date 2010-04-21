/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
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
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Abstract base class for action factories.
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
   * {@inheritDoc}
   */
  public E createAction(IAction action, IActionHandler actionHandler,
      IView<F> view, Locale locale) {
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
  public Map<String, Object> createActionContext(IActionHandler actionHandler,
      IView<F> view, IValueConnector viewConnector, String actionCommand,
      F actionWidget) {
    Map<String, Object> actionContext = actionHandler.createEmptyContext();

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
    if (refinedViewConnector instanceof ICollectionConnectorProvider
        && ((ICollectionConnectorProvider) refinedViewConnector)
            .getCollectionConnector() != null) {
      actionContext.put(ActionContextConstants.SELECTED_INDICES,
          ((ICollectionConnectorProvider) refinedViewConnector)
              .getCollectionConnector().getSelectedIndices());
    }
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
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
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
    if (view == null) {
      return;
    }
    IModelDescriptor modelDescriptor = null;
    IValueConnector viewConnector = null;
    if (view.getDescriptor() != null) {
      modelDescriptor = view.getDescriptor().getModelDescriptor();
    }
    viewConnector = view.getConnector();
    if (action.getActionabilityGates() != null) {
      Collection<IGate> clonedGates = new HashSet<IGate>();
      for (IGate gate : action.getActionabilityGates()) {
        if (!(gate instanceof ISecurable)
            || actionHandler.isAccessGranted((ISecurable) gate)) {
          final IGate clonedGate = gate.clone();
          if (clonedGate instanceof ISubjectAware) {
            ((ISubjectAware) clonedGate).setSubject(actionHandler.getSubject());
          }
          if (clonedGate instanceof IActionHandlerAware) {
            ((IActionHandlerAware) clonedGate).setActionHandler(actionHandler);
          }
          if (clonedGate instanceof IModelGate) {
            if (modelDescriptor instanceof ICollectionPropertyDescriptor<?>) {
              if (((IModelGate) clonedGate).isCollectionBased()) {
                ((IModelGate) clonedGate).setModel(null);
                ((ICollectionConnectorProvider) viewConnector)
                    .getCollectionConnector().addSelectionChangeListener(
                        new ISelectionChangeListener() {

                          public void selectionChange(SelectionChangeEvent evt) {
                            ICollectionConnector collConnector = (ICollectionConnector) evt
                                .getSource();
                            int[] newSelection = evt.getNewSelection();
                            Set<Object> selectedModels = null;
                            if (newSelection != null && newSelection.length > 0) {
                              selectedModels = new HashSet<Object>();
                              for (int i = 0; i < newSelection.length; i++) {
                                selectedModels.add(collConnector
                                    .getChildConnector(newSelection[i])
                                    .getConnectorValue());
                              }
                            }
                            ((IModelGate) clonedGate).setModel(selectedModels);
                          }
                        });
              } else {
                if (viewConnector.getModelConnector() != null) {
                  ((IModelGate) clonedGate).setModel(viewConnector
                      .getModelConnector().getModelProvider().getModel());
                } else {
                  ((IModelGate) clonedGate).setModel(null);
                }
                final IModelChangeListener modelChangeListener = new IModelChangeListener() {

                  public void modelChange(ModelChangeEvent evt) {
                    ((IModelGate) clonedGate).setModel(evt.getNewValue());
                  }
                };
                viewConnector.addPropertyChangeListener("modelConnector",
                    new PropertyChangeListener() {

                      public void propertyChange(PropertyChangeEvent evt) {
                        IValueConnector oldModelConnector = (IValueConnector) evt
                            .getOldValue();
                        IValueConnector newModelConnector = (IValueConnector) evt
                            .getNewValue();
                        if (oldModelConnector != null) {
                          oldModelConnector.getModelProvider()
                              .removeModelChangeListener(modelChangeListener);
                        }
                        if (newModelConnector != null) {
                          ((IModelGate) clonedGate).setModel(newModelConnector
                              .getModelProvider().getModel());
                          newModelConnector.getModelProvider()
                              .addModelChangeListener(modelChangeListener);
                        } else {
                          ((IModelGate) clonedGate).setModel(null);
                        }
                      }
                    });
              }
            } else if (((IModelGate) clonedGate).isCollectionBased()
                && viewConnector instanceof IItemSelectable) {
              ((IModelGate) clonedGate).setModel(null);
              ((IItemSelectable) viewConnector)
                  .addItemSelectionListener(new IItemSelectionListener() {

                    public void selectedItemChange(ItemSelectionEvent event) {
                      Object selectedItem = event.getSelectedItem();
                      if (selectedItem == event.getSource()) {
                        return;
                      }
                      if (selectedItem != null) {
                        if (selectedItem instanceof IValueConnector) {
                          Object connectorValue = ((IValueConnector) selectedItem)
                              .getConnectorValue();
                          if (connectorValue != null) {
                            ((IModelGate) clonedGate).setModel(Collections
                                .singleton(connectorValue));
                          } else {
                            ((IModelGate) clonedGate).setModel(null);
                          }
                        } else {
                          ((IModelGate) clonedGate).setModel(Collections
                              .singleton(event.getSelectedItem()));
                        }
                      } else {
                        ((IModelGate) clonedGate).setModel(null);
                      }
                    }
                  });
            } else if (modelDescriptor instanceof IComponentDescriptorProvider<?>) {
              ((IModelGate) clonedGate).setModel(viewConnector
                  .getConnectorValue());
              viewConnector.addValueChangeListener(new IValueChangeListener() {

                public void valueChange(ValueChangeEvent evt) {
                  ((IModelGate) clonedGate).setModel(evt.getNewValue());
                }
              });
            }
          }
          clonedGates.add(clonedGate);
        }
      }
      new GatesListener(uiAction, clonedGates);
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
}
