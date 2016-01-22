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
package org.jspresso.framework.application.frontend.controller.remote.mobile;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.frontend.command.remote.mobile.RemoteAnimationCommand;
import org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand;
import org.jspresso.framework.application.frontend.controller.remote.AbstractRemoteController;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RCardContainer;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RLabel;
import org.jspresso.framework.gui.remote.mobile.RMobileCardPage;
import org.jspresso.framework.gui.remote.mobile.RMobileForm;
import org.jspresso.framework.gui.remote.mobile.RMobileNavPage;
import org.jspresso.framework.gui.remote.mobile.RMobilePage;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.EPosition;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is is the mobile implementation of a &quot;remotable&quot; frontend
 * controller.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 8508 $
 */
public class MobileRemoteController extends AbstractRemoteController {

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   *
   * @param plainContent
   *     the plain content
   * @param htmlContent
   *     the html content
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   *
   * @param swfUrl
   *     the swf url
   * @param flashContext
   *     the flash context
   * @param actions
   *     the actions
   * @param title
   *     the title
   * @param sourceComponent
   *     the source component
   * @param context
   *     the context
   * @param dimension
   *     the dimension
   * @param reuseCurrent
   *     the reuse current
   */
  @Override
  public void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<RAction> actions, String title,
                                 RComponent sourceComponent, Map<String, Object> context, Dimension dimension,
                                 boolean reuseCurrent) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Navigate back.
   * @param context the context
   */
  @SuppressWarnings("unchecked")
  public void navigateBack(Map<String, Object> context) {
    registerCommand(new RemoteBackCommand());
    if (context != null) {
      // Update the context as if the view had changed.
      IView<RComponent> view = (IView<RComponent>) context.get(ActionContextConstants.VIEW);
      if (view != null) {
        view = view.getParent();
        if (view != null) {
          context.put(ActionContextConstants.VIEW, view);
          context.put(ActionContextConstants.VIEW_CONNECTOR, view.getConnector());
        }
      }
    }
  }

  /**
   * Animate page.
   *
   * @param page
   *     the page
   * @param animation
   *     the animation
   * @param direction
   *     the direction
   * @param reverse
   *     the reverse
   * @param duration
   *     the duration
   * @param hideView
   *     the hide view
   * @param callbackAction
   *     the callback action
   */
  public void animatePage(RMobilePage page, String animation, String direction, boolean reverse, int duration,
                          boolean hideView, RAction callbackAction) {
    RemoteAnimationCommand command = new RemoteAnimationCommand();
    if (page != null) {
      command.setTargetPeerGuid(page.getGuid());
    }
    command.setAnimation(animation);
    command.setDirection(direction);
    command.setReverse(reverse);
    command.setDuration(duration);
    command.setHideView(hideView);
    command.setCallbackAction(callbackAction);
    registerCommand(command);
  }

  /**
   * {@inheritDoc}
   *
   * @param workspaceName
   *     the workspace name
   * @return the r component
   */
  @Override
  protected RComponent createWorkspaceView(String workspaceName) {
    RMobileNavPage viewComponent = new RMobileNavPage(workspaceName + "_navigation");
    Workspace workspace = getWorkspace(workspaceName);
    viewComponent.setLabel(workspace.getI18nName());
    viewComponent.setToolTip(workspace.getI18nDescription());
    //    String workspaceI18nHeaderDescription = workspace.getI18nHeaderDescription();
    //    if (workspaceI18nHeaderDescription != null && workspaceI18nHeaderDescription.length() > 0) {
    //      RMobileForm headerForm = createHeaderForm(workspaceI18nHeaderDescription);
    //      viewComponent.setHeaderSections(headerForm);
    //    }
    IViewDescriptor workspaceNavigatorViewDescriptor = workspace.getViewDescriptor();
    IValueConnector workspaceConnector = getBackendController().getWorkspaceConnector(workspaceName);
    IView<RComponent> workspaceNavigator = createWorkspaceNavigator(workspaceName, workspaceNavigatorViewDescriptor);
    IView<RComponent> moduleAreaView = createModuleAreaView(workspaceName);
    viewComponent.setSelectionView(workspaceNavigator.getPeer());
    RMobileCardPage moduleAreaPage = new RMobileCardPage(workspaceName + "_moduleArea");
    moduleAreaPage.setPages((RCardContainer) moduleAreaView.getPeer());
    viewComponent.setNextPage(moduleAreaPage);
    getMvcBinder().bind(workspaceNavigator.getConnector(), workspaceConnector);
    return viewComponent;
  }

  private RMobileForm createHeaderForm(String i18nDescription) {
    RMobileForm headerForm = new RMobileForm(getGuidGenerator().generateGUID());
    RLabel headerLabel = new RLabel(getGuidGenerator().generateGUID());
    headerLabel.setLabel(i18nDescription);
    headerForm.setPosition(EPosition.TOP.name());
    headerForm.setBorderType(EBorderType.NONE.name());
    headerForm.setLabelsPosition(ELabelPosition.NONE.name());
    headerForm.setElementLabels(headerLabel);
    headerForm.setElements(headerLabel);
    headerForm.setElementWidths(1);
    headerForm.setLabelHorizontalPositions(EHorizontalPosition.LEFT.name());
    return headerForm;
  }

  /**
   * Handle workspace navigator selection. Resets selection if selected module is null in order to re-arm client list.
   *
   * @param workspaceName
   *     the workspace name
   * @param selectedConnector
   *     the selected connector
   */
  @Override
  protected void handleWorkspaceNavigatorSelection(String workspaceName, ICompositeValueConnector selectedConnector) {
    if (selectedConnector != null && selectedConnector.getConnectorValue() instanceof Module) {
      Module selectedModule = selectedConnector.getConnectorValue();
      displayModule(workspaceName, selectedModule);
    } else {
      displayModule(workspaceName, null);
    }
  }
}
