/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicNestedComponentPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * A default implementation for query view descriptor factory.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 1091 $
 */
public class BasicQueryViewDescriptorFactory<E, F, G> implements IQueryViewDescriptorFactory {

  private boolean useCompactComparableQueryStructures;

  private IDisplayableAction okCloseDialogAction;
  private IDisplayableAction modalDialogAction;
  private String             defaultFindIconImageUrl;

  /**
   * Constructs a new {@code BasicQueryViewDescriptorFactory} instance.
   */
  public BasicQueryViewDescriptorFactory() {
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public IViewDescriptor createQueryViewDescriptor(IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
                                                   IComponentDescriptor<? extends IQueryComponent>
                                                       queryComponentDescriptor,
                                                   Map<String, Object> actionContext) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();
    for (String queriableProperty : componentDescriptorProvider.getQueryableProperties()) {
      IPropertyDescriptor actualPropertyDescriptor = queryComponentDescriptor.getPropertyDescriptor(queriableProperty);
      // To preserve col spans for query structures.
      if (actualPropertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        if (isUseCompactComparableQueryStructures()) {
          IPropertyViewDescriptor propertyView = createCompactComparablePropertyViewDescriptor(
              queriableProperty, (ComparableQueryStructureDescriptor) actualPropertyDescriptor);
          propertyViewDescriptors.add(propertyView);
        } else {
          IPropertyViewDescriptor propertyView = createNestedComparablePropertyViewDescriptor(
              queriableProperty, actualPropertyDescriptor);
          propertyViewDescriptors.add(propertyView);
        }
      } else if (actualPropertyDescriptor instanceof EnumQueryStructureDescriptor) {
        BasicPropertyViewDescriptor propertyView = createEnumReferencePropertyViewDescriptor(queriableProperty,
            (EnumQueryStructureDescriptor) actualPropertyDescriptor);
        propertyViewDescriptors.add(propertyView);
      } else {
        BasicPropertyViewDescriptor propertyView = createDefaultPropertyViewDescriptor(queriableProperty);
        propertyViewDescriptors.add(propertyView);
      }
    }
    // We have to delay width computation in order to solve bug #663.
    for (IPropertyViewDescriptor propertyView : propertyViewDescriptors) {
      int width;
//      if (propertyView.getName().endsWith(
//          ComparableQueryStructureDescriptor.COMPARATOR)
//          || propertyView.getName().endsWith(
//              ComparableQueryStructureDescriptor.INF_VALUE)) {
//        width = 1;
//      } else if (propertyView.getName().endsWith(
//          ComparableQueryStructureDescriptor.SUP_VALUE)) {
//        width = 2;
//      } else {
      width = 4;
//      }
      ((BasicPropertyViewDescriptor) propertyView).setWidth(width);
    }
    queryComponentViewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);
    queryComponentViewDescriptor.setColumnCount(8);

