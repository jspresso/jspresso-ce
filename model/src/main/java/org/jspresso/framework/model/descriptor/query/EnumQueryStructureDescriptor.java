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
package org.jspresso.framework.model.descriptor.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.AbstractEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicSetDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;

/**
 * A query structure used to implement enumeration disjunctions in filters.
 *
 * @author Vincent Vandenschrick
 */
public class EnumQueryStructureDescriptor extends
    BasicReferencePropertyDescriptor<EnumQueryStructure> {

  /**
   * {@code TO_STRING} value.
   */
  public static final String             TO_STRING          = "toString";
  /**
   * {@code SELECTED} value.
   */
  public static final String             SELECTED           = "selected";
  /**
   * {@code VALUE} value.
   */
  public static final String             VALUE              = "value";
  /**
   * {@code I18N_VALUE} value.
   */
  public static final String             I18N_VALUE         = "i18nValue";

  /**
   * {@code ENUMERATION_VALUES} value.
   */
  public static final String             ENUMERATION_VALUES = "enumerationValues";

  /**
   * {@code ENUMERATION_VALUES} value.
   */
  public static final String             SELECTED_ENUMERATION_VALUES = "selectedEnumerationValues";

  private final IEnumerationPropertyDescriptor sourceDescriptor;

  /**
   * Constructs a new {@code EnumerationQueryStructureDescriptor} instance.
   *
   * @param propertyDescriptor
   *          the actual enumeration property descriptor to wrap.
   */
  public EnumQueryStructureDescriptor(
      AbstractEnumerationPropertyDescriptor propertyDescriptor) {

    super();

    this.sourceDescriptor = propertyDescriptor;
    setName(propertyDescriptor.getName());
    setI18nNameKey(propertyDescriptor.getI18nNameKey());
    setDescription(propertyDescriptor.getDescription());

    BasicComponentDescriptor<EnumQueryStructure> refDescriptor = new BasicComponentDescriptor<>(
        EnumQueryStructure.class.getName());

    BasicComponentDescriptor<EnumValueQueryStructure> elementDescriptor = new BasicComponentDescriptor<>(
        EnumValueQueryStructure.class.getName());

    BasicBooleanPropertyDescriptor selectedPropertyDescriptor = new BasicBooleanPropertyDescriptor();
    selectedPropertyDescriptor.setName(SELECTED);
    selectedPropertyDescriptor.setI18nNameKey("enumValue.selected");
    selectedPropertyDescriptor.setPreferredWidth(30);
    BasicPropertyDescriptor valuePropertyDescriptor = propertyDescriptor
        .clone();
    valuePropertyDescriptor.setName(VALUE);
    if (propertyDescriptor.getI18nNameKey() != null) {
      valuePropertyDescriptor.setI18nNameKey(propertyDescriptor
          .getI18nNameKey());
    } else {
      valuePropertyDescriptor.setI18nNameKey(propertyDescriptor.getName());
    }
    valuePropertyDescriptor.setReadOnly(true);
    BasicPropertyDescriptor i18nValuePropertyDescriptor = new BasicStringPropertyDescriptor();
    i18nValuePropertyDescriptor.setName(I18N_VALUE);
    i18nValuePropertyDescriptor.setReadOnly(true);

    List<IPropertyDescriptor> enumValuePropertyDescriptors = new ArrayList<>();
    enumValuePropertyDescriptors.add(selectedPropertyDescriptor);
    enumValuePropertyDescriptors.add(valuePropertyDescriptor);
    enumValuePropertyDescriptors.add(i18nValuePropertyDescriptor);
    elementDescriptor.setPropertyDescriptors(enumValuePropertyDescriptors);

    elementDescriptor.setRenderedProperties(Arrays.asList(SELECTED, VALUE));
    elementDescriptor.setToStringProperty(I18N_VALUE);

    BasicSetDescriptor<EnumValueQueryStructure> enumValuesReferencedDescriptor;
    enumValuesReferencedDescriptor = new BasicSetDescriptor<>();
    enumValuesReferencedDescriptor.setElementDescriptor(elementDescriptor);

    BasicCollectionPropertyDescriptor<EnumValueQueryStructure> enumerationValuesPropertyDescriptor;
    enumerationValuesPropertyDescriptor = new BasicCollectionPropertyDescriptor<>();
    enumerationValuesPropertyDescriptor.setName(ENUMERATION_VALUES);
    enumerationValuesPropertyDescriptor
        .setReferencedDescriptor(enumValuesReferencedDescriptor);

    BasicCollectionPropertyDescriptor<EnumValueQueryStructure> selectedEnumerationValuesPropertyDescriptor;
    selectedEnumerationValuesPropertyDescriptor = new BasicCollectionPropertyDescriptor<>();
    selectedEnumerationValuesPropertyDescriptor.setName(SELECTED_ENUMERATION_VALUES);
    selectedEnumerationValuesPropertyDescriptor
        .setReferencedDescriptor(enumValuesReferencedDescriptor);

    BasicStringPropertyDescriptor toStringPropertyDescriptor = new BasicStringPropertyDescriptor();
    toStringPropertyDescriptor.setName(TO_STRING);

    List<IPropertyDescriptor> enumPropertyDescriptors = new ArrayList<>();
    enumPropertyDescriptors.add(enumerationValuesPropertyDescriptor);
    enumPropertyDescriptors.add(selectedEnumerationValuesPropertyDescriptor);
    enumPropertyDescriptors.add(toStringPropertyDescriptor);
    refDescriptor.setPropertyDescriptors(enumPropertyDescriptors);
    refDescriptor.setToStringProperty(TO_STRING);

    setReferencedDescriptor(refDescriptor);
    setComputed(propertyDescriptor.isComputed());
    setSqlName(propertyDescriptor.getSqlName());
    setMandatory(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicReferencePropertyDescriptor<EnumQueryStructure> createQueryDescriptor() {
    return this;
  }

  /**
   * Gets the sourceDescriptor.
   *
   * @return the sourceDescriptor.
   */
  public IEnumerationPropertyDescriptor getSourceDescriptor() {
    return sourceDescriptor;
  }
}
