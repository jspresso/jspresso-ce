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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.security.EAuthorization;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Security context builder that helps building a security context.
 *
 * @author Vincent Vandenschrick
 */
public class SecurityContextBuilder implements ISecurityContextBuilder {

  private Map<String, Object>       currentSecurityContext;
  private final List<Map<String, Object>> snapshots;

  private static final String       LAST_PUSHED_VIEW = "LAST_PUSHED_VIEW";

  /**
   * Constructs a new {@code SecurityContextBuilder} instance.
   */
  public SecurityContextBuilder() {
    currentSecurityContext = new HashMap<>();
    currentSecurityContext.put(SecurityContextConstants.AUTH_TYPE,
        EAuthorization.VISIBLE);
    snapshots = new ArrayList<>();
  }

  /**
   * Returns the current security context this builder works on.
   *
   * @return the current security context this builder works on.
   */
  @Override
  public Map<String, Object> getSecurityContext() {
    // return a defensive copy.
    return new HashMap<>(currentSecurityContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SecurityContextBuilder pushToSecurityContext(Object contextElement) {
    snapshots.add(getSecurityContext());
    if (contextElement instanceof Workspace) {
      return append((Workspace) contextElement);
    }
    if (contextElement instanceof Module) {
      return append((Module) contextElement);
    }
    if (contextElement instanceof ActionMap) {
      return append((ActionMap) contextElement);
    }
    if (contextElement instanceof ActionList) {
      return append((ActionList) contextElement);
    }
    if (contextElement instanceof IAction) {
      return append((IAction) contextElement);
    }
    if (contextElement instanceof EAuthorization) {
      return append((EAuthorization) contextElement);
    }
    if (contextElement instanceof ITreeLevelDescriptor) {
      return append((ITreeLevelDescriptor) contextElement);
    }
    if (contextElement instanceof IViewDescriptor) {
      return append((IViewDescriptor) contextElement);
    }
    if (contextElement instanceof IModelDescriptor) {
      return append((IModelDescriptor) contextElement);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SecurityContextBuilder restoreLastSecurityContextSnapshot() {
    if (snapshots.size() > 0) {
      currentSecurityContext = snapshots.remove(snapshots.size() - 1);
    }
    return this;
  }

  private SecurityContextBuilder append(EAuthorization auth) {
    if (auth != null) {
      currentSecurityContext.put(SecurityContextConstants.AUTH_TYPE, auth);
    }
    return this;
  }

  private SecurityContextBuilder append(ITreeLevelDescriptor treeLevelDescriptor) {
    if (treeLevelDescriptor != null) {
      String permId = treeLevelDescriptor.getPermId();
      if (permId == null) {
        permId = treeLevelDescriptor.getNodeGroupDescriptor().getPermId();
      }
      append(treeLevelDescriptor.getNodeGroupDescriptor().getModelDescriptor());
      appendToViewChain(permId);
    }
    return this;
  }

  private SecurityContextBuilder append(IViewDescriptor viewDescriptor) {
    if (viewDescriptor != null) {
      // prevents double-push of same view descriptor
      if (!viewDescriptor.equals(currentSecurityContext.get(LAST_PUSHED_VIEW))) {
        currentSecurityContext.put(LAST_PUSHED_VIEW, viewDescriptor);
        append(viewDescriptor.getModelDescriptor());
        appendToViewChain(viewDescriptor.getPermId());
      }
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  private void appendToViewChain(String permId) {
    if (permId != null) {
      List<String> viewChain = (List<String>) currentSecurityContext
          .get(SecurityContextConstants.VIEW_CHAIN);
      if (viewChain == null) {
        viewChain = new ArrayList<>();
      } else {
        // copy to preserve snapshot.
        viewChain = new ArrayList<>(viewChain);
      }
      viewChain.add(permId);
      currentSecurityContext
          .put(SecurityContextConstants.VIEW_CHAIN, viewChain);
    }
  }

  private SecurityContextBuilder append(IModelDescriptor modelDescriptor) {
    if (modelDescriptor != null) {
      if (modelDescriptor instanceof IPropertyDescriptor) {
        currentSecurityContext.put(SecurityContextConstants.PROPERTY,
            ((IPropertyDescriptor) modelDescriptor).getPermId());
      }
      if (modelDescriptor instanceof IComponentDescriptorProvider<?>
          && ((IComponentDescriptorProvider<?>) modelDescriptor)
              .getComponentDescriptor() != null) {
        if (!(currentSecurityContext.get(LAST_PUSHED_VIEW) instanceof IPropertyViewDescriptor)) {
          // only dig the model if we are not on a reference property view (LOV
          // field). see bug #560.
          currentSecurityContext.put(SecurityContextConstants.MODEL,
              ((IComponentDescriptorProvider<?>) modelDescriptor)
                  .getComponentDescriptor().getPermId());
        }
      } else if (modelDescriptor instanceof ICollectionDescriptorProvider<?>) {
        currentSecurityContext.put(SecurityContextConstants.MODEL,
            ((ICollectionDescriptorProvider<?>) modelDescriptor)
                .getCollectionDescriptor().getElementDescriptor().getPermId());
      }
    }
    return this;
  }

  private SecurityContextBuilder append(Module module) {
    if (module != null) {
      Module currentModule = module;
      List<String> moduleChain = new ArrayList<>();
      while (currentModule != null) {
        if (currentModule.getPermId() != null) {
          moduleChain.add(currentModule.getPermId());
        }
        currentModule = currentModule.getParent();
      }
      currentSecurityContext.put(SecurityContextConstants.MODULE_CHAIN,
          moduleChain);
    }
    return this;
  }

  private SecurityContextBuilder append(Workspace workspace) {
    if (workspace != null) {
      currentSecurityContext.put(SecurityContextConstants.WORKSPACE,
          workspace.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(ActionList actionList) {
    if (actionList != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION_LIST,
          actionList.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(ActionMap actionMap) {
    if (actionMap != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION_MAP,
          actionMap.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(IAction action) {
    if (action != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION,
          action.getPermId());
    }
    return this;
  }
}
