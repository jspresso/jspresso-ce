/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Saves all the module entities as well as all its sub-modules entities
 * recursively. All previously registered persistence operations are also
 * performed.
 * 
 * @author Vincent Vandenschrick
 */
public class SaveModuleObjectAction extends SaveAction {

  /**
   * All the module entities as well as all its sub-modules entities
   * recursively.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected List<IEntity> getEntitiesToSave(Map<String, Object> context) {
    List<IEntity> entitiesToSave = new ArrayList<>();
    Module module = getModule(context);
    completeEntitiesToSave(module, entitiesToSave);
    return entitiesToSave;
  }

  private void completeEntitiesToSave(Module module,
      List<IEntity> entitiesToSave) {
    if (module instanceof BeanCollectionModule
        && ((BeanCollectionModule) module).getModuleObjects() != null) {
      for (Object moduleObject : ((BeanCollectionModule) module)
          .getModuleObjects()) {
        if (moduleObject instanceof IComponent) {
          completeEntitiesToSave((IComponent) moduleObject, entitiesToSave);
        }
      }
    } else if (module instanceof BeanModule) {
      Object moduleObject = ((BeanModule) module).getModuleObject();
      if (moduleObject instanceof IComponent) {
        completeEntitiesToSave((IComponent) moduleObject, entitiesToSave);
      }
    }
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        completeEntitiesToSave(subModule, entitiesToSave);
      }
    }
  }

  private void completeEntitiesToSave(IComponent component, List<IEntity> entitiesToSave) {
    if (component instanceof IEntity) {
      entitiesToSave.add((IEntity) component);
    } else {
      for (Map.Entry<String, Object> propertyEntry : component.straightGetProperties().entrySet()) {
        if (propertyEntry.getValue() instanceof IComponent) {
          completeEntitiesToSave((IComponent) propertyEntry.getValue(), entitiesToSave);
        } else if (propertyEntry.getValue() instanceof Collection<?>) {
          for (Object element : (Collection<?>) propertyEntry.getValue()) {
            if (element instanceof IComponent) {
              completeEntitiesToSave((IComponent) element, entitiesToSave);
            }
          }
        }
      }
    }
  }
}
