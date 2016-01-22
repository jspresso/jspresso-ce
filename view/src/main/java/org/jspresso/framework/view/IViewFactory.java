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
package org.jspresso.framework.view;

import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Factory for views.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public interface IViewFactory<E, F, G> {

  /**
   * Creates a new view from a view descriptor.
   *
   * @param viewDescriptor
   *          the view descriptor being the root of the view hierarchy to be
   *          constructed.
   * @param actionHandler
   *          the object responsible for executing the view actions (generally
   *          the frontend controller itself).
   * @param locale
   *          the locale the view must use for i18n.
   * @return the created view.
   */
  IView<E> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale);

  /**
   * Gets the action factory.
   *
   * @return the action factory.
   */
  IActionFactory<G, E> getActionFactory();

  /**
   * Gets the view connector factory.
   *
   * @return the view connector factory.
   */
  IConfigurableConnectorFactory getConnectorFactory();

  /**
   * Gets the icon factory.
   *
   * @return the icon factory.
   */
  IIconFactory<F> getIconFactory();

  /**
   * Stores user table preferences.
   *
   * @param tableId
   *          the table id used as preference key in the user store.
   * @param columnPrefs
   *          the array of {columnId,columnSize} for the table
   * @param actionHandler
   *          the action handler.
   */
  void storeTablePreferences(String tableId, Object[][] columnPrefs,
      IActionHandler actionHandler);

  /**
   * Request focus for a component.
   *
   * @param component
   *          the component to focus.
   */
  void focus(E component);

  /**
   * Request editing for a component.
   *
   * @param component
   *          the component to turn to editing mode.
   */
  void edit(E component);

  /**
   * Forces the refresh of a card view, potentially changing its visible card.
   *
   * @param cardView
   *          the card view to re-compute the visible card for.
   * @param unbindPrevious
   *          Should current view be unbound ?
   * @param actionHandler
   *          the action handler.
   * @param locale
   *          the locale.
   */
  void refreshCardView(IMapView<E> cardView, boolean unbindPrevious, IActionHandler actionHandler,
      Locale locale);

}
