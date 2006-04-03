/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import java.util.List;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.application.frontend.action.ulc.IDialogAwareAction;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCGridLayoutPane;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.UlcUtilities;
import com.ulcjava.base.application.event.serializable.IActionListener;
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

  private IActionFactory<IAction, ULCComponent> actionFactory;
  private IView<ULCComponent>                   mainView;
  private List<IDisplayableAction>              actions;

  /**
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Sets the mainView.
   * 
   * @param mainView
   *          the mainView to set.
   */
  public void setMainView(IView<ULCComponent> mainView) {
    this.mainView = mainView;
  }

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    final ULCDialog dialog;
    ULCWindow window = UlcUtilities.getWindowAncestor(getSourceComponent(context));
    dialog = new ULCDialog(window, getName(), true);
    ULCGridLayoutPane actionPanel = new ULCGridLayoutPane(1, 0, 5, 10);
    ULCButton defaultButton = null;
    for (IDisplayableAction action : actions) {
      ULCButton actionButton = new ULCButton();
      actionButton.setAction(actionFactory.createAction(action, actionHandler,
          mainView, getLocale(context)));
      if (action instanceof IDialogAwareAction) {
        final IDialogAwareAction finalAction = (IDialogAwareAction) action;
        actionButton.addActionListener(new IActionListener() {

          private static final long serialVersionUID = -6613837292845274145L;

          public void actionPerformed(@SuppressWarnings("unused")
          com.ulcjava.base.application.event.ActionEvent e) {
            finalAction.postActionExecution(dialog);
          }
        });
      }
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
    super.execute(actionHandler, context);
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(
      IActionFactory<IAction, ULCComponent> actionFactory) {
    this.actionFactory = actionFactory;
  }
}
