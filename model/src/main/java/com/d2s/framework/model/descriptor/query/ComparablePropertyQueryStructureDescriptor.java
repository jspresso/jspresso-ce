/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.model.component.query.ComparablePropertyQueryStructure;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicEnumerationPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicPropertyDescriptor;

/**
 * The component descriptor of comparable property query structures.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComparablePropertyQueryStructureDescriptor extends
    BasicComponentDescriptor<ComparablePropertyQueryStructure> {

  /**
   * <code>EQ</code>.
   */
  public static final String EQ = "EQ";
  /**
   * <code>GT</code>.
   */
  public static final String GT = "GT";
  /**
   * <code>LT</code>.
   */
  public static final String LT = "LT";
  /**
   * <code>LT</code>.
   */
  public static final String LG = "LG";

  /**
   * Constructs a new <code>ComparablePropertyQueryStructureDescriptor</code>
   * instance.
   * 
   * @param propertyDescriptor
   *            the comparable property descriptor to build the query descriptor
   *            for.
   */
  protected ComparablePropertyQueryStructureDescriptor(
      BasicPropertyDescriptor propertyDescriptor) {
    
    super(propertyDescriptor.getName());
    
    BasicEnumerationPropertyDescriptor comparatorPropertyDescriptor = new BasicEnumerationPropertyDescriptor();
    comparatorPropertyDescriptor.setName("comparator");
    comparatorPropertyDescriptor.setMaxLength(new Integer(2));
    comparatorPropertyDescriptor.setEnumerationName("OP");
    Map<String, String> values = new LinkedHashMap<String, String>();
    values.put(EQ, null);
    values.put(GT, null);
    values.put(LT, null);
    values.put(LG, null);
    
    BasicPropertyDescriptor infValuePropertyDescriptor = propertyDescriptor.clone();
    infValuePropertyDescriptor.setName("infValue");

    BasicPropertyDescriptor supValuePropertyDescriptor = propertyDescriptor.clone();
    infValuePropertyDescriptor.setName("supValue");
    
    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(comparatorPropertyDescriptor);
    propertyDescriptors.add(infValuePropertyDescriptor);
    propertyDescriptors.add(supValuePropertyDescriptor);
  }
}
