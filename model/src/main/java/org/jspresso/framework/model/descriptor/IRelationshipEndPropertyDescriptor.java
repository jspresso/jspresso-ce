/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This is the super-interface of all inter-component relationship descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
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
   *            the reverse relation end.
   */
  void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd);
}
