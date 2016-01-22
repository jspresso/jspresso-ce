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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.jspresso.framework.util.reflect.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An enhanced detached criteria that holds a sub-criteria registry.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class EnhancedDetachedCriteria extends DetachedCriteria {

  private static final long                                    serialVersionUID = 1477297471425065631L;
  private static final Logger                                  LOG              = LoggerFactory
                                                                                    .getLogger(EnhancedDetachedCriteria.class);

  private Map<DetachedCriteria, Map<String, DetachedCriteria>> subCriteriaRegistry;
  private Projection                                           currentProjection;
  private ResultTransformer                                    currentResultTransformer;
  private List<Order>                                          currentOrders;

  /**
   * Constructs a new {@code EnhancedDetachedCriteria} instance.
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
   * Constructs a new {@code EnhancedDetachedCriteria} instance.
   *
   * @param entityName
   *          entity name.
   */
  protected EnhancedDetachedCriteria(String entityName) {
    super(entityName);
  }

  /**
   * Constructs a new {@code EnhancedDetachedCriteria} instance.
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
   * Creates a new enhanced detached criteria.
   *
   * @param clazz
   *          class.
   * @return the new enhanced detached criteria.
   */
  public static EnhancedDetachedCriteria forClass(Class clazz) {
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
  public static EnhancedDetachedCriteria forClass(Class clazz, String alias) {
    return new EnhancedDetachedCriteria(clazz.getName(), alias);
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
   * @deprecated Use
   *             {@link #getSubCriteriaFor(DetachedCriteria, String, JoinType)}
   *             instead.
   */
  @Deprecated
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath, int joinType) {
    return getSubCriteriaFor(masterCriteria, associationPath,
        JoinType.parse(joinType));
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
      String associationPath, JoinType joinType) {
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
   * @deprecated use
   *             {@link #getSubCriteriaFor(DetachedCriteria, String, String, JoinType)}
   *             instead.
   */
  @Deprecated
  public DetachedCriteria getSubCriteriaFor(DetachedCriteria masterCriteria,
      String associationPath, String alias, int joinType) {
    return getSubCriteriaFor(masterCriteria, associationPath, alias,
        JoinType.parse(joinType));
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
      String associationPath, String alias, JoinType joinType) {
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
   * Keeps track of the current projection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria setProjection(Projection proj) {
    DetachedCriteria returnedCrit = super.setProjection(proj);
    currentProjection = proj;
    return returnedCrit;
  }

  /**
   * Keeps track of the current criteria result transformer.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria setResultTransformer(ResultTransformer transformer) {
    DetachedCriteria returnedCrit = super.setResultTransformer(transformer);
    currentResultTransformer = transformer;
    return returnedCrit;
  }

  /**
   * Gets the current criteria projection.
   *
   * @return the current criteria projection.
   */
  public Projection getProjection() {
    return currentProjection;
  }

  /**
   * Gets the current criteria result transformer.
   *
   * @return the current criteria result transformer.
   */
  public ResultTransformer getResultTransformer() {
    return currentResultTransformer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DetachedCriteria addOrder(Order order) {
    DetachedCriteria returnedCrit = super.addOrder(order);
    if (currentOrders == null) {
      currentOrders = new ArrayList<>();
      currentOrders.add(order);
    }
    return returnedCrit;
  }

  /**
   * Gets the current criteria orders.
   *
   * @return the current criteria orders.
   */
  public List<Order> getOrders() {
    if (currentOrders == null) {
      return null;
    }
    return new ArrayList<>(currentOrders);
  }

  /**
   * Removes an order by clause.
   */
  public void removeAllOrders() {
    try {
      List<Order> orders = privateGetOrders();
      orders.clear();
      currentOrders = null;
    } catch (Throwable ex) {
      LOG.error("An error occurred when removing orders.", ex);
    }
  }

  @SuppressWarnings("unchecked")
  private List<Order> privateGetOrders() throws IllegalAccessException,
      NoSuchFieldException {
    CriteriaImpl critImpl = (CriteriaImpl) ReflectHelper.getPrivateFieldValue(
        DetachedCriteria.class, "impl", this);
    List<Order> orders = (List<Order>) ReflectHelper.getPrivateFieldValue(
        CriteriaImpl.class, "orderEntries", critImpl);
    return orders;
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
      subCriteriaRegistry = new HashMap<>();
    }
    Map<String, DetachedCriteria> subCriterias = subCriteriaRegistry
        .get(masterCriteria);
    if (subCriterias == null) {
      subCriterias = new HashMap<>();
      subCriteriaRegistry.put(masterCriteria, subCriterias);
    }
    subCriterias.put(associationPath, subCriteria);
  }
}
