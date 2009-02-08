/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import java.util.Collection;

import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.gate.IGate;


/**
 * The sub views contract.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISubViewDescriptor extends IIconDescriptor, ISecurable {

  /**
   * Gets the model descriptor this view descriptor acts on.
   * 
   * @return the view model descriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets the collection of gates determining the readability state of this
   * property.
   * 
   * @return the collection of gates determining the readability state of this
   *         property.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state of this
   * property.
   * 
   * @return the collection of gates determining the writability state of this
   *         property.
   */
  Collection<IGate> getWritabilityGates();

  /**
   * Gets wether this view is read-only.
   * 
   * @return true if the view is read-only.
   */
  boolean isReadOnly();
  
  
  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *            the grantedRoles to set.
   */
  void setGrantedRoles(Collection<String> grantedRoles);
}
