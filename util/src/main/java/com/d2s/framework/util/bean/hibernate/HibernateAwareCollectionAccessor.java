/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.util.bean.ICollectionAccessor;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateAwareCollectionAccessor extends
    HibernateAwarePropertyAccessor implements ICollectionAccessor {

  /**
   * Constructs a new <code>HibernateAwareCollectionAccessor</code> instance.
   * 
   * @param delegate
   * @param hibernateTemplate
   */
  public HibernateAwareCollectionAccessor(ICollectionAccessor delegate,
      HibernateTemplate hibernateTemplate, IApplicationSession applicationSession) {
    super(delegate, hibernateTemplate, applicationSession);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  public void addToValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    getDelegate().addToValue(target, value);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    getDelegate().removeFromValue(target, value);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ICollectionAccessor getDelegate() {
    return (ICollectionAccessor) super.getDelegate();
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Collection getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return (Collection) super.getValue(target);
  }

}
