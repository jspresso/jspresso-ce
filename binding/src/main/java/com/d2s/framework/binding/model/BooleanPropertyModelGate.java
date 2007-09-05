/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.model.ModelChangeEvent;
import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This gate opens and closes based on the value of a boolean property of its
 * model.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanPropertyModelGate extends AbstractModelGate implements
    PropertyChangeListener {

  private IAccessor        accessor;
  private IAccessorFactory accessorFactory;
  private String           booleanPropertyName;
  private boolean          open;
  private boolean          openOnTrue;

  /**
   * Constructs a new <code>BooleanPropertyModelGate</code> instance.
   */
  public BooleanPropertyModelGate() {
    open = true;
    openOnTrue = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BooleanPropertyModelGate clone() {
    BooleanPropertyModelGate clonedGate = (BooleanPropertyModelGate) super
        .clone();
    clonedGate.open = true;
    return clonedGate;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * {@inheritDoc}
   */
  public void modelChange(ModelChangeEvent evt) {
    if (evt.getOldValue() instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) evt.getOldValue())
          .removePropertyChangeListener(booleanPropertyName, this);
    }
    if (evt.getNewValue() instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) evt.getNewValue()).addPropertyChangeListener(
          booleanPropertyName, this);
    }
    boolean oldOpen = isOpen();
    if (accessor != null && evt.getNewValue() != null) {
      try {
        Boolean modelValue = (Boolean) accessor.getValue(getModel());
        this.open = (modelValue != null && modelValue.booleanValue());
        if (!openOnTrue) {
          this.open = !this.open;
        }
      } catch (IllegalAccessException ex) {
        throw new NestedRuntimeException(ex);
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex);
      }
    } else {
      this.open = true;
    }
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

  /**
   * {@inheritDoc}
   */
  public void propertyChange(PropertyChangeEvent evt) {
    boolean oldOpen = isOpen();
    this.open = ((Boolean) evt.getNewValue()).booleanValue();
    if (!openOnTrue) {
      this.open = !this.open;
    }
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

  /**
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *            the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the booleanPropertyName.
   * 
   * @param booleanPropertyName
   *            the booleanPropertyName to set.
   */
  public void setBooleanPropertyName(String booleanPropertyName) {
    this.booleanPropertyName = booleanPropertyName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModelProvider(IModelProvider modelProvider) {
    if (accessor == null && modelProvider != null && accessorFactory != null) {
      accessor = accessorFactory.createPropertyAccessor(booleanPropertyName,
          modelProvider.getModelDescriptor().getModelType());
    }
    super.setModelProvider(modelProvider);
  }

  /**
   * Sets the openOnTrue.
   * 
   * @param openOnTrue
   *            the openOnTrue to set.
   */
  public void setOpenOnTrue(boolean openOnTrue) {
    this.openOnTrue = openOnTrue;
  }
}
