/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;

/**
 * Describes a property used for password values. For obvious security reasons,
 * this type of properties will hardly be part of a persistent entity. However
 * it is useful for defining transient view models, e.g. for implementing a
 * change password action. Jspresso will automatically adapt view fields
 * accordingly, using password fields, to interact with password properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicPasswordPropertyDescriptor extends
    BasicStringPropertyDescriptor implements IPasswordPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPasswordPropertyDescriptor clone() {
    BasicPasswordPropertyDescriptor clonedDescriptor = (BasicPasswordPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
