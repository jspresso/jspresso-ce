/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.event;

/**
 * Interface implemented by listeners on selectable selection.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISelectionChangeListener {

  /**
   * This method is triggered whenever a selectable the listener is bound to has
   * its selection changed. Ideally this method will only fire when the event
   * <code>oldValue</code> differs from <code>newValue</code>.
   * 
   * @param evt
   *            The event representing the change. This event will have :
   *            <li><code>source</code> set to the selectable which initiated
   *            the event.
   *            <li><code>oldSelection</code> set to the old selection.
   *            <li><code>newSelection</code> set to the new selection.
   */
  void selectionChange(SelectionChangeEvent evt);
}
