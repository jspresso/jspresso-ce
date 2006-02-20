/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.binding.AbstractValueConnector;
import com.d2s.framework.binding.ConnectorBindingException;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.bean.BeanChangeEvent;
import com.d2s.framework.util.bean.IAccessor;
import com.d2s.framework.util.bean.IAccessorFactory;
import com.d2s.framework.util.bean.IBeanChangeListener;
import com.d2s.framework.util.bean.IBeanProvider;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This connector is a java bean property connector. It attaches itself as a
 * PropertyChangeListener to its target bean on the property which is its
 * identifier. It then gets notified of all changes happening on the bean
 * property to forward the change to its attached
 * <code>IConnectorValueChangeListener</code>s.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BeanPropertyConnector extends AbstractValueConnector
    implements IBeanChangeListener, PropertyChangeListener {

  private IAccessor        accessor;
  private IAccessorFactory accessorFactory;

  /**
   * Constructs a new bean connector on a bean property.
   * 
   * @param property
   *          The java bean property to which the connector is bound at
   * @param accessorFactory
   *          The factory which is used to build the <code>IAccessor</code>
   *          used to access the java bean property bi-directionally
   */
  BeanPropertyConnector(String property, IAccessorFactory accessorFactory) {
    super(property);
    this.accessorFactory = accessorFactory;
  }

  /**
   * This method must be called whenever the connector's bean provider changes.
   * This method performs any necessary cleaning, attachements and notification
   * needed.
   * 
   * @param oldBeanProvider
   *          the old bean provider or null if none.
   */
  protected void beanProviderChanged(IBeanProvider oldBeanProvider) {
    IPropertyChangeCapable oldBean = null;
    IPropertyChangeCapable newBean = null;

    if (isValueAccessedAsProperty() && getBeanProvider() != null
        && accessor == null && accessorFactory != null) {
      accessor = accessorFactory.createPropertyAccessor(getId(),
          getBeanProvider().getBeanClass());
    }
    if (oldBeanProvider != null) {
      oldBean = oldBeanProvider.getBean();
      oldBeanProvider.removeBeanChangeListener(this);
    }
    if (getBeanProvider() != null) {
      getBeanProvider().addBeanChangeListener(this);
      newBean = getBeanProvider().getBean();
    }
    if (oldBean != null) {
      oldBean.removePropertyChangeListener(getId(), this);
    }
    if (newBean != null) {
      newBean.addPropertyChangeListener(getId(), this);
    }

    // line below is mainly used to initialize oldConnectorValue (the bean
    // property connector is not used as model yet since it is just being linked
    // to its parent). We would like to use the commented beanChange line but it
    // fails if the current connector is a collection connector.
    // setConnectorValue(getConnecteeValue());
    beanChange(new BeanChangeEvent(getBeanProvider(), oldBean, newBean));
  }

  /**
   * Since bean provider is usually the parent connector for this kind of
   * connector, this method is overloaded to call the
   * <code>beanProviderChanged</code> method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setParentConnector(IValueConnector parentConnector) {
    IBeanProvider oldBeanProvider = getBeanProvider();
    super.setParentConnector(parentConnector);
    beanProviderChanged(oldBeanProvider);
  }

  /**
   * Accesses the underlying bean property and gets its value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    try {
      return accessor.getValue(getBeanProvider().getBean());
    } catch (IllegalAccessException ex) {
      throw new ConnectorBindingException(ex);
    } catch (InvocationTargetException ex) {
      throw new ConnectorBindingException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ConnectorBindingException(ex);
    }
  }

  /**
   * Accesses the underlying bean property and sets its value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (!ObjectUtils.equals(aValue, getConnecteeValue())) {
      try {
        accessor.setValue(getBeanProvider().getBean(), aValue);
      } catch (IllegalAccessException ex) {
        throw new ConnectorBindingException(ex);
      } catch (InvocationTargetException ex) {
        throw new ConnectorBindingException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
  }

  /**
   * Detaches <code>this</code> as <code>PropertyChangeListener</code> on
   * the old bean instance and attaches as <code>PropertyChangeListener</code>
   * on the new bean instance. When this is done, it notifies its
   * <code>IConnectorValueChangeListener</code> s about a possible change on
   * the bean property value (the new bean property).
   * <p>
   * {@inheritDoc}
   */
  public void beanChange(BeanChangeEvent evt) {
    if (!(getParentConnector() instanceof ICollectionConnector)) {
      if (evt.getOldValue() != null) {
        evt.getOldValue().removePropertyChangeListener(getId(), this);
      }
      if (evt.getNewValue() != null) {
        evt.getNewValue().addPropertyChangeListener(getId(), this);
      }
    }

    boolean oldWritability;
    boolean newWritability;
    if (evt.getOldValue() != null) {
      oldWritability = super.isWritable();
    } else {
      oldWritability = false;
    }
    if (evt.getNewValue() != null) {
      newWritability = super.isWritable();
    } else {
      newWritability = isWritable();
    }
    firePropertyChange(WRITABLE_PROPERTY, oldWritability, newWritability);
    fireConnectorValueChange();
  }

  /**
   * Called when the underlying connectee value (the bean property) changes.
   * This implementation notifies its <code>IConnectorValueChangeListener</code>
   * s about the change passing the new bean property.
   * <p>
   * {@inheritDoc}
   */
  public void propertyChange(@SuppressWarnings("unused")
  PropertyChangeEvent evt) {
    fireConnectorValueChange();
  }

  /**
   * Gets the beanProvider.
   * 
   * @return the beanProvider.
   */
  protected IBeanProvider getBeanProvider() {
    if (getParentConnector() instanceof IBeanProvider) {
      return (IBeanProvider) getParentConnector();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    boolean writable = super.isWritable();
    if (accessor != null) {
      writable = writable && accessor.isWritable();
    }
    if (getBeanProvider() != null) {
      writable = writable && getBeanProvider().getBean() != null;
    }
    return writable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanPropertyConnector clone(String newConnectorId) {
    BeanPropertyConnector clonedConnector = (BeanPropertyConnector) super
        .clone(newConnectorId);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * Wether this is a 'real' property connector (a opposed to a BeanConnector).
   * 
   * @return true if this is a 'real' property connector.
   */
  protected boolean isValueAccessedAsProperty() {
    return true;
  }
}
