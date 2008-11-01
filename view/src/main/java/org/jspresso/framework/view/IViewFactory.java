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
package org.jspresso.framework.view;

import java.util.Locale;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * Factory for views.
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
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public interface IViewFactory<E, F, G> {

  /**
   * <code>TOOLTIP_ELLIPSIS</code> is "...".
   */
  String TOOLTIP_ELLIPSIS = "...";

  /**
   * Creates a new view from a view descriptor.
   * 
   * @param viewDescriptor
   *            the view descriptor being the root of the view hierarchy to be
   *            constructed.
   * @param actionHandler
   *            the object responsible for executing the view actions (generally
   *            the frontend controller itself).
   * @param locale
   *            the locale the view must use for I18N.
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
   * Gets the icon factory.
   * 
   * @return the icon factory.
   */
  IIconFactory<F> getIconFactory();

}
