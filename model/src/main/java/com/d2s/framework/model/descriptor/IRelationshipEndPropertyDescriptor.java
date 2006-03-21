/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This is the super-interface of all inter-component relationship descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
   * Sets the reverse relationship descriptor of the underlying relation.
   * Implementing classes are supposed to reflexively set both relation ends.
   * 
   * @param reverseRelationEnd
   *          the reverse relation end.
   */
  void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd);

  /**
   * Gets whether this collection property is a composition. If true, it means
   * that whenever the holding bean gets deleted, the referenced beans will also
   * be.
   * 
   * @return true if this is a composition relationship.
   */
  boolean isComposition();
}
