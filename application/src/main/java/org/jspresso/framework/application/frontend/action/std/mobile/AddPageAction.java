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
package org.jspresso.framework.application.frontend.action.std.mobile;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.collection.IPageable;

/**
 * A special mobile pagination action that adds a page to the current results.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AddPageAction<E, F, G> extends FrontendAction<E, F, G> {
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    IPageable pageableModel = getModel(context);
    try {
      Integer page = pageableModel.getPage();
      Integer pageCount = pageableModel.getPageCount();
      if (pageCount != null && (page == null || (page + 1 < pageCount))) {
        context.put(AbstractQbeAction.PAGINATE, null);
        pageableModel.setStickyResults(pageableModel.getResults());
        if (page != null) {
          pageableModel.setPage(page + 1);
        } else {
          pageableModel.setPage(1);
        }
        return super.execute(actionHandler, context);
      } else {
        return false;
      }
    } finally {
      context.remove(AbstractQbeAction.PAGINATE);
      pageableModel.setStickyResults(null);
    }
  }
}
