/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.file;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.ConnectorValueSetterCallback;
import org.jspresso.framework.model.descriptor.IFileFilterable;


/**
 * Lets the user browse the local file system and choose a file to update the
 * content of a binary property. Files are filtered based on the file filter
 * defined in the binary property descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OpenFileAsBinaryPropertyAction extends OpenFileAction {

  /**
   * Constructs a new <code>OpenFileAsBinaryPropertyAction</code> instance.
   */
  public OpenFileAsBinaryPropertyAction() {
    setFileOpenCallback(new ConnectorValueSetterCallback());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IFileFilterable modelDescriptor = (IFileFilterable) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    setFileFilter(modelDescriptor.getFileFilter());
    return super.execute(actionHandler, context);
  }
}
