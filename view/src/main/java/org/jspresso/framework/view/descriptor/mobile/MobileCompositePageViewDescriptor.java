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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A composite view descriptor that aggregates view sections on a single page.
 *
 * @author Vincent Vandenschrick
 */
public class MobileCompositePageViewDescriptor extends AbstractMobilePageViewDescriptor {

  private List<IMobilePageSectionViewDescriptor> pageSectionDescriptors;
  private boolean                                inlineEditing;
  private MobileCompositePageViewDescriptor      editorPage;

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
   * @param pageSectionDescriptors
   *     the page sections
   */
  public void setPageSectionDescriptors(List<IMobilePageSectionViewDescriptor> pageSectionDescriptors) {
    this.pageSectionDescriptors = pageSectionDescriptors;
  }

  /**
   * Gets page sections.
   *
   * @return the page sections
   */
  public List<IMobilePageSectionViewDescriptor> getPageSectionDescriptors() {
    if (pageSectionDescriptors != null) {
      List<IMobilePageSectionViewDescriptor> refinedPageSections = new ArrayList<>();
      IViewDescriptor previousViewDescriptor = null;
      for (IMobilePageSectionViewDescriptor pageSection : pageSectionDescriptors) {
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
   *
   * @return the child view descriptors
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IMobilePageSectionViewDescriptor> childViewDescriptors = getPageSectionDescriptors();
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
  protected MobileCompositePageViewDescriptor cloneEditable() {
    MobileCompositePageViewDescriptor editableClone = (MobileCompositePageViewDescriptor) clone();
    if (editableClone.getModelDescriptor() instanceof IReferencePropertyDescriptor<?>) {
      editableClone.setModelDescriptor(
          ((IReferencePropertyDescriptor<?>) editableClone.getModelDescriptor()).getReferencedDescriptor());
    }
    editableClone.setInlineEditing(true);
    if (pageSectionDescriptors != null) {
      List<IMobilePageSectionViewDescriptor> editableSections = new ArrayList<>();
      for (IMobilePageSectionViewDescriptor section : pageSectionDescriptors) {
        if (section instanceof MobileComponentViewDescriptor) {
          editableSections.add(section);
        }
      }
      editableClone.setPageSectionDescriptors(editableSections);
    }
    editableClone.setActionMap(null);
    return editableClone;
  }

  /**
   * Is inline editing.
   *
   * @return the boolean
   */
  public boolean isInlineEditing() {
    return inlineEditing || getModelDescriptor() == null;
  }

  /**
   * Sets inline editing.
   *
   * @param inlineEditing
   *     the inline editing
   */
  public void setInlineEditing(boolean inlineEditing) {
    this.inlineEditing = inlineEditing;
  }

  /**
   * Gets editing page. If not explicitly set, the editing page is computed based on the editable fields of the page.
   *
   * @return the editing page
   */
  public MobileCompositePageViewDescriptor getEditorPage() {
    if (editorPage == null) {
      editorPage = cloneEditable();
    }
    editorPage.setInlineEditing(true);
    return editorPage;
  }


  /**
   * Filter properties.
   *
   * @return the mobile composite page view descriptor
   */
  public MobileCompositePageViewDescriptor filterForReading() {
    List<IMobilePageSectionViewDescriptor> sections = getPageSectionDescriptors();
    List<IMobilePageSectionViewDescriptor> filteredSections = new ArrayList<>();
    for (IMobilePageSectionViewDescriptor section : sections) {
      if (section instanceof MobileComponentViewDescriptor) {
        MobileComponentViewDescriptor filteredSection = ((MobileComponentViewDescriptor) section).filterForReading();
        if (filteredSection.getPropertyViewDescriptors().size() > 0) {
          filteredSections.add(filteredSection);
        }
      } else {
        filteredSections.add(section);
      }
    }
    MobileCompositePageViewDescriptor filteredPage = (MobileCompositePageViewDescriptor) clone();
    filteredPage.setPageSectionDescriptors(filteredSections);
    return filteredPage;
  }

  /**
   * Filter properties.
   *
   * @return the mobile composite page view descriptor
   */
  public MobileCompositePageViewDescriptor filterForWriting() {
    List<IMobilePageSectionViewDescriptor> sections = getPageSectionDescriptors();
    List<IMobilePageSectionViewDescriptor> filteredSections = new ArrayList<>();
    for (IMobilePageSectionViewDescriptor section : sections) {
      if (section instanceof MobileComponentViewDescriptor) {
        MobileComponentViewDescriptor filteredSection = ((MobileComponentViewDescriptor) section).filterForWriting();
        if (filteredSection.getPropertyViewDescriptors().size() > 0) {
          filteredSections.add(filteredSection);
        }
      }
    }
    MobileCompositePageViewDescriptor filteredPage = (MobileCompositePageViewDescriptor) clone();
    filteredPage.setPageSectionDescriptors(filteredSections);
    return filteredPage;
  }

  /**
   * Sets editing page.
   *
   * @param editorPage
   *     the editing page
   */
  public void setEditorPage(MobileCompositePageViewDescriptor editorPage) {
    this.editorPage = editorPage;
  }
}
