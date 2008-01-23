/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelGate;
import com.d2s.framework.binding.model.IModelValueConnector;
import com.d2s.framework.model.EmbeddedModelProvider;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.util.gate.GateHelper;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.util.KeyStroke;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * An ulc action factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcActionFactory implements IActionFactory<IAction, ULCComponent> {

  private IIconFactory<ULCIcon> iconFactory;
  private ITranslationProvider  translationProvider;

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<ULCComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, ULCComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    IAction ulcAction = new ActionAdapter(action, actionHandler,
        sourceComponent, modelDescriptor, viewConnector, locale);
    if (action.getActionabilityGates() != null) {
      Collection<IGate> clonedGates = new HashSet<IGate>();
      for (IGate gate : action.getActionabilityGates()) {
        final IGate clonedGate = gate.clone();
        if (clonedGate instanceof IModelGate) {
          if (modelDescriptor instanceof IComponentDescriptorProvider) {
            ((IModelGate) clonedGate)
                .setModelProvider(new EmbeddedModelProvider(
                    (IComponentDescriptorProvider<?>) modelDescriptor));
            viewConnector
                .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

                  public void connectorValueChange(ConnectorValueChangeEvent evt) {
                    ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                        .getModelProvider()).setModel(evt.getNewValue());
                  }
                });
          } else if (modelDescriptor instanceof ICollectionPropertyDescriptor) {
            IRelationshipEndPropertyDescriptor reverseDescriptor = ((ICollectionPropertyDescriptor<?>) modelDescriptor)
                .getReverseRelationEnd();
            if (reverseDescriptor instanceof IComponentDescriptorProvider) {
              ((IModelGate) clonedGate)
                  .setModelProvider(new EmbeddedModelProvider(
                      (IComponentDescriptorProvider<?>) reverseDescriptor));
            } else if (reverseDescriptor instanceof ICollectionDescriptorProvider) {
              ((IModelGate) clonedGate)
                  .setModelProvider(new EmbeddedModelProvider(
                      ((ICollectionDescriptorProvider<?>) reverseDescriptor)
                          .getCollectionDescriptor().getElementDescriptor()));
            }
            final ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector();
            collectionConnector
                .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

                  public void connectorValueChange(@SuppressWarnings("unused")
                  ConnectorValueChangeEvent evt) {
                    if (collectionConnector.getModelConnector() != null) {
                      ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                          .getModelProvider())
                          .setModel(((IModelValueConnector) collectionConnector
                              .getModelConnector()).getModelProvider()
                              .getModel());
                    } else {
                      ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                          .getModelProvider()).setModel(null);
                    }
                  }
                });
          }
        }
        clonedGates.add(clonedGate);
      }
      new GatesListener(ulcAction, clonedGates);
    }
    return ulcAction;
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *            the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<ULCIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *            the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private final class ActionAdapter extends
      com.ulcjava.base.application.AbstractAction {

    private static final long                serialVersionUID = 5819377672533326496L;

    private com.d2s.framework.action.IAction action;
    private IActionHandler                   actionHandler;
    private IModelDescriptor                 modelDescriptor;
    private ULCComponent                     sourceComponent;
    private IValueConnector                  viewConnector;

    /**
     * Constructs a new <code>ActionAdapter</code> instance.
     * 
     * @param action
     * @param actionHandler
     * @param sourceComponent
     * @param modelDescriptor
     * @param viewConnector
     * @param locale
     */
    public ActionAdapter(IDisplayableAction action,
        IActionHandler actionHandler, ULCComponent sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
        Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.sourceComponent = sourceComponent;
      this.modelDescriptor = modelDescriptor;
      this.viewConnector = viewConnector;
      putValue(IAction.NAME, action.getI18nName(translationProvider, locale));
      String i18nDescription = action.getI18nDescription(translationProvider,
          locale);
      if (i18nDescription != null) {
        putValue(IAction.SHORT_DESCRIPTION, i18nDescription + TOOLTIP_ELLIPSIS);
      }
      putValue(IAction.SMALL_ICON, iconFactory.getIcon(
          action.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
      if (action.getMnemonicAsString() != null) {
        putValue(IAction.MNEMONIC_KEY, new Integer(KeyStroke.getKeyStroke(
            action.getMnemonicAsString()).getKeyCode()));
      }
    }

    /**
     * Triggers the action execution on the action handler. The following
     * initial action context is filled in :
     * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>
     * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code>
     * <li> <code>ActionContextConstants.SELECTED_INDICES</code>
     * <li> <code>ActionContextConstants.LOCALE</code>
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
      if (actionHandler != null) {
        Map<String, Object> actionContext = actionHandler.createEmptyContext();
        actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        actionContext.put(ActionContextConstants.VIEW_CONNECTOR, viewConnector);
        if (viewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector() != null) {
          actionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) viewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        actionContext.put(ActionContextConstants.ACTION_COMMAND, e
            .getActionCommand());
        actionContext.put(ActionContextConstants.ACTION_WIDGET, e.getSource());
        if (action.getInitialContext() != null) {
          actionContext.putAll(action.getInitialContext());
        }
        actionHandler.execute(action, actionContext);
      }
    }

  }

  private final class GatesListener implements PropertyChangeListener {

    private IAction           action;
    private Collection<IGate> gates;

    /**
     * Constructs a new <code>GatesListener</code> instance.
     * 
     * @param action
     *            the action to (de)activate based on gates state.
     * @param gates
     *            the gates that determine action state.
     */
    public GatesListener(IAction action, Collection<IGate> gates) {
      this.action = action;
      this.gates = gates;
      for (IGate gate : gates) {
        gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, this);
      }
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(@SuppressWarnings("unused")
    PropertyChangeEvent evt) {
      action.setEnabled(GateHelper.areGatesOpen(gates));
    }
  }
}
