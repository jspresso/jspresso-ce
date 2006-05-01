/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * A swing action factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingActionFactory implements IActionFactory<Action, JComponent> {

  private ITranslationProvider translationProvider;
  private IIconFactory<Icon>   iconFactory;

  /**
   * {@inheritDoc}
   */
  public Action createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<JComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public Action createAction(IDisplayableAction action,
      IActionHandler actionHandler, JComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    return new ActionAdapter(action, actionHandler, sourceComponent,
        modelDescriptor, viewConnector, locale);
  }

  private final class ActionAdapter extends AbstractAction {

    private static final long serialVersionUID = 5819377672533326496L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private JComponent        sourceComponent;
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
        IActionHandler actionHandler, JComponent sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
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
      putValue(Action.NAME, translationProvider.getTranslation(action.getName(),
          locale));
      putValue(Action.SHORT_DESCRIPTION, translationProvider.getTranslation(
          action.getDescription(), locale));
      putValue(Action.SMALL_ICON, iconFactory.getIcon(action.getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
      if (action.getMnemonicAsString() != null) {
        putValue(Action.MNEMONIC_KEY, new Integer(KeyStroke.getKeyStroke(
            action.getMnemonicAsString()).getKeyCode()));
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
        Map<String, Object> actionContext = actionHandler
            .createEmptyContext();
        actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        actionContext.put(ActionContextConstants.VIEW_CONNECTOR,
            viewConnector);
        if (viewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector() != null) {
          actionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) viewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        actionContext.put(ActionContextConstants.ACTION_PARAM, e
            .getActionCommand());
        if (action.getInitialContext() != null) {
          actionContext.putAll(action.getInitialContext());
        }
        try {
          actionHandler.execute(action, actionContext);
        } catch (Throwable ex) {
          actionHandler.handleException(ex, actionContext);
        }
      }
    }

  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<Icon> iconFactory) {
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
}
