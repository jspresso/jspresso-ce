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
package org.jspresso.framework.model.component;

import java.util.List;
import java.util.Map;

/**
 * A simple adapter to wrap a component used as selection criteria and a list of
 * components. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
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
public interface IQueryComponent extends Map<String, Object> {

  /**
   * "queriedComponents" string constant.
   */
  String QUERIED_COMPONENTS = "queriedComponents";

  /**
   * Gets the list of components result of the query.
   * 
   * @return the list of components result of the query.
   */
  List<? extends IComponent> getQueriedComponents();

  /**
   * Gets the contract of the components to query.
   * 
   * @return the contract of the components to query.
   */
  Class<?> getQueryContract();
  
  /**
   * Sets the list of components result of the query.
   * 
   * @param queriedComponents
   *            the list of components result of the query.
   */
  void setQueriedComponents(List<? extends IComponent> queriedComponents);
}
