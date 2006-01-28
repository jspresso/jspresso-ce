/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

/**
 * This public interfacemust be implemented by classes willing to keep track of
 * <code>IBeanProvider</code> bean changes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBeanChangeListener {

  /**
   * This method is triggered when the listener detects that a
   * <code>IBeanProvider</code> bean changed. Idealy this method will only
   * fire when <code>oldValue</code> differs from <code>newValue</code>.
   * 
   * @param evt
   *          The event representing the change. This event will have :
   *          <li><code>source</code> set to the bean provider which first
   *          initiated the event.
   *          <li><code>oldValue</code> set to the new source's bean.
   *          <li><code>newValue</code> set to the old source's bean.
   */
  void beanChange(BeanChangeEvent evt);

}
