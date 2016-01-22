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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.ICollectionPropertyProcessor;
import org.jspresso.framework.util.bean.integrity.IPropertyProcessor;
import org.jspresso.framework.util.collection.ESort;

/**
 * This descriptor is used to describe collection properties that are used
 * either as &quot;1-N&quot; or &quot;N-N&quot; &quot;N&quot; relationship end.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete collection component element type.
 */
public class BasicCollectionPropertyDescriptor<E> extends
    BasicRelationshipEndPropertyDescriptor implements
    ICollectionPropertyDescriptor<E> {

  private Boolean                  manyToMany;
  private Map<String, ESort>       orderingProperties;
  private ICollectionDescriptor<E> referencedDescriptor;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicCollectionPropertyDescriptor<E> clone() {
    BasicCollectionPropertyDescriptor<E> clonedDescriptor = (BasicCollectionPropertyDescriptor<E>) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return getReferencedDescriptor().getCollectionInterface();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, ESort> getOrderingProperties() {
    if (orderingProperties != null) {
      return orderingProperties;
    }
    if (getReferencedDescriptor() != null) {
      return getReferencedDescriptor().getOrderingProperties();
    }
    return null;
  }

  /**
   * Gets the referencedDescriptor.
   *
   * @return the referencedDescriptor.
   */
  @Override
  public ICollectionDescriptor<E> getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Gets the manyToMany.
   *
   * @return the manyToMany.
   */
  @Override
  public boolean isManyToMany() {
    if (getReverseRelationEnd() != null) {
      // priory ty is given to the reverse relation end.
      return getReverseRelationEnd() instanceof ICollectionPropertyDescriptor<?>;
    }
    if (manyToMany != null) {
      return manyToMany;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <F, G> void postprocessAdder(F component, Collection<G> collection,
      G addedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<F, Collection<G>, G> processor;
      processor = (ICollectionPropertyProcessor<F, Collection<G>, G>) propertyIntegrityProcessor;
      processor.postprocessAdder(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <F, G>  void postprocessRemover(F component, Collection<G> collection,
      G removedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<F, Collection<G>, G> processor;
      processor = (ICollectionPropertyProcessor<F, Collection<G>, G>) propertyIntegrityProcessor;
      processor.postprocessRemover(component, collection, removedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <F, G> void preprocessAdder(F component, Collection<G> collection,
      G addedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<F, Collection<G>, G> processor;
      processor = (ICollectionPropertyProcessor<F, Collection<G>, G>) propertyIntegrityProcessor;
      processor.preprocessAdder(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <F, G> void preprocessRemover(F component,
      Collection<G> collection, G removedValue) {
    // Mandatory checking should only happen on save. See bug #776 and #646.
    // if (isMandatory() && collection != null && collection.size() == 1
    // && collection.contains(removedValue)) {
    // throw new MandatoryPropertyException(this, component);
    // }
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<F, Collection<G>, G> processor;
      processor = (ICollectionPropertyProcessor<F, Collection<G>, G>) propertyIntegrityProcessor;
      processor.preprocessRemover(component, collection, removedValue);
    }
  }

  /**
   * Forces the collection property to be considered as a many to many
   * (&quot;N-N&quot;) end. When a relationship is bi-directional, setting both
   * ends as being collection properties turns {@code manyToMany=true}
   * automatically. But when the relationship is not bi-directional, Jspresso
   * has no mean to determine if the collection property is &quot;1-N&quot; or
   * &quot;N-N&quot;. Setting this property allows to inform Jspresso about it.
   * <p>
   * Default value is {@code false}.
   *
   * @param manyToMany
   *          the manyToMany to set.
   */
  public void setManyToMany(boolean manyToMany) {
    this.manyToMany = manyToMany;
  }

  /**
   * Ordering properties are used to sort this collection property if and only
   * if it is un-indexed (not a {@code List}). The sort order set on the
   * collection property can refine the default one that might have been set on
   * the referenced collection level. This property consist of a
   * {@code Map} whose entries are composed with :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the {@code ESort} enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or its
   * equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator. A {@code null} value (default) will not give any indication
   * for the collection property sort order and thus, will delegate to higher
   * specification levels (i.e. the referenced collection sort order).
   *
   * @param untypedOrderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ?> untypedOrderingProperties) {
    if (untypedOrderingProperties != null) {
      orderingProperties = new LinkedHashMap<>();
      for (Map.Entry<String, ?> untypedOrderingProperty : untypedOrderingProperties
          .entrySet()) {
        if (untypedOrderingProperty.getValue() instanceof ESort) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              (ESort) untypedOrderingProperty.getValue());
        } else if (untypedOrderingProperty.getValue() instanceof String) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              ESort.valueOf((String) untypedOrderingProperty.getValue()));
        } else {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              ESort.ASCENDING);
        }
      }
    } else {
      orderingProperties = null;
    }
  }

  /**
   * Qualifies the type of collection this property refers to. As of now,
   * Jspresso supports :
   * <ul>
   * <li>collections with {@code Set} semantic: do not allow for duplicates
   * and do not preserve the order of the elements in the data store</li>
   * <li>collections with {@code List} semantic: allows for duplicates and
   * preserves the order of the elements in the data store through an implicit
   * index column</li>
   * </ul>
   *
   * @param referencedDescriptor
   *          the referencedDescriptor to set.
   */
  public void setReferencedDescriptor(
      ICollectionDescriptor<E> referencedDescriptor) {
    this.referencedDescriptor = referencedDescriptor;
  }

  /**
   * return true for a 1-N relationship and false for a N-N relationship.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultComposition() {
    return !isManyToMany();
  }

  /**
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultSortablility() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public BasicRelationshipEndPropertyDescriptor createQueryDescriptor() {
    BasicCollectionPropertyDescriptor<E> defaultQueryDescriptor = (BasicCollectionPropertyDescriptor<E>) super
        .createQueryDescriptor();

    BasicReferencePropertyDescriptor<E> queryDescriptor = new BasicReferencePropertyDescriptor<>();
    queryDescriptor.setName(defaultQueryDescriptor.getName());
    queryDescriptor.setI18nNameKey(defaultQueryDescriptor.getI18nNameKey());
    queryDescriptor.setDescription(defaultQueryDescriptor.getDescription());
    queryDescriptor.setGrantedRoles(defaultQueryDescriptor.getGrantedRoles());
    queryDescriptor.setMandatory(defaultQueryDescriptor.isMandatory());
    queryDescriptor.setReadabilityGates(defaultQueryDescriptor
        .getReadabilityGates());
    queryDescriptor.setReadOnly(defaultQueryDescriptor.isReadOnly());
    queryDescriptor.setSqlName(defaultQueryDescriptor.getSqlName());
    queryDescriptor.setWritabilityGates(defaultQueryDescriptor
        .getWritabilityGates());

    queryDescriptor.setDelegateClassName(defaultQueryDescriptor
        .getDelegateClassName());
    queryDescriptor.setComputed(defaultQueryDescriptor.isComputed());

    queryDescriptor.setReferencedDescriptor(getReferencedDescriptor()
        .getElementDescriptor().createQueryDescriptor());
    return queryDescriptor;
  }
}
