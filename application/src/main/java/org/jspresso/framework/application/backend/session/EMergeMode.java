/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.session;

/**
 * This enumeration defines all the supported merge modes of a session.
 *
 * @author Vincent Vandenschrick
 */
public enum EMergeMode {

  /**
   * {@code MERGE_CLEAN_EAGER} merge mode should be used to override the
   * state of the target object graph whatever its current in-memory state is.
   * The target object graph is completely traversed, and each graph object
   * dirty state is cleaned. This merge mode implements a "deep reload"
   * semantics.
   */
  MERGE_CLEAN_EAGER,

  /**
   * {@code MERGE_CLEAN_LAZY} merge mode should be used to override the
   * state of the target object graph for nodes whose version is either obsolete
   * or whose in-memory state is dirty. The graph traversal is stopped whenever
   * a clean node in same version is met. Each traversed graph node dirty state
   * is cleaned. This merge mode is typically used when committing an save or
   * update transaction.
   */
  MERGE_CLEAN_LAZY,

  /**
   * {@code MERGE_EAGER} should be used to update the target object graph
   * state eagerly while keeping its dirty state. It merge mode is typically
   * used when implementing an in-memory transaction, like editing an object
   * graph in a unit of work thus allowing the user to commit ar cancel the
   * edition.
   */
  MERGE_EAGER,

  /**
   * {@code MERGE_LAZY} merge mode should be used to override the state of
   * the target object graph for nodes whose version is obsolete. The graph
   * traversal is stopped whenever a clean node in same version is met. Each
   * traversed graph node dirty state is cleaned if and only if its version is
   * newer. This merge mode is typically used when reading from database in a
   * QBE query.
   */
  MERGE_LAZY,

  /**
   * {@code MERGE_KEEP} merge mode is used to only register non-existing
   * graph object nodes. Existing nodes will simply be returned independently of
   * their state (even if they are obsolete).
   */
  MERGE_KEEP
}
