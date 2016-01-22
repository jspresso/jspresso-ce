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
package org.jspresso.framework.application.view;

import java.util.ArrayList;
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
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IQueryViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * A default implementation for query view descriptor factory.
 *
 * @version $LastChangedRevision : 1091 $
 * @author Vincent Vandenschrick
 * @param <E>           the actual gui component type used.
 * @param <F>           the actual icon type used.
 * @param <G>           the actual action type used.
 */
public class BasicQueryViewDescriptorFactory<E, F, G> implements
    IQueryViewDescriptorFactory {

  private boolean            useCompactComparableQueryStructures;

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
  public IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      IComponentDescriptor<? extends IQueryComponent> queryComponentDescriptor, Map<String, Object> actionContext) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();
    for (String queriableProperty : componentDescriptorProvider
        .getQueryableProperties()) {
      IPropertyDescriptor actualPropertyDescriptor = queryComponentDescriptor
          .getPropertyDescriptor(queriableProperty);
      // To preserve col spans for query structures.
      if (actualPropertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        if (isUseCompactComparableQueryStructures()) {
          BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
          propertyView.setName(queriableProperty);
          propertyView
              .setLovAction(createComparableEditAction((ComparableQueryStructureDescriptor) actualPropertyDescriptor));
          propertyView.setAutoCompleteEnabled(false);
          propertyViewDescriptors.add(propertyView);
        } else {
          BasicPropertyViewDescriptor propertyView;

          propertyView = new BasicPropertyViewDescriptor();
          propertyView.setName(queriableProperty + "."
              + ComparableQueryStructureDescriptor.COMPARATOR);
          propertyViewDescriptors.add(propertyView);

          propertyView = new BasicPropertyViewDescriptor();
          propertyView.setName(queriableProperty + "."
              + ComparableQueryStructureDescriptor.INF_VALUE);
          propertyViewDescriptors.add(propertyView);

          propertyView = new BasicPropertyViewDescriptor();
          propertyView.setName(queriableProperty + "."
              + ComparableQueryStructureDescriptor.SUP_VALUE);
          propertyViewDescriptors.add(propertyView);
        }
      } else if (actualPropertyDescriptor instanceof EnumQueryStructureDescriptor) {
        BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyView
            .setLovAction(createEnumSelectAction((EnumQueryStructureDescriptor) actualPropertyDescriptor));
        propertyView.setAutoCompleteEnabled(false);
        propertyViewDescriptors.add(propertyView);
      } else {
        BasicPropertyViewDescriptor propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyViewDescriptors.add(propertyView);
      }
    }
    // We have to delay width computation in order to solve bug #663.
    for (IPropertyViewDescriptor propertyView : propertyViewDescriptors) {
      int width;
      if (propertyView.getName().endsWith(
          ComparableQueryStructureDescriptor.COMPARATOR)
          || propertyView.getName().endsWith(
              ComparableQueryStructureDescriptor.INF_VALUE)) {
        width = 1;
      } else if (propertyView.getName().endsWith(
          ComparableQueryStructureDescriptor.SUP_VALUE)) {
        width = 2;
      } else {
        width = 4;
      }
      ((BasicPropertyViewDescriptor) propertyView).setWidth(width);
    }
    queryComponentViewDescriptor
        .setPropertyViewDescriptors(propertyViewDescriptors);
    queryComponentViewDescriptor.setColumnCount(8);

    queryComponentViewDescriptor.setModelDescriptor(queryComponentDescriptor);
    queryComponentViewDescriptor.setWidthResizeable(false);
    return queryComponentViewDescriptor;
  }

  /**
   * Create comparable edit action.
   *
   * @param comparablePropertyDescriptor the comparable property descriptor
   * @return the edit action
   */
  protected IDisplayableAction createComparableEditAction(ComparableQueryStructureDescriptor
                                                              comparablePropertyDescriptor) {
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
   *          the enumeration property descriptor to create the LOV action for.
   * @return the enumSelectAction.
   */
  protected IDisplayableAction createEnumSelectAction(
      final EnumQueryStructureDescriptor enumPropertyDescriptor) {
    EditComponentAction<E, F, G> enumSelectAction = new EditComponentAction<>();
    if (enumPropertyDescriptor.getIcon() != null) {
      enumSelectAction.setIcon(enumPropertyDescriptor.getIcon());
    } else {
      enumSelectAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    BasicBorderViewDescriptor viewDescriptor = new BasicBorderViewDescriptor();
    viewDescriptor.setName(EnumQueryStructureDescriptor.ENUMERATION_VALUES);
    viewDescriptor.setModelDescriptor(enumPropertyDescriptor
        .getReferencedDescriptor());
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
   *          the okCloseDialogAction to set.
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
   *          the modalDialogAction to set.
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
   *          the defaultFindIconImageUrl to set.
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
   * @param useCompactComparableQueryStructures the use compact comparable query structures
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
            && isUseCompactComparableQueryStructures())
            || propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
          if (!(propertyView instanceof BasicReferencePropertyViewDescriptor)) {
            BasicReferencePropertyViewDescriptor refPropertyView = new BasicReferencePropertyViewDescriptor();
            refPropertyView.setAction(propertyView.getAction());
            refPropertyView.setBackground(propertyView.getBackground());
            refPropertyView.setBorderType(propertyView.getBorderType());
            refPropertyView.setDescription(propertyView.getDescription());
            refPropertyView.setFont(propertyView.getFont());
            refPropertyView.setForeground(propertyView.getForeground());
            refPropertyView.setGrantedRoles(propertyView.getGrantedRoles());
            refPropertyView.setHorizontalAlignment(propertyView
                .getHorizontalAlignment());
            if (propertyView instanceof DefaultDescriptor) {
              refPropertyView.setI18nNameKey(((DefaultDescriptor) propertyView)
                  .getI18nNameKey());
            }
            refPropertyView.setIcon(propertyView.getIcon());
            refPropertyView.setLabelBackground(propertyView.getLabelBackground());
            refPropertyView.setLabelFont(propertyView.getLabelFont());
            refPropertyView.setLabelForeground(propertyView.getLabelForeground());
            refPropertyView.setModelDescriptor(propertyView.getModelDescriptor());
            refPropertyView.setName(propertyView.getName());
            refPropertyView.setPermId(propertyView.getPermId());
            refPropertyView.setPreferredSize(propertyView.getPreferredSize());
            refPropertyView.setReadabilityGates(propertyView.getReadabilityGates());
            refPropertyView.setReadOnly(propertyView.isReadOnly());
            refPropertyView.setWritabilityGates(propertyView.getWritabilityGates());
            refPropertyView.setWidth(propertyView.getWidth());
            refPropertyView.setAutoCompleteEnabled(false);
            propertyView = refPropertyView;
            propertyViews.set(i, propertyView);
          }
          if (((BasicReferencePropertyViewDescriptor) propertyView)
              .getLovAction() == null) {
            if (propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createComparableEditAction((ComparableQueryStructureDescriptor) propertyView.getModelDescriptor()));
            } else if (propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView)
                  .setLovAction(createEnumSelectAction((EnumQueryStructureDescriptor) propertyView
                      .getModelDescriptor()));
            }
          }
        }
      }
      ((BasicComponentViewDescriptor) viewDescriptor).setPropertyViewDescriptors(propertyViews);
    }
    if (viewDescriptor instanceof ICompositeViewDescriptor) {
      List<IViewDescriptor> children = ((ICompositeViewDescriptor) viewDescriptor)
          .getChildViewDescriptors();
      if (children != null) {
        for (IViewDescriptor childViewDesc : children) {
          adaptExistingViewDescriptor(childViewDesc);
        }
      }
    }
    return viewDescriptor;
  }
}
