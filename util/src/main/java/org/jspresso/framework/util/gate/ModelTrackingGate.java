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
package org.jspresso.framework.util.gate;

/**
 * A simple gate that opens whenever its model is not empty.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public class ModelTrackingGate extends AbstractModelGate {

  /**
   * {@code INSTANCE} is a singleton instance of a gate whose state depends
   * on the underlying action model (null => closed, not null => open).
   */
  public static final ModelTrackingGate INSTANCE    = new ModelTrackingGate();

  private static final Object           START_MODEL = new Object();

  /**
   * Constructs a new {@code ModelTrackingGate} instance.
   */
  public ModelTrackingGate() {
    setCollectionBased(false);
    setModel(START_MODEL);
  }

  /**
   * Restores the default model.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ModelTrackingGate clone() {
    ModelTrackingGate clonedGate = (ModelTrackingGate) super.clone();
    clonedGate.setModel(START_MODEL);
    return clonedGate;
  }

  /**
   * Returns true whenever the model is not null.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isOpen() {
    return getModel() != null;
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
