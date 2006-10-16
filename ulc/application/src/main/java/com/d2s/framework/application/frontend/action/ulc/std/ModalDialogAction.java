/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.std;

import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCGridLayoutPane;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.UlcUtilities;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * Base class dialog ulc actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModalDialogAction extends AbstractUlcAction {

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    final ULCDialog dialog;
    IView<ULCComponent> mainView = getMainView(context);
    ULCWindow window = UlcUtilities
        .getWindowAncestor(getSourceComponent(context));
    dialog = new ULCDialog(window, getI18nName(getTranslationProvider(context),
        getLocale(context)), true);
    ULCGridLayoutPane actionPanel = new ULCGridLayoutPane(1, 0, 5, 10);
    ULCButton defaultButton = null;
    for (IDisplayableAction action : getActions(context)) {
      ULCButton actionButton = new ULCButton();
      actionButton.setAction(getActionFactory(context).createAction(action,
          actionHandler, mainView, getLocale(context)));
      actionPanel.add(actionButton);
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    ULCBorderLayoutPane mainPanel = new ULCBorderLayoutPane();
    mainPanel.add(mainView.getPeer(), ULCBorderLayoutPane.CENTER);
    mainPanel.add(actionPanel, ULCBorderLayoutPane.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    if (defaultButton != null) {
      dialog.getRootPane().setDefaultButton(defaultButton);
    }
    dialog.pack();
    dialog.setVisible(true);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the actions.
   * 
   * @param context
   *          the action context.
   * @return the actions.
   */
  @SuppressWarnings("unchecked")
  public List<IDisplayableAction> getActions(Map<String, Object> context) {
    return (List<IDisplayableAction>) context
        .get(ActionContextConstants.DIALOG_ACTIONS);
  }

  /**
   * Gets the mainView.
   * 
   * @param context
   *          the action context.
   * @return the mainView.
   */
  @SuppressWarnings("unchecked")
  public IView<ULCComponent> getMainView(Map<String, Object> context) {
    return (IView<ULCComponent>) context
        .get(ActionContextConstants.DIALOG_VIEW);
  }

}
