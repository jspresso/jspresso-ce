/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.ulc.std;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.ulc.AbstractUlcAction;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedButton;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCBoxLayoutPane;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCFiller;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * Base class dialog ulc actions.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
    ULCWindow window = UlcUtil.getVisibleWindow((getSourceComponent(context)));
    dialog = new ULCDialog(window, getI18nName(getTranslationProvider(context),
        getLocale(context)), true);

    ULCBoxLayoutPane buttonBox = new ULCBoxLayoutPane(
        ULCBoxLayoutPane.LINE_AXIS);
    buttonBox.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));

    ULCExtendedButton defaultButton = null;
    for (IDisplayableAction action : getActions(context)) {
      ULCExtendedButton actionButton = new ULCExtendedButton();
      actionButton.setAction(getActionFactory(context).createAction(action,
          actionHandler, mainView, getLocale(context)));
      buttonBox.add(actionButton);
      buttonBox.add(ULCFiller.createHorizontalStrut(10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    ULCBorderLayoutPane actionPanel = new ULCBorderLayoutPane();
    actionPanel.add(buttonBox, ULCBorderLayoutPane.EAST);

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
  public IView<ULCComponent> getMainView(Map<String, Object> context) {
    return (IView<ULCComponent>) context
        .get(ActionContextConstants.DIALOG_VIEW);
  }

}
