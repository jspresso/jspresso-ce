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

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A composite view descriptor that aggregates view sections on a single page.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileCompositePageViewDescriptor extends AbstractMobilePageViewDescriptor {

  private List<IMobilePageSectionViewDescriptor> pageSections;
  private boolean inlineEditing;

  /**
   * Instantiates a new Mobile composite page view descriptor.
   */
  public MobileCompositePageViewDescriptor() {
    inlineEditing = false;
  }

  /**
   * Is cascading models.
   *
   * @return always false since a single mobile page does not cascade models.
   */
  @Override
  public boolean isCascadingModels() {
    return false;
  }

  /**
   * Sets cascading models. This operation is not supported on mobile pages.
   *
   * @param cascadingModels
   *     the cascading models
   */
  @Override
  public void setCascadingModels(boolean cascadingModels) {
    throw new UnsupportedOperationException("Cannot configure cascading model on Mobile containers");
  }

  /**
   * Sets page sections.
   *
   * @param pageSections
   *     the page sections
   */
  public void setPageSections(List<IMobilePageSectionViewDescriptor> pageSections) {
    this.pageSections = pageSections;
  }

  /**
   * Gets page sections.
   *
   * @return the page sections
   */
  public List<IMobilePageSectionViewDescriptor> getPageSections() {
    if (pageSections != null) {
      List<IMobilePageSectionViewDescriptor> refinedPageSections = new ArrayList<>();
      IViewDescriptor previousViewDescriptor = null;
      for (IMobilePageSectionViewDescriptor pageSection : pageSections) {
        completeChildDescriptor(pageSection, previousViewDescriptor);
        previousViewDescriptor = pageSection;
        if (pageSection instanceof MobileComponentViewDescriptor) {
          if (isInlineEditing()) {
            refinedPageSections.add(pageSection);
          } else {
            refinedPageSections.add(((MobileComponentViewDescriptor) pageSection).cloneReadOnly());
          }
        } else {
          refinedPageSections.add(pageSection);
        }
      }
      return refinedPageSections;
    }
    return null;
  }

  /**
   * Completes child view descriptors before returning them.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IMobilePageSectionViewDescriptor> childViewDescriptors = getPageSections();
    if (childViewDescriptors != null) {
      return new ArrayList<IViewDescriptor>(childViewDescriptors);
    }
    return null;
  }

  /**
   * Clones the page, keeping only form sections and making them editable.
   *
   * @return the editable clone of the page.
   */
  public MobileCompositePageViewDescriptor cloneEditable() {
    MobileCompositePageViewDescriptor editableClone = (MobileCompositePageViewDescriptor) clone();
    editableClone.setInlineEditing(true);
    if (pageSections != null) {
      List<IMobilePageSectionViewDescriptor> editableSections = new ArrayList<>();
      for (IMobilePageSectionViewDescriptor section : pageSections) {
        if (section instanceof MobileComponentViewDescriptor) {
          editableSections.add(section);
        }
      }
      editableClone.setPageSections(editableSections);
    }
    return editableClone;
  }

  /**
   * Is inline editing.
   *
   * @return the boolean
   */
  public boolean isInlineEditing() {
    return inlineEditing;
  }

  /**
   * Sets inline editing.
   *
   * @param inlineEditing the inline editing
   */
  public void setInlineEditing(boolean inlineEditing) {
    this.inlineEditing = inlineEditing;
  }
}
