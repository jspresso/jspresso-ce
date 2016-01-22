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
package org.jspresso.framework.application.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A Directory builder to list the application various application elements
 * available along with their permId.
 *
 * @author Vincent Vandenschrick
 */
public class ApplicationDirectoryBuilder {

  private String[]           excludePatterns;

  /**
   * {@code MODEL}.
   */
  public static final String MODEL      = "MODEL";

  /**
   * {@code NAVIGATION}.
   */
  public static final String NAVIGATION = "NAVIGATION";

  /**
   * {@code VIEW}.
   */
  public static final String VIEW       = "VIEW";

  /**
   * {@code ACTION}.
   */
  public static final String ACTION     = "ACTION";

  private final Set<String>        modelPermIds;
  private final Set<String>        navigationPermIds;
  private final Set<String>        viewPermIds;
  private final Set<String>        actionPermIds;

  /**
   * Constructs a new {@code ApplicationDirectoryBuilder} instance.
   */
  public ApplicationDirectoryBuilder() {
    modelPermIds = new TreeSet<>();
    navigationPermIds = new TreeSet<>();
    viewPermIds = new TreeSet<>();
    actionPermIds = new TreeSet<>();
    excludePatterns = new String[] {
      "org\\.jspresso\\.framework\\..*"
    };
  }

  /**
   * Processes a workspace recursively to register all reachable application
   * elements into the directory.
   *
   * @param frontendController
   *          the application front controller to analyse.
   * @return this.
   */
  public ApplicationDirectoryBuilder process(IFrontendController<?, ?, ?> frontendController) {
    if (frontendController.getActionMap() != null) {
      process(frontendController.getActionMap());
    }
    if (frontendController.getSecondaryActionMap() != null) {
      process(frontendController.getSecondaryActionMap());
    }
    for (String workspaceName : frontendController.getWorkspaceNames(true)) {
      Workspace workspace = frontendController.getWorkspace(workspaceName);
      String wsPermId = workspace.getPermId();
      if (wsPermId != null) {
        if (isPermIdExcluded(wsPermId) || navigationPermIds.contains(wsPermId)) {
          return this;
        }
        navigationPermIds.add(wsPermId);
      }
      for (Module module : workspace.getModules()) {
        process(module, wsPermId);
      }
    }
    return this;
  }

  private void process(IComponentDescriptor<?> componentDescriptor) {
    String compPermId = componentDescriptor.getPermId();
    if (compPermId != null) {
      if (isPermIdExcluded(compPermId) || modelPermIds.contains(compPermId)) {
        return;
      }
      modelPermIds.add(compPermId);
    }
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      process(propertyDescriptor, compPermId, modelPermIds);
      if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
        if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
          process(((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor());
        } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
          process(((ICollectionPropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getElementDescriptor());
        }
      }
    }
  }

  private void process(IPropertyDescriptor propertyDescriptor, String path,
      Set<String> permIdsSet) {
    String propertyPermId = propertyDescriptor.getPermId();
    if (propertyPermId != null) {
      if (path != null) {
        permIdsSet.add(path + "." + propertyPermId);
      } else {
        permIdsSet.add(propertyPermId);
      }
    }
  }

