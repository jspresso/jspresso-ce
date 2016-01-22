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
package org.jspresso.framework.application.frontend.action.std;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQbeAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;

/**
 * This action simply augment the context with a page offset integer (
 * {@code PAGE_OFFSET}). It is meant to be linked to a find/query action
 * that will further leverage this offset to navigate a pageable result set.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class PageOffsetAction<E, F, G> extends FrontendAction<E, F, G> {

  private Integer pageOffset;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    context.put(AbstractQbeAction.PAGINATE, null);
    IPageable pageableModel = getModel(context);
    /*
     * we are on the pagination button view.
     */
    int[] collectionViewPath = new int[] {
        -1, -1
    };
    List<?> stickyResults = null;
    IView<E> collectionView = getView(collectionViewPath, context);
    if (collectionView.getDescriptor() instanceof ICollectionViewDescriptor
        && ((ICollectionViewDescriptor) collectionView.getDescriptor())
            .getSelectionMode() == ESelectionMode.MULTIPLE_INTERVAL_CUMULATIVE_SELECTION
        && !(pageableModel instanceof FilterableBeanCollectionModule)) {
      stickyResults = getSelectedModels(collectionViewPath, context);
    }
    pageableModel.setStickyResults(stickyResults);

    try {
      if (pageOffset != null) {
        if (pageableModel.getPage() != null) {
          pageableModel.setPage(pageableModel.getPage() + pageOffset);
        } else {
          pageableModel.setPage(pageOffset);
        }
      }
      return super.execute(actionHandler, context);
    } finally {
      if (stickyResults != null) {
        setSelectedModels(collectionViewPath, stickyResults, context);
      }
      pageableModel.setStickyResults(null);
    }
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
