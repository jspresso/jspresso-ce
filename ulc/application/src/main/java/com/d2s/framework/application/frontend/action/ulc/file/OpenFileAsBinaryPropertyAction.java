/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.ConnectorValueSetterCallback;
import com.d2s.framework.model.descriptor.IFileFilterable;

/**
 * Lets the user browse the local file system and choose a file to update the
 * content of a binary property. Files are filtered based on the file filter
 * defined in the binary property descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
