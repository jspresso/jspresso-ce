/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.uid;

/**
 * Default implementation of IGUIDGenerator based on Marc A. Mnich RandomGUID
 * implementation.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
