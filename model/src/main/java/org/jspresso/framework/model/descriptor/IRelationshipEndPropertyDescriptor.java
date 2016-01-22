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
package org.jspresso.framework.model.descriptor;

/**
 * This is the super-interface of all inter-component relationship descriptors.
 *
 * @author Vincent Vandenschrick
 */
public interface IRelationshipEndPropertyDescriptor extends IPropertyDescriptor {

  /**
   * Gets the reverse relationship descriptor of the underlying relation.
   *
   * @return the reverse relation end.
   */
  IRelationshipEndPropertyDescriptor getReverseRelationEnd();

  /**
   * Gets whether this collection property is a composition. If true, it means
   * that whenever the holding bean gets deleted, the referenced beans will also
   * be.
   *
   * @return true if this is a composition relationship.
   */
  boolean isComposition();

  /**
   * Sets the reverse relationship descriptor of the underlying relation.
   * Implementing classes are supposed to reflexively set both relation ends.
   *
   * @param reverseRelationEnd
   *          the reverse relation end.
   */
  void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd);
}
