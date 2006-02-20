/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.util.bean.IListAccessor;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateAwareListAccessor extends
    HibernateAwareCollectionAccessor implements IListAccessor {

  /**
   * Constructs a new <code>HibernateAwareListAccessor</code> instance.
   * 
   * @param delegate
   * @param hibernateTemplate
   */
  public HibernateAwareListAccessor(IListAccessor delegate,
      HibernateTemplate hibernateTemplate, IApplicationSession applicationSession) {
    super(delegate, hibernateTemplate, applicationSession);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  public void addToValue(Object target, int index, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    getDelegate().addToValue(target, index, value);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IListAccessor getDelegate() {
    return (IListAccessor) super.getDelegate();
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return (List) super.getValue(target);
  }
}
