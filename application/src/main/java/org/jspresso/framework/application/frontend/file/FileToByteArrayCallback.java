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
package org.jspresso.framework.application.frontend.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.action.AbstractActionContextAware;

/**
 * Default handler implementation to fully read the file input stream into a byte
 * array and setting it in the context.
 *
 * @author Vincent Vandenschrick
 */
public class FileToByteArrayCallback extends AbstractActionContextAware
    implements IFileOpenCallback {

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancel(IActionHandler actionHandler, Map<String, Object> context) {
    context.remove(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fileChosen(String name, InputStream in,
      IActionHandler actionHandler, Map<String, Object> context) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      InputStream is = new BufferedInputStream(in);
      int b;
      while ((b = is.read()) != -1) {
        baos.write(b);
      }
      baos.flush();
      byte[] fileContent = baos.toByteArray();
      setActionParameter(fileContent, context);
    } catch (IOException ex) {
      throw new ActionException(ex);
    } finally {
      try {
        baos.close();
      } catch (IOException ex) {
        // NO-OP.
      }
    }
  }

}
