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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
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
import org.jspresso.framework.view.descriptor.ICompositeViewDescriptor;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;
import org.jspresso.framework.view.descriptor.INestingViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.ISubViewDescriptor;
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
    // TODO Auto-generated method stub
    return null;
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
                locale));
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
    IValueConnector connector = connectorFactory
        .createValueConnector(propertyDescriptor.getName());
    connector.setExceptionHandler(actionHandler);
    RAction openAction = actionFactory.createAction(
        openFileAsBinaryPropertyAction, actionHandler, view.getPeer(),
        propertyDescriptor, connector, locale);
    RAction saveAction = actionFactory.createAction(
        saveBinaryPropertyAsFileAction, actionHandler, view.getPeer(),
        propertyDescriptor, connector, locale);
    RAction resetAction = actionFactory.createAction(resetPropertyAction,
        actionHandler, view.getPeer(), propertyDescriptor, connector, locale);
    RAction infoAction = actionFactory.createAction(binaryPropertyInfoAction,
        actionHandler, view.getPeer(), propertyDescriptor, connector, locale);
    view.getPeer().setActions(
        Arrays.asList(new RAction[] {openAction, saveAction, resetAction,
            infoAction}));
    return view;
  }

  private IView<RComponent> createRelationshipEndPropertyView(
      IRelationshipEndPropertyDescriptor propertyDescriptor,
      List<String> renderedChildProperties, IActionHandler actionHandler,
      Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  private IView<RComponent> createEnumerationPropertyView(
      IEnumerationPropertyDescriptor propertyDescriptor,
      IActionHandler actionHandler, Locale locale) {
    IView<RComponent> view = createRComponentPropertyView(propertyDescriptor,
        actionHandler, locale);
    Map<String, String> translations = new HashMap<String, String>();
    for (String value : propertyDescriptor.getEnumerationValues()) {
      if (value != null && propertyDescriptor.isTranslated()) {
        translations.put(value, translationProvider.getTranslation(
            computeEnumerationKey(propertyDescriptor.getEnumerationName(),
                value), locale));
      }
    }
    view.getPeer().setTranslations(translations);
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
