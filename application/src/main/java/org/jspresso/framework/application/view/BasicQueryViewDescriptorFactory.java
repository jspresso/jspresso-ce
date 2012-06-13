/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
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
 * @version $LastChangedRevision: 1091 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class BasicQueryViewDescriptorFactory<E, F, G> implements IQueryViewDescriptorFactory {

  private boolean            horizontallyResizable;

  private IDisplayableAction okCloseDialogAction;
  private IDisplayableAction modalDialogAction;
  private String             defaultFindIconImageUrl;

  /**
   * Constructs a new <code>BasicQueryViewDescriptorFactory</code> instance.
   */
  public BasicQueryViewDescriptorFactory() {
    horizontallyResizable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      IComponentDescriptor<? extends IQueryComponent> queryComponentDescriptor) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    if (componentDescriptorProvider instanceof IReferencePropertyDescriptor) {
      Map<String, Object> initializationMapping = ((IReferencePropertyDescriptor<?>) componentDescriptorProvider)
          .getInitializationMapping();
      if (initializationMapping != null && initializationMapping.size() > 0) {
        // we must refine the rendered properties of the filter view to get rid
        // of pre-initialized properties.
        List<String> filterableProperties = new ArrayList<String>();
        for (String renderedProperty : queryComponentDescriptor.getRenderedProperties()) {
          if (!initializationMapping.containsKey(renderedProperty)) {
            filterableProperties.add(renderedProperty);
          }
        }
        queryComponentViewDescriptor.setRenderedProperties(filterableProperties);
      }
    }
    List<IPropertyViewDescriptor> propertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
    for (String queriableProperty : componentDescriptorProvider.getQueryableProperties()) {
      IPropertyDescriptor actualPropertyDescriptor = queryComponentDescriptor.getPropertyDescriptor(queriableProperty);
      // To preserve col spans for query structures.
      if (actualPropertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        BasicPropertyViewDescriptor propertyView;

        propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty + "." + ComparableQueryStructureDescriptor.COMPARATOR);
        propertyView.setWidth(new Integer(1));
        propertyViewDescriptors.add(propertyView);

        propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty + "." + ComparableQueryStructureDescriptor.INF_VALUE);
        propertyView.setWidth(new Integer(1));
        propertyViewDescriptors.add(propertyView);

        propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty + "." + ComparableQueryStructureDescriptor.SUP_VALUE);
        propertyView.setWidth(new Integer(2));
        propertyViewDescriptors.add(propertyView);
      } else if (actualPropertyDescriptor instanceof EnumQueryStructureDescriptor) {
        BasicReferencePropertyViewDescriptor propertyView = new BasicReferencePropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyView.setWidth(new Integer(4));
        propertyView.setLovAction(createEnumSelectAction((EnumQueryStructureDescriptor) actualPropertyDescriptor));
        propertyViewDescriptors.add(propertyView);
      } else {
        BasicPropertyViewDescriptor propertyView = new BasicPropertyViewDescriptor();
        propertyView.setName(queriableProperty);
        propertyView.setWidth(new Integer(4));
        propertyViewDescriptors.add(propertyView);
      }
    }
    queryComponentViewDescriptor.setPropertyViewDescriptors(propertyViewDescriptors);
    queryComponentViewDescriptor.setColumnCount(8);

    IViewDescriptor queryViewDescriptor;
    if (horizontallyResizable) {
      queryComponentViewDescriptor.setModelDescriptor(queryComponentDescriptor);
      queryViewDescriptor = queryComponentViewDescriptor;
    } else {
      BasicBorderViewDescriptor borderViewDescriptor = new BasicBorderViewDescriptor();
      borderViewDescriptor.setWestViewDescriptor(queryComponentViewDescriptor);
      borderViewDescriptor.setModelDescriptor(queryComponentDescriptor);
      queryViewDescriptor = borderViewDescriptor;
    }

    return queryViewDescriptor;
  }

  /**
   * Sets the horizontallyResizable.
   * 
   * @param horizontallyResizable
   *          the horizontallyResizable to set.
   */
  public void setHorizontallyResizable(boolean horizontallyResizable) {
    this.horizontallyResizable = horizontallyResizable;
  }

  /**
   * Creates the enumSelectAction.
   * 
   * @param enumPropertyDescriptor
   *          the enumeration property descriptor to create the LOV action for.
   * @return the enumSelectAction.
   */
  protected IDisplayableAction createEnumSelectAction(final EnumQueryStructureDescriptor enumPropertyDescriptor) {
    EditComponentAction<E, F, G> enumSelectAction = new EditComponentAction<E, F, G>();
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
   * {@inheritDoc}
   */
  @Override
  public void adaptExistingViewDescriptor(IViewDescriptor viewDescriptor) {
    if (viewDescriptor instanceof BasicComponentViewDescriptor) {
      List<IPropertyViewDescriptor> propertyViews = ((BasicComponentViewDescriptor) viewDescriptor)
          .getPropertyViewDescriptors();
      for (int i = 0; i < propertyViews.size(); i++) {
        IPropertyViewDescriptor propertyView = propertyViews.get(i);
        if (propertyView.getModelDescriptor() instanceof EnumQueryStructureDescriptor) {
          if (!(propertyView instanceof BasicReferencePropertyViewDescriptor)) {
            BasicReferencePropertyViewDescriptor refPropertyView = new BasicReferencePropertyViewDescriptor();
            refPropertyView.setAction(propertyView.getAction());
            refPropertyView.setBackground(propertyView.getBackground());
            refPropertyView.setBorderType(propertyView.getBorderType());
            refPropertyView.setDescription(propertyView.getDescription());
            refPropertyView.setFont(propertyView.getFont());
            refPropertyView.setForeground(propertyView.getForeground());
            refPropertyView.setGrantedRoles(propertyView.getGrantedRoles());
            refPropertyView.setHorizontalAlignment(propertyView.getHorizontalAlignment());
            if (propertyView instanceof DefaultDescriptor) {
              refPropertyView.setI18nNameKey(((DefaultDescriptor) propertyView).getI18nNameKey());
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
            propertyView = refPropertyView;
            propertyViews.set(i, propertyView);
          }
          if (((BasicReferencePropertyViewDescriptor) propertyView).getLovAction() == null) {
            ((BasicReferencePropertyViewDescriptor) propertyView)
                .setLovAction(createEnumSelectAction((EnumQueryStructureDescriptor) propertyView.getModelDescriptor()));
          }
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
  }
}
