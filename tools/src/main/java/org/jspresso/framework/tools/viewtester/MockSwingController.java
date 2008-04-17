/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.tools.viewtester;

import java.awt.Component;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.view.IIconFactory;
import org.springframework.dao.ConcurrencyFailureException;


/**
 * Default implementation of a mock swing frontend controller. This
 * implementation is usable "as-is".
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    Component sourceComponent = null;
    if (ex instanceof SecurityException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper.emphasis(ex
          .getMessage()), getTranslationProvider().getTranslation("error",
          getLocale()), JOptionPane.ERROR_MESSAGE, getIconFactory()
          .getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof BusinessException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale())), getTranslationProvider()
          .getTranslation("error", getLocale()), JOptionPane.ERROR_MESSAGE,
          getIconFactory().getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof ConcurrencyFailureException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale())),
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
  protected void displayWorkspace(@SuppressWarnings("unused")
  String workspaceName) {
    // Empty implementation.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Workspace getWorkspace(@SuppressWarnings("unused")
  String workspaceName) {
    return null;
  }
}
