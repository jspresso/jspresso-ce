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
 * This is the base abstract implementation for gates that are model-based.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractModelGate extends AbstractGate implements
    IModelGate {

  private boolean collectionBased;
  private Object  model;

  /**
   * Constructs a new {@code AbstractModelGate} instance.
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
   * Whenever this gate is installed on an action as actionability gate, there
   * might be cases where you want the gate to apply on the selected models of a
   * collection view (table, list) instead of the collection owning model
   * (master). This property allows turning on this behaviour.
   * <p>
   * When this property is turned on, the gate will open if and only if :
   * <ol>
   * <li>at least one collection element is selected on the view</li>
   * <li>the gate is open for <i>all</i> the selected elements</li>
   * </ol>
   * <p>
   * Default value is {@code false}, meaning that the gate will be assigned
   * the collection owning model independently of selected view elements. This
   * property has no effect for writability gates.
   *
   * @return the collectionBased.
   */
  @Override
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

  /**
   * Sets the model.
   *
   * @param model
   *          the model to set.
   * @internal
   */
  @Override
  public void setModel(Object model) {
    this.model = model;
  }

  /**
   * Gets the model.
   *
   * @return the model.
   */
  protected Object getModel() {
    return model;
  }

}
