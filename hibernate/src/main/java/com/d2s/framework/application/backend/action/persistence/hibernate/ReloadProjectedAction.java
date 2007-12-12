/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Reloads the projected object(s) in a transaction.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ReloadProjectedAction extends ReloadAction {

  /**
   * Saves the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    List<IEntity> entitiesToReload = new ArrayList<IEntity>();
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    SubModule module = (SubModule) moduleConnector.getConnectorValue();
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
