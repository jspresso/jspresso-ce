/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicQueryComponentDescriptor;
import com.d2s.framework.view.descriptor.IQueryViewDescriptorFactory;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for query view descriptor factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision: 1091 $
 * @author Vincent Vandenschrick
 */
public class BasicQueryViewDescriptorFactory implements IQueryViewDescriptorFactory {

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptor<Object> queryComponentDescriptor) {
    BasicQueryComponentDescriptor actualModelDescriptor = new BasicQueryComponentDescriptor(queryComponentDescriptor);
    BasicComponentViewDescriptor queryViewDescriptor = new BasicComponentViewDescriptor();
    queryViewDescriptor
        .setModelDescriptor(actualModelDescriptor);
    queryViewDescriptor
        .setName("filter");
    queryViewDescriptor
        .setBorderType(IViewDescriptor.TITLED);
    Map<String, Object> propertyWidths = new HashMap<String, Object>();
    for (String queriableProperty : queryComponentDescriptor
        .getQueryableProperties()) {
      // To preserve col spans for query structures.
      propertyWidths.put(queriableProperty, new Integer(3));
    }
    queryViewDescriptor
        .setPropertyWidths(propertyWidths);
    queryViewDescriptor
        .setColumnCount(6);
    return queryViewDescriptor;
  }
}
