/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;

/**
 * The component descriptor of comparable property query structures.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComparableQueryStructureDescriptor extends
    BasicReferencePropertyDescriptor<ComparableQueryStructure> {

  /**
   * <code>EQ</code> equals.
   */
  public static final String EQ         = "EQ";

  /**
   * <code>GT</code> greater than.
   */
  public static final String GT         = "GT";

  /**
   * <code>GT</code> greater than or equals.
   */
  public static final String GE         = "GE";

  /**
   * <code>LT</code> lower than.
   */
  public static final String LT         = "LT";

  /**
   * <code>LT</code> lower than or equals.
   */
  public static final String LE         = "LE";

  /**
   * <code>LG</code> between.
   */
  public static final String BE         = "BE";

  /**
   * <code>COMPARATOR</code> comparator.
   */
  public static final String COMPARATOR = "comparator";

  /**
   * <code>INF_VALUE</code> infValue.
   */
  public static final String INF_VALUE  = "infValue";

  /**
   * <code>SUP_VALUE</code> supValue.
   */
  public static final String SUP_VALUE  = "supValue";

  /**
   * Constructs a new <code>ComparablePropertyQueryStructureDescriptor</code>
   * instance.
   * 
   * @param propertyDescriptor
   *            the comparable property descriptor to build the query descriptor
   *            for.
   */
  public ComparableQueryStructureDescriptor(
      BasicPropertyDescriptor propertyDescriptor) {

    super();
    setName(propertyDescriptor.getName());

    BasicComponentDescriptor<ComparableQueryStructure> refDescriptor = new BasicComponentDescriptor<ComparableQueryStructure>(
        ComparableQueryStructure.class.getName());

    BasicEnumerationPropertyDescriptor comparatorPropertyDescriptor = new BasicEnumerationPropertyDescriptor();
    comparatorPropertyDescriptor.setName(COMPARATOR);
    comparatorPropertyDescriptor.setMaxLength(new Integer(2));
    comparatorPropertyDescriptor.setEnumerationName("OP");
    Map<String, String> values = new LinkedHashMap<String, String>();
    values.put(EQ, null);
    values.put(GT, null);
    values.put(GE, null);
    values.put(LT, null);
    values.put(LE, null);
    values.put(BE, null);
    comparatorPropertyDescriptor.setValuesAndIconImageUrls(values);
    comparatorPropertyDescriptor.setGrantedRoles(propertyDescriptor
        .getGrantedRoles());
    // comparatorPropertyDescriptor.setMandatory(true);

    BasicPropertyDescriptor infValuePropertyDescriptor = propertyDescriptor
        .clone();
    infValuePropertyDescriptor.setName(INF_VALUE);
    infValuePropertyDescriptor.setI18nNameKey("");

    BasicPropertyDescriptor supValuePropertyDescriptor = propertyDescriptor
        .clone();
    supValuePropertyDescriptor.setName(SUP_VALUE);
    supValuePropertyDescriptor.setI18nNameKey("");

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(comparatorPropertyDescriptor);
    propertyDescriptors.add(infValuePropertyDescriptor);
    propertyDescriptors.add(supValuePropertyDescriptor);

    refDescriptor.setPropertyDescriptors(propertyDescriptors);

    setReferencedDescriptor(refDescriptor);
  }
}
