/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.component.service.IComponentService;
import com.d2s.framework.model.component.service.ILifecycleInterceptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * An implementation used for query components.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @param <E>
 *            the concrete type of component.
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicQueryComponentDescriptor<E> implements IComponentDescriptor<E> {

  private IComponentDescriptor<E>              componentDescriptor;
  private BasicCollectionPropertyDescriptor<E> queriedComponentsPropertyDescriptor;
  private Class<? extends E>                   queryContract;

  /**
   * Constructs a new <code>BasicQueryComponentDescriptor</code> instance.
   * 
   * @param componentDescriptor
   *            the delegate entity descriptor.
   * @param queryContract
   *            the real contract this query component has.
   */
  public BasicQueryComponentDescriptor(
      IComponentDescriptor<E> componentDescriptor,
      Class<? extends E> queryContract) {
    this.componentDescriptor = componentDescriptor;
    this.queryContract = queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public Class<? extends E> getComponentContract() {
    return queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getComponentDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getDeclaredPropertyDescriptors() {
    Collection<IPropertyDescriptor> declaredPropertyDescriptors = componentDescriptor
        .getDeclaredPropertyDescriptors();
    declaredPropertyDescriptors.add(getQueriedEntitiesPropertyDescriptor());
    return declaredPropertyDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return componentDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return componentDescriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return componentDescriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return componentDescriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  public List<ILifecycleInterceptor<?>> getLifecycleInterceptors() {
    return componentDescriptor.getLifecycleInterceptors();
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return componentDescriptor.getName();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getOrderingProperties() {
    return componentDescriptor.getOrderingProperties();
  }

  /**
   * {@inheritDoc}
   */
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    if (IQueryComponent.QUERIED_COMPONENTS.equals(propertyName)) {
      return getQueriedEntitiesPropertyDescriptor();
    }
    return componentDescriptor.getPropertyDescriptor(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getPropertyDescriptors() {
    Collection<IPropertyDescriptor> propertyDescriptors = componentDescriptor
        .getPropertyDescriptors();
    propertyDescriptors.add(getQueriedEntitiesPropertyDescriptor());
    return propertyDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getQueryableProperties() {
    return componentDescriptor.getQueryableProperties();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    return componentDescriptor.getRenderedProperties();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Class<?>> getServiceContracts() {
    return componentDescriptor.getServiceContracts();
  }

  /**
   * {@inheritDoc}
   */
  public IComponentService getServiceDelegate(Method targetMethod) {
    return componentDescriptor.getServiceDelegate(targetMethod);
  }

  /**
   * {@inheritDoc}
   */
  public String getToStringProperty() {
    return componentDescriptor.getToStringProperty();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getUnclonedProperties() {
    return componentDescriptor.getUnclonedProperties();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return componentDescriptor.isComputed();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntity() {
    return componentDescriptor.isEntity();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPurelyAbstract() {
    return componentDescriptor.isPurelyAbstract();
  }

  /**
   * Gets the queriedComponentsPropertyDescriptor.
   * 
   * @return the queriedComponentsPropertyDescriptor.
   */
  private BasicCollectionPropertyDescriptor<E> getQueriedEntitiesPropertyDescriptor() {
    if (queriedComponentsPropertyDescriptor == null) {
      BasicCollectionDescriptor<E> queriedEntitiesCollectionDescriptor = new BasicCollectionDescriptor<E>();
      queriedEntitiesCollectionDescriptor.setCollectionInterface(Set.class);
      queriedEntitiesCollectionDescriptor
          .setElementDescriptor(componentDescriptor);
      queriedEntitiesCollectionDescriptor
          .setName(IQueryComponent.QUERIED_COMPONENTS);
      queriedEntitiesCollectionDescriptor
          .setDescription("queriedEntities.description");
      queriedComponentsPropertyDescriptor = new BasicCollectionPropertyDescriptor<E>();
      queriedComponentsPropertyDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);
      queriedComponentsPropertyDescriptor
          .setReferencedDescriptor(queriedEntitiesCollectionDescriptor);
    }
    return queriedComponentsPropertyDescriptor;
  }
}
