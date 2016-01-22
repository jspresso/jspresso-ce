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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * Abstract class for component view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentViewDescriptor extends BasicViewDescriptor implements IComponentViewDescriptor {

  private ELabelPosition                  labelsPosition;
  private List<String>                    renderedProperties;
  private List<IPropertyViewDescriptor>   propertyViewDescriptors;
  private AbstractComponentViewDescriptor readOnlyClone;
  private String                          labelFont;
  private String                          valueFont;

  /**
   * Instantiates a new Abstract component view descriptor.
   */
  public AbstractComponentViewDescriptor() {
    labelsPosition = ELabelPosition.ASIDE;
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
   * If you need to override the behaviour, override {@link #getPropertyViewDescriptors(boolean)}.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public final List<IPropertyViewDescriptor> getPropertyViewDescriptors() {
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
      declaredPropertyViewDescriptors = new ArrayList<>();
      for (String renderedProperty : viewRenderedProperties) {
        BasicPropertyViewDescriptor propertyViewDescriptor = new BasicPropertyViewDescriptor();
        propertyViewDescriptor.setName(renderedProperty);
        propertyViewDescriptor.setWidth(getPropertyWidth(renderedProperty));
        propertyViewDescriptor.setRenderedChildProperties(computeDefaultRenderedChildProperties(renderedProperty));
        propertyViewDescriptor.setModelDescriptor(componentDescriptor.getPropertyDescriptor(renderedProperty));
        propertyViewDescriptor.setLabelFont(getLabelFont());
        propertyViewDescriptor.setFont(getValueFont());
        declaredPropertyViewDescriptors.add(propertyViewDescriptor);
      }
    } else {
      for (IPropertyViewDescriptor pvd : declaredPropertyViewDescriptors) {
        if (pvd.getModelDescriptor() == null && pvd instanceof BasicPropertyViewDescriptor) {
          ((BasicPropertyViewDescriptor) pvd).setModelDescriptor(componentDescriptor.getPropertyDescriptor(
              pvd.getName()));
          if (pvd.getLabelFont() == null) {
            ((BasicPropertyViewDescriptor) pvd).setLabelFont(getLabelFont());
          }
          if (pvd.getFont() == null) {
            ((BasicPropertyViewDescriptor) pvd).setFont(getValueFont());
          }
        }
      }
    }
    List<IPropertyViewDescriptor> actualPropertyViewDescriptors;
    if (explodeComponentReferences) {
      actualPropertyViewDescriptors = new ArrayList<>();
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
   *     the labelsPosition to set.
   */
  public void setLabelsPosition(ELabelPosition labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * This property allows for configuring the fields of the component view in a
   * very customizable manner, thus overriding the model descriptor defaults.
   * Each property view descriptor contained in the list describes a form field
   * that will be rendered in the UI accordingly.
   * <p/>
   * For instance, a writable property can be made specifically read-only on
   * this component view by specifying its property view descriptor read-only.
   * In that case, the model remains untouched and only the view is impacted.
   * <p/>
   * Following the same scheme, you can assign a list of writability gates on a
   * field to introduce dynamic field editability on the view without modifying
   * the model.
   * <p/>
   * A last, yet important, example of column view descriptor usage is the
   * role-based field set configuration. Whenever you want a field to be
   * available only for certain user roles (profiles), you can configure a field
   * property view descriptor with a list of granted roles. If the user doesn't
   * have the field(s)required role, the forbidden field(s) simply won't be
   * displayed. This allows for high authorization-based versatility.
   * <p/>
   * There are many other usages of defining field property view descriptors
   * (like individual labels color and font), all of them being linked to
   * customizing the form fields without impacting the model.
   *
   * @param propertyViewDescriptors
   *     the propertyViewDescriptors to set.
   */
  public void setPropertyViewDescriptors(List<IPropertyViewDescriptor> propertyViewDescriptors) {
    this.propertyViewDescriptors = propertyViewDescriptors;
  }

  /**
   * Gets property width.
   *
   * @param propertyName
   *     the property name
   * @return the property width
   */
  protected abstract Integer getPropertyWidth(String propertyName);

  /**
   * Compute default rendered child properties.
   *
   * @param propertyName
   *     the property name
   * @return the list
   */
  protected abstract List<String> computeDefaultRenderedChildProperties(String propertyName);

  /**
   * This is somehow a shortcut to using the
   * {@code propertyViewDescriptors} property. Instead of providing a
   * full-blown list of property view descriptors to configure the component
   * view fields, you just pass-in a list of property names. view fields are
   * then created from this list, keeping model defaults for all fields
   * characteristics.
   * <p/>
   * Whenever the property value is {@code null} (default), the fields list
   * is determined from the component descriptor {@code renderedProperties}
   * property.
   *
   * @param renderedProperties
   *     the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  /**
   * Gets the renderedProperties.
   *
   * @return the renderedProperties.
   */
  private List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      renderedProperties = ((IComponentDescriptorProvider<?>) getModelDescriptor()).getComponentDescriptor()
                                                                                   .getRenderedProperties();
    }
    return renderedProperties;
  }

  /**
   * Queries the model property descriptor to determine read-only state.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    boolean readOnly = super.isReadOnly();
    if (!readOnly && getModelDescriptor() != null) {
      if (getModelDescriptor() instanceof IComponentDescriptorProvider<?>) {
        return ((IComponentDescriptorProvider<?>) getModelDescriptor()).getComponentDescriptor().isReadOnly();
      }
    }
    return readOnly;
  }

  /**
   * Clone the component view in read only mode.
   *
   * @return the read-only component view descriptor.
   */
  protected synchronized AbstractComponentViewDescriptor cloneReadOnly() {
    if (readOnlyClone == null && getModelDescriptor() != null) {
      readOnlyClone = (AbstractComponentViewDescriptor) clone();
      List<IPropertyViewDescriptor> readOnlyDescriptors = new ArrayList<>();
      for (IPropertyViewDescriptor descriptor : getPropertyViewDescriptors(false)) {
        BasicPropertyViewDescriptor readOnlyDescriptor = (BasicPropertyViewDescriptor) ((BasicPropertyViewDescriptor)
            descriptor)
            .clone();
        readOnlyDescriptor.setReadOnly(true);
        readOnlyDescriptors.add(readOnlyDescriptor);
      }
      readOnlyClone.setPropertyViewDescriptors(readOnlyDescriptors);
    }
    return readOnlyClone;
  }

  /**
   * Gets label font.
   *
   * @return the label font
   */
  @Override
  public String getLabelFont() {
    return labelFont;
  }

  /**
   * This property defines the font of the property labels. It might differ from
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
   * <p/>
   * This property can be overridden on a field basis using an explicit property view definition.
   * <p/>
   * Default value is {@code null}, meaning use default component font.
   *
   * @param labelFont
   *     the labelFont to set.
   */
  public void setLabelFont(String labelFont) {
    this.labelFont = labelFont;
  }

  /**
   * Gets value font.
   *
   * @return the value font
   */
  @Override
  public String getValueFont() {
    return valueFont;
  }

  /**
   * This property defines the font of the property values. The font must be string encoded using the pattern
   * <b>&quot;[name];[style];[size]&quot;</b> :
   * <ul>
   * <li><b>[name]</b> is the name of the font, e.g. <i>arial</i>.</li>
   * <li><b>[style]</b> is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC
   * combined with the '|' character, e.g. <i>BOLD|ITALIC</i>.</li>
   * <li><b>[size]</b> is the size of the font, e.g. <i>10</i>.</li>
   * </ul>
   * Any of the above pattern section can be left empty, thus falling back to
   * the component default.
   * <p/>
   * This property can be overridden on a field basis using an explicit property view definition.
   * <p/>
   * Default value is {@code null}, meaning use default component font.
   *
   * @param valueFont
   *     the valueFont to set.
   */
  public void setValueFont(String valueFont) {
    this.valueFont = valueFont;
  }
}
