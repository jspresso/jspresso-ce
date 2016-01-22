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
package org.jspresso.framework.application.frontend.action;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.action.AbstractAction;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.ModelTrackingGate;
import org.jspresso.framework.util.gate.NotEmptyCollectionSelectionTrackingGate;
import org.jspresso.framework.util.gate.SingleCollectionSelectionTrackingGate;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This is the base class for frontend actions. To get a better understanding of
 * how actions are organized in Jspresso, please refer to
 * {@code AbstractAction} documentation.
 * <p>
 * This base class allows for visual (name, icon, toolTip) as well as
 * accessibility (accelerator, mnemonic shortcuts) and actionability (using
 * gates) parametrization.
 * <p>
 * A frontend action is to be executed by the frontend controller in the context
 * of the UI. It can thus access the view structure, interact visually with the
 * user, and so on. A frontend action can chain a backend action but the
 * opposite will be prevented.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class FrontendAction<E, F, G> extends AbstractAction implements
    IDisplayableAction {

  /**
   * {@code COMPONENT_TO_FOCUS} is COMPONENT_TO_FOCUS.
   */
  public static final String COMPONENT_TO_FOCUS = "COMPONENT_TO_FOCUS";
  private       String                acceleratorAsString;
  private       Collection<IGate>     actionabilityGates;
  private final DefaultIconDescriptor actionDescriptor;
  private       boolean               collectionBased;
  private       boolean               multiSelectionEnabled;
  private       String                mnemonicAsString;
  private       String                styleName;

  /**
   * Constructs a new {@code AbstractFrontendAction} instance.
   */
  public FrontendAction() {
    actionDescriptor = new DefaultIconDescriptor();
    setCollectionBased(false);
    setMultiSelectionEnabled(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!getClass().isAssignableFrom(obj.getClass()) && !obj.getClass().isAssignableFrom(getClass())) {
      return false;
    }
    final IDisplayableAction other = (IDisplayableAction) obj;
    if (getName() == null) {
      return false;
    }
    return getName().equals(other.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAcceleratorAsString() {
    return acceleratorAsString;
  }

  /**
   * Gets the actionabilityGates.
   *
   * @return the actionabilityGates.
   */
  @Override
  public Collection<IGate> getActionabilityGates() {
    return actionabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return actionDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() != null) {
      return translationProvider.getTranslation(getDescription(), "", locale);
    }
    return getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(getName(), locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return actionDescriptor.getIcon();
  }

  /**
   * Gets the icon image URL or null if no icon is set.
   *
   * @return the icon image URL or null if no icon is set.
   */
  protected String getIconImageURL() {
    Icon icon = getIcon();
    if (icon != null) {
      return icon.getIconImageURL();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMnemonicAsString() {
    return mnemonicAsString;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return actionDescriptor.getName();
  }

  /**
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
  public String getPermId() {
    String permId = super.getPermId();
    if (permId == null) {
      return getName();
    }
    return permId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result;
    if (getName() != null) {
      result += getName().hashCode();
    }
    return result;
  }

  /**
   * Returns false.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isBackend() {
    return false;
  }

  /**
   * Gets the collectionBased.
   *
   * @return the collectionBased.
   */
  @Override
  public boolean isCollectionBased() {
    return collectionBased;
  }

  /**
   * Gets the multiSelectionEnabled.
   *
   * @return the multiSelectionEnabled.
   */
  @Override
  public boolean isMultiSelectionEnabled() {
    return multiSelectionEnabled;
  }

  /**
   * Configures a keyboard accelerator shortcut on this action. Support of this
   * feature depends on the UI execution platform. The syntax used consists of
   * listing keys that should be pressed to trigger the action, i.e.
   * {@code alt d} or {@code ctrl c}. This is the syntax supported by
   * the {@code javax.swing.KeyStroke#getKeyStroke(...)} swing static
   * method.
   *
   * @param acceleratorAsString
   *          the acceleratorAsString to set.
   */
  public void setAcceleratorAsString(String acceleratorAsString) {
    this.acceleratorAsString = acceleratorAsString;
  }

  /**
   * Assigns a collection of gates to determine action <i>actionability</i>. An
   * action will be considered actionable (enabled) if and only if all gates are
   * open. This mechanism is mainly used for dynamic UI authorization based on
   * model state, e.g. a validated invoice should not be validated twice.
   * <p>
   * Action assigned gates will be cloned for each concrete action instance
   * created and bound to its respective UI component (usually a button). So
   * basically, each action instance will have its own, unshared collection of
   * actionability gates.
   * <p>
   * Jspresso provides a useful set of gate types, like the binary property gate
   * that open/close based on the value of a boolean property of the view model
   * the action is installed to.
   * <p>
   * By default, frontend actions are assigned a generic gate that closes
   * (disables the action) when the view is not assigned any model.
   *
   * @param actionabilityGates
   *          the actionabilityGates to set.
   */
  public void setActionabilityGates(Collection<IGate> actionabilityGates) {
    this.actionabilityGates = actionabilityGates;
    completeActionabilityGates();
  }

  /**
   * Declares the action as working on a collection of objects. Collection based
   * actions will typically be installed on selectable views (table, list, tree)
   * and will be enabled only when the view selection is not empty (a default
   * gate is installed for this purpose). Moreover, model gates that are
   * configured on collection based actions take their model from the view
   * selected components instead of the view model itself. In case of
   * multi-selection enabled UI views, the actionability gates will actually
   * open if and only if their opening condition is met for all the selected
   * items.
   *
   * @param collectionBased
   *          the collectionBased to set.
   */
  public void setCollectionBased(boolean collectionBased) {
    this.collectionBased = collectionBased;
    completeActionabilityGates();
  }

  /**
   * Declares the action as being able to run on a collection containing more
   * than 1 element. A multiSelectionEnabled = false action will be disabled
   * when the selection contains no or more than one element.
   *
   * @param multiSelectionEnabled
   *          the multiSelectionEnabled to set.
   */
  public void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
    this.multiSelectionEnabled = multiSelectionEnabled;
    completeActionabilityGates();
  }

  /**
   * Sets the key used to compute the internationalized description of the
   * action. The translated description is then usually used as toolTip for the
   * action.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    actionDescriptor.setDescription(description);
  }

  /**
   * Sets the icon image URL used to decorate the action UI component peer.
   * <p>
   * Supported URL protocols include :
   * <ul>
   * <li>all JVM supported protocols</li>
   * <li>the <b>jar:/</b> pseudo URL protocol</li>
   * <li>the <b>classpath:/</b> pseudo URL protocol</li>
   * </ul>
   *
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    actionDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the icon.
   *
   * @param icon
   *          the icon to set.
   */
  public void setIcon(Icon icon) {
    actionDescriptor.setIcon(icon);
  }

  /**
   * Configures the mnemonic key used for this action. Support of this feature
   * depends on the UI execution platform. Mnemonics are typically used in menu
   * and menu items.
   *
   * @param mnemonicStringRep
   *          the mnemonic to set represented as a string as KeyStroke factory
   *          would parse it.
   */
  public void setMnemonicAsString(String mnemonicStringRep) {
    this.mnemonicAsString = mnemonicStringRep;
  }

  /**
   * Sets the key used to compute the internationalized name of the action. The
   * translated name is then usually used as label for the action (button label,
   * menu label, ...).
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    actionDescriptor.setName(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", getName())
        .append("description", getDescription())
        .append("iconImageURL", getIcon()).toString();
  }

  private void completeActionabilityGates() {
    if (actionabilityGates == null) {
      actionabilityGates = new LinkedHashSet<>();
    }
    if (isCollectionBased()) {
      actionabilityGates.remove(ModelTrackingGate.INSTANCE);
      actionabilityGates.add(NotEmptyCollectionSelectionTrackingGate.INSTANCE);
    } else {
      actionabilityGates
          .remove(NotEmptyCollectionSelectionTrackingGate.INSTANCE);
      actionabilityGates.add(ModelTrackingGate.INSTANCE);
    }
    if (isMultiSelectionEnabled()) {
      actionabilityGates.remove(SingleCollectionSelectionTrackingGate.INSTANCE);
    } else {
      actionabilityGates.add(SingleCollectionSelectionTrackingGate.INSTANCE);
    }
  }

  /**
   * Gets the actionFactory.
   *
   * @param context
   *          the action context.
   * @return the actionFactory.
   */
  protected IActionFactory<G, E> getActionFactory(Map<String, Object> context) {
    return getViewFactory(context).getActionFactory();
  }

  /**
   * Retrieves the widget which triggered the action from the action context.
   *
   * @param context
   *          the action context.
   * @return the widget which triggered the action.
   */
  @SuppressWarnings("unchecked")
  protected E getActionWidget(Map<String, Object> context) {
    return (E) context.get(ActionContextConstants.ACTION_WIDGET);
  }

  /**
   * Retrieves the concrete action (swing, remote) that was triggered triggered
   * from the action context.
   *
   * @param context
   *          the action context.
   * @return the concrete action that was triggered.
   */
  @SuppressWarnings("unchecked")
  protected G getUiAction(Map<String, Object> context) {
    return (G) context.get(ActionContextConstants.UI_ACTION);
  }

  /**
   * Retrieves the UI action event from the action context.
   *
   * @param context
   *          the action context.
   * @return the UI action event that was triggered.
   */
  protected Object getUiEvent(Map<String, Object> context) {
    return context.get(ActionContextConstants.UI_EVENT);
  }

  /**
   * Gets the frontend controller out of the action context.
   *
   * @param context
   *          the action context.
   * @return the frontend controller.
   */
  @Override
  protected IFrontendController<E, F, G> getController(
      Map<String, Object> context) {
    return getFrontendController(context);
  }

  /**
   * Gets the iconFactory.
   *
   * @param context
   *          the action context.
   * @return the iconFactory.
   */
  protected IIconFactory<F> getIconFactory(Map<String, Object> context) {
    return getViewFactory(context).getIconFactory();
  }

  /**
   * Gets the mvcBinder.
   *
   * @param context
   *          the action context.
   * @return the mvcBinder.
   */
  protected IMvcBinder getMvcBinder(Map<String, Object> context) {
    return getController(context).getMvcBinder();
  }

  /**
   * Retrieves the widget this action was triggered from. It may serve to
   * determine the root window or dialog for instance. It uses a well-known
   * action context key which is : <li>
   * {@code ActionContextConstants.SOURCE_COMPONENT}.
   *
   * @param context
   *          the action context.
   * @return the source widget this action was triggered from.
   */
  @SuppressWarnings("unchecked")
  protected E getSourceComponent(Map<String, Object> context) {
    return (E) context.get(ActionContextConstants.SOURCE_COMPONENT);
  }

  /**
   * Gets the viewFactory.
   *
   * @param context
   *          the action context.
   * @return the viewFactory.
   */
  protected IViewFactory<E, F, G> getViewFactory(Map<String, Object> context) {
    return getController(context).getViewFactory();
  }

  /**
   * Gets the lastUpdated.
   *
   * @return the lastUpdated.
   */
  @Override
  public long getLastUpdated() {
    return actionDescriptor.getLastUpdated();
  }

  /**
   * Sets the lastUpdated.
   *
   * @param lastUpdated
   *          the lastUpdated to set.
   * @internal
   */
  public void setLastUpdated(long lastUpdated) {
    actionDescriptor.setLastUpdated(lastUpdated);
  }

  /**
   * Gets the styleName.
   *
   * @return the styleName.
   */
  @Override
  public String getStyleName() {
    return styleName;
  }

  /**
   * Assigns the style name to use for this view. The way it is actually
   * leveraged depends on the UI channel. It will generally be mapped to some
   * sort of CSS style name.
   * <p>
   * Default value is {@code null}, meaning that a default style is used.
   *
   * @param styleName
   *          the styleName to set.
   */
  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  /**
   * Sets the icon preferred width.
   *
   * @param iconPreferredWidth
   *          the iconPreferredWidth to set.
   * @see org.jspresso.framework.util.descriptor.DefaultIconDescriptor#setIconPreferredWidth(int)
   */
  public void setIconPreferredWidth(int iconPreferredWidth) {
    actionDescriptor.setIconPreferredWidth(iconPreferredWidth);
  }

  /**
   * Sets the icon preferred height.
   *
   * @param iconPreferredHeight
   *          the iconPreferredHeight to set.
   * @see org.jspresso.framework.util.descriptor.DefaultIconDescriptor#setIconPreferredHeight(int)
   */
  public void setIconPreferredHeight(int iconPreferredHeight) {
    actionDescriptor.setIconPreferredHeight(iconPreferredHeight);
  }

}
