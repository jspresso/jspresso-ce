/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import java.util.Collection;
import java.util.Locale;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.EBorderType;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is the abstract base descriptor for all views. Its main purpose, since
 * it cannot be used directly, is to factorize common properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicViewDescriptor extends DefaultIconDescriptor
    implements IViewDescriptor {

  private ActionMap          actionMap;
  private String             background;
  private EBorderType        borderType = EBorderType.NONE;
  private String             font;
  private String             foreground;
  private IModelDescriptor   modelDescriptor;
  private Collection<String> grantedRoles;
  private Collection<IGate>  readabilityGates;
  private boolean            readOnly;
  private Collection<IGate>  writabilityGates;
  private Integer            preferredWidth;
  private Integer            preferredHeight;

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    if (grantedRoles == null && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof ISecurable) {
        return ((ISecurable) getModelDescriptor()).getGrantedRoles();
      }
    }
    return grantedRoles;
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    // Gates are handled both on model connector and view connector. It is not
    // necessary to fetch the model gates here. Only component view descriptors
    // use their model gates since they are often backed by a reference property
    // connector.
    // if (readabilityGates == null && getModelDescriptor() != null) {
    // if (getModelDescriptor() instanceof IGateAccessible) {
    // return ((IGateAccessible) getModelDescriptor()).getReadabilityGates();
    // }
    // }
    return readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    // Gates are handled both on model connector and view connector. It is not
    // necessary to fetch the model gates here. Only component view descriptors
    // use their model gates since they are often backed by a reference property
    // connector.
    // if (writabilityGates == null && getModelDescriptor() != null) {
    // if (getModelDescriptor() instanceof IGateAccessible) {
    // return ((IGateAccessible) getModelDescriptor()).getWritabilityGates();
    // }
    // }
    return writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    if (!readOnly && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IGateAccessible) {
        return ((IGateAccessible) getModelDescriptor()).isReadOnly();
      }
    }
    return readOnly;
  }

  /**
   * Assigns the roles that are authorized to use this view. Whenever the user
   * is not granted sufficient privileges, the view is replaced by an empty
   * section at runtime. Setting the collection of granted roles to
   * <code>null</code> (default value) disables role based authorization on the
   * view level. The framework then checks for the model roles authorizations
   * and will apply the same restrictions. If both view and model granted roles
   * collections are <code>null</code>, then access is granted to anyone.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *          the readabilityGates to set.
   * @internal
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Allows to set a view read-only, i.e. none of the view part will allow for
   * updating the underlying model. This is mainly a shortcut to assigning an
   * &quot;always closed&quot; writability gate. One difference though is that,
   * since the framework knows that the view will never be updatable, it may
   * take specific decisions to render properties in a slightly different way,
   * e.g. instead of using a disabled textfied, use a label.
   * <p>
   * Default value is <code>false</code>, i.e. view is updatable.
   * 
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Assigns a collection of gates to determine view <i>writability</i>. A view
   * will be considered writable (updatable) if and only if all gates are open.
   * This mecanism is mainly used for dynamic UI authorization based on model
   * state, e.g. a validated invoice should not be editable anymore.
   * <p>
   * View assigned gates will be cloned for each view instance created and
   * backed by this descriptor. So basically, each view instance will have its
   * own, unshared collection of writability gates.
   * <p>
   * Jspresso provides a useful set of gate types, like the binary property gate
   * that open/close based on the value of a boolean property of the view model.
   * <p>
   * By default, view descriptors are not assigned any gates collection, i.e.
   * there is no writability restriction. Note however that view actual
   * writability is the combination of view <i>and</i> model writability.
   * 
   * @param writabilityGates
   *          the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

  /**
   * Gets the actionMap.
   * 
   * @return the actionMap.
   */
  public ActionMap getActionMap() {
    return actionMap;
  }

  /**
   * {@inheritDoc}
   */
  public String getBackground() {
    return background;
  }

  /**
   * {@inheritDoc}
   */
  public EBorderType getBorderType() {
    return borderType;
  }

  /**
   * {@inheritDoc}
   */
  public String getFont() {
    return font;
  }

  /**
   * {@inheritDoc}
   */
  public String getForeground() {
    return foreground;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() == null) {
      if (getModelDescriptor() != null) {
        return getModelDescriptor().getI18nDescription(translationProvider,
            locale);
      }
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (getName() == null) {
      if (getModelDescriptor() != null) {
        return getModelDescriptor().getI18nName(translationProvider, locale);
      }
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null
        && getModelDescriptor() instanceof IComponentDescriptor<?>) {
      iconImageURL = ((IComponentDescriptor<?>) getModelDescriptor())
          .getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * Assigns the view action map. An action map is generally represented as a
   * toolbar attached to the view. The toolbar follows the structure of the
   * action map :
   * <ul>
   * <li>each action list is contained in its own toolbar section which is
   * visually separated from the other sections. This allows for visually
   * grouping related actions as they are grouped in the action lists.</li>
   * <li>each action contained in an action list is represented by a toolbar
   * button using the action image as icon and translated action description as
   * tooltip.</li>
   * </ul>
   * Depending on the UI channel, the view action map may also be replicated in
   * a component contextual menu. In that case, the translated action name is
   * used to label each menu item. The same grouping rules apply for the
   * contextual menu than for the toolbar.
   * 
   * @param actionMap
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the background color of the UI component. The color must be defined
   * using its string hexadecimal representation (<i>0xrgba</i> encoded).
   * <p>
   * Default value is <code>null</code>, meaning use UI default.
   * 
   * @param background
   *          the background to set.
   */
  public void setBackground(String background) {
    this.background = background;
  }

  /**
   * Sets the border type of the view. This is either a value of the
   * <code>EBorderType</code> enum or its equivalent string representation :
   * <ul>
   * <li><code>NONE</code> for no border</li>
   * <li><code>SIMPLE</code> for a line border</li>
   * <li><code>TITLED</code> for a titled border. The view is then labeled with
   * its translated name and and icon. Whenever the view name has not been
   * explicitely set, the model name is used is used.</li>
   * </ul>
   * <p>
   * Default value is <code>EBorderType.NONE</code>, i.e. no border.
   * 
   * @param borderType
   *          the borderType to set.
   */
  public void setBorderType(EBorderType borderType) {
    this.borderType = borderType;
  }

  /**
   * Allows to customize the font used by the UI component. The font must be
   * string encoded using the pattern <b>&quot;[name];[style];[size]&quot;</b> :
   * <ul>
   * <li><b>[name]</b> is the name of the font, e.g. <i>arial</i>.</li>
   * <li><b>[style]</b> is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC
   * combined with the '|' character, e.g. <i>BOLD|ITALIC</i>.</li>
   * <li><b>[size]</b> is the size of the font, e.g. <i>10</i>.</li>
   * </ul>
   * Any of the above pattern section can be left empty, thus falling back to
   * the component default.
   * <p>
   * Default value is <code>null</code>, meaning use default component font.
   * 
   * @param font
   *          the font to set.
   */
  public void setFont(String font) {
    this.font = font;
  }

  /**
   * Sets the foreground color of the UI component. The color must be defined
   * using its string hexadecimal representation (<i>0xrgba</i> encoded).
   * <p>
   * Default value is <code>null</code>, meaning use UI default.
   * 
   * @param foreground
   *          the foreground to set.
   */
  public void setForeground(String foreground) {
    this.foreground = foreground;
  }

  /**
   * Assigns the model descriptor backing the view. The model descriptor serves
   * several purposes :
   * <ul>
   * <li>configuration of the view content. For instance whenever a form is
   * assigned a component model descriptor, it will install 1 field per
   * component rendering properties, unless otherwise specified in the view
   * descriptor itself.</li>
   * <li>configuration of the binding layer. There is no need for the developer
   * to configure anything for the binding to occur between the view and the
   * model. Based on their model descriptor, Jspresso will setup all the
   * necessary plumbing to efficiently synchronize model properties with their
   * view couterpart bi-directionally. This synchronization occurs implicitely
   * using the <i>observer</i> pattern and one of the Jspresso key contract is
   * to guarantee this synchronization seamlessly.</li>
   * </ul>
   * Although it is the developer responsibility to make sure the correct model
   * descriptor is assigned to the view, there are cases where the framework
   * will infer it. For instance, a composite view will by default transmit its
   * model descriptor to its children that do not have their model descriptor
   * explicitely set. This allows for setting the model descriptor only on the
   * composite view and keep default <code>null</code> value on the children as
   * an implicite model inheritance enablement.
   * 
   * @param modelDescriptor
   *          the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Dimension getPreferredSize() {
    if (preferredWidth != null && preferredHeight != null) {
      return new Dimension(preferredWidth.intValue(), preferredHeight
          .intValue());
    }
    return null;
  }

  /**
   * Allows to set a preferred width (in pixels) for the created peer UI
   * component. This will override default and give hints to the UI layouting
   * system.
   * 
   * @param preferredWidth
   *          the preferredWidth to set.
   */
  public void setPreferredWidth(Integer preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  /**
   * Allows to set a preferred height (in pixels) for the created peer UI
   * component. This will override default and give hints to the UI layouting
   * system.
   * 
   * @param preferedHeight
   *          the preferedHeight to set.
   */
  public void setPreferredHeight(Integer preferedHeight) {
    this.preferredHeight = preferedHeight;
  }
}
