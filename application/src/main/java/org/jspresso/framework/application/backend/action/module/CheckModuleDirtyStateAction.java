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
package org.jspresso.framework.application.backend.action.module;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action informs the user of module content dirty state. It is meant to
 * operate on bean (collection) modules that handle entities.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class CheckModuleDirtyStateAction<E, F, G> extends
    FrontendAction<E, F, G> {

  /**
   * Checks dirty state of the module content and informs the user if state is
   * dirty.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Module currentModule = getModule(context);
    boolean dirty = false;
    if (currentModule instanceof BeanModule) {
      Object moduleContent = ((BeanModule) currentModule).getModuleObject();
      if (moduleContent instanceof IEntity
          && getController(context).getBackendController().isDirtyInDepth(
              (IEntity) moduleContent)) {
        dirty = true;
      }
    } else if (currentModule instanceof BeanCollectionModule) {
      if (getController(context).getBackendController().isAnyDirtyInDepth(
          ((BeanCollectionModule) currentModule).getModuleObjects())) {
        dirty = true;
      }
    }
    if (dirty) {
      // getController(context).popupInfo(
      // getSourceComponent(context),
      // getTranslationProvider(context).getTranslation(
      // "module.content.dirty.title", getLocale(context)),
      // getIconFactory(context).getInfoIconImageURL(),
      // getTranslationProvider(context).getTranslation(
      // "module.content.dirty.message", getLocale(context)));
    }
    currentModule.setDirty(dirty);
    return super.execute(actionHandler, context);
  }
}
