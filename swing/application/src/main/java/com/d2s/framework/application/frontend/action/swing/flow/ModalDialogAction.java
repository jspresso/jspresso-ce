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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.application.frontend.action.swing.IDialogAwareAction;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.action.IDisplayableAction;

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

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    final JDialog dialog;
    IView<JComponent> mainView = getMainView(context);
    Window window = SwingUtilities
        .getWindowAncestor(getSourceComponent(context));
    if (window instanceof Dialog) {
      dialog = new JDialog((Dialog) window, getName(), true);
    } else {
      dialog = new JDialog((Frame) window, getName(), true);
    }
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new GridLayout(1, 0, 5, 10));
    JButton defaultButton = null;
    for (IDisplayableAction action : getActions(context)) {
      JButton actionButton = new JButton();
      actionButton.setAction(getActionFactory(context).createAction(action,
          actionHandler, mainView, getLocale(context)));
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
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(mainView.getPeer(), BorderLayout.CENTER);
    mainPanel.add(actionPanel, BorderLayout.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    if (defaultButton != null) {
      dialog.getRootPane().setDefaultButton(defaultButton);
    }
    dialog.pack();
    if (window != null) {
      dialog.setLocation(window.getX()
          + (window.getWidth() - dialog.getWidth()) / 2, window.getY()
          + (window.getHeight() - dialog.getHeight()) / 2);
    }
    dialog.setVisible(true);
    super.execute(actionHandler, context);
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
  public IView<JComponent> getMainView(Map<String, Object> context) {
    return (IView<JComponent>) context.get(ActionContextConstants.DIALOG_VIEW);
  }

}
