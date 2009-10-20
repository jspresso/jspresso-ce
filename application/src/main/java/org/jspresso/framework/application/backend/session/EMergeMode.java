/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public enum EMergeMode {

  /**
   * <code>MERGE_CLEAN_EAGER</code> means that the whole object graph will be
   * traversed independently of the state of the walked entities. This is
   * generally used for "reload" behaviour whenever an entity deep in the graph
   * might have been updated in memory.
   */
  MERGE_CLEAN_EAGER,

  /**
   * <code>MERGE_CLEAN_LAZY</code> means that a clean registered instance will
   * be returned immediately if it is in the same version than the entity to
   * merge. If this merge mode is used, the whole object graph might not be
   * traversed.
   */
  MERGE_CLEAN_LAZY,

  /**
   * <code>MERGE_KEEP</code> means that the registered instance will be kept
   * whatever its state is.
   */
  MERGE_KEEP
}
