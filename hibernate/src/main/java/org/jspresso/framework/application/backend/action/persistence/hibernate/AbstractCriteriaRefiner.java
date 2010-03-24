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

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.jspresso.framework.application.action.AbstractActionContextAware;

/**
 * Base abstract class for criteria refiner.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCriteriaRefiner extends
    AbstractActionContextAware implements ICriteriaRefiner {

  /**
   * Gets (or creates a new) sub-criteria sing a given association path.
   * 
   * @param associationPath
   *          the association path for the sub-criteria.
   * @param criteria
   *          the base criteria.
   * @param subCriteriaRegistry
   *          the registry of existing sub-criteria.
   * @return the join criteria.
   */
  protected DetachedCriteria getCriteria(String associationPath,
      DetachedCriteria criteria,
      Map<DetachedCriteria, Map<String, DetachedCriteria>> subCriteriaRegistry) {
    DetachedCriteria dc = null;
    Map<String, DetachedCriteria> subcriterias = subCriteriaRegistry
        .get(criteria);
    if (subcriterias != null) {
      dc = subcriterias.get(associationPath);
    }
    if (dc == null) {
      dc = criteria.createCriteria(associationPath);
      if (subcriterias == null) {
        subcriterias = new HashMap<String, DetachedCriteria>();
      }
      subcriterias.put(associationPath, dc);
    }
    return dc;
  }
}
