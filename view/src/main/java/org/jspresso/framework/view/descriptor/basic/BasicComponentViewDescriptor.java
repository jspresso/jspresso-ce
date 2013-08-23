/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentViewDescriptor extends BasicViewDescriptor implements
    IComponentViewDescriptor {

  private int                           columnCount;
  private ELabelPosition                labelsPosition;
  private EHorizontalPosition           labelsHorizontalPosition;
  private List<IPropertyViewDescriptor> propertyViewDescriptors;
  private Map<String, Integer>          propertyWidths;
  private Map<String, List<String>>     renderedChildProperties;
  private List<String>                  renderedProperties;
  private boolean                       verticallyScrollable;

  /**
   * Constructs a new {@code BasicComponentViewDescriptor} instance.
   */
  public BasicComponentViewDescriptor() {
    columnCount = 1;
    labelsPosition = ELabelPosition.ASIDE;
    verticallyScrollable = false;
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
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    Icon icon = super.getIcon();
    if (icon == null) {
      icon = ((IComponentDescriptorProvider<?>) getModelDescriptor()).getComponentDescriptor().getIcon();
      setIcon(icon);
    }
    return icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ELabelPosition getLabelsPosition() {
    return labelsPosition;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IPropertyViewDescriptor> getPropertyViewDescriptors() {
    return getPropertyViewDescriptors(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IPropertyViewDescriptor> getPropertyViewDescriptors(boolean explodeComponentReferences) {
    IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) getModelDescriptor())
        .getComponentDescriptor();
    List<IPropertyViewDescriptor> declaredPropertyViewDescriptors = propertyViewDescriptors;
    if (declaredPropertyViewDescriptors == null) {
      List<String> viewRenderedProperties = getRenderedProperties();
      declaredPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (String renderedProperty : viewRenderedProperties) {
        BasicPropertyViewDescriptor propertyViewDescriptor = new BasicPropertyViewDescriptor();
        propertyViewDescriptor.setName(renderedProperty);
        propertyViewDescriptor.setWidth(getPropertyWidth(renderedProperty));
        propertyViewDescriptor.setRenderedChildProperties(computeDefaultRenderedChildProperties(renderedProperty));
        propertyViewDescriptor.setModelDescriptor(componentDescriptor.getPropertyDescriptor(renderedProperty));
        declaredPropertyViewDescriptors.add(propertyViewDescriptor);
      }
    }
    List<IPropertyViewDescriptor> actualPropertyViewDescriptors;
    if (explodeComponentReferences) {
      actualPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (IPropertyViewDescriptor propertyViewDescriptor : declaredPropertyViewDescriptors) {
        List<IPropertyViewDescriptor> exploded = PropertyViewDescriptorHelper.explodeComponentReferences(
            propertyViewDescriptor, (IComponentDescriptorProvider<?>) getModelDescriptor());
        if (exploded.size() > 0) {
          if (propertyViewDescriptor.getWidth() != null && propertyViewDescriptor.getWidth() > exploded.size()) {
            ((BasicPropertyViewDescriptor) exploded.get(exploded.size() - 1)).setWidth(
                propertyViewDescriptor.getWidth() - exploded.size() + 1);
          }
          actualPropertyViewDescriptors.addAll(exploded);
        }
      }
    } else {
      actualPropertyViewDescriptors = declaredPropertyViewDescriptors;
    }
    return actualPropertyViewDescriptors;
  }

  /**
   * Gets the readabilityGates.
   *
   * @return the readabilityGates.
   */
  @Override
  public Collection<IGate> getReadabilityGates() {
    Collection<IGate> gates = super.getReadabilityGates();
    if (gates == null && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IGateAccessible) {
        return ((IGateAccessible) getModelDescriptor()).getReadabilityGates();
      }
    }
    return gates;
  }

  /**
   * Gets the writabilityGates.
   *
   * @return the writabilityGates.
   */
  @Override
  public Collection<IGate> getWritabilityGates() {
    Collection<IGate> gates = super.getWritabilityGates();
    if (gates == null && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IGateAccessible) {
        return ((IGateAccessible) getModelDescriptor()).getWritabilityGates();
      }
    }
    return gates;
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
   * Instructs Jspresso where to place the fields label. This is either a value
   * of the {@code ELabelPosition} enum or its equivalent string
   * representation :
   * <ul>
   * <li>{@code ABOVE} for placing each field label above the property UI
   * component</li>
   * <li>{@code ASIDE} for placing each field label aside the property UI
   * component</li>
   * <li>{@code NONE} for completely disabling fields labelling on the view
   * </li>
   * </ul>
   * Default value is {@code ELabelPosition.ASIDE}, i.e. fields label next
   * to the property UI component.
   *
   * @param labelsPosition
   *          the labelsPosition to set.
   */
  public void setLabelsPosition(ELabelPosition labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * This property allows for configuring the fields of the component view in a
   * very customizable manner, thus overriding the model descriptor defaults.
   * Each property view descriptor contained in the list describes a form field
   * that will be rendered in the UI accordingly.
   * <p>
   * For instance, a writable property can be made specifically read-only on
   * this component view by specifying its property view descriptor read-only.
   * In that case, the model remains untouched and only the view is impacted.
   * <p>
   * Following the same scheme, you can assign a list of writability gates on a
   * field to introduce dynamic field editability on the view without modifying
   * the model.
   * <p>
   * A last, yet important, example of column view descriptor usage is the
   * role-based field set configuration. Whenever you want a field to be
   * available only for certain user roles (profiles), you can configure a field
   * property view descriptor with a list of granted roles. If the user doesn't
   * have the field(s)required role, the forbidden field(s) simply won't be
   * displayed. This allows for high authorization-based versatility.
   * <p>
   * There are many other usages of defining field property view descriptors
   * (like individual labels color and font), all of them being linked to
   * customizing the form fields without impacting the model.
   * 
   * @param propertyViewDescriptors
   *          the propertyViewDescriptors to set.
   */
  public void setPropertyViewDescriptors(
      List<IPropertyViewDescriptor> propertyViewDescriptors) {
    this.propertyViewDescriptors = propertyViewDescriptors;
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
    this.propertyWidths = new HashMap<String, Integer>();
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

  /**
   * This is somehow a shortcut to using the
   * {@code propertyViewDescriptors} property. Instead of providing a
   * full-blown list of property view descriptors to configure the component
   * view fields, you just pass-in a list of property names. view fields are
   * then created from this list, keeping model defaults for all fields
   * characteristics.
   * <p>
   * Whenever the property value is {@code null} (default), the fields list
   * is determined from the component descriptor {@code renderedProperties}
   * property.
   *
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  private Integer getPropertyWidth(String propertyName) {
    if (propertyWidths != null) {
      return propertyWidths.get(propertyName);
    }
    return 1;
  }

  private List<String> computeDefaultRenderedChildProperties(String propertyName) {
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
   * Gets the renderedProperties.
   * 
   * @return the renderedProperties.
   */
  private List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      renderedProperties = ((IComponentDescriptorProvider<?>) getModelDescriptor())
          .getComponentDescriptor().getRenderedProperties();
    }
    return renderedProperties;
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
      if (getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
        return ((IComponentDescriptorProvider<?>) getModelDescriptor())
            .getComponentDescriptor().isReadOnly();
      }
    }
    return readOnly;
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
