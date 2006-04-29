/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import java.util.Locale;
import java.util.Map;

import javax.swing.KeyStroke;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * An ulc action factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcActionFactory implements IActionFactory<IAction, ULCComponent> {

  private ITranslationProvider  labelTranslator;
  private ITranslationProvider  descriptionTranslator;
  private IIconFactory<ULCIcon> iconFactory;

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<ULCComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, ULCComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    return new ActionAdapter(action, actionHandler, sourceComponent,
        modelDescriptor, viewConnector, locale);
  }

  private final class ActionAdapter extends
      com.ulcjava.base.application.AbstractAction {

    private static final long                     serialVersionUID = 5819377672533326496L;

    private com.d2s.framework.action.IAction action;
    private IActionHandler                        actionHandler;
    private ULCComponent                          sourceComponent;
    private IModelDescriptor                      modelDescriptor;
    private IValueConnector                       viewConnector;
    private Locale                                locale;

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
        IActionHandler actionHandler, ULCComponent sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
        Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.sourceComponent = sourceComponent;
      this.modelDescriptor = modelDescriptor;
      this.viewConnector = viewConnector;
      this.locale = locale;
      putValue(IAction.NAME, labelTranslator.getTranslation(action.getName(),
          locale));
      putValue(IAction.SHORT_DESCRIPTION, descriptionTranslator.getTranslation(
          action.getDescription(), locale));
      putValue(IAction.SMALL_ICON, iconFactory.getIcon(
          action.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
      if (action.getMnemonicAsString() != null) {
        putValue(IAction.MNEMONIC_KEY, new Integer(KeyStroke.getKeyStroke(
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
    @Override
    public void actionPerformed(ActionEvent e) {
      if (actionHandler != null) {
        Map<String, Object> initialActionContext = actionHandler.createEmptyContext();
        initialActionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        initialActionContext.put(ActionContextConstants.VIEW_CONNECTOR,
            viewConnector);
        if (viewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector() != null) {
          initialActionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) viewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        initialActionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        initialActionContext.put(ActionContextConstants.LOCALE, locale);
        initialActionContext.put(ActionContextConstants.ACTION_PARAM, e
            .getActionCommand());
        if (action.getInitialContext() != null) {
          initialActionContext.putAll(action.getInitialContext());
        }
        actionHandler.execute(action, initialActionContext);
      }
    }

  }

  /**
   * Sets the descriptionTranslator.
   * 
   * @param descriptionTranslator
   *          the descriptionTranslator to set.
   */
  public void setDescriptionTranslator(
      ITranslationProvider descriptionTranslator) {
    this.descriptionTranslator = descriptionTranslator;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<ULCIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the labelTranslator.
   * 
   * @param labelTranslator
   *          the labelTranslator to set.
   */
  public void setLabelTranslator(ITranslationProvider labelTranslator) {
    this.labelTranslator = labelTranslator;
  }
}
