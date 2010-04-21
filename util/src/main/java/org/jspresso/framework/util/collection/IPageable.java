/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
  String NEXT_PAGE_ENABLED     = "nextPageEnabled";

  /**
   * "page" string constant.
   */
  String PAGE                  = "page";

  /**
   * "pageCount" string constant.
   */
  String PAGE_COUNT            = "pageCount";

  /**
   * "pageSize" string constant.
   */
  String PAGE_SIZE             = "pageSize";

  /**
   * "previousPageEnabled" string constant.
   */
  String PREVIOUS_PAGE_ENABLED = "previousPageEnabled";

  /**
   * "recordCount" string constant.
   */
  String RECORD_COUNT          = "recordCount";

  /**
   * Gets the page number used for query actions.
   * 
   * @return the page number used for query actions.
   */
  Integer getPage();

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
}
