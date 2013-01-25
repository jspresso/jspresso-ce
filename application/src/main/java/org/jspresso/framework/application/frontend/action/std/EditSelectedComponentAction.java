/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.CloseDialogAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This action should be installed on collection views. It takes the selected
 * component and edit it in a modal dialog. Editing happens in a &quot;Unit of
 * Work&quot; meaning that it can be rolled-back when canceling.
 * 
 * @version $LastChangedRevision: 2601 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class EditSelectedComponentAction<E, F, G> extends
    AbstractEditComponentAction<E, F, G> {

  private FrontendAction<E, F, G> cancelAction;
  private FrontendAction<E, F, G> okAction;

  /**
   * Gets the cancelAction.
   * 
   * @return the cancelAction.
   */
  @Override
  protected IDisplayableAction getCancelAction() {
    return cancelAction;
  }

  /**
   * Gets the okAction.
   * 
   * @return the okAction.
   */
  @Override
  protected IDisplayableAction getOkAction() {
    return okAction;
  }

  /**
   * Gets the selected model.
   * 
   * @param context
   *          the action context.
   * @return the model.
   */
  @Override
  protected Object getComponentToEdit(Map<String, Object> context) {
    IBackendController c = getBackendController(context);
    try {
      c.beginUnitOfWork(null);
      IEntity uowEntity = c.cloneInUnitOfWork(
          (IEntity) getSelectedModel(context), true);
      return uowEntity;
    } finally {
      c.rollbackUnitOfWork(null);
    }
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(FrontendAction<E, F, G> cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(FrontendAction<E, F, G> okAction) {
    this.okAction = okAction;
  }

  /**
   * Default OK action.
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
  public static class DefaultOkAction<E, F, G> extends
      CloseDialogAction<E, F, G> {

    /**
     * Merges eagerly the edited entity.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      IEntity entityClone = (IEntity) getModel(context);
      getBackendController(context).merge(entityClone, EMergeMode.MERGE_EAGER);
      return super.execute(actionHandler, context);
    }
  }
}
