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
package org.jspresso.framework.mockups;

import org.jspresso.framework.application.frontend.file.IFileSaveCallback;

/**
 * A mock for SaveFileAction.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class SaveFileActionMock<E, F, G> extends ChooseFileActionMock<E, F, G> {

  private String            contentType;
  private IFileSaveCallback fileSaveCallback;

  /**
   * Gets the contentType.
   *
   * @return the contentType.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets the contentType.
   *
   * @param contentType
   *          the contentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Gets the fileSaveCallback.
   *
   * @return the fileSaveCallback.
   */
  public IFileSaveCallback getFileSaveCallback() {
    return fileSaveCallback;
  }

  /**
   * Sets the fileSaveCallback.
   *
   * @param fileSaveCallback
   *          the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }

}
