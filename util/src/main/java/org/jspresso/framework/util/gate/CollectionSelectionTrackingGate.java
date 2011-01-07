/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.gate;

import java.util.Collection;

/**
 * A simple gate that tracks collection selection (actually the sub-collection
 * is set as model) and opens whenever the selection is not empty.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @internal
 */
public class CollectionSelectionTrackingGate extends AbstractModelGate {

  /**
   * <code>INSTANCE</code> is a singleton instance of a gate whose state depends
   * on the underlying collection connector selection (empty => closed, not
   * empty => open).
   */
  public static final CollectionSelectionTrackingGate INSTANCE = new CollectionSelectionTrackingGate();

  /**
   * Constructs a new <code>CollectionSelectionTrackingGate</code> instance.
   */
  public CollectionSelectionTrackingGate() {
    setCollectionBased(true);
  }

  /**
   * Returns true whenever the model collection is not empty.
   * <p>
   * {@inheritDoc}
   */
  public boolean isOpen() {
    if (getModel() instanceof Collection<?>) {
      return !((Collection<?>) getModel()).isEmpty();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModel(Object model) {
    boolean oldOpen = isOpen();
    super.setModel(model);
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

}
