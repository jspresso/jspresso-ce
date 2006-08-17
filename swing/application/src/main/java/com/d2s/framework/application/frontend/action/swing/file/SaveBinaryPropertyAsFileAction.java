/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.ConnectorValueGetterCallback;
import com.d2s.framework.model.descriptor.IBinaryPropertyDescriptor;

/**
 * Lets the user browse the local file system and choose a file to store the
 * content of a binary property. Files are filtered based on the file filter
 * defined in the binary property descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveBinaryPropertyAsFileAction extends SaveFileAction {

  /**
   * Constructs a new <code>OpenFileAsBinaryPropertyAction</code> instance.
   */
  public SaveBinaryPropertyAsFileAction() {
    setFileSaveCallback(new ConnectorValueGetterCallback());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IBinaryPropertyDescriptor modelDescriptor = (IBinaryPropertyDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
    setFileFilter(modelDescriptor.getFileFilter());
    return super.execute(actionHandler, context);
  }
}
