/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.entity.IEntity;


/**
 * Reloads the projected object(s) in a transaction.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ReloadModuleObjectAction extends ReloadAction {

  /**
   * Saves the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    List<IEntity> entitiesToReload = new ArrayList<IEntity>();
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    Module module = (Module) moduleConnector.getConnectorValue();
    if (module instanceof BeanCollectionModule
        && ((BeanCollectionModule) module).getModuleObjects() != null) {
      for (Object entity : ((BeanCollectionModule) module).getModuleObjects()) {
        entitiesToReload.add((IEntity) entity);
      }
    } else if (module instanceof BeanModule) {
      entitiesToReload.add((IEntity) ((BeanModule) module).getModuleObject());
    }
    return entitiesToReload;
  }
}
