/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import com.d2s.framework.util.bean.BeanChangeSupport;
import com.d2s.framework.util.bean.IBeanChangeListener;
import com.d2s.framework.util.bean.IBeanProvider;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This class implements the connector mechanism on a java bean. This type of
 * connector is not targetted at a specific property but at the bean instance
 * itself. This implies that the <code>getConnectorValue</code> method returns
 * the bean instance itself.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanConnector extends BeanRefPropertyConnector {

  private InnerBeanProvider beanProvider;

  /**
   * Constructs a new instance based on the bean class passed as parameter.
   * 
   * @param id
   *          the id of this bean connector.
   * @param beanClass
   *          the bean class on which all the contained bean connectors will
   *          act.
   * @param beanConnectorFactory
   *          the factory used to create the child property connectors.
   */
  BeanConnector(String id, Class beanClass,
      IBeanConnectorFactory beanConnectorFactory) {
    super(id, beanClass, beanConnectorFactory);
    this.beanProvider = new InnerBeanProvider(beanClass);
    beanProviderChanged(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IBeanProvider getBeanProvider() {
    return beanProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class getBeanClass() {
    return getBeanProvider().getBeanClass();
  }

  /**
   * Returns the bean itself (the java bean instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getBeanProvider().getBean();
  }

  /**
   * Sets the bean itself (the java bean instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    ((InnerBeanProvider) getBeanProvider())
        .setBean((IPropertyChangeCapable) aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValueAccessedAsProperty() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanConnector clone(String newConnectorId) {
    BeanConnector clonedConnector = (BeanConnector) super.clone(newConnectorId);
    clonedConnector.beanProvider = new InnerBeanProvider(beanProvider
        .getBeanClass());
    clonedConnector.beanProviderChanged(null);
    return clonedConnector;
  }

  private static final class InnerBeanProvider implements IBeanProvider {

    private IPropertyChangeCapable bean;
    private BeanChangeSupport      beanChangeSupport;
    private Class                  beanClass;

    private InnerBeanProvider(Class beanClass) {
      this.beanClass = beanClass;
    }

    /**
     * {@inheritDoc}
     */
    public void addBeanChangeListener(IBeanChangeListener listener) {
      if (listener != null) {
        if (beanChangeSupport == null) {
          beanChangeSupport = new BeanChangeSupport(this);
        }
        beanChangeSupport.addBeanChangeListener(listener);
      }
    }

    /**
     * Gets the bean instance held internally.
     * <p>
     * {@inheritDoc}
     */
    public IPropertyChangeCapable getBean() {
      return bean;
    }

    /**
     * Gets the bean class held internally.
     * <p>
     * {@inheritDoc}
     */
    public Class getBeanClass() {
      return beanClass;
    }

    /**
     * {@inheritDoc}
     */
    public void removeBeanChangeListener(IBeanChangeListener listener) {
      if (listener != null && beanChangeSupport != null) {
        beanChangeSupport.removeBeanChangeListener(listener);
      }
    }

    /**
     * Sets a new internally held bean instance and forwards the change to all
     * <code>IBeanChangeListener</code>s. In this case this is the enclosing
     * <code>BeanConnector</code>.
     * 
     * @param newBean
     *          the new bean instance.
     */
    protected void setBean(IPropertyChangeCapable newBean) {
      IPropertyChangeCapable oldBean = bean;
      bean = newBean;
      if (bean != null) {
        beanClass = bean.getClass();
      }
      if (beanChangeSupport != null) {
        beanChangeSupport.fireBeanChange(oldBean, newBean);
      }
    }
  }
}
