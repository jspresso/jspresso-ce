/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.view.descriptor.basic;

import org.jspresso.framework.application.model.descriptor.WorkspaceDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTreeViewDescriptor;


/**
 * This is a default implementation of a simple workspace view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
