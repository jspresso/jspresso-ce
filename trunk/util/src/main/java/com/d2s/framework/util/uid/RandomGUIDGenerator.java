/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.uid;

/**
 * Default implementation of IGUIDGenerator based on Marc A. Mnich RandomGUID
 * implementation.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RandomGUIDGenerator implements IGUIDGenerator {

  /**
   * Generates a GUID based on Marc A. Mnich RandomGUID implementation.
   * <p>
   * {@inheritDoc}
   */
  public String generateGUID() {
    return new RandomGUID().toString();
  }

}
