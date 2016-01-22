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

import org.jspresso.framework.gui.remote.RCardContainer;

/**
 * A mobile card page.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileCardPage extends RMobilePage {

  private static final long serialVersionUID = -3116785224570727873L;

  private RCardContainer pages;

  /**
   * Constructs a new {@code RMobileCardPage} instance.
   *
   * @param guid
   *          the guid
   */
  public RMobileCardPage(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileCardPage} instance. Only used for
   * serialization support.
   */
  public RMobileCardPage() {
    // For serialization support
  }

  /**
   * Gets pages.
   *
   * @return the pages
   */
  public RCardContainer getPages() {
    return pages;
  }

  /**
   * Sets pages.
   *
   * @param pages the pages
   */
  public void setPages(RCardContainer pages) {
    this.pages = pages;
  }
}
