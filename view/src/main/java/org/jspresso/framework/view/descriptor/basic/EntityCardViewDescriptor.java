/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import javax.security.auth.Subject;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This card view provides a simple card determination strategy that is based on
 * the bound model type. This strategy pulls up the card whose model descriptor
 * matches the type of the bound model.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * Uses the component contract name as card name.
   * <p>
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model,
      @SuppressWarnings("unused") Subject subject) {
    if (model != null) {
      if (model instanceof IComponent) {
        return ((IComponent) model).getComponentContract().getName();
      }
      return model.getClass().getName();
    }
    return null;
  }

  /**
   * Registers the list of card view descriptors. Every time the bound model
   * changes, this list is iterated until a card with a matching model is found.
   * The first matching card is displayed. Whenever no registered card matches,
   * an empty view is displayed.
   * 
   * @param viewDescriptors
   *          the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    Map<String, IViewDescriptor> classCardMapping = new LinkedHashMap<String, IViewDescriptor>();
    for (IViewDescriptor componentViewDescriptor : viewDescriptors) {
      if (!(componentViewDescriptor.getModelDescriptor() instanceof IComponentDescriptor<?>)) {
        throw new ViewException(
            "Entity card view does not support cards without model"
                + " descriptor or with a model descriptor that is not a component descriptor.");
      }
      classCardMapping.put(((IComponentDescriptor<?>) componentViewDescriptor
          .getModelDescriptor()).getComponentContract().getName(),
          componentViewDescriptor);
    }
    setCardViewDescriptors(classCardMapping);
  }

}
