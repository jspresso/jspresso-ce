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
package org.jspresso.framework.application.view.descriptor.basic;

import javax.security.auth.Subject;

import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.view.descriptor.basic.AbstractCardViewDescriptor;

/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the workspace structure.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @internal
 */
public class WorkspaceCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model,
      @SuppressWarnings("unused") Subject subject) {
    if (model instanceof Module) {
      return Integer.toString(((Module) model).getProjectedViewDescriptor()
          .hashCode());
    }
    return null;
  }
}
