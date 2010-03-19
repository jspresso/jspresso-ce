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
 * An enhanced detached criteria that remembers sub-criterions to prevent
 * creating duplicates.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EnhancedDetachedCriteria extends DetachedCriteria {

  private static final long             serialVersionUID = -6699961470677072574L;
  private Map<String, DetachedCriteria> subCriterias;

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param impl
   *          the actual implementation.
   * @param criteria
   *          the criteria.
   */
  protected EnhancedDetachedCriteria(CriteriaImpl impl, Criteria criteria) {
    super(impl, criteria);
  }

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param entityName
   *          the entity name.
   * @param alias
   *          the alias.
   */
  protected EnhancedDetachedCriteria(String entityName, String alias) {
    super(entityName, alias);
  }

  /**
   * Constructs a new <code>EnhancedDetachedCriteria</code> instance.
   * 
   * @param entityName
   *          the entity name.
   */
  protected EnhancedDetachedCriteria(String entityName) {
    super(entityName);
  }

  /**
   * Creates an EnhancedDetachedCriteria instance.
   * 
   * @param entityName
   *          the entity name.
   * @return the created EnhancedDetachedCriteria instance.
   */
  public static EnhancedDetachedCriteria forEntityName(String entityName) {
    return new EnhancedDetachedCriteria(entityName);
  }

  /**
   * Creates an EnhancedDetachedCriteria instance.
   * 
   * @param entityName
   *          the entity name.
   * @param alias
   *          the criteria alias.
   * @return the created EnhancedDetachedCriteria instance.
   */
  public static EnhancedDetachedCriteria forEntityName(String entityName,
      String alias) {
    return new EnhancedDetachedCriteria(entityName, alias);
  }

  /**
   * Creates an EnhancedDetachedCriteria instance.
   * 
   * @param clazz
   *          the entity class.
   * @return the created EnhancedDetachedCriteria instance.
   */
  public static EnhancedDetachedCriteria forClass(Class<?> clazz) {
    return new EnhancedDetachedCriteria(clazz.getName());
  }

  /**
   * Creates an EnhancedDetachedCriteria instance.
   * 
   * @param clazz
   *          the entity class.
   * @param alias
   *          the criteria alias.
   * @return the created EnhancedDetachedCriteria instance.
   */
  public static EnhancedDetachedCriteria forClass(Class<?> clazz, String alias) {
    return new EnhancedDetachedCriteria(clazz.getName(), alias);
  }

  /**
   * Tries to retrieve an existing subcriterion for the association path before
   * creating a new one.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria createCriteria(String associationPath) {
    DetachedCriteria subCriteria = null;
    if (subCriterias == null) {
      subCriterias = new HashMap<String, DetachedCriteria>();
    } else {
      subCriteria = subCriterias.get(associationPath);
    }
    if (subCriteria == null) {
      subCriteria = super.createCriteria(associationPath);
      subCriterias.put(associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Tries to retrieve an existing subcriterion for the association path before
   * creating a new one.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria createCriteria(String associationPath, int joinType) {
    DetachedCriteria subCriteria = null;
    if (subCriterias == null) {
      subCriterias = new HashMap<String, DetachedCriteria>();
    } else {
      subCriteria = subCriterias.get(associationPath);
    }
    if (subCriteria == null) {
      subCriteria = super.createCriteria(associationPath, joinType);
      subCriterias.put(associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Tries to retrieve an existing subcriterion for the association path before
   * creating a new one.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria createCriteria(String associationPath, String alias) {
    DetachedCriteria subCriteria = null;
    if (subCriterias == null) {
      subCriterias = new HashMap<String, DetachedCriteria>();
    } else {
      subCriteria = subCriterias.get(associationPath);
    }
    if (subCriteria == null) {
      subCriteria = super.createCriteria(associationPath, alias);
      subCriterias.put(associationPath, subCriteria);
    }
    return subCriteria;
  }

  /**
   * Tries to retrieve an existing subcriterion for the association path before
   * creating a new one.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria createCriteria(String associationPath, String alias,
      int joinType) {
    DetachedCriteria subCriteria = null;
    if (subCriterias == null) {
      subCriterias = new HashMap<String, DetachedCriteria>();
    } else {
      subCriteria = subCriterias.get(associationPath);
    }
    if (subCriteria == null) {
      subCriteria = super.createCriteria(associationPath, alias, joinType);
      subCriterias.put(associationPath, subCriteria);
    }
    return subCriteria;
  }

}
