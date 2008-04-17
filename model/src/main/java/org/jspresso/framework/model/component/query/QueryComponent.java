/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component.query;

import java.util.List;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ObjectEqualityMap;


/**
 * The default implementation of a query component.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryComponent extends ObjectEqualityMap<String, Object> implements
    IQueryComponent {

  private static final long       serialVersionUID = 4271673164192796253L;

  private IComponentDescriptor<?> componentDescriptor;

  /**
   * Constructs a new <code>QueryComponent</code> instance.
   * 
   * @param componentDescriptor
   *            the query componentDescriptor.
   */
  public QueryComponent(IComponentDescriptor<?> componentDescriptor) {
    this.componentDescriptor = componentDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public List<? extends IComponent> getQueriedComponents() {
    return (List<? extends IComponent>) get(QUERIED_COMPONENTS);
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getQueryContract() {
    return componentDescriptor.getQueryComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public void setQueriedComponents(List<? extends IComponent> queriedComponents) {
    put(QUERIED_COMPONENTS, queriedComponents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object get(Object key) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor((String) key);
    Object actualValue = super.get(key);
    if (actualValue == null
        && propertyDescriptor instanceof IReferencePropertyDescriptor) {
      IComponentDescriptor<?> referencedDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      if (isInlineComponentDescriptor(referencedDescriptor)) {
        QueryComponent referencedQueryComponent = new QueryComponent(referencedDescriptor);
        put((String) key, referencedQueryComponent);
        return referencedQueryComponent;
      }
    }
    return actualValue;
  }

  private boolean isInlineComponentDescriptor(
      IComponentDescriptor<?> referencedComponentDescriptor) {
    return !IEntity.class.isAssignableFrom(referencedComponentDescriptor
        .getComponentContract())
        && !referencedComponentDescriptor.isPurelyAbstract();
  }
}
