/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Base implementation of a model based gate.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractModelGate extends AbstractGate implements
    IModelGate {

  private Object  model;
  private boolean collectionBased;

  /**
   * Constructs a new <code>AbstractModelGate</code> instance.
   */
  public AbstractModelGate() {
    collectionBased = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractModelGate clone() {
    AbstractModelGate clonedGate = (AbstractModelGate) super.clone();
    clonedGate.model = null;
    return clonedGate;
  }

  /**
   * Gets the model.
   * 
   * @return the model.
   */
  protected Object getModel() {
    return model;
  }

  /**
   * Sets the model.
   * 
   * @param model
   *          the model to set.
   */
  public void setModel(Object model) {
    this.model = model;
  }

  /**
   * Gets the collectionBased.
   * 
   * @return the collectionBased.
   */
  public boolean isCollectionBased() {
    return collectionBased;
  }

  /**
   * Sets the collectionBased.
   * 
   * @param collectionBased
   *          the collectionBased to set.
   */
  public void setCollectionBased(boolean collectionBased) {
    this.collectionBased = collectionBased;
  }

}
