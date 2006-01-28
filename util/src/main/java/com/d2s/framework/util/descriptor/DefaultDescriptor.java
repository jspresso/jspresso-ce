/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.descriptor;

/**
 * This class is a default convennience implementation of
 * <code>IDescriptor</code>.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultDescriptor implements IDescriptor {

  private String name;

  private String description;

  /**
   * The description getter.
   * 
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * The description setter.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The name getter.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * The name setter.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
}
