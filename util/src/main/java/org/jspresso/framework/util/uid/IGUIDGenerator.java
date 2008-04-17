/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.uid;

/**
 * This interface has to be implemented by generators of GUID.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
