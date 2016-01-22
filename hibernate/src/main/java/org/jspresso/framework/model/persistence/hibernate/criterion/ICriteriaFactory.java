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
package org.jspresso.framework.model.persistence.hibernate.criterion;

import java.util.Map;

import org.jspresso.framework.model.component.IQueryComponent;

/**
 * A factory used to create a criteria based on a query component.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public interface ICriteriaFactory {

  /**
   * Takes a criteria and applies ordering specs to it.
   *
   * @param criteria
   *          the enhanced detached criteria to apply ordering for.
   * @param queryComponent
   *          the query component holding the sort order specs.
   * @param context
   *          the current action context.
   */
  void completeCriteriaWithOrdering(EnhancedDetachedCriteria criteria,
      IQueryComponent queryComponent, Map<String, Object> context);

  /**
   * Creates an Hibernate detached criteria by traversing a query component.
   * Whenever the query component is not valid (references a transient entity
   * for instance), the method should return null.
   *
   * @param queryComponent
   *          the query component to traverse.
   * @param context
   *          the current action context.
   * @return the detached criteria or null if the query component is invalid.
   */
  EnhancedDetachedCriteria createCriteria(IQueryComponent queryComponent,
      Map<String, Object> context);

}
