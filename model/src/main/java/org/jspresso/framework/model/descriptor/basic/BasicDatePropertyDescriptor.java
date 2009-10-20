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
package org.jspresso.framework.model.descriptor.basic;

import java.util.Date;

import org.jspresso.framework.model.descriptor.EDateType;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;

/**
 * Default implementation of a date descriptor.
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
public class BasicDatePropertyDescriptor extends BasicScalarPropertyDescriptor
    implements IDatePropertyDescriptor {

  private EDateType type;

  /**
   * Constructs a new <code>BasicDatePropertyDescriptor</code> instance.
   */
  public BasicDatePropertyDescriptor() {
    type = EDateType.DATE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDatePropertyDescriptor clone() {
    BasicDatePropertyDescriptor clonedDescriptor = (BasicDatePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComparableQueryStructureDescriptor createQueryDescriptor() {
    return new ComparableQueryStructureDescriptor(super.createQueryDescriptor());
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Date.class;
  }

  /**
   * {@inheritDoc}
   */
  public EDateType getType() {
    return type;
  }

  /**
   * Sets the type.
   * 
   * @param type
   *          the type to set.
   */
  public void setType(EDateType type) {
    this.type = type;
  }
}
