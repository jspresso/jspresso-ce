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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.model.gate.BooleanPropertyModelGate;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.gate.IGate;

/**
 * The component descriptor of comparable property query structures.
 *
 * @internal
 * @author Vincent Vandenschrick
 */
public class ComparableQueryStructureDescriptor extends
    BasicReferencePropertyDescriptor<ComparableQueryStructure> {

  /**
   * {@code LG} between.
   */
  public static final String BE         = "BE";

  /**
   * {@code COMPARATOR} comparator.
   */
  public static final String COMPARATOR = "comparator";

  /**
   * {@code EQ} equals.
   */
  public static final String EQ         = "EQ";

  /**
   * {@code GT} greater than or equals.
   */
  public static final String GE         = "GE";

  /**
   * {@code GT} greater than.
   */
  public static final String GT         = "GT";

  /**
   * {@code INF_VALUE} infValue.
   */
  public static final String INF_VALUE  = "infValue";

  /**
   * {@code LT} lower than or equals.
   */
  public static final String LE         = "LE";

  /**
   * {@code LT} lower than.
   */
  public static final String LT         = "LT";

  /**
   * {@code NU} is null.
   */
  public static final String NU         = "NU";

  /**
   * {@code NN} is not null.
   */
  public static final String NN         = "NN";

  /**
   * {@code SUP_VALUE} supValue.
   */
  public static final String SUP_VALUE = "supValue";

  /**
   * {@code INF_VALUE_USED} infValueUsed.
   */
  public static final String INF_VALUE_USED = "infValueUsed";
  /**
   * {@code SUP_VALUE_USED} supValueUsed.
   */
  public static final String SUP_VALUE_USED = "supValueUsed";

  /**
   * Constructs a new {@code ComparablePropertyQueryStructureDescriptor}
   * instance.
   *
   * @param propertyDescriptor
   *     the comparable property descriptor to build the query descriptor          for.
   * @param accessorFactory
   *     the accessor factory
   */
  public ComparableQueryStructureDescriptor(
      BasicPropertyDescriptor propertyDescriptor, IAccessorFactory accessorFactory) {

    super();
    setName(propertyDescriptor.getName());
    setI18nNameKey(propertyDescriptor.getI18nNameKey());
    setDescription(propertyDescriptor.getDescription());

    BasicComponentDescriptor<ComparableQueryStructure> refDescriptor = new BasicComponentDescriptor<>(
        ComparableQueryStructure.class.getName());

    BasicEnumerationPropertyDescriptor comparatorPropertyDescriptor = new BasicEnumerationPropertyDescriptor();
    comparatorPropertyDescriptor.setName(COMPARATOR);
    if (getI18nNameKey() != null) {
      comparatorPropertyDescriptor.setI18nNameKey(getI18nNameKey());
    } else {
      comparatorPropertyDescriptor.setI18nNameKey(getName());
    }
    comparatorPropertyDescriptor.setEnumerationName("OP");
    Map<String, String> values = new LinkedHashMap<>();
    values.put(EQ, null);
    values.put(GT, null);
    values.put(GE, null);
    values.put(LT, null);
    values.put(LE, null);
    values.put(BE, null);
    values.put(NU, null);
    values.put(NN, null);
    comparatorPropertyDescriptor.setValuesAndIconImageUrls(values);
    comparatorPropertyDescriptor.setGrantedRoles(propertyDescriptor
        .getGrantedRoles());
    // comparatorPropertyDescriptor.setMandatory(true);

    BasicPropertyDescriptor infValuePropertyDescriptor = propertyDescriptor
        .clone();
    infValuePropertyDescriptor.setName(INF_VALUE);
    infValuePropertyDescriptor.setI18nNameKey("");
    BooleanPropertyModelGate infValueGate = new BooleanPropertyModelGate();
    infValueGate.setAccessorFactory(accessorFactory);
    infValueGate.setPropertyName(INF_VALUE_USED);
    infValuePropertyDescriptor.setWritabilityGates(Arrays.asList((IGate)infValueGate));

    BasicPropertyDescriptor supValuePropertyDescriptor = propertyDescriptor
        .clone();
    supValuePropertyDescriptor.setName(SUP_VALUE);
    supValuePropertyDescriptor.setI18nNameKey("");
    BooleanPropertyModelGate supValueGate = new BooleanPropertyModelGate();
    supValueGate.setAccessorFactory(accessorFactory);
    supValueGate.setPropertyName(SUP_VALUE_USED);
    supValuePropertyDescriptor.setWritabilityGates(Arrays.asList((IGate) supValueGate));

    BasicStringPropertyDescriptor toStringPropertyDescriptor = new BasicStringPropertyDescriptor();
    toStringPropertyDescriptor.setName(IComponentDescriptor.TO_STRING);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>();
    propertyDescriptors.add(comparatorPropertyDescriptor);
    propertyDescriptors.add(infValuePropertyDescriptor);
    propertyDescriptors.add(supValuePropertyDescriptor);
    propertyDescriptors.add(toStringPropertyDescriptor);

    refDescriptor.setPropertyDescriptors(propertyDescriptors);
    refDescriptor.setRenderedProperties(Arrays.asList(COMPARATOR, INF_VALUE, SUP_VALUE));
    refDescriptor.setToStringProperty(IComponentDescriptor.TO_STRING);

    setReferencedDescriptor(refDescriptor);
    setComputed(propertyDescriptor.isComputed());
    if (isComputed()) {
      if (propertyDescriptor.isFilterOnly()) {
        setFilterOnly(true);
      } else if (propertyDescriptor.isModifiable()) {
        setDelegateWritable(true);
      }
    }
    setSqlName(propertyDescriptor.getSqlName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicReferencePropertyDescriptor<ComparableQueryStructure> createQueryDescriptor() {
    return this;
  }
}
