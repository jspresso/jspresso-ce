/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security;

import java.util.Collection;

/**
 * Marks a resource as capable of holding security restrictions.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISecurable {

  /**
   * Gets the list of roles which are granted acces to this resource. If null is
   * retured the access control is disabled. an empty collection means that
   * nobody can access the resource (this might not be very useful).
   * 
   * @return list of roles which are granted acces to this resource
   */
  Collection<String> getGrantedRoles();
}
