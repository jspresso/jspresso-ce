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
package org.jspresso.framework.application.frontend.action.remote.mobile;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.IView;

/**
 * Check current selected module object. If it is transient, removes it from the current module and go back.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class CleanModuleAndGoBackIfTransientAction extends AbstractRemoteAction {

  private BackPageAction backPageAction = new BackPageAction();

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Object model = getModel(context);
    if (model instanceof IEntity && !((IEntity) model).isPersistent()) {
      Module currentModule = getModule(context);
      if (currentModule instanceof BeanCollectionModule) {
        ((BeanCollectionModule) currentModule).removeFromModuleObjects(model);
      }
      actionHandler.execute(backPageAction, context);
    }
    return super.execute(actionHandler, context);
  }
}
