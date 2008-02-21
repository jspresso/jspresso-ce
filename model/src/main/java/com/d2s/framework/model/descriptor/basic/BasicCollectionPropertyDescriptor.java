/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Collection;
import java.util.List;

import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.util.bean.integrity.ICollectionIntegrityProcessor;
import com.d2s.framework.util.bean.integrity.IPropertyIntegrityProcessor;

/**
 * Default implementation of a collection descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete collection component element type.
 */
public class BasicCollectionPropertyDescriptor<E> extends
    BasicRelationshipEndPropertyDescriptor implements
    ICollectionPropertyDescriptor<E> {

  private Boolean                  manyToMany;
  private List<String>             orderingProperties;
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
   * Gets the orderingProperties.
   * 
   * @return the orderingProperties.
   */
  public List<String> getOrderingProperties() {
    if (orderingProperties != null) {
      return orderingProperties;
    }
    if (getParentDescriptor() != null) {
      return ((ICollectionPropertyDescriptor<?>) getParentDescriptor())
          .getOrderingProperties();
    }
    return getReferencedDescriptor().getElementDescriptor()
        .getOrderingProperties();
  }

  /**
   * Gets the referencedDescriptor.
   * 
   * @return the referencedDescriptor.
   */
  @SuppressWarnings("unchecked")
  public ICollectionDescriptor<E> getReferencedDescriptor() {
    if (referencedDescriptor != null) {
      return referencedDescriptor;
    }
    if (getParentDescriptor() != null) {
      return ((ICollectionPropertyDescriptor<E>) getParentDescriptor())
          .getReferencedDescriptor();
    }
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
      return getReverseRelationEnd() instanceof ICollectionPropertyDescriptor;
    }
    if (manyToMany != null) {
      return manyToMany.booleanValue();
    }
    if (getParentDescriptor() != null) {
      return ((ICollectionPropertyDescriptor<?>) getParentDescriptor())
          .isManyToMany();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void postprocessAdder(Object component, Collection<?> collection,
      Object addedValue) {
    List<IPropertyIntegrityProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionIntegrityProcessor<Object, Collection<?>> processor =
        (ICollectionIntegrityProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.postprocessAdderIntegrity(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void postprocessRemover(Object component, Collection<?> collection,
      Object removedValue) {
    List<IPropertyIntegrityProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionIntegrityProcessor<Object, Collection<?>> processor =
        (ICollectionIntegrityProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor
          .postprocessRemoverIntegrity(component, collection, removedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessAdder(Object component, Collection<?> collection,
      Object addedValue) {
    List<IPropertyIntegrityProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionIntegrityProcessor<Object, Collection<?>> processor =
        (ICollectionIntegrityProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.preprocessAdderIntegrity(component, collection, addedValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void preprocessRemover(Object component, Collection<?> collection,
      Object removedValue) {
    List<IPropertyIntegrityProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<?, ?> propertyIntegrityProcessor : processors) {
      ICollectionIntegrityProcessor<Object, Collection<?>> processor =
        (ICollectionIntegrityProcessor<Object, Collection<?>>) propertyIntegrityProcessor;
      processor.preprocessRemoverIntegrity(component, collection, removedValue);
    }
  }

  /**
   * Sets the manyToMany.
   * 
   * @param manyToMany
   *            the manyToMany to set.
   */
  public void setManyToMany(boolean manyToMany) {
    this.manyToMany = new Boolean(manyToMany);
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param orderingProperties
   *            the orderingProperties to set.
   */
  public void setOrderingProperties(List<String> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * Sets the referencedDescriptor.
   * 
   * @param referencedDescriptor
   *            the referencedDescriptor to set.
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
