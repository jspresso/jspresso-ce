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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Saves the projected object(s) in a transaction.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveModuleObjectAction extends SaveAction {

  /**
   * Saves the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected List<IEntity> getEntitiesToSave(Map<String, Object> context) {
    List<IEntity> entitiesToSave = new ArrayList<IEntity>();
    Module module = getModule(context);
    if (module instanceof BeanCollectionModule
        && ((BeanCollectionModule) module).getModuleObjects() != null) {
      for (Object entity : ((BeanCollectionModule) module).getModuleObjects()) {
        entitiesToSave.add((IEntity) entity);
      }
    } else if (module instanceof BeanModule) {
      entitiesToSave.add((IEntity) ((BeanModule) module).getModuleObject());
    }
    return entitiesToSave;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    boolean ret = super.execute(actionHandler, context);
    if (ret) {
      getModule(context).setDirty(false);
    }
    return ret;
  }

}
