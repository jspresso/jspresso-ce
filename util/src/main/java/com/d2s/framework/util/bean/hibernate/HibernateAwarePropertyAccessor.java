/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean.hibernate;

import java.lang.reflect.InvocationTargetException;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.IAccessor;
import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateAwarePropertyAccessor implements IAccessor {

  private IAccessor           delegate;
  private HibernateTemplate   hibernateTemplate;
  private IApplicationSession applicationSession;

  /**
   * Constructs a new <code>HibernateAwarePropertyAccessor</code> instance.
   * 
   * @param delegate
   * @param hibernateTemplate
   */
  public HibernateAwarePropertyAccessor(IAccessor delegate,
      HibernateTemplate hibernateTemplate,
      IApplicationSession applicationSession) {
    this.delegate = delegate;
    this.hibernateTemplate = hibernateTemplate;
    this.applicationSession = applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    if (target instanceof IEntity) {
      final IEntity targetEntity = (IEntity) target;
      try {
        hibernateTemplate.execute(new HibernateCallback() {

          public Object doInHibernate(Session session) {
            Object delegateValue = null;
            try {
              delegateValue = delegate.getValue(targetEntity);
              if (!Hibernate.isInitialized(delegateValue)) {
                IEntity targetEntityClone = targetEntity.clone(true);
                targetEntityClone.straightSetProperty(getProperty(),
                    delegateValue);
                session.lock(targetEntityClone, LockMode.NONE);
                delegateValue = delegate.getValue(targetEntityClone);
                hibernateTemplate.initialize(delegateValue);
                applicationSession.merge(targetEntityClone,
                    MergeMode.MERGE_CLEAN_INITIALIZED);
              }
            } catch (IllegalAccessException ex) {
              throw new NestedRuntimeException(ex);
            } catch (InvocationTargetException ex) {
              throw new NestedRuntimeException(ex);
            } catch (NoSuchMethodException ex) {
              throw new NestedRuntimeException(ex);
            }
            return null;
          }
        });
      } catch (NestedRuntimeException ex) {
        Throwable checkedException = ex.getCause();
        if (checkedException instanceof IllegalAccessException) {
          throw (IllegalAccessException) checkedException;
        } else if (checkedException instanceof InvocationTargetException) {
          throw (InvocationTargetException) checkedException;
        } else if (checkedException instanceof NoSuchMethodException) {
          throw (NoSuchMethodException) checkedException;
        }
        throw ex;
      }
    }
    return delegate.getValue(target);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWritable() {
    return delegate.isWritable();
  }

  /**
   * TODO Comment needed.
   * @return sqdqsd
   */
  public String getProperty() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    delegate.setValue(target, value);
  }

  /**
   * Gets the delegate.
   * 
   * @return the delegate.
   */
  protected IAccessor getDelegate() {
    return delegate;
  }

  /**
   * Gets the hibernateTemplate.
   * 
   * @return the hibernateTemplate.
   */
  protected HibernateTemplate getHibernateTemplate() {
    return hibernateTemplate;
  }
}
