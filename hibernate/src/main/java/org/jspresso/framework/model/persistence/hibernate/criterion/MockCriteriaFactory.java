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
 * Empty criteria factory.
 *
 * @author Vincent Vandenschrick
 */
public class MockCriteriaFactory implements ICriteriaFactory {

  /**
   * Complete criteria with ordering.
   *
   * @param criteria the criteria
   * @param queryComponent the query component
   * @param context the context
   */
  @Override
  public void completeCriteriaWithOrdering(EnhancedDetachedCriteria criteria, IQueryComponent queryComponent,
                                           Map<String, Object> context) {

  }

  /**
   * Create criteria.
   *
   * @param queryComponent the query component
   * @param context the context
   * @return the enhanced detached criteria
   */
  @Override
  public EnhancedDetachedCriteria createCriteria(IQueryComponent queryComponent, Map<String, Object> context) {
    return null;
  }

}
