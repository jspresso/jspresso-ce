/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.collection;

/**
 * Pageable models.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision: 1826 $
 * @author Vincent Vandenschrick
 */
public interface IPageable {

  /**
   * "page" string constant.
   */
  String PAGE                  = "page";

  /**
   * "pageSize" string constant.
   */
  String PAGE_SIZE             = "pageSize";

  /**
   * "recordCount" string constant.
   */
  String RECORD_COUNT          = "recordCount";

  /**
   * "pageCount" string constant.
   */
  String PAGE_COUNT            = "pageCount";

  /**
   * "nextPageEnabled" string constant.
   */
  String NEXT_PAGE_ENABLED     = "nextPageEnabled";

  /**
   * "previousPageEnabled" string constant.
   */
  String PREVIOUS_PAGE_ENABLED = "previousPageEnabled";

  /**
   * Gets the page number used for query actions.
   * 
   * @return the page number used for query actions.
   */
  Integer getPage();

  /**
   * Sets the page number used for query actions.
   * 
   * @param page
   *          the page number used for query actions.
   */
  void setPage(Integer page);

  /**
   * Gets the page size used for query actions.
   * 
   * @return the page size used for query actions.
   */
  Integer getPageSize();

  /**
   * Sets the page size used for query actions.
   * 
   * @param pageSize
   *          the page size used for query actions.
   */
  void setPageSize(Integer pageSize);

  /**
   * Gets the current record count.
   * 
   * @return the the current record count.
   */
  Integer getRecordCount();

  /**
   * Sets the current record count.
   * 
   * @param recordCount
   *          current record count.
   */
  void setRecordCount(Integer recordCount);

  /**
   * Gets the current page count.
   * 
   * @return the the current page count.
   */
  Integer getPageCount();

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
}
