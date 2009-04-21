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

import java.util.Map;

/**
 * This interface is implemented by descriptors of reference properties.
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
 * @param <E>
 *          the concrete component type.
 */
public interface IReferencePropertyDescriptor<E> extends
    IRelationshipEndPropertyDescriptor, IComponentDescriptorProvider<E> {

  /**
   * Gets the initialization map between master object attributes and query
   * entity for LOV. For instance, a mapping holding (attrA,attrB) will indicate
   * that the lov query entity should be initialized with its 'attrA' value
   * initialized with the 'attrB' value of its master.
   * 
   * @return the initialisation mapping.
   */
  Map<String, String> getInitializationMapping();

  /**
   * Gets the descriptor of the component referenced by this property.
   * 
   * @return the referenced component descriptor
   */
  IComponentDescriptor<E> getReferencedDescriptor();

  /**
   * When a list of value is triggered, this is the default page size of the
   * result list.
   * 
   * @return the default page size of the result list.
   */
  Integer getPageSize();

}
