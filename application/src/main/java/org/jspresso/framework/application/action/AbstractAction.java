/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.action;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Base class for all application actions. Takes care of the context reference
 * as well as the input context keys reference.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAction implements IAction {

  /**
   * <code>ACTION_MODEL_NAME</code>.
   */
  protected static final String ACTION_MODEL_NAME = "ActionModel";
  private Collection<String>    grantedRoles;

  private boolean               longOperation;

  /**
   * {@inheritDoc}
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLongOperation() {
    return longOperation;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * Sets the longOperation.
   * 
   * @param longOperation
   *          the longOperation to set.
   */
  public void setLongOperation(boolean longOperation) {
    this.longOperation = longOperation;
  }

  /**
   * Gets the controller (frontend or backend) out of the action context.
   * 
   * @param context
   *          the action context.
   * @return the controller (frontend or backend).
   */
  protected abstract IController getController(Map<String, Object> context);

  /**
   * Retrieves the locale the action has to use to execute from its context
   * using a well-known key.
   * 
   * @param context
   *          the action context.
   * @return the locale the action executes in.
   */
  protected abstract Locale getLocale(Map<String, Object> context);

  /**
   * This is a utility method which is able to retrieve the model descriptor
   * this action has been executed on from its context. It uses well-known
   * context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code> to get the the
   * view model descriptor the action executes on.
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the model descriptor this model action was triggered on.
   */
  protected IModelDescriptor getModelDescriptor(Map<String, Object> context) {
    return (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
  }

  /**
   * Gets a translation provider out of the action context.
   * 
   * @param context
   *          the action context.
   * @return the translation provider.
   */
  protected ITranslationProvider getTranslationProvider(
      Map<String, Object> context) {
    return getController(context).getTranslationProvider();
  }
}
