/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.query.ComparableQueryStructureDescriptor;

/**
 * An implementation used for query components.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicQueryComponentDescriptor extends
    AbstractComponentDescriptor<IQueryComponent> {

  private IComponentDescriptor<Object> componentDescriptor;

  /**
   * Constructs a new <code>BasicQueryComponentDescriptor</code> instance.
   * 
   * @param componentDescriptor
   *            the delegate entity descriptor.
   */
  public BasicQueryComponentDescriptor(
      IComponentDescriptor<Object> componentDescriptor) {
    super(componentDescriptor.getComponentContract().getName());
    this.componentDescriptor = componentDescriptor;
    Collection<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      propertyDescriptors.add(propertyDescriptor.createQueryDescriptor());
    }
    BasicCollectionDescriptor<Object> queriedEntitiesCollectionDescriptor = new BasicCollectionDescriptor<Object>();
    queriedEntitiesCollectionDescriptor.setCollectionInterface(List.class);
    queriedEntitiesCollectionDescriptor
        .setElementDescriptor(componentDescriptor);
    queriedEntitiesCollectionDescriptor
        .setName(IQueryComponent.QUERIED_COMPONENTS);
    queriedEntitiesCollectionDescriptor
        .setDescription("queriedEntities.description");
    BasicCollectionPropertyDescriptor<Object> qCPDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    qCPDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);
    qCPDescriptor.setReferencedDescriptor(queriedEntitiesCollectionDescriptor);

    propertyDescriptors.add(qCPDescriptor);
    setPropertyDescriptors(propertyDescriptors);
    setDescription(componentDescriptor.getDescription());
    setIconImageURL(componentDescriptor.getIconImageURL());
    List<String> qProperties = new ArrayList<String>();
    for (String queryableProperty : componentDescriptor
        .getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(queryableProperty);
      if (propertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getRenderedProperties()) {
          qProperties.add(propertyDescriptor.getName() + "."
              + nestedRenderedProperty);
        }
        ((BasicPropertyDescriptor) ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getPropertyDescriptor(
                ComparableQueryStructureDescriptor.COMPARATOR))
            .setI18nNameKey(propertyDescriptor.getName());
      } else {
        qProperties.add(propertyDescriptor.getName());
      }
    }
    setRenderedProperties(qProperties);
    setToStringProperty(componentDescriptor.getToStringProperty());
    setUnclonedProperties(componentDescriptor.getUnclonedProperties());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<IQueryComponent> getComponentContract() {
    return IQueryComponent.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getQueryComponentContract() {
    return componentDescriptor.getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntity() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPurelyAbstract() {
    return false;
  }
}
