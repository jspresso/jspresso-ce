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
package org.jspresso.framework.application.model.mobile;

import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.view.descriptor.mobile.MobileWorkspaceViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.AbstractTreeViewDescriptor;

/**
 * A workspace is an group of functional application modules. You may decide
 * arbitrarily how to group modules into workspaces but a good approach might be
 * to design the workspaces based on roles (i.e. business activities). This
 * helps to clearly separates tasks-unrelated modules and eases authorization
 * management since a workspace can be granted or forbidden as a whole by
 * Jspresso security.
 * <p>
 * Workspaces might be graphically represented differently depending on the UI
 * technology used. For instance, the Swing and ULC channels use a MDI UI in
 * which each workspace is represented as an internal frame (document). On the
 * other hand, Flex and qooxdoo channels represent workspaces stacked in an
 * accordion. Whatever the graphical representation is, there is at most one
 * workspace active at a time for a user session - either the active (focused)
 * internal frame or the expanded accordion section.
 *
 * @author Vincent Vandenschrick
 */
public class MobileWorkspace extends Workspace {

  /**
   * Create mobile workspace view descriptor.
   *
   * @return the workspace view descriptor
   */
  @Override
  protected AbstractTreeViewDescriptor createWorkspaceViewDescriptor() {
    return new MobileWorkspaceViewDescriptor();
  }
}
