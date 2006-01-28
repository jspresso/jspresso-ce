/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.action.IDisplayableAction;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.application.frontend.action.swing.IDialogAwareAction;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IView;

/**
 * Base class dialog swing actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModalDialogAction extends AbstractSwingAction {

  private IActionFactory<Action, JComponent> actionFactory;
  private IView<JComponent>                  mainView;
  private List<IDisplayableAction>           actions;

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
  public void setMainView(IView<JComponent> mainView) {
    this.mainView = mainView;
  }

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    final JDialog dialog;
    Window window = SwingUtilities.getWindowAncestor(getSourceComponent());
    if (window instanceof Dialog) {
      dialog = new JDialog((Dialog) window, getName(), true);
    } else {
      dialog = new JDialog((Frame) window, getName(), true);
    }
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new GridLayout(1, 0, 5, 10));
    for (IDisplayableAction action : actions) {
      JButton actionButton = new JButton();
      actionButton.setAction(actionFactory.createAction(action, actionHandler,
          mainView, getLocale()));
      if (action instanceof IDialogAwareAction) {
        final IDialogAwareAction finalAction = (IDialogAwareAction) action;
        actionButton.addActionListener(new ActionListener() {

          public void actionPerformed(@SuppressWarnings("unused")
          ActionEvent e) {
            finalAction.postActionExecution(dialog);
          }
        });
      }
      actionPanel.add(actionButton);
    }
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(mainView.getPeer(), BorderLayout.CENTER);
    mainPanel.add(actionPanel, BorderLayout.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    dialog.pack();
    if (window != null) {
      // // dialog.setSize(Math.min(window.getWidth(), dialog.getWidth()),
      // // Math.min(
      // // window.getHeight(), dialog.getHeight()));
      dialog.setLocation(window.getX()
          + (window.getWidth() - dialog.getWidth()) / 2, window.getY()
          + (window.getHeight() - dialog.getHeight()) / 2);
      // dialog.setLocation(window.getX(), window.getY());
    }
    dialog.setVisible(true);
    return super.execute(actionHandler);
  }

  /**
   * Sets the actionFactory.
   * 
   * @param actionFactory
   *          the actionFactory to set.
   */
  public void setActionFactory(IActionFactory<Action, JComponent> actionFactory) {
    this.actionFactory = actionFactory;
  }
}
