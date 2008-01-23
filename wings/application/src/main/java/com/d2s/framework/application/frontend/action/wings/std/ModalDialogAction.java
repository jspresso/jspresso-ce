/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.std;

import java.util.List;
import java.util.Map;

import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SDialog;
import org.wings.SFrame;
import org.wings.SPanel;
import org.wings.SSpacer;
import org.wings.border.SEmptyBorder;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;

/**
 * Base class dialog wings actions.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModalDialogAction extends AbstractWingsAction {

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    final SDialog dialog;
    IView<SComponent> mainView = getMainView(context);
    SFrame window = getSourceComponent(context).getParentFrame();
    dialog = new SDialog(window, getI18nName(getTranslationProvider(context),
        getLocale(context)));

    SPanel buttonBox = new SPanel(new SBoxLayout(/*dialog, */SBoxLayout.X_AXIS));
    buttonBox.setBorder(new SEmptyBorder(new java.awt.Insets(5, 10, 5, 10)));

    SButton defaultButton = null;
    for (IDisplayableAction action : getActions(context)) {
      SButton actionButton = new SButton();
      actionButton.setAction(getActionFactory(context).createAction(action,
          actionHandler, mainView, getLocale(context)));
      buttonBox.add(actionButton);
      buttonBox.add(new SSpacer(10, 10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    SPanel actionPanel = new SPanel(new SBorderLayout());
    actionPanel.add(buttonBox, SBorderLayout.EAST);

    SPanel mainPanel = new SPanel(new SBorderLayout());
    mainPanel.add(mainView.getPeer(), SBorderLayout.CENTER);
    mainPanel.add(actionPanel, SBorderLayout.SOUTH);
    dialog.add(mainPanel);
    //dialog.setClosable(false);
    if (defaultButton != null) {
      dialog.setDefaultButton(defaultButton);
    }
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
  public IView<SComponent> getMainView(Map<String, Object> context) {
    return (IView<SComponent>) context.get(ActionContextConstants.DIALOG_VIEW);
  }

}
