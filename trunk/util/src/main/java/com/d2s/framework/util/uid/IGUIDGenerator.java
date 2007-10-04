/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.uid;

/**
 * This interface has to be implemented by generators of GUID.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IGUIDGenerator {

  /**
   * Generates a new GUID.
   * 
   * @return the GUID generated.
   */
  String generateGUID();
}
