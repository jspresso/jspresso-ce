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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * The type Create entity from lov persist action.
 *
 * @param <E>
 *     the type parameter
 * @param <F>
 *     the type parameter
 * @param <G>
 *     the type parameter
 * @author Vincent Vandenschrick
 */
public class CreateEntityFromLovPersistAction<E, F, G> extends FrontendAction<E,F,G> {

  /**
   * Execute boolean.
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    if (super.execute(actionHandler, context)) {
      IQueryComponent lovQueryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
      List<IEntity> createdEntityInstance = getActionParameter(context);

      lovQueryComponent.setQueriedComponents(createdEntityInstance);
      return true;
    }
    return false;
  }
}
