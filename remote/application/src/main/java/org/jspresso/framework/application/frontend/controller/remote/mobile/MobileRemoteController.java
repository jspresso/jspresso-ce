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
package org.jspresso.framework.application.frontend.controller.remote.mobile;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.controller.remote.AbstractRemoteController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is is the mobile implementation of a &quot;remotable&quot; frontend
 * controller.
 * 
 * @version $LastChangedRevision: 8508 $
 * @author Vincent Vandenschrick
 */
public class MobileRemoteController extends AbstractRemoteController {

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<RAction> actions, String title,
                                 RComponent sourceComponent, Map<String, Object> context, Dimension dimension,
                                 boolean reuseCurrent) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * {@inheritDoc}
   */
  protected RComponent createWorkspaceView(String workspaceName) {
    RSplitContainer viewComponent = new RSplitContainer(workspaceName + "_split");
    viewComponent.setOrientation(EOrientation.HORIZONTAL.toString());
    IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(workspaceName).getViewDescriptor();
    IValueConnector workspaceConnector = getBackendController().getWorkspaceConnector(workspaceName);
    IView<RComponent> workspaceNavigator = createWorkspaceNavigator(workspaceName,
        workspaceNavigatorViewDescriptor);
    IView<RComponent> moduleAreaView = createModuleAreaView(workspaceName);
    viewComponent.setLeftTop(workspaceNavigator.getPeer());
    viewComponent.setRightBottom(moduleAreaView.getPeer());
    getMvcBinder().bind(workspaceNavigator.getConnector(), workspaceConnector);
    return viewComponent;
  }
}
