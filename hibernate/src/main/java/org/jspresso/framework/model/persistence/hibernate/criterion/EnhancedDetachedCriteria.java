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
package org.jspresso.framework.model.persistence.hibernate.criterion;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;

/**
 * An enhanced detached criteria that holds a sub-criteria registry.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EnhancedDetachedCriteria extends DetachedCriteria {

  private static final long                                    serialVersionUID = 1477297471425065631L;

  private Map<DetachedCriteria, Map<String, DetachedCriteria>> subCriteriaRegistry;

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param impl
   *          criteria impl.
   * @param criteria
   *          criteria.
   */
  protected EnhancedDetachedCriteria(CriteriaImpl impl, Criteria criteria) {
    super(impl, criteria);
  }

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param entityName
   *          entity name.
   * @param alias
   *          alias.
   */
  protected EnhancedDetachedCriteria(String entityName, String alias) {
    super(entityName, alias);
  }

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param entityName
   *          entity name.
   */
  protected EnhancedDetachedCriteria(String entityName) {
    super(entityName);
  }

  /**
   * Creates or gets a previously registered sub-criteria.
   * 
   * @param masterCriteria
   *          the parent criteria holding the sub-criteria.
   * @param associationPath
   *          the association path.
   * @return the new or previously registered sub-criteria.
   */
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath) {
    DetachedCriteria subCriteria = getRegisteredSubCriteria(masterCriteria,
        associationPath);
    if (subCriteria == null) {
      subCriteria = masterCriteria.createCriteria(associationPath);
      registerSubCriteria(masterCriteria, associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Creates or gets a previously registered sub-criteria.
   * 
   * @param masterCriteria
   *          the parent criteria holding the sub-criteria.
   * @param associationPath
   *          the association path.
   * @param joinType
   *          the join type.
   * @return the new or previously registered sub-criteria.
   */
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath, int joinType) {
    DetachedCriteria subCriteria = getRegisteredSubCriteria(masterCriteria,
        associationPath);
    if (subCriteria == null) {
      subCriteria = masterCriteria.createCriteria(associationPath, joinType);
      registerSubCriteria(masterCriteria, associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Creates or gets a previously registered sub-criteria.
   * 
   * @param masterCriteria
   *          the parent criteria holding the sub-criteria.
   * @param associationPath
   *          the association path.
   * @param alias
   *          the alias.
   * @return the new or previously registered sub-criteria.
   */
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath, String alias) {
    DetachedCriteria subCriteria = getRegisteredSubCriteria(masterCriteria,
        associationPath);
    if (subCriteria == null) {
      subCriteria = masterCriteria.createCriteria(associationPath, alias);
      registerSubCriteria(masterCriteria, associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Creates or gets a previously registered sub-criteria.
   * 
   * @param masterCriteria
   *          the parent criteria holding the sub-criteria.
   * @param associationPath
   *          the association path.
   * @param alias
   *          the alias.
   * @param joinType
   *          the join type.
   * @return the new or previously registered sub-criteria.
   */
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath, String alias, int joinType) {
    DetachedCriteria subCriteria = getRegisteredSubCriteria(masterCriteria,
        associationPath);
    if (subCriteria == null) {
      subCriteria = masterCriteria.createCriteria(associationPath, alias,
          joinType);
      registerSubCriteria(masterCriteria, associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Creates a new enhanced detached criteria.
   * 
   * @param entityName
   *          entity name.
   * @return the new enhanced detached criteria.
   */
  public static EnhancedDetachedCriteria forEntityName(String entityName) {
    return new EnhancedDetachedCriteria(entityName);
  }

  /**
   * Creates a new enhanced detached criteria.
   * 
   * @param entityName
   *          entity name.
   * @param alias
   *          alias.
   * @return the new enhanced detached criteria.
   */
  public static EnhancedDetachedCriteria forEntityName(String entityName,
      String alias) {
    return new EnhancedDetachedCriteria(entityName, alias);
  }

  /**
   * Creates a new enhanced detached criteria.
   * 
   * @param clazz
   *          class.
   * @return the new enhanced detached criteria.
   */
  public static EnhancedDetachedCriteria forClass(Class<?> clazz) {
    return new EnhancedDetachedCriteria(clazz.getName());
  }

  /**
   * Creates a new enhanced detached criteria.
   * 
   * @param clazz
   *          class.
   * @param alias
   *          alias.
   * @return the new enhanced detached criteria.
   */
  public static EnhancedDetachedCriteria forClass(Class<?> clazz, String alias) {
    return new EnhancedDetachedCriteria(clazz.getName(), alias);
  }

  private DetachedCriteria getRegisteredSubCriteria(
      DetachedCriteria masterCriteria, String associationPath) {
    if (subCriteriaRegistry == null) {
      return null;
    }
    Map<String, DetachedCriteria> subCriterias = subCriteriaRegistry
        .get(masterCriteria);
    if (subCriterias == null) {
      return null;
    }
    return subCriterias.get(associationPath);
  }

  private void registerSubCriteria(DetachedCriteria masterCriteria,
      String associationPath, DetachedCriteria subCriteria) {
    if (subCriteriaRegistry == null) {
      subCriteriaRegistry = new HashMap<DetachedCriteria, Map<String, DetachedCriteria>>();
    }
    Map<String, DetachedCriteria> subCriterias = subCriteriaRegistry
        .get(masterCriteria);
    if (subCriterias == null) {
      subCriterias = new HashMap<String, DetachedCriteria>();
      subCriteriaRegistry.put(masterCriteria, subCriterias);
    }
    subCriterias.put(associationPath, subCriteria);
  }

}
