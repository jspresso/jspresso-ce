/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.swing.BrowserControl;
import org.jspresso.framework.util.swing.SwingUtil;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import chrriis.dj.nativeswing.swtimpl.components.FlashPluginOptions;
import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;

/**
 * Default implementation of a mock swing frontend controller. This
 * implementation is usable "as-is".
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MockSwingController extends
    AbstractFrontendController<JComponent, Icon, Action> {

  /**
   * {@inheritDoc}
   */
  public void displayModalDialog(JComponent mainView, List<Action> actions,
      String title, JComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
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

    if (dimension != null) {
      mainView.setPreferredSize(new java.awt.Dimension(dimension.getWidth(),
          dimension.getHeight()));
    }
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
    SwingUtil.centerInParent(dialog);
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
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
              getIconFactory().getLargeIconSize()));
    } else if (ex instanceof BusinessException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale()))),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              getIconFactory().getLargeIconSize()));
    } else if (ex instanceof DataIntegrityViolationException) {
      JOptionPane
          .showMessageDialog(
              sourceComponent,
              HtmlHelper
                  .toHtml(HtmlHelper
                      .emphasis(getTranslationProvider()
                          .getTranslation(
                              refineIntegrityViolationTranslationKey((DataIntegrityViolationException) ex),
                              getLocale()))), getTranslationProvider()
                  .getTranslation("error", getLocale()),
              JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
                  getIconFactory().getLargeIconSize()));
    } else if (ex instanceof ConcurrencyFailureException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale()))),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              getIconFactory().getLargeIconSize()));
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
  public Workspace getWorkspace(@SuppressWarnings("unused") String workspaceName) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void displayFlashObject(String swfUrl,
      Map<String, String> flashContext, List<Action> actions, String title,
      JComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {

    JFlashPlayer flashPlayer = new JFlashPlayer();
    FlashPluginOptions options = new FlashPluginOptions();
    options.setVariables(flashContext);
    flashPlayer.load(swfUrl, options);

    displayModalDialog(flashPlayer, actions, title, sourceComponent, context,
        dimension, reuseCurrent);
  }

  /**
   * {@inheritDoc}
   */
  public void popupInfo(JComponent sourceComponent, String title,
      String iconImageUrl, String message) {
    JOptionPane.showMessageDialog(SwingUtil
        .getWindowOrInternalFrame(sourceComponent), message, title,
        JOptionPane.INFORMATION_MESSAGE, getIconFactory().getIcon(iconImageUrl,
            getIconFactory().getLargeIconSize()));
  }

  /**
   * {@inheritDoc}
   */
  public void popupOkCancel(JComponent sourceComponent, String title,
      String iconImageUrl, String message, IAction okAction,
      IAction cancelAction, Map<String, Object> context) {
    int selectedOption = JOptionPane.showConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(sourceComponent), message, title,
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
        getIconFactory().getIcon(iconImageUrl,
            getIconFactory().getLargeIconSize()));
    IAction nextAction = null;
    if (selectedOption == JOptionPane.OK_OPTION) {
      nextAction = okAction;
    } else {
      nextAction = cancelAction;
    }
    if (nextAction != null) {
      execute(nextAction, context);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNo(JComponent sourceComponent, String title,
      String iconImageUrl, String message, IAction yesAction, IAction noAction,
      Map<String, Object> context) {
    int selectedOption = JOptionPane.showConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(sourceComponent), message, title,
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
        getIconFactory().getIcon(iconImageUrl,
            getIconFactory().getLargeIconSize()));
    IAction nextAction = null;
    if (selectedOption == JOptionPane.YES_OPTION) {
      nextAction = yesAction;
    } else {
      nextAction = noAction;
    }
    if (nextAction != null) {
      execute(nextAction, context);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNoCancel(JComponent sourceComponent, String title,
      String iconImageUrl, String message, IAction yesAction, IAction noAction,
      IAction cancelAction, Map<String, Object> context) {
    int selectedOption = JOptionPane.showConfirmDialog(SwingUtil
        .getWindowOrInternalFrame(sourceComponent), message, title,
        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
        getIconFactory().getIcon(iconImageUrl,
            getIconFactory().getLargeIconSize()));
    IAction nextAction = null;
    if (selectedOption == JOptionPane.YES_OPTION) {
      nextAction = yesAction;
    } else if (selectedOption == JOptionPane.NO_OPTION) {
      nextAction = noAction;
    } else {
      nextAction = cancelAction;
    }
    if (nextAction != null) {
      execute(nextAction, context);
    }
  }
}