    queryComponentViewDescriptor.setModelDescriptor(queryComponentDescriptor);
    queryComponentViewDescriptor.setWidthResizeable(false);
    return queryComponentViewDescriptor;
  }

  /**
   * Create default property view descriptor basic property view descriptor.
   *
   * @param queriableProperty
   *     the queriable property
   * @return the basic property view descriptor
   */
  protected BasicPropertyViewDescriptor createDefaultPropertyViewDescriptor(String queriableProperty) {
    BasicPropertyViewDescriptor propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(queriableProperty);
    return propertyView;
  }

  /**
   * Create enum reference property view descriptor basic reference property view descriptor.
   *
   * @param queriableProperty
   *     the queriable property
   * @param actualPropertyDescriptor
   *     the actual property descriptor
   * @return the basic reference property view descriptor
   */
  protected BasicPropertyViewDescriptor createEnumReferencePropertyViewDescriptor(String queriableProperty,
                                                                                           EnumQueryStructureDescriptor actualPropertyDescriptor) {
    BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
    propertyView.setName(queriableProperty);
    propertyView.setLovAction(createEnumSelectAction(actualPropertyDescriptor));
    propertyView.setAutoCompleteEnabled(false);
    return propertyView;
  }

  /**
   * Create compact comparable property view descriptor basic reference property view descriptor.
   *
   * @param queriableProperty
   *     the queriable property
   * @param actualPropertyDescriptor
   *     the actual property descriptor
   * @return the basic reference property view descriptor
   */
  protected BasicPropertyViewDescriptor createCompactComparablePropertyViewDescriptor(String queriableProperty,
                                                                                               ComparableQueryStructureDescriptor actualPropertyDescriptor) {
    BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
    propertyView.setName(queriableProperty);
    propertyView.setLovAction(
        createComparableEditAction(actualPropertyDescriptor));
    propertyView.setAutoCompleteEnabled(false);
    return propertyView;
  }

  /**
   * Create nested comparable property view descriptor basic nested component property view descriptor.
   *
   * @param queriableProperty
   *     the queriable property
   * @param actualPropertyDescriptor
   *     the actual property descriptor
   * @return the basic nested component property view descriptor
   */
  protected BasicPropertyViewDescriptor createNestedComparablePropertyViewDescriptor(
      String queriableProperty, IPropertyDescriptor actualPropertyDescriptor) {
    BasicComponentViewDescriptor comparableComponentViewDescriptor = new BasicComponentViewDescriptor();
    comparableComponentViewDescriptor.setLabelsPosition(ELabelPosition.NONE);
    comparableComponentViewDescriptor.setWidthResizeable(false);
    comparableComponentViewDescriptor.setColumnCount(3);
    comparableComponentViewDescriptor.setModelDescriptor(
        ((ComparableQueryStructureDescriptor) actualPropertyDescriptor).getReferencedDescriptor());
    comparableComponentViewDescriptor.setRenderedProperties(Arrays
        .asList(ComparableQueryStructureDescriptor.COMPARATOR,
            ComparableQueryStructureDescriptor.INF_VALUE,
            ComparableQueryStructureDescriptor.SUP_VALUE));
    BasicNestedComponentPropertyViewDescriptor propertyView = new BasicNestedComponentPropertyViewDescriptor();
    propertyView.setName(queriableProperty);
    propertyView.setNestedComponentViewDescriptor(comparableComponentViewDescriptor);
    propertyView.setModelDescriptor(actualPropertyDescriptor);
    return propertyView;
  }

  /**
   * Create comparable edit action.
   *
   * @param comparablePropertyDescriptor
   *     the comparable property descriptor
   * @return the edit action
   */
  protected IDisplayableAction createComparableEditAction(
      ComparableQueryStructureDescriptor comparablePropertyDescriptor) {
    EditComponentAction<E, F, G> comparableEditAction = new EditComponentAction<>();
    if (comparablePropertyDescriptor.getIcon() != null) {
      comparableEditAction.setIcon(comparablePropertyDescriptor.getIcon());
    } else {
      comparableEditAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    BasicComponentViewDescriptor viewDescriptor = new BasicComponentViewDescriptor();

    BasicPropertyViewDescriptor propertyView;

    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.COMPARATOR);
    propertyViewDescriptors.add(propertyView);

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.INF_VALUE);
    propertyViewDescriptors.add(propertyView);

    propertyView = new BasicPropertyViewDescriptor();
    propertyView.setName(ComparableQueryStructureDescriptor.SUP_VALUE);
    propertyViewDescriptors.add(propertyView);

    viewDescriptor.setColumnCount(3);
    viewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);

    viewDescriptor.setName(comparablePropertyDescriptor.getName());
    viewDescriptor.setModelDescriptor(comparablePropertyDescriptor.getReferencedDescriptor());

    comparableEditAction.setViewDescriptor(viewDescriptor);
    comparableEditAction.setCancelAction(null);
    comparableEditAction.setOkAction(getOkCloseDialogAction());

    comparableEditAction.setNextAction(getModalDialogAction());

    return comparableEditAction;
  }

  /**
   * Creates the enumSelectAction.
   *
   * @param enumPropertyDescriptor
   *     the enumeration property descriptor to create the LOV action for.
   * @return the enumSelectAction.
   */
  protected IDisplayableAction createEnumSelectAction(final EnumQueryStructureDescriptor enumPropertyDescriptor) {
    EditComponentAction<E, F, G> enumSelectAction = new EditComponentAction<>();
    if (enumPropertyDescriptor.getIcon() != null) {
      enumSelectAction.setIcon(enumPropertyDescriptor.getIcon());
    } else {
      enumSelectAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    BasicBorderViewDescriptor viewDescriptor = new BasicBorderViewDescriptor();
    viewDescriptor.setName(EnumQueryStructureDescriptor.ENUMERATION_VALUES);
    viewDescriptor.setModelDescriptor(enumPropertyDescriptor.getReferencedDescriptor());
    BasicTableViewDescriptor table = new BasicTableViewDescriptor();
    table.setName(EnumQueryStructureDescriptor.ENUMERATION_VALUES);
    viewDescriptor.setCenterViewDescriptor(table);
    enumSelectAction.setViewDescriptor(viewDescriptor);
    enumSelectAction.setCancelAction(null);
    enumSelectAction.setOkAction(getOkCloseDialogAction());

    enumSelectAction.setNextAction(getModalDialogAction());

    return enumSelectAction;
  }

  /**
   * Gets the okCloseDialogAction.
   *
   * @return the okCloseDialogAction.
   */
  protected IDisplayableAction getOkCloseDialogAction() {
    return okCloseDialogAction;
  }

  /**
   * Sets the okCloseDialogAction.
   *
   * @param okCloseDialogAction
   *     the okCloseDialogAction to set.
   */
  public void setOkCloseDialogAction(IDisplayableAction okCloseDialogAction) {
    this.okCloseDialogAction = okCloseDialogAction;
  }

  /**
   * Gets the modalDialogAction.
   *
   * @return the modalDialogAction.
   */
  protected IDisplayableAction getModalDialogAction() {
    return modalDialogAction;
  }

  /**
   * Sets the modalDialogAction.
   *
   * @param modalDialogAction
   *     the modalDialogAction to set.
   */
  public void setModalDialogAction(IDisplayableAction modalDialogAction) {
    this.modalDialogAction = modalDialogAction;
  }

  /**
   * Gets the defaultFindIconImageUrl.
   *
   * @return the defaultFindIconImageUrl.
   */
  protected String getDefaultFindIconImageUrl() {
    return defaultFindIconImageUrl;
  }

  /**
   * Sets the defaultFindIconImageUrl.
   *
   * @param defaultFindIconImageUrl
   *     the defaultFindIconImageUrl to set.
   */
  public void setDefaultFindIconImageUrl(String defaultFindIconImageUrl) {
    this.defaultFindIconImageUrl = defaultFindIconImageUrl;
  }


  /**
   * Is use compact comparable query structures.
   *
   * @return the boolean
   */
  protected boolean isUseCompactComparableQueryStructures() {
    return useCompactComparableQueryStructures;
  }

  /**
   * Sets use compact comparable query structures.
   *
   * @param useCompactComparableQueryStructures
   *     the use compact comparable query structures
   */
  public void setUseCompactComparableQueryStructures(boolean useCompactComparableQueryStructures) {
    this.useCompactComparableQueryStructures = useCompactComparableQueryStructures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <V extends IViewDescriptor> V adaptExistingViewDescriptor(V viewDescriptor) {
    if (viewDescriptor instanceof BasicComponentViewDescriptor) {
      List<IPropertyViewDescriptor> propertyViews = ((BasicComponentViewDescriptor) viewDescriptor)
          .getPropertyViewDescriptors(false);
      for (int i = 0; i < propertyViews.size(); i++) {
        IPropertyViewDescriptor propertyView = propertyViews.get(i);
        if ((propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor
            && isUseCompactComparableQueryStructures()) || propertyView
            .getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
          if (!(propertyView instanceof BasicReferencePropertyViewDescriptor)) {
            propertyView = adaptReferencePropertyViewDescriptor(propertyView);
            propertyViews.set(i, propertyView);
          }
          if (((BasicReferencePropertyViewDescriptor) propertyView).getLovAction() == null) {
            if (propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createComparableEditAction((ComparableQueryStructureDescriptor) propertyView.getModelDescriptor()));
            } else if (propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createEnumSelectAction((EnumQueryStructureDescriptor) propertyView.getModelDescriptor()));
            }
          }
        } else if (propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) {
          BasicPropertyViewDescriptor nestedComparablePropertyViewDescriptor = createNestedComparablePropertyViewDescriptor(
              propertyView.getName(), (IPropertyDescriptor) propertyView.getModelDescriptor());
          copyStyle(propertyView, nestedComparablePropertyViewDescriptor);
          propertyView = nestedComparablePropertyViewDescriptor;
          propertyViews.set(i, propertyView);
        }
      }
      ((BasicComponentViewDescriptor) viewDescriptor).setPropertyViewDescriptors(propertyViews);
    }
    if (viewDescriptor instanceof ICompositeViewDescriptor) {
      List<IViewDescriptor> children = ((ICompositeViewDescriptor) viewDescriptor).getChildViewDescriptors();
      if (children != null) {
        for (IViewDescriptor childViewDesc : children) {
          adaptExistingViewDescriptor(childViewDesc);
        }
      }
    }
    return viewDescriptor;
  }

  /**
   * Adapt reference property view descriptor basic reference property view descriptor.
   *
   * @param propertyView
   *     the property view
   * @return the basic reference property view descriptor
   */
  protected BasicReferencePropertyViewDescriptor adaptReferencePropertyViewDescriptor(
      IPropertyViewDescriptor propertyView) {
    BasicReferencePropertyViewDescriptor refPropertyView = new BasicReferencePropertyViewDescriptor();
    copyStyle(propertyView, refPropertyView);
    refPropertyView.setAutoCompleteEnabled(false);
    return refPropertyView;
  }

  private void copyStyle(IPropertyViewDescriptor source, BasicPropertyViewDescriptor dest) {
    dest.setAction(source.getAction());
    dest.setBackground(source.getBackground());
    dest.setBorderType(source.getBorderType());
    dest.setDescription(source.getDescription());
    dest.setFont(source.getFont());
    dest.setForeground(source.getForeground());
    dest.setGrantedRoles(source.getGrantedRoles());
    dest.setHorizontalAlignment(source.getHorizontalAlignment());
    if (source instanceof DefaultDescriptor) {
      dest.setI18nNameKey(((DefaultDescriptor) source).getI18nNameKey());
    }
    dest.setIcon(source.getIcon());
    dest.setLabelBackground(source.getLabelBackground());
    dest.setLabelFont(source.getLabelFont());
    dest.setLabelForeground(source.getLabelForeground());
    dest.setModelDescriptor(source.getModelDescriptor());
    dest.setName(source.getName());
    dest.setPermId(source.getPermId());
    dest.setPreferredSize(source.getPreferredSize());
    dest.setReadabilityGates(source.getReadabilityGates());
    dest.setReadOnly(source.isReadOnly());
    dest.setWritabilityGates(source.getWritabilityGates());
    dest.setWidth(source.getWidth());
  }
}
