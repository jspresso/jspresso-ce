/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.view.descriptor.basic;

import org.jspresso.framework.application.model.descriptor.WorkspaceDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTreeViewDescriptor;


/**
 * This is a default implementation of a simple workspace view descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class BasicWorkspaceViewDescriptor extends BasicTreeViewDescriptor {

  /**
   * Constructs a new <code>BasicWorkspaceViewDescriptor</code> instance.
   */
  public BasicWorkspaceViewDescriptor() {
    BasicSimpleTreeLevelDescriptor modulesTreeLevelDescriptor = new BasicSimpleTreeLevelDescriptor();
    BasicListViewDescriptor moduleNodeGroupDescriptor = new BasicListViewDescriptor();
    moduleNodeGroupDescriptor
        .setModelDescriptor(WorkspaceDescriptor.WORKSPACE_DESCRIPTOR
            .getPropertyDescriptor("modules"));
    moduleNodeGroupDescriptor.setRenderedProperty("i18nName");
    modulesTreeLevelDescriptor.setNodeGroupDescriptor(moduleNodeGroupDescriptor);
    modulesTreeLevelDescriptor.setChildDescriptor(new BasicModuleViewDescriptor());

    setChildDescriptor(modulesTreeLevelDescriptor);
    setRenderedProperty("i18nName");
  }
}
