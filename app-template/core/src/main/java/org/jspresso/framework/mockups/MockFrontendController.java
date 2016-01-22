/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.mockups;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.preferences.IPreferencesStore;

/**
 * A mock for FrontendController.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */

public class MockFrontendController<E, F, G> extends
    AbstractFrontendController<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl,
      Map<String, String> flashContext, List<G> actions, String title,
      E sourceComponent, Map<String, Object> context, Dimension dimension,
      boolean reuseCurrent) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayDialog(E mainView, List<G> actions, String title,
      E sourceComponent, Map<String, Object> context, Dimension dimension,
      boolean reuseCurrent, boolean modal) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec, String target) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupInfo(E sourceComponent, String title, String iconImageUrl,
      String message) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupOkCancel(E sourceComponent, String title,
      String iconImageUrl, String message, IAction okAction,
      IAction cancelAction, Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNo(E sourceComponent, String title, String iconImageUrl,
      String message, IAction yesAction, IAction noAction,
      Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNoCancel(E sourceComponent, String title,
      String iconImageUrl, String message, IAction yesAction, IAction noAction,
      IAction cancelAction, Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatusInfo(String statusInfo) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IPreferencesStore createClientPreferencesStore() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void login() {
    // NO-OP
  }
}
