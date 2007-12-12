/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model;

/**
 * This public interface must be implemented by classes willing to keep track of
 * <code>IModelProvider</code> model changes.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelChangeListener {

  /**
   * This method is triggered when the listener detects that a
   * <code>IModelProvider</code> model changed. Idealy this method will only
   * fire when <code>oldValue</code> differs from <code>newValue</code>.
   * 
   * @param evt
   *            The event representing the change. This event will have :
   *            <li><code>source</code> set to the bean provider which first
   *            initiated the event.
   *            <li><code>oldValue</code> set to the new source's model.
   *            <li><code>newValue</code> set to the old source's model.
   */
  void modelChange(ModelChangeEvent evt);

}
