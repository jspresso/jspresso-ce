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
package org.jspresso.framework.application.backend.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Reloads all the module entities as well as all its sub-modules entities
 * recursively. The whole entities graphs are reloaded from the persistent
 * store.
 *
 * @author Vincent Vandenschrick
 */
public class ReloadModuleObjectAction extends ReloadAction {

  /**
   * All the module entities as well as all its sub-modules entities
   * recursively.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    List<IEntity> entitiesToReload = new ArrayList<>();
    Module module = getModule(context);
    completeEntitiesToReload(module, entitiesToReload);
    return entitiesToReload;
  }

  private void completeEntitiesToReload(Module module,
                                        List<IEntity> entitiesToReload) {
    if (module instanceof BeanCollectionModule
        && ((BeanCollectionModule) module).getModuleObjects() != null) {
      for (Object moduleObject : ((BeanCollectionModule) module).getModuleObjects()) {
        if (moduleObject instanceof IEntity) {
          entitiesToReload.add((IEntity) moduleObject);
        }
      }
    } else if (module instanceof BeanModule) {
      Object moduleObject = ((BeanModule) module).getModuleObject();
      if (moduleObject instanceof IEntity) {
        entitiesToReload.add((IEntity) moduleObject);
      }
    }
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        completeEntitiesToReload(subModule, entitiesToReload);
      }
    }
  }
}
