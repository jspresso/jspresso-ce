/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.projection;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicObjectPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicStringPropertyDescriptor;

/**
 * The model descriptor of projection objects.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ProjectionDescriptor extends BasicComponentDescriptor {

  /**
   * Constructs a new <code>ProjectionDescriptor</code> instance.
   */
  public ProjectionDescriptor() {
    BasicReferencePropertyDescriptor parentDescriptor = new BasicReferencePropertyDescriptor();
    parentDescriptor.setName("parent");
    parentDescriptor.setReferencedDescriptor(this);

    BasicCollectionDescriptor projectionListDescriptor = new BasicCollectionDescriptor();
    projectionListDescriptor.setCollectionInterface(List.class);
    projectionListDescriptor.setElementDescriptor(this);

    BasicCollectionPropertyDescriptor childrenDescriptor = new BasicCollectionPropertyDescriptor();
    childrenDescriptor.setReferencedDescriptor(projectionListDescriptor);
    childrenDescriptor.setName("children");

    BasicObjectPropertyDescriptor projectedObjectDescriptor = new BasicObjectPropertyDescriptor();
    projectedObjectDescriptor.setName("projectedObject");

    BasicCollectionPropertyDescriptor projectedObjectsDescriptor = new BasicCollectionPropertyDescriptor();
    projectedObjectsDescriptor.setName("projectedObjects");

    BasicStringPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName("name");

    BasicStringPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName("description");

    parentDescriptor.setReverseRelationEnd(childrenDescriptor);
    childrenDescriptor.setReverseRelationEnd(parentDescriptor);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(projectedObjectDescriptor);
    propertyDescriptors.add(projectedObjectsDescriptor);
    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);
    propertyDescriptors.add(parentDescriptor);
    propertyDescriptors.add(childrenDescriptor);
    setPropertyDescriptors(propertyDescriptors);
  }
}
