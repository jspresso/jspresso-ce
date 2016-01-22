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
package org.jspresso.framework.application.frontend.action.lov.mobile;

import java.util.Collections;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.application.frontend.action.lov.LovAction;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageAware;

/**
 * This is a standard &quot;List Of Values&quot; action for mobile environment.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class MobileLovAction<E, F, G> extends LovAction<E, F, G> {

  /**
   * Handle preselected item.
   *
   * @param preselectedItem
   *     the preselected item
   * @param queryComponent
   *     the query component
   * @param lovView
   *     the lov view
   */
  @Override
  protected void handlePreselectedItem(Object preselectedItem, IQueryComponent queryComponent, IView<E> lovView) {
    // Since in mobile environment, item selection triggers ok action, we must disable it.
  }


  /**
   * Determines the default selection mode for the result view.
   *
   * @param lovContext
   *     the LOV context.
   * @return the default selection mode for the result view.
   */
  @Override
  protected ESelectionMode getDefaultSelectionMode(Map<String, Object> lovContext) {
    return ESelectionMode.SINGLE_SELECTION;
  }

  @Override
  protected void feedContextWithDialog(IReferencePropertyDescriptor<IComponent> erqDescriptor,
                                       IQueryComponent queryComponent, IView<E> lovView, IActionHandler actionHandler,
                                       Map<String, Object> context) {
    super.feedContextWithDialog(erqDescriptor, queryComponent, lovView, actionHandler, context);
    context.put(ModalDialogAction.DIALOG_ACTIONS, Collections.emptyList());
    context.put(ModalDialogAction.DIALOG_TITLE, erqDescriptor.getReferencedDescriptor().getI18nName(
        getTranslationProvider(context), getLocale(context)));
  }

  @Override
  protected IViewDescriptor createLovViewDescriptor(IReferencePropertyDescriptor<IComponent> erqDescriptor,
                                                    Map<String, Object> context) {
    IViewDescriptor viewDescriptor = super.createLovViewDescriptor(erqDescriptor, context);
    if (viewDescriptor instanceof IMobilePageAware) {
      ((IMobilePageAware) viewDescriptor).setBackAction(getCancelAction());
      ((IMobilePageAware) viewDescriptor).setMainAction(getFindAction());
    }
    return viewDescriptor;
  }
}
