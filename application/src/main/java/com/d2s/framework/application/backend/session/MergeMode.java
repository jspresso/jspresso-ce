/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.session;

/**
 * This enumeration defines all the supported merge modes of a session.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public enum MergeMode {

  /**
   * <code>MERGE_KEEP</code> means that the registered instance will be kept
   * whatever its state is.
   */
  MERGE_KEEP,

  /**
   * <code>MERGE_CLEAN_LAZY</code> means that a clean registered instance will
   * be returned immediately if it is in the same version than the entity to
   * merge. If this merge mode is used, the whole object graph might not be
   * traversed.
   */
  MERGE_CLEAN_LAZY,

  /**
   * <code>MERGE_CLEAN_EAGER</code> means that the whole object graph will be
   * traversed independently of the state of the walked entities. This is
   * generally used for "reload" behaviour whenever an entity deep in the graph
   * might have been updated in memory.
   */
  MERGE_CLEAN_EAGER
}
