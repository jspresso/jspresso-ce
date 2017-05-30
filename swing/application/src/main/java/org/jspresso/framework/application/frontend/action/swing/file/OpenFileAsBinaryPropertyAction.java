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
package org.jspresso.framework.application.frontend.action.swing.file;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.ConnectorValueSetterCallback;
import org.jspresso.framework.model.descriptor.IFileFilterable;

/**
 * Lets the user browse the local file system and choose a file to update the
 * content of a binary property. Files are filtered based on the file filter
 * defined in the binary property descriptor.
 *
 * @author Vincent Vandenschrick
 */
public class OpenFileAsBinaryPropertyAction extends OpenFileAction {

  private String modelPath;
  private ConnectorValueSetterCallback fileOpenCallback;

  /**
   * Constructs a new {@code OpenFileAsBinaryPropertyAction} instance.
   */
  public OpenFileAsBinaryPropertyAction() {
    fileOpenCallback = new ConnectorValueSetterCallback();
    setFileOpenCallback(fileOpenCallback);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Map<String, List<String>> getFileFilter(Map<String, Object> context) {
    IFileFilterable modelDescriptor = (IFileFilterable) getModelDescriptor(getModelPath(context), context);
    return modelDescriptor.getFileFilter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Integer getFileMaxSize(Map<String, Object> context) {
    IFileFilterable modelDescriptor = (IFileFilterable) getModelDescriptor(getModelPath(context), context);
    return modelDescriptor.getMaxLength();
  }

  /**
   * Gets model path.
   *
   * @param context
   *     the context
   * @return the model path
   */
  protected String getModelPath(Map<String, Object> context) {
    return modelPath;
  }

  /**
   * Sets model path.
   *
   * @param modelPath
   *     the model path
   */
  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
    fileOpenCallback.setModelPath(modelPath);
  }
}
