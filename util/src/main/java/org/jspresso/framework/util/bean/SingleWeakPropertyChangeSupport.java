/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Set;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;

import org.jspresso.framework.util.collection.TWeakHashSet;

/**
 * This property change support prevents from adding twice the same property
 * change listener.
 *
 * @author Vincent Vandenschrick
 */
public class SingleWeakPropertyChangeSupport extends WeakPropertyChangeSupport {


  private static final long serialVersionUID = 1609651022337480186L;

  /**
   * Constructs a {@code WeakPropertyChangeSupport} object.
   *
   * @param sourceBean
   *     The bean to be given as the source for any events.
   */
  public SingleWeakPropertyChangeSupport(Object sourceBean) {
    super(sourceBean);
  }

  @Override
  protected Collection<WeakEntry<PropertyChangeListener>> createListenersCollection() {
    return new TLinkedHashSet<>(1);
  }

  /**
   * Create child.
   *
   * @return the property change support
   */
  @Override
  protected WeakPropertyChangeSupport createChild() {
    return new SingleWeakPropertyChangeSupport(getSource());
  }

}
