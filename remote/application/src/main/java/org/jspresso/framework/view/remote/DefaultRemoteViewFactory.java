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
package org.jspresso.framework.view.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionable;
import org.jspresso.framework.gui.remote.RBorderContainer;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RConstrainedGridContainer;
import org.jspresso.framework.gui.remote.RLabel;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.gui.remote.RTable;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.security.EAuthorization;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.ITableViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default factory for remote views.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public class DefaultRemoteViewFactory extends AbstractRemoteViewFactory {

  /**
   * Constructs a new {@code DefaultRemoteViewFactory} instance.
   */
  public DefaultRemoteViewFactory() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createConstrainedGridView(IConstrainedGridViewDescriptor viewDescriptor,
                                                                 IActionHandler actionHandler, Locale locale) {
    RConstrainedGridContainer viewComponent = createRConstrainedGridContainer(viewDescriptor);
    List<RComponent> cells = new ArrayList<>();
    List<CellConstraints> cellConstraints = new ArrayList<>();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();

    for (IViewDescriptor childViewDescriptor : viewDescriptor.getChildViewDescriptors()) {
      IView<RComponent> childView = createView(childViewDescriptor, actionHandler, locale);
      cellConstraints.add(viewDescriptor.getCellConstraints(childViewDescriptor));
      cells.add(childView.getPeer());
      childrenViews.add(childView);
    }
    viewComponent.setCells(cells.toArray(new RComponent[cells.size()]));
    viewComponent.setCellConstraints(cellConstraints.toArray(new CellConstraints[cellConstraints.size()]));
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * Creates a remote constrained grid container.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RConstrainedGridContainer createRConstrainedGridContainer(IConstrainedGridViewDescriptor viewDescriptor) {
    return new RConstrainedGridContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote split container.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RSplitContainer createRSplitContainer(ISplitViewDescriptor viewDescriptor) {
    return new RSplitContainer(getGuidGenerator().generateGUID());
  }

  /**
   * Creates a remote table.
   *
   * @param viewDescriptor
   *     the component view descriptor.
   * @return the created remote component.
   */
  protected RTable createRTable(ITableViewDescriptor viewDescriptor) {
    RTable component = new RTable(getGuidGenerator().generateGUID());
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICompositeView<RComponent> createSplitView(ISplitViewDescriptor viewDescriptor,
                                                       IActionHandler actionHandler, Locale locale) {
    RSplitContainer viewComponent = createRSplitContainer(viewDescriptor);
    viewComponent.setOrientation(viewDescriptor.getOrientation().name());
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent, viewDescriptor);
    List<IView<RComponent>> childrenViews = new ArrayList<>();

    if (viewDescriptor.getLeftTopViewDescriptor() != null) {
      IView<RComponent> leftTopView = createView(viewDescriptor.getLeftTopViewDescriptor(), actionHandler, locale);
      viewComponent.setLeftTop(leftTopView.getPeer());
      childrenViews.add(leftTopView);
    }
    if (viewDescriptor.getRightBottomViewDescriptor() != null) {
      IView<RComponent> rightBottomView = createView(viewDescriptor.getRightBottomViewDescriptor(), actionHandler,
          locale);
      viewComponent.setRightBottom(rightBottomView.getPeer());
      childrenViews.add(rightBottomView);
    }
    Integer userSplitSeparatorPosition = getUserSplitSeparatorPosition(viewDescriptor, actionHandler);
    if (userSplitSeparatorPosition != null && userSplitSeparatorPosition > 0) {
      viewComponent.setSeparatorPosition(userSplitSeparatorPosition);
    }
    view.setChildren(childrenViews);
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RComponent decorateWithPaginationView(RComponent viewPeer, RComponent paginationViewPeer) {
    RBorderContainer decorator = new RBorderContainer(getGuidGenerator().generateGUID());
    decorator.setCenter(viewPeer);
    decorator.setSouth(paginationViewPeer);
    return decorator;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  protected IView<RComponent> createTableView(ITableViewDescriptor viewDescriptor, IActionHandler actionHandler,
                                              Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    IComponentDescriptor<?> rowDescriptor = modelDescriptor.getCollectionDescriptor().getElementDescriptor();
    final ICompositeValueConnector rowConnectorPrototype = getConnectorFactory().createCompositeValueConnector(
        modelDescriptor.getName() + "Element", rowDescriptor.getToHtmlProperty());
    final ICollectionConnector connector = getConnectorFactory().createCollectionConnector(modelDescriptor.getName(),
        getMvcBinder(), rowConnectorPrototype);
    RTable viewComponent = createRTable(viewDescriptor);
    viewComponent.setColumnReorderingAllowed(viewDescriptor.isColumnReorderingAllowed());
    viewComponent.setSingleClickEdit(viewDescriptor.isSingleClickEdit());
    IView<RComponent> view = constructView(viewComponent, viewDescriptor, connector);

    viewComponent.setSortable(viewDescriptor.isSortable());
    if (viewDescriptor.getSortingAction() != null) {
      viewComponent.setSortingAction(
          getActionFactory().createAction(viewDescriptor.getSortingAction(), actionHandler, view, locale));
    }
    viewComponent.setHorizontallyScrollable(viewDescriptor.isHorizontallyScrollable());
    List<RComponent> columns = new ArrayList<>();
    List<RComponent> columnHeaders = new ArrayList<>();
    List<String> columnIds = new ArrayList<>();
    List<IView<RComponent>> propertyViews = new ArrayList<>();
    Map<IPropertyViewDescriptor, Object[]> userColumnViewDescriptors = getUserColumnViewDescriptors(viewDescriptor,
        actionHandler);
    for (Map.Entry<IPropertyViewDescriptor, Object[]> columnViewDescriptorEntry : userColumnViewDescriptors
        .entrySet()) {
      IPropertyViewDescriptor columnViewDescriptor = columnViewDescriptorEntry.getKey();
      if (isPropertyViewAccessGranted(columnViewDescriptor, rowDescriptor, actionHandler)) {
        IView<RComponent> column = createView(columnViewDescriptor, actionHandler, locale);
        column.setParent(view);
        // Do not use standard createColumnConnector method to preserve
        // formatted value connectors.
        // IValueConnector columnConnector = createColumnConnector(columnId,
        // rowDescriptor);
        IValueConnector columnConnector = column.getConnector();
        String propertyName = columnViewDescriptor.getModelDescriptor().getName();
        rowConnectorPrototype.addChildConnector(propertyName, columnConnector);
        boolean writable = !columnViewDescriptor.isReadOnly();
        if (writable) {
          try {
            actionHandler.pushToSecurityContext(EAuthorization.ENABLED);
            writable = actionHandler.isAccessGranted(columnViewDescriptor);
          } finally {
            actionHandler.restoreLastSecurityContextSnapshot();
          }
        }
        if (columnViewDescriptor.getAction() != null && !columnViewDescriptor.isReadOnly()) {
          for (IValueChangeListener listener : columnConnector.getValueChangeListeners()) {
            if (listener instanceof ConnectorActionAdapter) {
              // to avoid the action to be fired by the editor.
              columnConnector.removeValueChangeListener(listener);
            }
          }
          // We must listen for incoming connector value change to trigger the
          // action.
          columnConnector.addValueChangeListener(
              new ConnectorActionAdapter<>(columnViewDescriptor.getAction(), getActionFactory(), actionHandler, view));
        }
        if (!writable) {
          columnConnector.setLocallyWritable(false);
        } else if (columnViewDescriptor.isReadOnlyExplicitlyConfigured()) {
          columnConnector.setLocallyWritable(!columnViewDescriptor.isReadOnly());
        }
        IPropertyDescriptor propertyDescriptor = rowDescriptor.getPropertyDescriptor(propertyName);
        columns.add(column.getPeer());
        RLabel headerLabel = createPropertyLabel(columnViewDescriptor, column.getPeer(), actionHandler, locale);
        String viewDescription = computeViewDescription(columnViewDescriptor, actionHandler, locale);
        headerLabel.setToolTip(viewDescription);
        columnHeaders.add(headerLabel);
        if (!columnViewDescriptor.isReadOnly() && propertyDescriptor.isMandatory()
            && !(propertyDescriptor instanceof IBooleanPropertyDescriptor)) {
          if (columnViewDescriptor.getLabelForeground() == null) {
            headerLabel.setForeground(getTableHeaderMandatoryPropertyColorHex());
          }
          headerLabel.setLabel(decorateMandatoryPropertyLabel(headerLabel.getLabel()));
        }
        columnIds.add(computeColumnIdentifier(viewDescriptor, columnViewDescriptor));
        if (column.getPeer() instanceof RActionable && columnViewDescriptor.getAction() != null) {
          RAction action = getActionFactory().createAction(columnViewDescriptor.getAction(), actionHandler, view,
              locale);
          configurePropertyViewAction(columnViewDescriptor, action);
          ((RActionable) column.getPeer()).setAction(action);
        }
        final Object[] columnCaracteristics = columnViewDescriptorEntry.getValue();
        if (columnCaracteristics != null) {
          int visibilityHeight = ((boolean) columnCaracteristics[1]) ? 1 : -1;
          Dimension columnPreferredSize = new Dimension((Integer) columnCaracteristics[0], visibilityHeight);
          column.getPeer().setPreferredSize(columnPreferredSize);
        }
        propertyViews.add(column);
      }
    }
    completeChildrenViewsWithDynamicToolTips(rowConnectorPrototype, propertyViews, rowDescriptor);
    completeChildrenViewsWithDynamicBackgrounds(rowConnectorPrototype, propertyViews, rowDescriptor);
    completeChildrenViewsWithDynamicForegrounds(rowConnectorPrototype, propertyViews, rowDescriptor);
    completeChildrenViewsWithDynamicFonts(rowConnectorPrototype, propertyViews, rowDescriptor);
    viewComponent.setColumns(columns.toArray(new RComponent[columns.size()]));
    viewComponent.setColumnHeaders(columnHeaders.toArray(new RComponent[columnHeaders.size()]));
    viewComponent.setColumnIds(columnIds.toArray(new String[columnIds.size()]));
    viewComponent.setSelectionMode(viewDescriptor.getSelectionMode().name());
    if (viewDescriptor.getRowAction() != null) {
      viewComponent.setRowAction(
          getActionFactory().createAction(viewDescriptor.getRowAction(), actionHandler, view, locale));
    }

    completeViewWithDynamicBackground(viewComponent, viewDescriptor, rowDescriptor, rowConnectorPrototype);
    completeViewWithDynamicForeground(viewComponent, viewDescriptor, rowDescriptor, rowConnectorPrototype);
    completeViewWithDynamicFont(viewComponent, viewDescriptor, rowDescriptor, rowConnectorPrototype);

    if (rowConnectorPrototype instanceof IRemoteStateOwner) {
      viewComponent.setRowPrototype((RemoteCompositeValueState) ((IRemoteStateOwner) rowConnectorPrototype).getState());
    }

    // bind rowConnectorPrototype for column action gates.
    connector.addSelectionChangeListener(new ISelectionChangeListener() {
      @Override
      public void selectionChange(SelectionChangeEvent evt) {
        int leadingIndex = evt.getLeadingIndex();
        IValueConnector selectedModelConnector = null;
        if (leadingIndex >= 0) {
          IValueConnector selectedViewConnector = connector.getChildConnector(leadingIndex);
          if (selectedViewConnector != null) {
            selectedModelConnector = selectedViewConnector.getModelConnector();
          }
        }
        getMvcBinder().bind(rowConnectorPrototype, selectedModelConnector);
      }
    });
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void selectChildViewIndex(RComponent viewComponent, int index) {
    if (viewComponent instanceof RTabContainer) {
      RTabContainer rTab = ((RTabContainer) viewComponent);
      if (rTab.getSelectedIndex() != index) {
        rTab.setSelectedIndex(index);

        RemoteSelectionCommand selectionCommand = new RemoteSelectionCommand();
        selectionCommand.setTargetPeerGuid(rTab.getGuid());
        selectionCommand.setLeadingIndex(rTab.translateExcludingHiddenChildren(index));
        getRemoteCommandHandler().registerCommand(selectionCommand);
      }
    }
  }

}
