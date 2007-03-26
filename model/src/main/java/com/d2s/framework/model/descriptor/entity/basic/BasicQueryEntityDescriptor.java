/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.entity.basic;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.d2s.framework.model.component.service.IComponentService;
import com.d2s.framework.model.component.service.ILifecycleInterceptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Default implementation of entity descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicQueryEntityDescriptor implements
    IComponentDescriptor<IEntity> {

  private IComponentDescriptor<IEntity>              entityDescriptor;
  private Class<? extends IEntity>                   queryContract;
  private BasicCollectionPropertyDescriptor<IEntity> queriedEntitiesPropertyDescriptor;

  /**
   * Constructs a new <code>BasicQueryEntityDescriptor</code> instance.
   *
   * @param entityDescriptor
   *          the delegate entity descriptor.
   * @param queryContract
   *          the real contract this query entity has.
   */
  public BasicQueryEntityDescriptor(
      IComponentDescriptor<IEntity> entityDescriptor,
      Class<? extends IEntity> queryContract) {
    this.entityDescriptor = entityDescriptor;
    this.queryContract = queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getDeclaredPropertyDescriptors() {
    Collection<IPropertyDescriptor> declaredPropertyDescriptors = entityDescriptor
        .getDeclaredPropertyDescriptors();
    declaredPropertyDescriptors.add(getQueriedEntitiesPropertyDescriptor());
    return declaredPropertyDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    if (IQueryEntity.QUERIED_ENTITIES.equals(propertyName)) {
      return getQueriedEntitiesPropertyDescriptor();
    }
    return entityDescriptor.getPropertyDescriptor(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getPropertyDescriptors() {
    Collection<IPropertyDescriptor> propertyDescriptors = entityDescriptor
        .getPropertyDescriptors();
    propertyDescriptors.add(getQueriedEntitiesPropertyDescriptor());
    return propertyDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<? extends IEntity> getComponentDescriptor() {
    return this;
  }

  /**
   * Gets the queriedEntitiesPropertyDescriptor.
   *
   * @return the queriedEntitiesPropertyDescriptor.
   */
  private BasicCollectionPropertyDescriptor getQueriedEntitiesPropertyDescriptor() {
    if (queriedEntitiesPropertyDescriptor == null) {
      BasicCollectionDescriptor<IEntity> queriedEntitiesCollectionDescriptor = new BasicCollectionDescriptor<IEntity>();
      queriedEntitiesCollectionDescriptor.setCollectionInterface(Set.class);
      queriedEntitiesCollectionDescriptor
          .setElementDescriptor(entityDescriptor);
      queriedEntitiesCollectionDescriptor
          .setName(IQueryEntity.QUERIED_ENTITIES);
      queriedEntitiesCollectionDescriptor
          .setDescription("queriedEntities.description");
      queriedEntitiesPropertyDescriptor = new BasicCollectionPropertyDescriptor<IEntity>();
      queriedEntitiesPropertyDescriptor.setName(IQueryEntity.QUERIED_ENTITIES);
      queriedEntitiesPropertyDescriptor
          .setReferencedDescriptor(queriedEntitiesCollectionDescriptor);
    }
    return queriedEntitiesPropertyDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<? extends IEntity> getComponentContract() {
    return queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return entityDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return entityDescriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return entityDescriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return entityDescriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  public List<ILifecycleInterceptor> getLifecycleInterceptors() {
    return entityDescriptor.getLifecycleInterceptors();
  }

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return queryContract;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return entityDescriptor.getName();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getOrderingProperties() {
    return entityDescriptor.getOrderingProperties();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getQueryableProperties() {
    return entityDescriptor.getQueryableProperties();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    return entityDescriptor.getRenderedProperties();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Class> getServiceContracts() {
    return entityDescriptor.getServiceContracts();
  }

  /**
   * {@inheritDoc}
   */
  public IComponentService getServiceDelegate(Method targetMethod) {
    return entityDescriptor.getServiceDelegate(targetMethod);
  }

  /**
   * {@inheritDoc}
   */
  public String getToStringProperty() {
    return entityDescriptor.getToStringProperty();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getUnclonedProperties() {
    return entityDescriptor.getUnclonedProperties();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return entityDescriptor.isComputed();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPurelyAbstract() {
    return entityDescriptor.isPurelyAbstract();
  }

}
