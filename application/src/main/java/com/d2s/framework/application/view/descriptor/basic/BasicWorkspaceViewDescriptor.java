/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import com.d2s.framework.application.model.descriptor.WorkspaceDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicListViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSimpleTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicTreeViewDescriptor;

/**
 * This is a default implementation of a simple module view descriptor.
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
