/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision$
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
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return getReferencedDescriptor().getCollectionInterface();
  }

  /**
   * Ordering properties are used to sort this collection property if and only
   * if it is un-indexed (not a <code>List</code>). The sort order set on the
   * collection property can refine the default one that might have been set on
   * the referenced collection level. This property consist of a
   * <code>Map</code> whose entries are composed with :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the <code>ESort</code> enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or
   * their equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator. A <code>null</code> value (default) will not give any indication
   * for the collection property sort order and thus, will delegate to higher
   * specification levels (i.e. the referenced collection sort order).
   * 
   * @return the orderingProperties.
   */
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
  public ICollectionDescriptor<E> getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Gets the manyToMany.
   * 
   * @return the manyToMany.
   */
  public boolean isManyToMany() {
    if (getReverseRelationEnd() != null) {
      // priory ty is given to the reverse relation end.
      return getReverseRelationEnd() instanceof ICollectionPropertyDescriptor<?>;
    }
    if (manyToMany != null) {
      return manyToMany.booleanValue();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void postprocessAdder(Object component, Collection<?> collection,
      Object addedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<Object, Collection<?>> processor;
      processor = (ICollectionPropertyProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.postprocessAdder(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void postprocessRemover(Object component, Collection<?> collection,
      Object removedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<Object, Collection<?>> processor;
      processor = (ICollectionPropertyProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.postprocessRemover(component, collection, removedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessAdder(Object component, Collection<?> collection,
      Object addedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<Object, Collection<?>> processor;
      processor = (ICollectionPropertyProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.preprocessAdder(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessRemover(Object component, Collection<?> collection,
      Object removedValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionPropertyProcessor<Object, Collection<?>> processor;
      processor = (ICollectionPropertyProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.preprocessRemover(component, collection, removedValue);
    }
  }

  /**
   * Forces the collection property to be considered as a many to many
   * (&quot;N-N&quot;) end. When a relationship is bi-directional, setting both
   * ends as being collection properties turns <code>manyToMany=true</code>
   * automatically. But when the relationship is not bi-directional, Jspresso
   * has no mean to determine if the collection property is &quot;1-N&quot; or
   * &quot;N-N&quot;. Setting this property allows to inform Jspresso about it.
   * <p>
   * Default value is <code>false</code>.
   * 
   * @param manyToMany
   *          the manyToMany to set.
   */
  public void setManyToMany(boolean manyToMany) {
    this.manyToMany = new Boolean(manyToMany);
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param untypedOrderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ?> untypedOrderingProperties) {
    if (untypedOrderingProperties != null) {
      orderingProperties = new LinkedHashMap<String, ESort>();
      for (Map.Entry<String, ?> untypedOrderingProperty : untypedOrderingProperties
          .entrySet()) {
        if (untypedOrderingProperty.getValue() instanceof ESort) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              (ESort) untypedOrderingProperty.getValue());
        } else if (untypedOrderingProperty.getValue() instanceof String) {
          orderingProperties.put(untypedOrderingProperty.getKey(), ESort
              .valueOf((String) untypedOrderingProperty.getValue()));
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
   * <li>collections with <code>Set</code> semantic: do not allow for duplicates
   * and do not preserve the order of the elements in the datastore</li>
   * <li>collections with <code>List</code> semantic: allows for duplicates and
   * preserves the order of the elements in the datastore through an implicit
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
    if (getReverseRelationEnd() == null
        || getReverseRelationEnd() instanceof ICollectionPropertyDescriptor<?>) {
      return false;
    }
    return true;
  }
}
