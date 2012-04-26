/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.collection;

import java.util.List;

/**
 * Pageable models.
 * 
 * @version $LastChangedRevision: 1826 $
 * @author Vincent Vandenschrick
 */
public interface IPageable {

  /**
   * "nextPageEnabled" string constant.
   */
  String NEXT_PAGE_ENABLED       = "nextPageEnabled";

  /**
   * "page" string constant.
   */
  String PAGE                    = "page";

  /**
   * "displayPageIndex" string constant.
   */
  String DISPLAY_PAGE_INDEX      = "displayPageIndex";

  /**
   * "pageCount" string constant.
   */
  String PAGE_COUNT              = "pageCount";

  /**
   * "pageNavigationEnabled" string constant.
   */
  String PAGE_NAVIGATION_ENABLED = "pageNavigationEnabled";

  /**
   * "pageSize" string constant.
   */
  String PAGE_SIZE               = "pageSize";

  /**
   * "previousPageEnabled" string constant.
   */
  String PREVIOUS_PAGE_ENABLED   = "previousPageEnabled";

  /**
   * "recordCount" string constant.
   */
  String RECORD_COUNT            = "recordCount";

  /**
   * Gets the page number used for query actions.
   * 
   * @return the page number used for query actions.
   */
  Integer getPage();

  /**
   * Gets the page index to display.
   * 
   * @return the page index to display graphically.
   */
  Integer getDisplayPageIndex();

  /**
   * Sets the page index to display.
   * 
   * @param displayPageIndex
   *          the page index to display.
   */
  void setDisplayPageIndex(Integer displayPageIndex);

  /**
   * Gets the current page count.
   * 
   * @return the the current page count.
   */
  Integer getPageCount();

  /**
   * Gets the page size used for query actions.
   * 
   * @return the page size used for query actions.
   */
  Integer getPageSize();

  /**
   * Gets the current record count.
   * 
   * @return the the current record count.
   */
  Integer getRecordCount();

  /**
   * Wether navigation to next page is enabled.
   * 
   * @return true if navigation to next page is enabled, false otherwise.
   */
  boolean isNextPageEnabled();

  /**
   * Wether page navigation is enabled.
   * 
   * @return true if page navigation is enabled, false otherwise.
   */
  boolean isPageNavigationEnabled();

  /**
   * Wether navigation to previous page is enabled.
   * 
   * @return true if navigation to previous page is enabled, false otherwise.
   */
  boolean isPreviousPageEnabled();

  /**
   * Sets the page number used for query actions.
   * 
   * @param page
   *          the page number used for query actions.
   */
  void setPage(Integer page);

  /**
   * Sets the page size used for query actions.
   * 
   * @param pageSize
   *          the page size used for query actions.
   */
  void setPageSize(Integer pageSize);

  /**
   * Sets the current record count.
   * 
   * @param recordCount
   *          current record count.
   */
  void setRecordCount(Integer recordCount);

  /**
   * Gets the collection of sticky results.
   * 
   * @param stickyResults
   *          the collection of sticky results.
   */
  void setStickyResults(List<?> stickyResults);

  /**
   * Sets the collection of sticky results.
   * 
   * @return the collection of sticky results.
   */
  List<?> getStickyResults();
}
