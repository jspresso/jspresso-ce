/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision$
 */
public class RMobileNavPage extends RMobilePage {

  private static final long serialVersionUID = 5221935284465846374L;
  private RComponent  headerView;
  private RComponent  selectionView;
  private RMobilePage nextPage;
  private RAction     pageEndAction;

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
   * Gets page end action.
   *
   * @return the page end action
   */
  public RAction getPageEndAction() {
    return pageEndAction;
  }

  /**
   * Sets page end action.
   *
   * @param pageEndAction the page end action
   */
  public void setPageEndAction(RAction pageEndAction) {
    this.pageEndAction = pageEndAction;
  }

  /**
   * Gets header view.
   *
   * @return the header view
   */
  public RComponent getHeaderView() {
    return headerView;
  }

  /**
   * Sets header view.
   *
   * @param headerView the header view
   */
  public void setHeaderView(RComponent headerView) {
    this.headerView = headerView;
  }
}
