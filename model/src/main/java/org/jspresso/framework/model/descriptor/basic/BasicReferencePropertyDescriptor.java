/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.basic;

import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Default implementation of a reference descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component type.
 */
public class BasicReferencePropertyDescriptor<E> extends
    BasicRelationshipEndPropertyDescriptor implements
    IReferencePropertyDescriptor<E> {

  private EFetchType              fetchType = EFetchType.SELECT;
  private Map<String, Object>     initializationMapping;
  private Boolean                 oneToOne;
  private Integer                 pageSize;
  private IComponentDescriptor<E> referencedDescriptor;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicReferencePropertyDescriptor<E> clone() {
    BasicReferencePropertyDescriptor<E> clonedDescriptor = (BasicReferencePropertyDescriptor<E>) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicReferencePropertyDescriptor<E> createQueryDescriptor() {
    BasicReferencePropertyDescriptor<E> queryDescriptor = (BasicReferencePropertyDescriptor<E>) super
        .createQueryDescriptor();
    IComponentDescriptor<E> realReferencedDescriptor = queryDescriptor
        .getReferencedDescriptor();
    IComponentDescriptor<E> queryReferencedDescriptor = realReferencedDescriptor
        .createQueryDescriptor();
    queryDescriptor.setReferencedDescriptor(queryReferencedDescriptor);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getComponentDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * Gets the fetchType.
   * 
   * @return the fetchType.
   */
  public EFetchType getFetchType() {
    return fetchType;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> getInitializationMapping() {
    return initializationMapping;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return getReferencedDescriptor().getComponentContract();
  }

  /**
   * Gets the pageSize.
   * 
   * @return the pageSize.
   */
  public Integer getPageSize() {
    if (pageSize == null) {
      return getComponentDescriptor().getPageSize();
    }
    return pageSize;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Gets the oneToOne.
   * 
   * @return the oneToOne.
   */
  public boolean isOneToOne() {
    if (getReverseRelationEnd() != null) {
      // priory ty is given to the reverse relation end.
      return getReverseRelationEnd() instanceof IReferencePropertyDescriptor<?>;
    }
    if (oneToOne != null) {
      return oneToOne.booleanValue();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return getDelegateClassName() == null;
  }

  /**
   * This property allows to finely tune fetching strategy of the ORM on this relationship end. This is either a value of the
   * <code>EFetchType</code> enum or its equivalent string representation :
   * <ul>
   * <li><code>SELECT</code> for default 2nd select strategy (lazy)</li>
   * <li><code>JOIN</code> for a join select strategy (not lazy)</li>
   * </ul>
   * <p>
   * Default value is <code>EFetchType.JOIN</code>, i.e. 2nd select strategy.
   * 
   * @param fetchType the fetchType to set.
   */
  public void setFetchType(EFetchType fetchType) {
    this.fetchType = fetchType;
  }

  /**
   * This property allows to pre-initialize UI filters that are based on this
   * reference property. This includes :
   * <ul>
   * <li>explicit filters that are dispayed for &quot;list of values&quot;</li>
   * <li>implicit filters thet are use behind the scene for UI auto-completion</li>
   * </ul>
   * <p>
   * The initialization mapping property is a <code>Map</code> keyed by
   * referenced type property names (the properties to be initialized).
   * <p>
   * Values in this map can be either :
   * <ul>
   * <li>a <b>constant value</b>. In that case, the filter property is
   * initialize with this constant value.</li>
   * <li>a owning component <b>property name</b>. In that case, the filter
   * property is initialize with the value of the owning component property.</li>
   * </ul>
   * 
   * @param initializationMapping
   *          the initializationMapping to set.
   */
  public void setInitializationMapping(Map<String, Object> initializationMapping) {
    this.initializationMapping = initializationMapping;
  }

  /**
   * Forces the reference property to be considered as a one to one
   * (&quot;1-1&quot;) end. When a relationship is bi-directional, setting both
   * ends as being reference properties turns <code>oneToOne=true</code>
   * automatically. But when the relationship is not bi-directional, Jspresso
   * has no mean to determine if the reference property is &quot;N-1&quot; or
   * &quot;1-1&quot;. Setting this property allows to inform Jspresso about it.
   * <p>
   * Default value is <code>false</code>.
   * 
   * @param oneToOne
   *          the oneToOne to set.
   */
  public void setOneToOne(boolean oneToOne) {
    this.oneToOne = new Boolean(oneToOne);
  }

  /**
   * This property allows for defining the page size of &quot;lists of
   * values&quot; that are built by the UI for this reference property. Whenever
   * the <code>pageSize</code> property is set to <code>null</code> on the
   * reference property level, Jspresso falls back to the element type default
   * page size or turns off paging if the former is also not set.
   * 
   * @param pageSize
   *          the pageSize to set.
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  
  /**
   * Qualifies the type of element this property refers to. It may point to any
   * type of component descriptor, i.e. entity, interface or component
   * descriptor.
   * 
   * @param referencedDescriptor
   *          the referencedDescriptor to set.
   */
  public void setReferencedDescriptor(
      IComponentDescriptor<E> referencedDescriptor) {
    this.referencedDescriptor = referencedDescriptor;
  }

  
  /**
   * return false.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultComposition() {
    // if (getReverseRelationEnd() == null
    // || getReverseRelationEnd() instanceof IReferencePropertyDescriptor<?>) {
    // return true;
    // }
    return false;
  }
}
