/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.std;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * Base class dialog swing actions.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    final JDialog dialog;
    IView<JComponent> mainView = getMainView(context);
    Window window = SwingUtil.getVisibleWindow(getSourceComponent(context));
    if (window instanceof Dialog) {
      dialog = new JDialog((Dialog) window, getI18nName(
          getTranslationProvider(context), getLocale(context)), true);
    } else {
      dialog = new JDialog((Frame) window, getI18nName(
          getTranslationProvider(context), getLocale(context)), true);
    }

    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
    buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

    JButton defaultButton = null;
    for (IDisplayableAction action : getActions(context)) {
      JButton actionButton = new JButton();
      SwingUtil.configureButton(actionButton);
      actionButton.setAction(getActionFactory(context).createAction(action,
          actionHandler, mainView, getLocale(context)));
      buttonBox.add(actionButton);
      buttonBox.add(Box.createHorizontalStrut(10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BorderLayout());
    actionPanel.add(buttonBox, BorderLayout.EAST);

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
    SwingUtil.centerInParent(dialog);
    dialog.setVisible(true);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the actions.
   * 
   * @param context
   *            the action context.
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
   *            the action context.
   * @return the mainView.
   */
  @SuppressWarnings("unchecked")
  public IView<JComponent> getMainView(Map<String, Object> context) {
    return (IView<JComponent>) context.get(ActionContextConstants.DIALOG_VIEW);
  }

}
