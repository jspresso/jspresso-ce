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
package org.jspresso.framework.security;

import java.util.Map;

/**
 * This interface is implemented by Jspresso security context builders.
 *
 * @author Vincent Vandenschrick
 */
public interface ISecurityContextBuilder {

  /**
   * Returns the current security context this builder works on.
   *
   * @return the current security context this builder works on.
   */
  Map<String, Object> getSecurityContext();

  /**
   * Completes the security context by registering an application element. The
   * way the context is actually amended depends on internal rules based on the
   * type of element. The original security context should be stacked so that it
   * can be restored later by a call to {@code restoreLastSnapshot()}.
   *
   * @param contextElement
   *          the element to complement the context with.
   * @return itself.
   */
  ISecurityContextBuilder pushToSecurityContext(Object contextElement);

  /**
   * Restores the last recorded snapshot.
   *
   * @return itself.
   */
  ISecurityContextBuilder restoreLastSecurityContextSnapshot();
}
