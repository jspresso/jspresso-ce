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
package org.jspresso.framework.binding.model;

import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This connector is a simple java model property connector. "Simple" means not
 * a model reference and not a collection.
 *
 * @author Vincent Vandenschrick
 */

public class ModelScalarPropertyConnector extends ModelPropertyConnector {

  /**
   * Constructs a new model property connector on a simple model property.
   */
  ModelScalarPropertyConnector(IScalarPropertyDescriptor modelDescriptor,
      IAccessorFactory accessorFactory) {
    super(modelDescriptor, accessorFactory);
  }
}
