/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * Default implementation of a relationship descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
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
    if (reverseRelationEnd != null) {
      return reverseRelationEnd;
    }
    if (getParentDescriptor() != null) {
      return ((IRelationshipEndPropertyDescriptor) getParentDescriptor())
          .getReverseRelationEnd();
    }
    return reverseRelationEnd;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComposition() {
    if (composition != null) {
      return composition.booleanValue();
    }
    if (getParentDescriptor() != null) {
      return ((IRelationshipEndPropertyDescriptor) getParentDescriptor())
          .isComposition();
    }
    return getDefaultComposition();
  }

  /**
   * Sets the composition.
   * 
   * @param composition
   *            the composition to set.
   */
  public void setComposition(boolean composition) {
    this.composition = new Boolean(composition);
  }

  /**
   * Sets the reverseRelationEnd property.
   * 
   * @param reverseRelationEnd
   *            the reverseRelationEnd to set.
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
