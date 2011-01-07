/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * This action simply augment the context with a page offset integer (
 * <code>PAGE_OFFSET</code>). It is meant to be linked to a find/query action
 * that will further leverage this offset to navigate a pageable result set.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class PageOffsetAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * Page offset action constant.
   */
  public static final String PAGE_OFFSET = "PAGE_OFFSET";

  private Integer            pageOffset;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    context.put(PAGE_OFFSET, pageOffset);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the page offset to be set to the action context.
   * 
   * @param pageOffset
   *          the pageOffset to set.
   */
  public void setPageOffset(Integer pageOffset) {
    this.pageOffset = pageOffset;
  }
}
