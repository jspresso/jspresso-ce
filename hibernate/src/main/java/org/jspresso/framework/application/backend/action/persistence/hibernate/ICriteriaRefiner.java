/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.jspresso.framework.model.component.IQueryComponent;

/**
 * This interface is used to refine hibernate queries and can be injected in
 * <code>QueryEntitiesAction</code>.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICriteriaRefiner {

  /**
   * Refines a query criteria.
   * 
   * @param criteria
   *          the query criteria to refine.
   * @param subCriteriaRegistry
   *          the sub-criteria registry used to avoid duplicate association
   *          paths in queries.
   * @param queryComponent
   *          the query component.
   * @param context
   *          the action context.
   */
  void refineCriteria(DetachedCriteria criteria,
      Map<DetachedCriteria, Map<String, DetachedCriteria>> subCriteriaRegistry,
      IQueryComponent queryComponent, Map<String, Object> context);
}
