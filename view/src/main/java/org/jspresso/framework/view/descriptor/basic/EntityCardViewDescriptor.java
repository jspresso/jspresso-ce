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
package org.jspresso.framework.view.descriptor.basic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * This class is a card view descriptor that displays entities. Choice of the
 * card to display is based on the model entity contract.
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
public class EntityCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * Uses the entity contract name as card name.
   * <p>
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof IEntity) {
      return ((IEntity) model).getComponentContract().getName();
    }
    return null;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *            the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    Map<String, IViewDescriptor> classCardMapping = new LinkedHashMap<String, IViewDescriptor>();
    for (IViewDescriptor entityViewDescriptor : viewDescriptors) {
      classCardMapping.put(((IComponentDescriptor<?>) entityViewDescriptor
          .getModelDescriptor()).getComponentContract().getName(),
          entityViewDescriptor);
    }
    setCardViewDescriptors(classCardMapping);
  }

}
