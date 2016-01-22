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
public class RMobileNavPage extends RMobilePage {

  private static final long serialVersionUID = 5221935284465846374L;
  private RComponent[]         headerSections;
  private RComponent           selectionView;
  private RMobilePage          nextPage;
  private RMobileNavPage       editorPage;
  private RAction              editAction;

  /**
   * Constructs a new {@code RMobileCardPage} instance.
   *
   * @param guid
   *     the guid
   */
  public RMobileNavPage(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileCardPage} instance. Only used for
   * serialization support.
   */
  public RMobileNavPage() {
    // For serialization support
  }

  /**
   * Gets selection view.
   *
   * @return the selection view
   */
  public RComponent getSelectionView() {
    return selectionView;
  }

  /**
   * Sets selection view.
   *
   * @param selectionView
   *     the selection view
   */
  public void setSelectionView(RComponent selectionView) {
    this.selectionView = selectionView;
  }

  /**
   * Gets next page.
   *
   * @return the next page
   */
  public RMobilePage getNextPage() {
    return nextPage;
  }

  /**
   * Sets next page.
   *
   * @param nextPage
   *     the next page
   */
  public void setNextPage(RMobilePage nextPage) {
    this.nextPage = nextPage;
  }

  /**
   * Get header sections.
   *
   * @return the r component [ ]
   */
  public RComponent[] getHeaderSections() {
    return headerSections;
  }

  /**
   * Sets header sections.
   *
   * @param headerSections the header sections
   */
  public void setHeaderSections(RComponent... headerSections) {
    this.headerSections = headerSections;
  }

  /**
   * Gets editor page.
   *
   * @return the editor page
   */
  public RMobileNavPage getEditorPage() {
    return editorPage;
  }

  /**
   * Sets editor page.
   *
   * @param editorPage the editor page
   */
  public void setEditorPage(RMobileNavPage editorPage) {
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
