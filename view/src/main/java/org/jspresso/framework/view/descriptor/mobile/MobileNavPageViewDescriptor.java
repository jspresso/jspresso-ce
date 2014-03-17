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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Navigation page view descriptors that are able to navigate to another page based on a selection component.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileNavPageViewDescriptor extends AbstractMobilePageViewDescriptor {

  private IViewDescriptor           headerView;
  private IViewDescriptor           selectionView;
  private IMobilePageViewDescriptor nextPage;

  /**
   * Is cascading models.
   *
   * @return always true since a mobile list always cascade models to the next page.
   */
  @Override
  public boolean isCascadingModels() {
    return false;
  }

  /**
   * Sets cascading models. This operation is not supported on mobile lists.
   *
   * @param cascadingModels
   *     the cascading models
   */
  @Override
  public void setCascadingModels(boolean cascadingModels) {
    throw new UnsupportedOperationException("Cannot configure cascading model on Mobile containers");
  }

  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    return Arrays.asList(getHeaderView(), getSelectionView());
  }

  /**
   * Gets selection view.
   *
   * @return the selection view
   */
  public IViewDescriptor getSelectionView() {
    completeChildDescriptor(selectionView, null);
    return selectionView;
  }

  /**
   * Sets selection view. Supports only tree or list.
   *
   * @param selectionView the selection view
   */
  public void setSelectionView(IViewDescriptor selectionView) {
    if (selectionView instanceof IListViewDescriptor || selectionView instanceof ITreeViewDescriptor) {
      this.selectionView = selectionView;
    } else {
      throw new IllegalArgumentException("Only list or tree is supported as selection view.");
    }
  }

  /**
   * Gets next page.
   *
   * @return the next page
   */
  public IMobilePageViewDescriptor getNextPage() {
    completeChildDescriptor(nextPage, selectionView);
    return nextPage;
  }

  /**
   * Sets next page.
   *
   * @param nextPage the next page
   */
  public void setNextPage(IMobilePageViewDescriptor nextPage) {
    this.nextPage = nextPage;
  }


  /**
   * Delegates to the selection view.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider, Locale locale) {
    if (getName() == null && getI18nNameKey() == null) {
      return getSelectionView().getI18nName(translationProvider, locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * Delegates to the selection view.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider, Locale locale) {
    if (getDescription() == null) {
      return getSelectionView().getI18nDescription(translationProvider, locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * Gets header view.
   *
   * @return the header view
   */
  public IViewDescriptor getHeaderView() {
    completeChildDescriptor(headerView, null);
    return headerView;
  }

  /**
   * Sets header view.
   *
   * @param headerView the header view
   */
  public void setHeaderView(IViewDescriptor headerView) {
    this.headerView = headerView;
  }
}
