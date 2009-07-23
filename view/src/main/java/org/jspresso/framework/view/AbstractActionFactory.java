/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;

import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelGate;
import org.jspresso.framework.model.EmbeddedModelProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.gate.GateHelper;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Abstract base class for action factories.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual action class created.
 * @param <F>
 *          the actual component class the created actions are installed in.
 * @param <G>
 *          the actual icon class the created actions support.
 */
public abstract class AbstractActionFactory<E, F, G> implements
    IActionFactory<E, F> {

  private IIconFactory<G>      iconFactory;
  private ITranslationProvider translationProvider;

  /**
   * Creates and attach the necessary action gates.
   * 
   * @param action
   *          the displayable Jspresso action.
   * @param modelDescriptor
   *          the model descriptor of the view.
   * @param viewConnector
   *          the view connector.
   * @param uiAction
   *          the created ui specific action.
   */
  protected void attachActionGates(IDisplayableAction action,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      E uiAction) {
    if (action.getActionabilityGates() != null) {
      Collection<IGate> clonedGates = new HashSet<IGate>();
      for (IGate gate : action.getActionabilityGates()) {
        final IGate clonedGate = gate.clone();
        if (clonedGate instanceof IModelGate) {
          if (modelDescriptor instanceof IComponentDescriptorProvider<?>) {
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
          } else if (modelDescriptor instanceof ICollectionPropertyDescriptor<?>) {
            ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector().addConnectorValueChangeListener(
                    new IConnectorValueChangeListener() {

                      public void connectorValueChange(
                          ConnectorValueChangeEvent evt) {
                        ICollectionConnector collectionConnector = (ICollectionConnector) evt
                            .getSource();
                        if (((IModelGate) clonedGate).getModelProvider() == null) {
                          ((IModelGate) clonedGate)
                              .setModelProvider(new EmbeddedModelProvider(
                                  collectionConnector.getModelProvider()
                                      .getModelDescriptor()));
                        }
                        if (collectionConnector.getModelConnector() != null) {
                          ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                              .getModelProvider()).setModel(collectionConnector
                              .getModelProvider().getModel());
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
      new GatesListener(uiAction, clonedGates);
    }
  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<G> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private final class GatesListener implements PropertyChangeListener {

    private E                 action;
    private Collection<IGate> gates;

    /**
     * Constructs a new <code>GatesListener</code> instance.
     * 
     * @param action
     *          the action to (de)activate based on gates state.
     * @param gates
     *          the gates that determine action state.
     */
    public GatesListener(E action, Collection<IGate> gates) {
      this.action = action;
      this.gates = gates;
      setActionEnabled(action, GateHelper.areGatesOpen(gates));
      for (IGate gate : gates) {
        gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, this);
      }
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(
        @SuppressWarnings("unused") PropertyChangeEvent evt) {
      setActionEnabled(action, GateHelper.areGatesOpen(gates));
    }
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  protected IIconFactory<G> getIconFactory() {
    return iconFactory;
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }
}
