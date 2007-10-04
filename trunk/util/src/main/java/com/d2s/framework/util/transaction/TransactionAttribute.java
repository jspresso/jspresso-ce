/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.transaction;

/**
 * This exception hould be raised when a service fails due to a non-system
 * exception.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TransactionAttribute {

  /**
   * Requires a transaction but can use an existing one.
   */
  public static final int REQUIRED     = 0;

  /**
   * Requires a new transaction even if there is an existing one.
   */
  public static final int REQUIRES_NEW = 1;

  /**
   * Doesn't require a transaction but can execute in an existing one.
   */
  public static final int SUPPORTS     = 2;
}
