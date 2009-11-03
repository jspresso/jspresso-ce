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
package org.jspresso.framework.util.context;

import java.util.Map;

/**
 * This interface is implemented by all classes which need to be aware of a
 * context. This context is basically a <code>Map</code> of arbitrary values.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IContextAware {

  /**
   * Gets the current context.
   * 
   * @return the context key/value pairs.
   */
  Map<String, Object> getContext();

  /**
   * Sets the current context.
   * 
   * @param context
   *            the context to set.
   */
  void setContext(Map<String, Object> context);
}
