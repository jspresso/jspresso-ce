/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean.hibernate;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.util.bean.IAccessor;
import com.d2s.framework.util.bean.IAccessorFactory;
import com.d2s.framework.util.bean.ICollectionAccessor;
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
public class HibernateAwareAccessorFactory implements IAccessorFactory {

  private IAccessorFactory    delegate;
  private HibernateTemplate   hibernateTemplate;
  private IApplicationSession applicationSession;

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property, Class beanClass) {
    return new HibernateAwarePropertyAccessor(delegate.createPropertyAccessor(
        property, beanClass), hibernateTemplate, applicationSession);
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class beanClass, Class elementClass) {
    ICollectionAccessor delegateAccessor = delegate
        .createCollectionPropertyAccessor(property, beanClass, elementClass);
    if (delegateAccessor instanceof IListAccessor) {
      return new HibernateAwareListAccessor((IListAccessor) delegateAccessor,
          hibernateTemplate, applicationSession);
    }
    return new HibernateAwareCollectionAccessor(delegateAccessor,
        hibernateTemplate, applicationSession);
  }

  /**
   * Sets the hibernateTemplate.
   * 
   * @param hibernateTemplate
   *          the hibernateTemplate to set.
   */
  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }

  /**
   * Sets the delegate.
   * 
   * @param delegate
   *          the delegate to set.
   */
  public void setDelegate(IAccessorFactory delegate) {
    this.delegate = delegate;
  }

  
  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }

}
