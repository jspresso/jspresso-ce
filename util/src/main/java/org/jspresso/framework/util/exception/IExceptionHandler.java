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
package org.jspresso.framework.util.exception;

import java.util.Map;

/**
 * This interface establishes the general contract of an object able to
 * intercept and handle exceptions.
 *
 * @author Vincent Vandenschrick
 */
public interface IExceptionHandler {

  /**
   * This method is called whenever an exception occurs and has to be handled by
   * the handler.
   *
   * @param ex
   *          the exception that occurs.
   * @param context
   *          the context where some extra information can be retrieved.
   * @return true if the exception has been fully handled and false if it may
   *         still be propagated.
   */
  boolean handleException(Throwable ex, Map<String, Object> context);
}
