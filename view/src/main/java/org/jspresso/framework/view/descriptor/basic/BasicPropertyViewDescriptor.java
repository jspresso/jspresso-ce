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
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.view.descriptor.EHorizontalAlignment;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * This view descriptor serves 2 purposes :
 * <ul>
 * <li>configure complex, component based views : refine <i>columns</i> of table
 * views and <i>fields</i> of component (form) views.</li>
 * <li>display a single property as an autonomous view, i.e. not as a table
 * column or a form field.</li>
 * </ul>
 * The second usage might be a little bit unusual, but here is a use-case
 * scenario : display a text area which maps a text property that contains XML
 * content. This text area must be displayed in a split pane and provide actions
 * to interact directly with the FS (save content to a file, load content from a
 * file, ...). In that case, defining a property view alone on the text property
 * of the owning component might be a good solution.
 *
 * @author Vincent Vandenschrick
 */
public class BasicPropertyViewDescriptor extends BasicViewDescriptor implements
    IPropertyViewDescriptor {

  private String               labelBackground;
  private String               labelFont;
  private String               labelForeground;
  private EHorizontalPosition  labelHorizontalPosition;
  private List<String>         renderedChildProperties;
  private Integer              width;
  private IAction              action;
  private EHorizontalAlignment horizontalAlignment;
  private Boolean              sortable;
  private List<String>         forClientTypes;

  /**
   * Gets the labelBackground.
   *
   * @return the labelBackground.
   */
  @Override
  public String getLabelBackground() {
    return labelBackground;
  }

  /**
   * Gets the labelFont.
   *
   * @return the labelFont.
   */
  @Override
  public String getLabelFont() {
    return labelFont;
  }

  /**
   * Gets the labelForeground.
   *
   * @return the labelForeground.
   */
  @Override
  public String getLabelForeground() {
    return labelForeground;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedChildProperties() {
    return renderedChildProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getDefaultRenderedChildProperties() {
    IModelDescriptor propertyDescriptor = getModelDescriptor();
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      return ((ICollectionDescriptor<?>) ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
          .getCollectionDescriptor()).getElementDescriptor().getRenderedProperties();
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      return ((IReferencePropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor().getRenderedProperties();
    }
    return null;
  }

  /**
   * Gets the width.
   *
   * @return the width.
   */
  @Override
  public Integer getWidth() {
    if (width != null) {
      return width;
    }
    return 1;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the background color of the corresponding label. It might
   * differ from the field component one. The color must be defined using its
   * string hexadecimal representation (<i>0xargb</i> encoded).
   * <p>
   * Default value is {@code null}, meaning use UI default.
   *
   * @param labelBackground
   *          the labelBackground to set.
   */
  public void setLabelBackground(String labelBackground) {
    this.labelBackground = labelBackground;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the font of the corresponding label. It might differ from
   * the field component one. The font must be string encoded using the pattern
   * <b>&quot;[name];[style];[size]&quot;</b> :
   * <ul>
   * <li><b>[name]</b> is the name of the font, e.g. <i>arial</i>.</li>
   * <li><b>[style]</b> is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC
   * combined with the '|' character, e.g. <i>BOLD|ITALIC</i>.</li>
   * <li><b>[size]</b> is the size of the font, e.g. <i>10</i>.</li>
   * </ul>
   * Any of the above pattern section can be left empty, thus falling back to
   * the component default.
   * <p>
   * Default value is {@code null}, meaning use default component font.
   *
   * @param labelFont
   *          the labelFont to set.
   */
  public void setLabelFont(String labelFont) {
    this.labelFont = labelFont;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the foreground color of the corresponding label. It might
   * differ from the field component one. The color must be defined using its
   * string hexadecimal representation (<i>0xargb</i> encoded).
   * <p>
   * Default value is {@code null}, meaning use UI default.
   *
   * @param labelForeground
   *          the labelForeground to set.
   */
  public void setLabelForeground(String labelForeground) {
    this.labelForeground = labelForeground;
  }

  /**
   * Whenever the property descriptor backing the view is not scalar, this
   * property allows to override which of the referenced component fields should
   * be displayed :
   * <ul>
   * <li>as columns when the rendered property is a collection property</li>
   * <li>as fields when the rendered property is a reference property</li>
   * </ul>
   * The property must be configured with a {@code List} containing the
   * property names to render for the child element(s).
   * <p>
   * A {@code null} value (default), means that the non-scalar property
   * will be rendered using default rendered properties as specified in its
   * referenced model descriptor.
   * <p>
   * Please note that this is quite unusual to embed non-scalar properties
   * directly in a property view. Although permitted, you won't have as much
   * flexibility in the content layouting as you would have when using composite
   * views; so the latter is by far recommended.
   *
   * @param renderedChildProperties
   *          the renderedChildProperties to set.
   */
  public void setRenderedChildProperties(List<String> renderedChildProperties) {
    this.renderedChildProperties = renderedChildProperties;
  }

  /**
   * When the property has to be displayed in a grid-like layout (e.g. in a
   * component view), this property defines the umber of grid columns the
   * corresponding UI component will span.
   * <p>
   * Default value is {@code null}, meaning use default span of 1.
   *
   * @param width
   *          the width to set.
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * Gets the action.
   *
   * @return the action.
   */
  @Override
  public IAction getAction() {
    return action;
  }

  /**
   * Configures the action to be triggered when <i>acting</i> on this property.
   * There are 2 cases :
   * <p>
   * <ol>
   * <li>If the property is read-only, then assigning an action turns the
   * property into a clickable hyperlink</li>
   * <li>If the property is read-write, the registered action will be triggered
   * when the user changes the value of the field. Note that in that case, the
   * action is executed <i>after</i> the model has been updated. However the old
   * property value can be retrieved from the context action param.</li>
   * </ol>
   *
   * @param action
   *          the action to set.
   */
  public void setAction(IAction action) {
    this.action = action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Integer getPreferredWidth() {
    Integer w = super.getPreferredWidth();
    if (w == null && getModelDescriptor() instanceof IPropertyDescriptor) {
      w = ((IPropertyDescriptor) getModelDescriptor()).getPreferredWidth();
    }
    return w;
  }

  /**
   * This property allows to control the property alignment in views that
   * support it. This is either a value of the {@code EHorizontalAlignment}
   * enum or its equivalent string representation :
   * <ul>
   * <li>{@code LEFT} for left alignment</li>
   * <li>{@code CENTER} for center alignment</li>
   * <li>{@code RIGHT} for right alignment</li>
   * </ul>
   * <p>
   * Default value is {@code null}, meaning use property type default.
   *
   * @param horizontalAlignment
   *          the horizontalAlignment to set.
   */
  public void setHorizontalAlignment(EHorizontalAlignment horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EHorizontalAlignment getHorizontalAlignment() {
    if (horizontalAlignment != null) {
      return horizontalAlignment;
    }
    if (getModelDescriptor() instanceof INumberPropertyDescriptor) {
      return EHorizontalAlignment.RIGHT;
    }
    return EHorizontalAlignment.LEFT;
  }

  /**
   * Gets the sortability.
   *
   * @return the sortable.
   */
  @Override
  public boolean isSortable() {
    if (sortable != null) {
      return sortable;
    }
    if (getModelDescriptor() instanceof IPropertyDescriptor) {
      return ((IPropertyDescriptor) getModelDescriptor()).isSortable();
    }
    return true;
  }

  /**
   * Configure the sortability of a property view when used to defines a table
   * column for instance. Whenever it is not explicitly set, it falls back to
   * the model property sortability. If no model descriptor is set, defaults to
   * {@code true}.
   *
   * @param sortable
   *          the sortable to set.
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  /**
   * Queries the model property descriptor to determine read-only state.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    boolean readOnly = super.isReadOnly();
    if (!readOnly && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IGateAccessible) {
        return ((IGateAccessible) getModelDescriptor()).isReadOnly();
      }
    }
    return readOnly;
  }

  /**
   * Gets label horizontal position.
   *
   * @return the label horizontal position
   */
  @Override
  public EHorizontalPosition getLabelHorizontalPosition() {
    return labelHorizontalPosition;
  }

  /**
   * Configures the label horizontal position. There are special cases when the default label position has to be
   * overridden. This is either a value of the {@code EHorizontalPosition}
   * enum or its equivalent string representation :
   * <ul>
   * <li>{@code LEFT} for left position</li>
   * <li>{@code RIGHT} for right position</li>
   * </ul>
   * <p>
   * Default value is {@code LEFT}.
   *
   * @param labelHorizontalPosition the label horizontal position
   */
  public void setLabelHorizontalPosition(EHorizontalPosition labelHorizontalPosition) {
    this.labelHorizontalPosition = labelHorizontalPosition;
  }

  /**
   * Gets the restricted list of client types that will display this property view. Defaults to {@code null},
   * which means no restriction. The provided list is not empty, then the actual session client type is matched
   * against each of the restricted client type with either equal or substring or regexp semantic.
   *
   * @return the for client types
   */
  @Override
  public List<String> getForClientTypes() {
    return forClientTypes;
  }

  /**
   * Sets for client types.
   *
   * @param forClientTypes the for client types
   */
  public void setForClientTypes(List<String> forClientTypes) {
    this.forClientTypes = forClientTypes;
  }
}
