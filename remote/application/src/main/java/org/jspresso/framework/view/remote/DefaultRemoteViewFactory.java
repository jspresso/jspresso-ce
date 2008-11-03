/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.remote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IColorPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
import org.jspresso.framework.view.descriptor.ITabViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Factory for remote views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision: 1463 $
 * @author Vincent Vandenschrick
 */
public class DefaultRemoteViewFactory extends
    AbstractViewFactory<RComponent, RIcon, RAction> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTreeView(
      ITreeViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") IActionHandler actionHandler, Locale locale) {
    ICompositeValueConnector connector = createTreeViewConnector(
        viewDescriptor, locale);

    RComponent viewComponent = createRComponent();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    view.setConnector(connector);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createCardView(
      ICardViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = getConnectorFactory()
        .createCompositeValueConnector(
            getConnectorIdForComponentView(viewDescriptor), null);
    RComponent viewComponent = createRComponent();
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    for (ISubViewDescriptor propertyViewDescriptor : viewDescriptor
        .getPropertyViewDescriptors()) {
      String propertyName = propertyViewDescriptor.getName();
      IPropertyDescriptor propertyDescriptor = ((IComponentDescriptorProvider<?>) viewDescriptor
          .getModelDescriptor()).getComponentDescriptor()
          .getPropertyDescriptor(propertyName);
      if (propertyDescriptor == null) {
        throw new ViewException("Property descriptor [" + propertyName
            + "] does not exist for model descriptor "
            + viewDescriptor.getModelDescriptor().getName() + ".");
      }
      IView<RComponent> propertyView = createPropertyView(propertyDescriptor,
          viewDescriptor.getRenderedChildProperties(propertyName),
          actionHandler, locale);
      try {
        actionHandler.checkAccess(propertyViewDescriptor);
      } catch (SecurityException ex) {
        propertyView.setPeer(createSecurityPanel());
      }
      propertyView.setParent(view);
      connector.addChildConnector(propertyView.getConnector());
      if (propertyViewDescriptor.getReadabilityGates() != null) {
        for (IGate gate : propertyViewDescriptor.getReadabilityGates()) {
          propertyView.getConnector().addReadabilityGate(gate.clone());
        }
      }
      if (propertyViewDescriptor.getWritabilityGates() != null) {
        for (IGate gate : propertyViewDescriptor.getWritabilityGates()) {
          propertyView.getConnector().addWritabilityGate(gate.clone());
        }
      }
      propertyView.getConnector().setLocallyWritable(
          !propertyViewDescriptor.isReadOnly());
    }
    return view;
  }

  private RComponent createRComponent() {
    return new RComponent();
  }

  private IView<RComponent> createRComponentPropertyView(
      IPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    RComponent viewComponent = createRComponent();
    IValueConnector connector = getConnectorFactory().createValueConnector(
        propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    view.getPeer().setActions(
        createBinaryActions(view.getPeer(), view.getConnector(),
            propertyDescriptor, actionHandler, locale));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createListView(
      IListViewDescriptor viewDescriptor, @SuppressWarnings("unused") IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    RComponent viewComponent = createRComponent();
    IView<RComponent> view = constructView(viewComponent, viewDescriptor,
        connector);

    if (viewDescriptor.getRenderedProperty() != null) {
      IValueConnector cellConnector = createColumnConnector(viewDescriptor
          .getRenderedProperty(), modelDescriptor.getCollectionDescriptor()
          .getElementDescriptor());
      rowConnectorPrototype.addChildConnector(cellConnector);
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTableView(
      ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = getConnectorFactory()
        .createCompositeValueConnector(
            modelDescriptor.getName() + "Element",
            modelDescriptor.getCollectionDescriptor().getElementDescriptor()
                .getToStringProperty());
    ICollectionConnector connector = getConnectorFactory()
        .createCollectionConnector(modelDescriptor.getName(), getMvcBinder(),
            rowConnectorPrototype);
    RComponent viewComponent = createRComponent();
    viewComponent.setIcon(getIconFactory().getIcon(
        modelDescriptor.getCollectionDescriptor().getElementDescriptor()
            .getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor);
    view.setConnector(connector);

    Map<String, Class<?>> columnClassesByIds = new HashMap<String, Class<?>>();
    List<String> columnConnectorKeys = new ArrayList<String>();
    Set<String> forbiddenColumns = new HashSet<String>();
    for (ISubViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String columnId = columnViewDescriptor.getName();
      try {
        actionHandler.checkAccess(columnViewDescriptor);
        IValueConnector columnConnector = createColumnConnector(columnId,
            modelDescriptor.getCollectionDescriptor().getElementDescriptor());
        rowConnectorPrototype.addChildConnector(columnConnector);
        columnClassesByIds.put(columnId, modelDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getPropertyDescriptor(columnId).getModelType());
        columnConnectorKeys.add(columnId);
        if (columnViewDescriptor.getReadabilityGates() != null) {
          for (IGate gate : columnViewDescriptor.getReadabilityGates()) {
            columnConnector.addReadabilityGate(gate.clone());
          }
        }
        if (columnViewDescriptor.getWritabilityGates() != null) {
          for (IGate gate : columnViewDescriptor.getWritabilityGates()) {
            columnConnector.addWritabilityGate(gate.clone());
          }
        }
        columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
      } catch (SecurityException ex) {
        // The column simply won't be added.
        forbiddenColumns.add(columnId);
      }
    }
    List<IView<RComponent>> columns = new ArrayList<IView<RComponent>>();
    view.setChildren(columns);
    for (ISubViewDescriptor columnViewDescriptor : viewDescriptor
        .getColumnViewDescriptors()) {
      String propertyName = columnViewDescriptor.getName();
      if (!forbiddenColumns.contains(propertyName)) {
        IPropertyDescriptor propertyDescriptor = modelDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getPropertyDescriptor(propertyName);
        IView<RComponent> column = createPropertyView(propertyDescriptor, null,
            actionHandler, locale);
        column.setParent(view);
        columns.add(column);
      }
    }
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    RAction lovAction = createLovAction(view.getPeer(), view.getConnector(),
        propertyDescriptor, actionHandler, locale);
    lovAction.setName(getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale));
    lovAction.setDescription(getTranslationProvider().getTranslation(
        "lov.element.description",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            getTranslationProvider(), locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      lovAction.setIcon(getIconFactory().getIcon(
          propertyDescriptor.getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    view.getPeer().setActions(Collections.singletonList(lovAction));
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    Map<String, String> translations = new HashMap<String, String>();
    Map<String, RIcon> icons = new HashMap<String, RIcon>();
    for (String value : propertyDescriptor.getEnumerationValues()) {
      if (value != null && propertyDescriptor.isTranslated()) {
        translations.put(value, getTranslationProvider().getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
        icons.put(value, getIconFactory().getIcon(
            propertyDescriptor.getIconImageURL(value),
            IIconFactory.TINY_ICON_SIZE));
      }
    }
    view.getPeer().setRenderingTranslations(translations);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void showCardInPanel(RComponent cardsPeer, String cardName) {
    // TODO see how it should be implemented.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createBorderView(
      IBorderViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createGridView(
      IGridViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createSplitView(
      ISplitViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createTabView(
      ITabViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithActions(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale, IView<RComponent> view) {
    if (viewDescriptor.getActionMap() != null) {
      List<RAction> viewActions = new ArrayList<RAction>();
      for (Iterator<ActionList> iter = viewDescriptor.getActionMap()
          .getActionLists().iterator(); iter.hasNext();) {
        ActionList nextActionList = iter.next();
        for (IDisplayableAction action : nextActionList.getActions()) {
          RAction rAction = getActionFactory().createAction(action,
              actionHandler, view, locale);
          viewActions.add(rAction);
        }
      }
      view.getPeer().setActions(viewActions);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configureFontColorsAndDescription(
      @SuppressWarnings("unused") IViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") Locale locale,
      @SuppressWarnings("unused") IView<RComponent> view) {
    // This is not to be configured on server-side
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent createSecurityPanel() {
    // TODO construct a special component ?
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<RComponent> view, Locale locale) {
    //TODO set name ?
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDecimalPropertyView(
      IDecimalPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createIntegerPropertyView(
      IIntegerPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createBooleanPropertyView(
      IBooleanPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createColorPropertyView(
      IColorPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDatePropertyView(
      IDatePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createDurationPropertyView(
      IDurationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createStringPropertyView(
      IStringPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<RComponent> createTimePropertyView(
      ITimePropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      Locale locale) {
    return createRComponentPropertyView(propertyDescriptor, actionHandler, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithDescription(
      IPropertyDescriptor propertyDescriptor, Locale locale,
      IView<RComponent> view) {
    if (view != null && propertyDescriptor.getDescription() != null) {
      view.getPeer().setDescription(
          propertyDescriptor.getI18nDescription(getTranslationProvider(),
              locale)
              + TOOLTIP_ELLIPSIS);
    }
  }
}
