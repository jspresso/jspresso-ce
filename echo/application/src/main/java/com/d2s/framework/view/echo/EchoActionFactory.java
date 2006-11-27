/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.echo;

import java.util.Locale;
import java.util.Map;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * An echo2 action factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EchoActionFactory implements
    IActionFactory<ActionListener, Component> {

  /**
   * {@inheritDoc}
   */
  public ActionListener createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<Component> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public ActionListener createAction(IDisplayableAction action,
      IActionHandler actionHandler, Component sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    return new ActionAdapter(action, actionHandler, sourceComponent,
        modelDescriptor, viewConnector, locale);
  }

  private final class ActionAdapter implements ActionListener {

    private static final long serialVersionUID = 5819377672533326496L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private Component         sourceComponent;
    private IModelDescriptor  modelDescriptor;
    private IValueConnector   viewConnector;

    /**
     * Constructs a new <code>ActionAdapter</code> instance.
     *
     * @param action
     * @param actionHandler
     * @param sourceComponent
     * @param modelDescriptor
     * @param viewConnector
     * @param locale
     */
    public ActionAdapter(IDisplayableAction action,
        IActionHandler actionHandler, Component sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
        @SuppressWarnings("unused")
        Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.sourceComponent = sourceComponent;
      this.modelDescriptor = modelDescriptor;
      if (modelDescriptor instanceof ICollectionDescriptor) {
        this.viewConnector = ((ICollectionConnectorProvider) viewConnector)
            .getCollectionConnector();
      } else {
        this.viewConnector = viewConnector;
      }
    }

    /**
     * Triggers the action execution on the action handler. The following
     * initial action context is filled in :
     * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>
     * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code>
     * <li> <code>ActionContextConstants.SELECTED_INDICES</code>
     * <li> <code>ActionContextConstants.LOCALE</code>
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
      if (actionHandler != null) {
        Map<String, Object> actionContext = actionHandler.createEmptyContext();
        actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        actionContext.put(ActionContextConstants.VIEW_CONNECTOR, viewConnector);
        if (viewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector() != null) {
          actionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) viewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        actionContext.put(ActionContextConstants.ACTION_COMMAND, e
            .getActionCommand());
        actionContext.put(ActionContextConstants.ACTION_WIDGET, e.getSource());
        if (action.getInitialContext() != null) {
          actionContext.putAll(action.getInitialContext());
        }
        actionHandler.execute(action, actionContext);
      }
    }
  }
}