  private void process(Module module, String path) {
    String modulePermId = module.getPermId();
    if (modulePermId != null) {
      if (path != null) {
        modulePermId = path + "." + modulePermId;
      }
      if (isPermIdExcluded(modulePermId)
          || navigationPermIds.contains(modulePermId)) {
        return;
      }
      navigationPermIds.add(modulePermId);
    }
    if (module.getViewDescriptor() != null) {
      process(module.getViewDescriptor(), null);
    }
    if (module instanceof BeanCollectionModule
        && ((BeanCollectionModule) module).getElementViewDescriptor() != null) {
      process(((BeanCollectionModule) module).getElementViewDescriptor(), null);
    }
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        process(subModule, modulePermId);
      }
    }
  }

  private void process(IViewDescriptor viewDescriptor, String path) {
    String viewPermId = viewDescriptor.getPermId();
    if (viewPermId != null) {
      if (path != null) {
        viewPermId = path + "." + viewPermId;
      }
      if (isPermIdExcluded(viewPermId) || viewPermIds.contains(viewPermId)) {
        return;
      }
      viewPermIds.add(viewPermId);
    }
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
      process(((IComponentDescriptorProvider<?>) viewDescriptor.getModelDescriptor()).getComponentDescriptor());
    } else if (viewDescriptor.getModelDescriptor() instanceof ICollectionDescriptorProvider<?>) {
      process(((ICollectionDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getCollectionDescriptor().getElementDescriptor());
    }
    if (viewDescriptor.getActionMap() != null) {
      process(viewDescriptor.getActionMap());
    }
    if (viewDescriptor.getSecondaryActionMap() != null) {
      process(viewDescriptor.getSecondaryActionMap());
    }
    if (viewDescriptor instanceof ICompositeViewDescriptor) {
      for (IViewDescriptor childViewDescriptor : ((ICompositeViewDescriptor) viewDescriptor)
          .getChildViewDescriptors()) {
        process(childViewDescriptor, /* viewPermId */null);
      }
    } else if (viewDescriptor instanceof ITableViewDescriptor
        && viewPermId != null) {
      for (IViewDescriptor columnViewDescriptor : ((ITableViewDescriptor) viewDescriptor)
          .getColumnViewDescriptors()) {
        process(
            (IPropertyDescriptor) columnViewDescriptor.getModelDescriptor(),
            viewPermId, viewPermIds);
      }
    } else if (viewDescriptor instanceof IComponentViewDescriptor
        && viewPermId != null) {
      for (IViewDescriptor propertyViewDescriptor : ((IComponentViewDescriptor) viewDescriptor)
          .getPropertyViewDescriptors()) {
        process(
            (IPropertyDescriptor) propertyViewDescriptor.getModelDescriptor(),
            viewPermId, viewPermIds);
      }
    }
  }

  private void process(ActionMap actionMap) {
    String amPermId = actionMap.getPermId();
    if (amPermId != null) {
      if (isPermIdExcluded(amPermId) || actionPermIds.contains(amPermId)) {
        return;
      }
      actionPermIds.add(amPermId);
    }
    for (ActionList actionList : actionMap
        .getActionLists(new ISecurityHandler() {

          @Override
          public ISecurityContextBuilder restoreLastSecurityContextSnapshot() {
            return this;
          }

          @Override
          public ISecurityContextBuilder pushToSecurityContext(
              Object contextElement) {
            return this;
          }

          @Override
          public Map<String, Object> getSecurityContext() {
            return null;
          }

          @Override
          public boolean isAccessGranted(ISecurable securable) {
            return true;
          }
        })) {
      process(actionList, amPermId);
    }
  }

  private void process(ActionList actionList, String path) {
    String alPermId = actionList.getPermId();
    if (alPermId != null) {
      if (path != null) {
        alPermId = path + "." + alPermId;
      }
      if (isPermIdExcluded(alPermId) || actionPermIds.contains(alPermId)) {
        return;
      }
      actionPermIds.add(alPermId);
    }
    for (IDisplayableAction action : actionList.getActions()) {
      process(action, alPermId);
    }
  }

  private void process(IDisplayableAction action, String path) {
    String actionPermId = action.getPermId();
    if (actionPermId != null) {
      if (path != null) {
        actionPermId = path + "." + actionPermId;
      }
      if (isPermIdExcluded(actionPermId)
          || actionPermIds.contains(actionPermId)) {
        return;
      }
      actionPermIds.add(actionPermId);
    }
  }

  /**
   * Extracts the permId store from this directory builder.
   *
   * @return the application elements directory.
   */
  public Map<String, Set<String>> toApplicationDirectory() {
    Map<String, Set<String>> applicationDirectory = new HashMap<>();
    applicationDirectory.put(NAVIGATION, navigationPermIds);
    applicationDirectory.put(VIEW, viewPermIds);
    applicationDirectory.put(MODEL, modelPermIds);
    applicationDirectory.put(ACTION, actionPermIds);
    return applicationDirectory;
  }

  private boolean isPermIdExcluded(String permId) {
    if (excludePatterns != null && permId != null) {
      for (String excludePattern : excludePatterns) {
        if (Pattern.matches(excludePattern, permId)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Sets the excludePatterns.
   *
   * @param excludePatterns
   *          the excludePatterns to set.
   */
  public void setExcludePatterns(String... excludePatterns) {
    this.excludePatterns = excludePatterns;
  }
}
