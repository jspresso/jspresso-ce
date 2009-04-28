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
package org.jspresso.framework.tools.viewtester;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.swing.BrowserControl;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IIconFactory;
import org.springframework.dao.ConcurrencyFailureException;

/**
 * Default implementation of a mock swing frontend controller. This
 * implementation is usable "as-is".
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
public class MockSwingController extends
    AbstractFrontendController<JComponent, Icon, Action> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModalDialog(JComponent mainView, List<Action> actions,
      String title, JComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(mainView, actions, title, sourceComponent,
        context, dimension, reuseCurrent);
    final JDialog dialog;
    Window window = SwingUtil.getVisibleWindow(sourceComponent);
    if (window instanceof JDialog) {
      if (reuseCurrent) {
        dialog = (JDialog) window;
        dialog.getContentPane().removeAll();
      } else {
        dialog = new JDialog((JDialog) window, title, true);
      }
    } else {
      dialog = new JDialog((Frame) window, title, true);
    }

    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
    buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

    JButton defaultButton = null;
    for (Action action : actions) {
      JButton actionButton = new JButton();
      SwingUtil.configureButton(actionButton);
      actionButton.setAction(action);
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
    mainPanel.add(mainView, BorderLayout.CENTER);
    mainPanel.add(actionPanel, BorderLayout.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    if (defaultButton != null) {
      dialog.getRootPane().setDefaultButton(defaultButton);
    }
    dialog.pack();
    if (dimension != null) {
      dialog.setSize(dimension.getWidth(), dimension.getHeight());
    }
    SwingUtil.centerInParent(dialog);
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    try {
      BrowserControl.displayURL(urlSpec);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(JComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    Window actionWindow = SwingUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof JDialog) {
      actionWindow.dispose();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    Component sourceComponent = null;
    if (ex instanceof SecurityException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(ex.getMessage())),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof BusinessException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale()))),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof ConcurrencyFailureException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale()))),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              IIconFactory.LARGE_ICON_SIZE));
    } else {
      ex.printStackTrace();
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CallbackHandler createLoginCallbackHandler() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Workspace getWorkspace(
      @SuppressWarnings("unused") String workspaceName) {
    return null;
  }
}
