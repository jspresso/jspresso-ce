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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;

/**
 * Component view descriptors are surely one of the most commonly used view
 * descriptors in Jspresso. It allows to implement advanced form-like views to
 * interact with a single component model. Component properties that are
 * displayed in the view are organized in an invisible grid. Each field
 * component is labelled with the property name it displays and labels can be
 * configured to be displayed aside or above their peer field. Property fields
 * can be configured to span multiple form columns. Component view offer various
 * straightforward customizations, but the most advanced and powerful one is
 * definitely the {@code propertyViewDescriptors} property tat allows to
 * fine-tune each component UI field individually.
 * <p>
 * The description property is used to compute view tooltips and support the
 * following rules :
 * <ol>
 * <li>if the description is a property name of the underlying model, this
 * property will be used to compute the (dynamic) tooltip (depending on the
 * actual model).</li>
 * <li>if the description is not a property name of the underlying model, the
 * the tooltip is considered static and the translation will searched in the
 * application resource bundles.</li>
 * <li>if the description is the empty string (''), the tooltip is de-activated.
 * </li>
 * <li>if the description is not set, then the toHtml property (see toHtml
 * property on entities / components definition) is used as dynamic property.
 * And the toHtml falls back to the toString if not set, which falls back to the
 * 1st string rendered property if not set.</li>
 * </ol>
 * Note that on every case above, HTML is supported. This way, you can have
 * really useful tooltips (event multi-line), in order to detail some synthetic
 * data. Moreover, this rule is available for the form tooltip, but also for
 * each individual field (property view) in the form.
 *
 * @author Vincent Vandenschrick
 */
