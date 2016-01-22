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
package org.jspresso.framework.model.descriptor.basic;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.util.lang.StringUtils;

/**
 * Default implementation of a reference descriptor.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete component type.
 */
public class BasicReferencePropertyDescriptor<E> extends
    BasicRelationshipEndPropertyDescriptor implements
    IReferencePropertyDescriptor<E> {

  private Map<String, Object>               initializationMapping;
  private Boolean                           oneToOne;
  private Integer                           pageSize;
  private IComponentDescriptor<? extends E> referencedDescriptor;
  private List<String>                      queryableProperties;
  private List<String>                      renderedProperties;

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
    IComponentDescriptor<? extends E> realReferencedDescriptor = queryDescriptor
        .getReferencedDescriptor();
    IComponentDescriptor<? extends E> queryReferencedDescriptor = realReferencedDescriptor
        .createQueryDescriptor();
    queryDescriptor.setReferencedDescriptor(queryReferencedDescriptor);
    return queryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<? extends E> getComponentDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getInitializationMapping() {
    return initializationMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return getReferencedDescriptor().getComponentContract();
  }

  /**
   * Gets the pageSize.
   *
   * @return the pageSize.
   */
  @Override
  public Integer getPageSize() {
    if (pageSize == null) {
      return getComponentDescriptor().getPageSize();
    }
    return pageSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<? extends E> getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Gets the oneToOne.
   *
   * @return the oneToOne.
   */
  @Override
  public boolean isOneToOne() {
    if (getReverseRelationEnd() != null) {
      // priory ty is given to the reverse relation end.
      return getReverseRelationEnd() instanceof IReferencePropertyDescriptor<?>;
    }
    if (oneToOne != null) {
      return oneToOne;
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
   * This property allows to pre-initialize UI filters that are based on this
   * reference property. This includes :
   * <ul>
   * <li>explicit filters that are displayed for &quot;list of values&quot;</li>
   * <li>implicit filters that are use behind the scene for UI auto-completion</li>
   * </ul>
   * <p>
   * The initialization mapping property is a {@code Map} keyed by
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
   * ends as being reference properties turns {@code oneToOne=true}
   * automatically. But when the relationship is not bi-directional, Jspresso
   * has no mean to determine if the reference property is &quot;N-1&quot; or
   * &quot;1-1&quot;. Setting this property allows to inform Jspresso about it.
   * <p>
   * Default value is {@code false}.
   *
   * @param oneToOne
   *          the oneToOne to set.
   */
  public void setOneToOne(boolean oneToOne) {
    this.oneToOne = oneToOne;
  }

  /**
   * This property allows for defining the page size of &quot;lists of
   * values&quot; that are built by the UI for this reference property. Whenever
   * the {@code pageSize} property is set to {@code null} on the
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
      IComponentDescriptor<? extends E> referencedDescriptor) {
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

  /**
   * This property allows to define which of the component properties are to be
   * rendered by default when displaying a list of value on this component
   * family. For instance, a table will render 1 column per rendered property of
   * the component. Any type of property can be used except collection
   * properties. Since this is a {@code List} queryable properties are
   * rendered in the same order.
   * <p>
   * Whenever this property is {@code null} (default value) Jspresso
   * determines the default set of properties to render based on the referenced
   * component descriptor.
   *
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = StringUtils.ensureSpaceFree(renderedProperties);
  }

  /**
   * This property allows to define which of the component properties are to be
   * used in the list of value filter that are based on this component family.
   * Since this is a {@code List} queryable properties are rendered in the
   * same order.
   * <p>
   * Whenever this this property is {@code null} (default value), Jspresso
   * chooses the default set of queryable properties based on the referenced
   * component descriptor.
   *
   * @param queryableProperties
   *          the queryableProperties to set.
   */
  public void setQueryableProperties(List<String> queryableProperties) {
    this.queryableProperties = StringUtils.ensureSpaceFree(queryableProperties);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getQueryableProperties() {
    if (queryableProperties == null) {
      return getReferencedDescriptor().getQueryableProperties();
    }
    return AbstractComponentDescriptor.explodeComponentReferences(
        getReferencedDescriptor(), queryableProperties);
    // return queryableProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      return getReferencedDescriptor().getRenderedProperties();
    }
    return AbstractComponentDescriptor.explodeComponentReferences(
        getReferencedDescriptor(), renderedProperties);
    // return renderedProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultMandatory() {
    return EntityHelper.isInlineComponentReference(this);
  }
}
