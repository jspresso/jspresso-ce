/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component;

import java.util.List;
import java.util.Map;

/**
 * A simple adapter to wrap a component used as selection criteria and a list of
 * components. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IQueryComponent extends Map<String, Object> {

  /**
   * "queriedComponents" string constant.
   */
  String QUERIED_COMPONENTS    = "queriedComponents";

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
   * Gets the list of components result of the query.
   * 
   * @return the list of components result of the query.
   */
  List<? extends IComponent> getQueriedComponents();

  /**
   * Gets the contract of the components to query.
   * 
   * @return the contract of the components to query.
   */
  Class<?> getQueryContract();

  /**
   * Sets the list of components result of the query.
   * 
   * @param queriedComponents
   *          the list of components result of the query.
   */
  void setQueriedComponents(List<? extends IComponent> queriedComponents);

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
