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
package org.jspresso.framework.application.frontend.controller.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is is the default implementation of a &quot;remotable&quot; frontend
 * controller. It will implement a 3-tier architecture. The remote controller
 * lives on server-side and communicates with generic UI engines that are
 * deployed on client side. As of now, the remote frontend controller is used by
 * the <b>Flex</b> and <b>qooxdoo</b> frontends. Communication happens through
 * the use of generic UI commands that are produced/consumed on both sides of
 * the network.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultRemoteController extends AbstractRemoteController {

  /**
   * Constructs a new {@code DefaultRemoteController} instance.
   */
  public DefaultRemoteController() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<RAction> actions, String title,
                                 RComponent sourceComponent, Map<String, Object> context, Dimension dimension,
                                 boolean reuseCurrent) {
    displayModalDialog(null, context, reuseCurrent);
    RemoteFlashDisplayCommand flashCommand = new RemoteFlashDisplayCommand();
    flashCommand.setSwfUrl(swfUrl);
    flashCommand.setTitle(title);
    flashCommand.setActions(actions.toArray(new RAction[actions.size()]));
    flashCommand.setUseCurrent(reuseCurrent);
    List<String> paramNames = new ArrayList<>();
    List<String> paramValues = new ArrayList<>();
    for (Map.Entry<String, String> flashVar : flashContext.entrySet()) {
      paramNames.add(flashVar.getKey());
      paramValues.add(flashVar.getValue());
    }
    flashCommand.setParamNames(paramNames.toArray(new String[paramNames.size()]));
    flashCommand.setParamValues(paramValues.toArray(new String[paramValues.size()]));
    flashCommand.setDimension(dimension);
    registerCommand(flashCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    RemoteClipboardCommand clipboardCommand = new RemoteClipboardCommand();
    clipboardCommand.setPlainContent(plainContent);
    clipboardCommand.setHtmlContent(htmlContent);
    registerCommand(clipboardCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
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

  /**
   * User logged in.
   */
  @Override
  protected void userLoggedIn() {
    if (getWorkspaceNames() != null && getWorkspaceNames().size() > 0) {
      displayWorkspace(getWorkspaceNames().get(0));
    }
    super.userLoggedIn();
  }
}
