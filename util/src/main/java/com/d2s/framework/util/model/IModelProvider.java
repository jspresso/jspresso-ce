/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.model;

/**
 * This public interface should be implemented by any class being able to
 * provide a model instance. It allows for registration of
 * <code>IModelChangeListener</code> which can keep track of model object
 * changes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelProvider {

  /**
   * Gets the bean object of this provider.
   * 
   * @return The bean object
   */
  Object getModel();

  /**
   * Gets the model type of this provider.
   * 
   * @return The bean class object
   */
  Class getModelClass();

  /**
   * Adds a new bean listener to this model provider.
   * 
   * @param listener
   *          The added listener
   */
  void addModelChangeListener(IModelChangeListener listener);

  /**
   * Removes a bean listener from this model provider.
   * 
   * @param listener
   *          The added listener
   */
  void removeModelChangeListener(IModelChangeListener listener);
}
