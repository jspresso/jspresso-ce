/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.tools.viewtester;

import java.awt.Component;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.springframework.dao.ConcurrencyFailureException;

import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.view.IIconFactory;

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
  public void handleException(Throwable ex, @SuppressWarnings("unused")
  Map<String, Object> context) {
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
  protected Module getModule(@SuppressWarnings("unused")
  String moduleName) {
    return null;
  }
}
