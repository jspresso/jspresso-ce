/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.uid;

/**
 * An instance that creates unique ids sequentially for a JVM.
 * 
 * @version $LastChangedRevision: 2529 $
 * @author Vincent Vandenschrick
 */
public class JVMGUIDGenerator implements IGUIDGenerator {

  private static int instanceIndex;
  private static final Object LOCK = new Object();

  private String     instanceId;
  private long       index = 0;

  /**
   * Constructs a new <code>JVMGUIDGenerator</code> instance.
   */
  public JVMGUIDGenerator() {
    synchronized (LOCK) {
      instanceId = Integer.toHexString(instanceIndex++);
    }
  }

  /**
   * Generates a GUID based on a locally kept index.
   * <p>
   * {@inheritDoc}
   */
  public synchronized String generateGUID() {
    return instanceId + Long.toHexString(index++);
  }
}
