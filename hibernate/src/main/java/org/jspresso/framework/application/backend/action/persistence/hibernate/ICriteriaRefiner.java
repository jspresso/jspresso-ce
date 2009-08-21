/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

/**
 * This interface is used to refine hibernate queries and can be injected in
 * <code>QueryEntitiesAction</code>.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
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
   * @param context
   *          the action context.
   */
  void refineCriteria(DetachedCriteria criteria, Map<String, Object> context);
}
