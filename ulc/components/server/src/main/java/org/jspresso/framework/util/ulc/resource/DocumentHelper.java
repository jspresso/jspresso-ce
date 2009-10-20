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
package org.jspresso.framework.util.ulc.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

import com.ulcjava.base.application.ClientContext;

/**
 * This class allows to register a resource provider by a unique id and to
 * display the resource provider's content for a given id.
 */
public final class DocumentHelper {

  private static final String APPLICATION_PREFIX = "application/";

  private DocumentHelper() {
    // Helper class constructor.
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *          the identifier under which the resource has been registered.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  public static void showDocument(String id) throws IOException {
    showDocument(id, null);
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *          the identifier under which the resource has been registered.
   * @param target
   *          the target browser id.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  public static void showDocument(String id, String target) throws IOException {
    if (inDevelopmentEnvironment()) {
      IResource resourceProvider = ResourceManager.getInstance().getRegistered(
          id);
      String fileExtension = ".tmp";
      if (resourceProvider.getMimeType() != null
          && resourceProvider.getMimeType().startsWith(APPLICATION_PREFIX)) {
        fileExtension = "."
            + resourceProvider.getMimeType().substring(
                APPLICATION_PREFIX.length());
      }
      String url = createTemporaryFile(resourceProvider.getContent(),
          fileExtension).toURI().toURL().toString();
      ClientContext.showDocument(url, target);
    } else if (inServletContainerEnvironment()) {
      String url = ResourceProviderServlet.computeDownloadUrl(id);
      ClientContext.showDocument(url, target);
    } else {
      throw new IllegalStateException(
          "Could not determine server runtime environment.");
    }
  }

  private static File createTemporaryFile(InputStream is, String fileExtension)
      throws IOException {
    File file = File.createTempFile(DocumentHelper.class.getName(),
        fileExtension);
    file.deleteOnExit();

    BufferedInputStream inputStream = new BufferedInputStream(is);
    BufferedOutputStream outputStream = new BufferedOutputStream(
        new FileOutputStream(file));

    IoHelper.copyStream(inputStream, outputStream);

    inputStream.close();
    outputStream.close();

    return file;
  }

  private static boolean inDevelopmentEnvironment() {
    try {
      Class.forName("com.ulcjava.base.development.DevelopmentRunner");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }

  private static boolean inServletContainerEnvironment() {
    try {
      Class
          .forName("com.ulcjava.container.servlet.application.ServletContainerContext");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }
}
