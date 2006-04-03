/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.security.demo;

import java.security.Principal;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DemoPrincipal implements Principal, java.io.Serializable {

  private static final long serialVersionUID = -7412558834725479377L;
  private String            name;

  /**
   * Create a DemoPrincipal with a Sample username.
   * <p>
   * 
   * @param name
   *          the Demo username for this user.
   */
  public DemoPrincipal(String name) {
    if (name == null) {
      throw new NullPointerException("illegal null input");
    }

    this.name = name;
  }

  /**
   * Return the Demo username for this <code>DemoPrincipal</code>.
   * <p>
   * 
   * @return the Demo username for this <code>SamplePrincipal</code>
   */
  public String getName() {
    return name;
  }

  /**
   * Return a string representation of this <code>DemoPrincipal</code>.
   * <p>
   * 
   * @return a string representation of this <code>SamplePrincipal</code>.
   */
  @Override
  public String toString() {
    return ("DemoPrincipal:  " + name);
  }

  /**
   * Compares the specified Object with this <code>DemoPrincipal</code> for
   * equality. Returns true if the given object is also a
   * <code>SamplePrincipal</code> and the two DemoPrincipals have the same
   * username.
   * <p>
   * 
   * @param o
   *          Object to be compared for equality with this
   *          <code>DemoPrincipal</code>.
   * @return true if the specified Object is equal equal to this
   *         <code>DemoPrincipal</code>.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (this == o) {
      return true;
    }

    if (!(o instanceof DemoPrincipal)) {
      return false;
    }
    DemoPrincipal that = (DemoPrincipal) o;

    if (this.getName().equals(that.getName())) {
      return true;
    }
    return false;
  }

  /**
   * Return a hash code for this <code>DemoPrincipal</code>.
   * <p>
   * 
   * @return a hash code for this <code>DemoPrincipal</code>.
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
