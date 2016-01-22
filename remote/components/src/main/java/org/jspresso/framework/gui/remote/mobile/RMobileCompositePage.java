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
package org.jspresso.framework.gui.remote.mobile;

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;

/**
 * A mobile nav page.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileCompositePage extends RMobilePage {

  private static final long serialVersionUID = 1457626334914255111L;
  private RComponent[]         pageSections;
  private RMobileCompositePage editorPage;
  private RAction              editAction;

  /**
   * Constructs a new {@code RMobileCardPage} instance.
   *
   * @param guid
   *     the guid
   */
  public RMobileCompositePage(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileCardPage} instance. Only used for
   * serialization support.
   */
  public RMobileCompositePage() {
    // For serialization support
  }

  /**
   * Get page sections.
   *
   * @return the r component [ ]
   */
  public RComponent[] getPageSections() {
    return pageSections;
  }

  /**
   * Sets page sections.
   *
   * @param pageSections
   *     the page sections
   */
  public void setPageSections(RComponent... pageSections) {
    this.pageSections = pageSections;
  }

  /**
   * Gets editor page.
   *
   * @return the editor page
   */
  public RMobileCompositePage getEditorPage() {
    return editorPage;
  }

  /**
   * Sets editor page.
   *
   * @param editorPage
   *     the editor page
   */
  public void setEditorPage(RMobileCompositePage editorPage) {
    this.editorPage = editorPage;
  }


  /**
   * Gets edit action.
   *
   * @return the edit action
   */
  public RAction getEditAction() {
    return editAction;
  }

  /**
   * Sets edit action.
   *
   * @param editAction the edit action
   */
  public void setEditAction(RAction editAction) {
    this.editAction = editAction;
  }
}
