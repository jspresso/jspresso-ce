/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * This class is a card view descriptor that displays entities. Choice of the
 * card to display is based on the model entity contract.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * Uses the entity contract name as card name.
   * <p>
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof IEntity) {
      return ((IEntity) model).getContract().getName();
    }
    return null;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *            the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    Map<String, IViewDescriptor> classCardMapping = new LinkedHashMap<String, IViewDescriptor>();
    for (IViewDescriptor entityViewDescriptor : viewDescriptors) {
      classCardMapping.put(((IComponentDescriptor<?>) entityViewDescriptor
          .getModelDescriptor()).getComponentContract().getName(),
          entityViewDescriptor);
    }
    setCardViewDescriptors(classCardMapping);
  }

}
