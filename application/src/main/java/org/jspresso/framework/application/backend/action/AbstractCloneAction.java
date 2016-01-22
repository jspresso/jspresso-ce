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
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.IValueConnector;

/**
 * An action used duplicate a domain object. the cloned domain object is set as model for the current view.
 *
 * <pre>
 * protected abstract Object cloneElement(Object element,
 *     Map&lt;String, Object&gt; context)
 * </pre>
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCloneAction extends
    BackendAction {

  /**
   * Clones an element.
   *
   * @param element
   *          the element to clone.
   * @param context
   *          the action context.
   * @return the cloned element.
   */
  protected abstract Object cloneElement(Object element,
      Map<String, Object> context);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector == null) {
      return false;
    }
    Object clone = cloneElement(modelConnector.getConnectorValue(), context);
    setActionParameter(clone, context);
    return super.execute(actionHandler, context);
  }

}