public class BasicComponentViewDescriptor extends AbstractComponentViewDescriptor
    implements IComponentViewDescriptor {

  private int                           columnCount;
  private EHorizontalPosition           labelsHorizontalPosition;
  private Map<String, Integer>          propertyWidths;
  private Map<String, List<String>>     renderedChildProperties;
  private boolean                       verticallyScrollable;
  private boolean                       widthResizeable;

  /**
   * Constructs a new {@code BasicComponentViewDescriptor} instance.
   */
  public BasicComponentViewDescriptor() {
    super();
    columnCount = 1;
    verticallyScrollable = false;
    widthResizeable = true;
    labelsHorizontalPosition = EHorizontalPosition.LEFT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColumnCount() {
    return columnCount;
  }

  /**
   * Configures the number of columns on this component view. Property fields
   * that are to be displayed in the view are spread across columns and rows
   * following their defined order. Whenever a row does not contain enough empty
   * cells to receive the next field (either because the last column has been
   * reached or the next field has been configured to span multiple columns and
   * there is not enough cells left in the current row to satisfy the span), a
   * new row is created and the next field is added to the first column on the
   * new row.
   * <p>
   * Note that column count and span are defined in fields coordinates (the
   * <i>field</i> including the property UI component + its label). The
   * underlying grid is actually finer since it has to cope with the labels; but
   * this is internal implementation details and Jspresso takes care of it,
   * without the developer having to cope with labels placements.
   * <p>
   * Default value is 1, meaning that all rendered fields will be stacked in a
   * single column.
   *
   * @param columnCount
   *          the columnCount to set.
   */
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  /**
   * This property allows to simply define property spans in the underlying grid
   * without having to extensively define the
   * {@code propertyViewDescriptors} property. It must be configured with a
   * {@code Map} containing only the properties that need to span more than
   * 1 column. The other properties will follow the default span of 1.
   * <p>
   * The {@code Map} is :
   * <ul>
   * <li>keyed by the name of the property</li>
   * <li>valued by the number of columns of the property span</li>
   * </ul>
   * Default value is {@code null}, meaning all property fields have a span
   * of 1.
   *
   * @param propertyWidths
   *          the propertyWidths to set.
   */
  public void setPropertyWidths(Map<String, Object> propertyWidths) {
    this.propertyWidths = new HashMap<>();
    for (Map.Entry<String, Object> propertyWidth : propertyWidths.entrySet()) {
      if (propertyWidth.getValue() instanceof String) {
        this.propertyWidths.put(propertyWidth.getKey(),
            Integer.valueOf((String) propertyWidth.getValue()));
      } else {
        this.propertyWidths.put(propertyWidth.getKey(),
            ((Number) propertyWidth.getValue()).intValue());
      }
    }
  }

  /**
   * Whenever a rendered property is not scalar, this property allows to
   * override which of the referenced component fields should be displayed :
   * <ul>
   * <li>as columns when the rendered property is a collection property</li>
   * <li>as fields when the rendered property is a reference property</li>
   * </ul>
   * The property must be configured with a {@code Map} which is :
   * <ul>
   * <li>keyed by the non-scalar property name</li>
   * <li>valued by the list of the property names to render for the child
   * element(s)</li>
   * </ul>
   * <p>
   * A {@code null} value (default), means that all non-scalar properties
   * will be rendered using default rendered properties as specified in their
   * referenced model descriptor.
   * <p>
   * Please note that this is quite unusual to embed non-scalar properties
   * directly in a component view. Although permitted, you won't have as much
   * flexibility in the content layouting as you would have when using composite
   * views; so the latter is by far recommended.
   *
   * @param renderedChildProperties
   *          the renderedChildProperties to set.
   */
  public void setRenderedChildProperties(
      Map<String, List<String>> renderedChildProperties) {
    this.renderedChildProperties = renderedChildProperties;
  }

  @Override
  protected Integer getPropertyWidth(String propertyName) {
    if (propertyWidths != null) {
      return propertyWidths.get(propertyName);
    }
    return 1;
  }

  @Override
  protected List<String> computeDefaultRenderedChildProperties(String propertyName) {
    List<String> childProperties = null;
    if (renderedChildProperties != null) {
      childProperties = renderedChildProperties.get(propertyName);
    }
    if (childProperties == null) {
      IPropertyDescriptor childPropertyDescriptor = ((IComponentDescriptorProvider<?>) getModelDescriptor())
          .getComponentDescriptor().getPropertyDescriptor(propertyName);
      if (childPropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        return ((ICollectionDescriptor<?>) ((ICollectionPropertyDescriptor<?>) childPropertyDescriptor)
            .getCollectionDescriptor()).getElementDescriptor()
            .getRenderedProperties();
      } else if (childPropertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
        // return the toString property
        return Collections
            .singletonList(((IReferencePropertyDescriptor<?>) childPropertyDescriptor)
                .getReferencedDescriptor().getToStringProperty());
      }
    }
    return childProperties;
  }

  /**
   * Gets the verticallyScrollable.
   *
   * @return the verticallyScrollable.
   */
  @Override
  public boolean isVerticallyScrollable() {
    return verticallyScrollable;
  }

  /**
   * This property allows to define the form vertical scrolling behaviour.
   * Whenever it is set to true, the corresponding UI component will install a
   * vertical scroll bar when the available vertical space is not enough.
   * <p>
   * Default value is {@code false}.
   *
   * @param verticallyScrollable
   *          the verticallyScrollable to set.
   */
  public void setVerticallyScrollable(boolean verticallyScrollable) {
    this.verticallyScrollable = verticallyScrollable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return false;
  }

  /**
   * This property allows to define the form horizontal fill behaviour.
   * Whenever it is set to true, the corresponding UI component will fill all its available horizontal space.
   * <p/>
   * Default value is {@code true}.
   *
   * @param widthResizeable
   *     the widthResizeable to set.
   */
  public void setWidthResizeable(boolean widthResizeable) {
    this.widthResizeable = widthResizeable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWidthResizeable() {
    return widthResizeable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isScrollable() {
    return isVerticallyScrollable() || isHorizontallyScrollable();
  }

  /**
   * Gets label horizontal position.
   *
   * @return the label horizontal position
   */
  @Override
  public EHorizontalPosition getLabelsHorizontalPosition() {
    return labelsHorizontalPosition;
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
   * @param labelsHorizontalPosition the label horizontal position
   */
  public void setLabelsHorizontalPosition(EHorizontalPosition labelsHorizontalPosition) {
    this.labelsHorizontalPosition = labelsHorizontalPosition;
  }
}
