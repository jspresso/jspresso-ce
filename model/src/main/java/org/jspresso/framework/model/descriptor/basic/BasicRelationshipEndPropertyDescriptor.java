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
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * This is the abstract base descriptor for all relationship properties.
 * relationship properties include :
 * <ul>
 * <li><i>reference</i> properties, i.e. &qot;N to 1&qot; or &qot;1 to 1&qot;
 * properties</li>
 * <li><i>collection</i> properties, i.e. &qot;1 to N&qot; or &qot;N to N&qot;
 * properties</li>
 * </ul>
 * Other type of properties are named <i>scalar</i> properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicRelationshipEndPropertyDescriptor extends
    BasicPropertyDescriptor implements IRelationshipEndPropertyDescriptor {

  private Boolean                            composition;
  private IRelationshipEndPropertyDescriptor reverseRelationEnd;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicRelationshipEndPropertyDescriptor clone() {
    BasicRelationshipEndPropertyDescriptor clonedDescriptor = (BasicRelationshipEndPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IRelationshipEndPropertyDescriptor getReverseRelationEnd() {
    return reverseRelationEnd;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComposition() {
    if (composition != null) {
      return composition.booleanValue();
    }
    return getDefaultComposition();
  }

  /**
   * Instructs the framework that this property has to be treated as a
   * <i>composition</i>, in the UML terminology. This implies that reachable
   * entities that are referenced by this property follow the owning entity
   * lifecycle. For instance, when the owning entity is deleted, the referenced
   * entities in composition properties are also deleted.
   * <p>
   * Whenever this property is not explicitely set by the developer, Jspresso
   * uses sensible defaults :
   * <ul>
   * <li><i>collection properties</i> are compositions <b>unless</b> they are
   * bidirectional &qot;N to N&qot;</li>
   * <li><i>reference properties</i> are not composition</li>
   * </ul>
   * <p>
   * This property is strictly behavioural and does not impact the domain state
   * itself.
   * 
   * @param composition
   *          the composition to set.
   */
  public void setComposition(boolean composition) {
    this.composition = new Boolean(composition);
  }

  /**
   * Sets the reverseRelationEnd property.
   * 
   * @param reverseRelationEnd
   *          the reverseRelationEnd to set.
   */
  public void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd) {
    if (this.reverseRelationEnd != reverseRelationEnd) {
      if (this.reverseRelationEnd != null) {
        this.reverseRelationEnd.setReverseRelationEnd(null);
      }
      this.reverseRelationEnd = reverseRelationEnd;
      if (reverseRelationEnd != null) {
        reverseRelationEnd.setReverseRelationEnd(this);
      }
    }
  }

  /**
   * Gets the default composition of a relationship end.
   * 
   * @return the default composition of a relationship end.
   */
  protected abstract boolean getDefaultComposition();
}
