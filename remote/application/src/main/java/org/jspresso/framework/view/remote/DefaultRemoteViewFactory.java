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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IConnectorSelector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.binding.swing.ConnectorHierarchyTreeModel;
import org.jspresso.framework.binding.swing.ConnectorTreeHelper;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.BasicCompositeView;
import org.jspresso.framework.view.BasicView;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.TreeDescriptorHelper;
import org.jspresso.framework.view.descriptor.basic.BasicListViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSubviewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

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
public class DefaultRemoteViewFactory implements
    IViewFactory<RComponent, RIcon, RAction> {

  private IConfigurableConnectorFactory       connectorFactory;
  private ITranslationProvider                translationProvider;
  private IActionFactory<RAction, RComponent> actionFactory;
  private IIconFactory<RIcon>                 iconFactory;
  private IDisplayableAction                  binaryPropertyInfoAction;
  private IDisplayableAction                  lovAction;
  private IDisplayableAction                  openFileAsBinaryPropertyAction;
  private IDisplayableAction                  resetPropertyAction;
  private IDisplayableAction                  saveBinaryPropertyAsFileAction;
  private IMvcBinder                          mvcBinder;

  /**
   * {@inheritDoc}
   */
  public IView<RComponent> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = null;
    if (viewDescriptor instanceof IComponentViewDescriptor) {
      view = createComponentView((IComponentViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof INestingViewDescriptor) {
      view = createNestingView((INestingViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IImageViewDescriptor) {
      view = createImageView((IImageViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof IPropertyViewDescriptor) {
      view = createPropertyView((IPropertyViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICollectionViewDescriptor) {
      view = createCollectionView((ICollectionViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICompositeViewDescriptor) {
      view = createCompositeView((ICompositeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ICardViewDescriptor) {
      view = createCardView((ICardViewDescriptor) viewDescriptor,
          actionHandler, locale);
    } else if (viewDescriptor instanceof ITreeViewDescriptor) {
      view = createTreeView((ITreeViewDescriptor) viewDescriptor,
          actionHandler, locale);
    }
    return view;
  }

  private IView<RComponent> createTreeView(ITreeViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    ITreeLevelDescriptor rootDescriptor = viewDescriptor
        .getRootSubtreeDescriptor();
    RComponent viewComponent = createRComponent();
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor, null);
//    viewComponent.setCellRenderer(new ConnectorTreeCellRenderer(viewDescriptor,
//        locale));
    ICompositeValueConnector connector = null;
    if (rootDescriptor instanceof ICompositeTreeLevelDescriptor) {
      IConfigurableCollectionConnectorListProvider compositeConnector = connectorFactory
          .createConfigurableCollectionConnectorListProvider(
              ModelRefPropertyConnector.THIS_PROPERTY,
              ((ICompositeTreeLevelDescriptor) rootDescriptor)
                  .getNodeGroupDescriptor().getRenderedProperty());
      List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<ICollectionConnectorProvider>();
      if (((ICompositeTreeLevelDescriptor) rootDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor subtreeViewDescriptor : ((ICompositeTreeLevelDescriptor) rootDescriptor)
            .getChildrenDescriptors()) {
          ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
              viewDescriptor, subtreeViewDescriptor, 1);
          compositeConnector.addChildConnector(subtreeConnector);
          subtreeConnectors.add(subtreeConnector);
        }
      }
      compositeConnector.setCollectionConnectorProviders(subtreeConnectors);
      connector = compositeConnector;
    } else if (rootDescriptor instanceof ISimpleTreeLevelDescriptor) {
      IConfigurableCollectionConnectorProvider simpleConnector = connectorFactory
          .createConfigurableCollectionConnectorProvider(
              ModelRefPropertyConnector.THIS_PROPERTY,
              ((ISimpleTreeLevelDescriptor) rootDescriptor)
                  .getNodeGroupDescriptor().getRenderedProperty());
      if (((ISimpleTreeLevelDescriptor) rootDescriptor).getChildDescriptor() != null) {
        ICollectionConnectorProvider subtreeConnector = createNodeGroupConnector(
            viewDescriptor, ((ISimpleTreeLevelDescriptor) rootDescriptor)
                .getChildDescriptor(), 1);
        simpleConnector.addChildConnector(subtreeConnector);
        simpleConnector.setCollectionConnectorProvider(subtreeConnector);
      }
      connector = simpleConnector;
    }
    if (connector instanceof IConnectorSelector) {
      ((IConnectorSelector) connector).setTracksChildrenSelection(true);
    }
    view.setConnector(connector);
    return view;
  }

  private ICollectionConnectorProvider createNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ITreeLevelDescriptor subtreeViewDescriptor, int depth) {
    if (subtreeViewDescriptor instanceof ICompositeTreeLevelDescriptor) {
      return createCompositeNodeGroupConnector(viewDescriptor,
          (ICompositeTreeLevelDescriptor) subtreeViewDescriptor, depth);
    } else if (subtreeViewDescriptor instanceof ISimpleTreeLevelDescriptor) {
      return createSimpleNodeGroupConnector(viewDescriptor,
          (ISimpleTreeLevelDescriptor) subtreeViewDescriptor, depth);
    }
    return null;
  }

  private ICollectionConnectorProvider createCompositeNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ICompositeTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionDescriptorProvider<?> nodeGroupModelDescriptor = ((ICollectionDescriptorProvider<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor());
    IConfigurableCollectionConnectorListProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorListProvider(
            nodeGroupModelDescriptor.getName() + "Element",
            subtreeViewDescriptor.getNodeGroupDescriptor()
                .getRenderedProperty());
    List<ICollectionConnectorProvider> subtreeConnectors = new ArrayList<ICollectionConnectorProvider>();
    if (subtreeViewDescriptor.getChildrenDescriptors() != null
        && depth < viewDescriptor.getMaxDepth()) {
      for (ITreeLevelDescriptor childDescriptor : subtreeViewDescriptor
          .getChildrenDescriptors()) {
        ICollectionConnectorProvider childConnector = createNodeGroupConnector(
            viewDescriptor, childDescriptor, depth + 1);
        nodeGroupPrototypeConnector.addChildConnector(childConnector);
        subtreeConnectors.add(childConnector);
      }
    }
    nodeGroupPrototypeConnector
        .setCollectionConnectorProviders(subtreeConnectors);
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  private ICollectionConnectorProvider createSimpleNodeGroupConnector(
      ITreeViewDescriptor viewDescriptor,
      ISimpleTreeLevelDescriptor subtreeViewDescriptor, int depth) {
    ICollectionPropertyDescriptor<?> nodeGroupModelDescriptor = (ICollectionPropertyDescriptor<?>) subtreeViewDescriptor
        .getNodeGroupDescriptor().getModelDescriptor();
    IConfigurableCollectionConnectorProvider nodeGroupPrototypeConnector = connectorFactory
        .createConfigurableCollectionConnectorProvider(nodeGroupModelDescriptor
            .getName()
            + "Element", subtreeViewDescriptor.getNodeGroupDescriptor()
            .getRenderedProperty());
    if (subtreeViewDescriptor.getChildDescriptor() != null
        && depth < viewDescriptor.getMaxDepth()) {
      ICollectionConnectorProvider childConnector = createNodeGroupConnector(
          viewDescriptor, subtreeViewDescriptor.getChildDescriptor(), depth + 1);
      nodeGroupPrototypeConnector.addChildConnector(childConnector);
      nodeGroupPrototypeConnector
          .setCollectionConnectorProvider(childConnector);
    }
    ICollectionConnector nodeGroupCollectionConnector = connectorFactory
        .createCollectionConnector(nodeGroupModelDescriptor.getName(),
            mvcBinder, nodeGroupPrototypeConnector);
    return nodeGroupCollectionConnector;
  }

  private IView<RComponent> createCardView(ICardViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createCompositeView(
      ICompositeViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createCollectionView(
      ICollectionViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createPropertyView(
      IPropertyViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    IView<RComponent> view = createPropertyView(
        (IPropertyDescriptor) viewDescriptor.getModelDescriptor(),
        viewDescriptor.getRenderedChildProperties(), actionHandler, locale);
    return constructView(view.getPeer(), viewDescriptor, view.getConnector());
  }

  private IView<RComponent> createImageView(
      IImageViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createNestingView(
      INestingViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createComponentView(
      IComponentViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICompositeValueConnector connector = connectorFactory
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
        propertyView.setPeer(createSecurityComponent());
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

  private RComponent createSecurityComponent() {
    return new RComponent();
  }

  private String getConnectorIdForComponentView(
      IComponentViewDescriptor viewDescriptor) {
    if (viewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
      return ModelRefPropertyConnector.THIS_PROPERTY;
    }
    return viewDescriptor.getModelDescriptor().getName();
  }

  private RComponent createRComponent() {
    return new RComponent();
  }

  private IView<RComponent> constructView(RComponent viewComponent,
      IViewDescriptor descriptor, IValueConnector connector) {
    BasicView<RComponent> view = new BasicView<RComponent>(viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private BasicCompositeView<RComponent> constructCompositeView(
      RComponent viewComponent, IViewDescriptor descriptor,
      IValueConnector connector) {
    BasicCompositeView<RComponent> view = new BasicCompositeView<RComponent>(
        viewComponent);
    view.setConnector(connector);
    view.setDescriptor(descriptor);
    return view;
  }

  private IView<RComponent> createPropertyView(
      IPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<RComponent> view = null;
    if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
      view = createEnumerationPropertyView(
          (IEnumerationPropertyDescriptor) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      view = createRelationshipEndPropertyView(
          (IRelationshipEndPropertyDescriptor) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    } else if (propertyDescriptor instanceof IBinaryPropertyDescriptor) {
      view = createBinaryPropertyView(
          (IBinaryPropertyDescriptor) propertyDescriptor, actionHandler, locale);
    } else {
      view = createRComponentPropertyView(propertyDescriptor, actionHandler,
          locale);
    }
    if (view != null) {
      if (propertyDescriptor.getName() != null) {
        view.getPeer().setName(
            propertyDescriptor.getI18nName(getTranslationProvider(), locale));
      }
      if (propertyDescriptor.getDescription() != null) {
        view.getPeer().setDescription(
            propertyDescriptor.getI18nDescription(getTranslationProvider(),
                locale)
                + TOOLTIP_ELLIPSIS);
      }
    }
    return view;
  }

  private IView<RComponent> createRComponentPropertyView(
      IPropertyDescriptor propertyDescriptor, IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    RComponent viewComponent = createRComponent();
    IValueConnector connector = connectorFactory
        .createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    return constructView(viewComponent, null, connector);
  }

  private IView<RComponent> createBinaryPropertyView(
      IBinaryPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    RAction openAction = actionFactory.createAction(
        openFileAsBinaryPropertyAction, actionHandler, view.getPeer(),
        propertyDescriptor, view.getConnector(), locale);
    RAction saveAction = actionFactory.createAction(
        saveBinaryPropertyAsFileAction, actionHandler, view.getPeer(),
        propertyDescriptor, view.getConnector(), locale);
    RAction resetAction = actionFactory.createAction(resetPropertyAction,
        actionHandler, view.getPeer(), propertyDescriptor, view.getConnector(),
        locale);
    RAction infoAction = actionFactory.createAction(binaryPropertyInfoAction,
        actionHandler, view.getPeer(), propertyDescriptor, view.getConnector(),
        locale);
    view.getPeer().setActions(
        Arrays.asList(new RAction[] {openAction, saveAction, resetAction,
            infoAction}));
    return view;
  }

  private IView<RComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<RComponent> view = null;
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      view = createReferencePropertyView(
          (IReferencePropertyDescriptor<?>) propertyDescriptor, actionHandler,
          locale);
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      view = createCollectionPropertyView(
          (ICollectionPropertyDescriptor<?>) propertyDescriptor,
          renderedChildProperties, actionHandler, locale);
    }
    return view;
  }

  private IView<RComponent> createCollectionPropertyView(
      ICollectionPropertyDescriptor<?> propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    IView<RComponent> view;
    if (renderedChildProperties != null && renderedChildProperties.size() > 1) {
      BasicTableViewDescriptor viewDescriptor = new BasicTableViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      List<ISubViewDescriptor> columnViewDescriptors = new ArrayList<ISubViewDescriptor>();
      for (String renderedProperty : renderedChildProperties) {
        BasicSubviewDescriptor columnDescriptor = new BasicSubviewDescriptor();
        columnDescriptor.setName(renderedProperty);
        columnViewDescriptors.add(columnDescriptor);
      }
      viewDescriptor.setColumnViewDescriptors(columnViewDescriptors);
      viewDescriptor.setName(propertyDescriptor.getName());
      view = createTableView(viewDescriptor, actionHandler, locale);
    } else {
      BasicListViewDescriptor viewDescriptor = new BasicListViewDescriptor();
      viewDescriptor.setModelDescriptor(propertyDescriptor);
      if (renderedChildProperties != null
          && renderedChildProperties.size() == 1) {
        viewDescriptor.setRenderedProperty(renderedChildProperties.get(0));
      }
      viewDescriptor.setName(propertyDescriptor.getName());
      view = createListView(viewDescriptor, actionHandler, locale);
    }
    return view;
  }

  private IView<RComponent> createListView(
      BasicListViewDescriptor viewDescriptor,
      @SuppressWarnings("unused") IActionHandler actionHandler,
      @SuppressWarnings("unused") Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            viewDescriptor.getRenderedProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
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

  private IValueConnector createColumnConnector(String columnId,
      IComponentDescriptor<?> descriptor) {
    IPropertyDescriptor propertyDescriptor = descriptor
        .getPropertyDescriptor(columnId);
    if (propertyDescriptor == null) {
      throw new ViewException("No property " + columnId + " defined for "
          + descriptor.getComponentContract());
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return connectorFactory.createCompositeValueConnector(columnId,
          ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getToStringProperty());
    }
    return connectorFactory.createValueConnector(propertyDescriptor.getName());
  }

  private IView<RComponent> createTableView(
      BasicTableViewDescriptor viewDescriptor, IActionHandler actionHandler,
      Locale locale) {
    ICollectionDescriptorProvider<?> modelDescriptor = ((ICollectionDescriptorProvider<?>) viewDescriptor
        .getModelDescriptor());
    ICompositeValueConnector rowConnectorPrototype = connectorFactory
        .createCompositeValueConnector(modelDescriptor.getName() + "Element",
            modelDescriptor.getCollectionDescriptor().getElementDescriptor()
                .getToStringProperty());
    ICollectionConnector connector = connectorFactory
        .createCollectionConnector(modelDescriptor.getName(), mvcBinder,
            rowConnectorPrototype);
    RComponent viewComponent = createRComponent();
    viewComponent.setIcon(iconFactory.getIcon(modelDescriptor
        .getCollectionDescriptor().getElementDescriptor().getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    BasicCompositeView<RComponent> view = constructCompositeView(viewComponent,
        viewDescriptor, connector);

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

  private IView<RComponent> createReferencePropertyView(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    RAction fieldAction = actionFactory.createAction(lovAction, actionHandler,
        view.getPeer(), propertyDescriptor, view.getConnector(), locale);
    fieldAction.setName(getTranslationProvider().getTranslation(
        "lov.element.name",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            translationProvider, locale)}, locale));
    fieldAction.setDescription(getTranslationProvider().getTranslation(
        "lov.element.description",
        new Object[] {propertyDescriptor.getReferencedDescriptor().getI18nName(
            translationProvider, locale)}, locale)
        + TOOLTIP_ELLIPSIS);
    if (propertyDescriptor.getReferencedDescriptor().getIconImageURL() != null) {
      fieldAction.setIcon(iconFactory.getIcon(propertyDescriptor
          .getReferencedDescriptor().getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }
    view.getPeer().setActions(Collections.singletonList(fieldAction));
    return view;
  }

  private IView<RComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    Map<String, String> translations = new HashMap<String, String>();
    Map<String, RIcon> icons = new HashMap<String, RIcon>();
    for (String value : propertyDescriptor.getEnumerationValues()) {
      if (value != null && propertyDescriptor.isTranslated()) {
        translations.put(value, translationProvider.getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
        icons.put(value, iconFactory.getIcon(propertyDescriptor
            .getIconImageURL(value), IIconFactory.TINY_ICON_SIZE));
      }
    }
    view.getPeer().setRenderingTranslations(translations);
    return view;
  }

  private String computeEnumerationKey(String keyPrefix, String value) {
    return keyPrefix + "." + value;
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }

  /**
   * {@inheritDoc}
   */
  public IActionFactory<RAction, RComponent> getActionFactory() {
    return actionFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IIconFactory<RIcon> getIconFactory() {
    return iconFactory;
  }
}
