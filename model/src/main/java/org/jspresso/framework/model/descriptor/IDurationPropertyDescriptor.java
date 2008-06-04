/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of duration properties
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
public interface IDurationPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * One Day constant.
   */
  int ONE_DAY    = 24 * 60 * 60 * 1000;

  /**
   * One hour constant.
   */
  int ONE_HOUR   = 60 * 60 * 1000;

  /**
   * One minute constant.
   */
  int ONE_MINUTE = 60 * 1000;

  /**
   * One second constant.
   */
  int ONE_SECOND = 1000;

  /**
   * One week constant.
   */
  int ONE_WEEK   = 7 * 24 * 60 * 60 * 1000;

  /**
   * Gets the upper bound of this duration property in milliseconds. The method
   * should use the constants defined above.
   * 
   * @return the maximum number of milliseconds this duration property can take.
   */
  Long getMaxMillis();

}
