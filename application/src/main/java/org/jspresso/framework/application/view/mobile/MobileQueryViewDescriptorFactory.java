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
package org.jspresso.framework.application.view.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.application.view.BasicQueryViewDescriptorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicReferencePropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.mobile.MobileListViewDescriptor;

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
public class MobileQueryViewDescriptorFactory<E, F, G> extends BasicQueryViewDescriptorFactory<E, F, G> {

  private FrontendAction<E, F, G> selectEnumAction;

  /**
   * Instantiates a new Mobile query view descriptor factory.
   */
  public MobileQueryViewDescriptorFactory() {
    selectEnumAction = new FrontendAction<E, F, G>() {
      @Override
      public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
        EnumQueryStructure eqs = getParentModel(context);
        List<EnumValueQueryStructure> selectedEnumValues = getSelectedModels(context);
        for (EnumValueQueryStructure enumValue : eqs.getEnumerationValues()) {
          if (selectedEnumValues != null && selectedEnumValues.contains(enumValue)) {
            enumValue.setSelected(true);
          } else {
            enumValue.setSelected(false);
          }
        }
        return super.execute(actionHandler, context);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public IViewDescriptor createQueryViewDescriptor(IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
                                                   IComponentDescriptor<? extends IQueryComponent>
                                                       queryComponentDescriptor,
                                                   IAction propertyViewAction,
                                                   IDisplayableAction propertyViewCharAction,
                                                   Map<String, Object> actionContext) {
    MobileComponentViewDescriptor queryComponentViewDescriptor = new MobileComponentViewDescriptor();
    queryComponentViewDescriptor.setLabelsPosition(ELabelPosition.NONE);
    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<>();
    for (String queriableProperty : componentDescriptorProvider.getQueryableProperties()) {
      IPropertyDescriptor actualPropertyDescriptor = queryComponentDescriptor.getPropertyDescriptor(queriableProperty);
      if (actualPropertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyView.setLovAction(
            createComparableEditAction((ComparableQueryStructureDescriptor) actualPropertyDescriptor, null));
        propertyView.setAutoCompleteEnabled(false);
        propertyViewDescriptors.add(propertyView);
      } else if (actualPropertyDescriptor instanceof EnumQueryStructureDescriptor) {
        BasicPropertyViewDescriptor propertyView = createEnumReferencePropertyViewDescriptor(queriableProperty,
            (EnumQueryStructureDescriptor) actualPropertyDescriptor, propertyViewAction);
        propertyViewDescriptors.add(propertyView);
      } else {
        BasicPropertyViewDescriptor propertyView = createDefaultPropertyViewDescriptor(queriableProperty,
            actualPropertyDescriptor);
        propertyViewDescriptors.add(propertyView);
      }
    }
    queryComponentViewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);

    queryComponentViewDescriptor.setModelDescriptor(queryComponentDescriptor);

    return queryComponentViewDescriptor;
  }

  /**
   * Create comparable edit action.
   *
   * @param comparablePropertyDescriptor
   *     the comparable property descriptor
   * @param okNextAction
   *     the ok next action
   * @return the edit action
   */
  @Override
  protected IDisplayableAction createComparableEditAction(
      ComparableQueryStructureDescriptor comparablePropertyDescriptor, IAction okNextAction) {
    EditComponentAction<E, F, G> comparableEditAction = new EditComponentAction<>();
    if (comparablePropertyDescriptor.getIcon() != null) {
      comparableEditAction.setIcon(comparablePropertyDescriptor.getIcon());
    } else {
      comparableEditAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    MobileComponentViewDescriptor viewDescriptor = new MobileComponentViewDescriptor();

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

    viewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);

    viewDescriptor.setName(comparablePropertyDescriptor.getName());
    viewDescriptor.setModelDescriptor(comparablePropertyDescriptor.getReferencedDescriptor());

    comparableEditAction.setViewDescriptor(viewDescriptor);
    comparableEditAction.setCancelAction(null);
    comparableEditAction.setOkAction(completeOkDialogAction(okNextAction));

    comparableEditAction.setNextAction(getModalDialogAction());

    return comparableEditAction;
  }


  /**
   * Creates the enumSelectAction.
   *
   * @param enumPropertyDescriptor
   *     the enumeration property descriptor to create the LOV action for.
   * @param okNextAction
   *     the ok next action
   * @return the enumSelectAction.
   */
  @Override
  protected IDisplayableAction createEnumSelectAction(final EnumQueryStructureDescriptor enumPropertyDescriptor,
                                                      IAction okNextAction) {
    EditComponentAction<E, F, G> enumSelectAction = new EditComponentAction<>();
    if (enumPropertyDescriptor.getIcon() != null) {
      enumSelectAction.setIcon(enumPropertyDescriptor.getIcon());
    } else {
      enumSelectAction.setIconImageURL(getDefaultFindIconImageUrl());
    }

    MobileBorderViewDescriptor viewDescriptor = new MobileBorderViewDescriptor();
    viewDescriptor.setName(EnumQueryStructureDescriptor.ENUMERATION_VALUES);
    viewDescriptor.setModelDescriptor(enumPropertyDescriptor.getReferencedDescriptor());
    MobileListViewDescriptor list = new MobileListViewDescriptor();
    list.setSelectionMode(ESelectionMode.MULTIPLE_INTERVAL_CUMULATIVE_SELECTION);
    list.setShowArrow(false);
    list.setName(EnumQueryStructureDescriptor.ENUMERATION_VALUES);
    list.setRenderedProperty(EnumQueryStructureDescriptor.I18N_VALUE);
    list.setItemSelectionAction(selectEnumAction);
    list.setSelectionModelDescriptor(enumPropertyDescriptor.getReferencedDescriptor().getPropertyDescriptor(
        EnumQueryStructureDescriptor.SELECTED_ENUMERATION_VALUES));
    viewDescriptor.setCenterViewDescriptor(list);
    enumSelectAction.setViewDescriptor(viewDescriptor);
    enumSelectAction.setCancelAction(null);
    enumSelectAction.setOkAction(completeOkDialogAction(okNextAction));

    enumSelectAction.setNextAction(getModalDialogAction());

    return enumSelectAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <V extends IViewDescriptor> V adaptExistingViewDescriptor(V viewDescriptor) {
    if (viewDescriptor instanceof MobileComponentViewDescriptor) {
      List<IPropertyViewDescriptor> propertyViews = ((MobileComponentViewDescriptor) viewDescriptor)
          .getPropertyViewDescriptors(false);
      for (int i = 0; i < propertyViews.size(); i++) {
        IPropertyViewDescriptor propertyView = propertyViews.get(i);
        if ((propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) || propertyView
            .getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
          if (!(propertyView instanceof BasicReferencePropertyViewDescriptor)) {
            BasicReferencePropertyViewDescriptor refPropertyView = adaptReferencePropertyViewDescriptor(propertyView);
            propertyView = refPropertyView;
            propertyViews.set(i, propertyView);
          }
          if (((BasicReferencePropertyViewDescriptor) propertyView).getLovAction() == null) {
            if (propertyView.getModelDescriptor() instanceof ComparableQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createComparableEditAction((ComparableQueryStructureDescriptor) propertyView.getModelDescriptor(),
                      null));
            } else if (propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
              ((BasicReferencePropertyViewDescriptor) propertyView).setLovAction(
                  createEnumSelectAction((EnumQueryStructureDescriptor) propertyView.getModelDescriptor(), null));
            }
          }
        }
      }
      ((MobileComponentViewDescriptor) viewDescriptor).setPropertyViewDescriptors(propertyViews);
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
}
