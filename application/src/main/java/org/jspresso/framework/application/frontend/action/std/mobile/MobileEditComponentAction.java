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
package org.jspresso.framework.application.frontend.action.std.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.IMobilePageAware;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;

/**
 * This is the mobile version of the edit component action.
 * {@code okAction} and {@code cancelAction}) are added as page actions instead of dialog ones.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class MobileEditComponentAction<E, F, G> extends EditComponentAction<E, F, G> {

  @Override
  protected IViewDescriptor getViewDescriptor(Map<String, Object> context) {
    IViewDescriptor viewDescriptor = super.getViewDescriptor(context);
    IMobilePageAware refinedViewDescriptor;
    if (viewDescriptor instanceof IMobilePageAware && viewDescriptor instanceof BasicViewDescriptor) {
      refinedViewDescriptor = (IMobilePageAware) ((BasicViewDescriptor) viewDescriptor).clone();
    } else {
      refinedViewDescriptor = new MobileBorderViewDescriptor();
      ((MobileBorderViewDescriptor) refinedViewDescriptor).setModelDescriptor(viewDescriptor.getModelDescriptor());
      ((MobileBorderViewDescriptor) refinedViewDescriptor).setCenterViewDescriptor(viewDescriptor);
    }
    refinedViewDescriptor.setMainAction(getOkAction());
    refinedViewDescriptor.setBackAction(getCancelAction());
    viewDescriptor = (IViewDescriptor) refinedViewDescriptor;
    return viewDescriptor;
  }

  @Override
  protected List<IDisplayableAction> getDialogActions() {
    if (getComplementaryActions() != null) {
      return new ArrayList<>(getComplementaryActions());
    }
    return Collections.emptyList();
  }
}
