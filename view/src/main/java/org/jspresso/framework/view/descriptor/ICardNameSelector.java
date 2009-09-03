/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

/**
 * This interface is implemented by strategies determining a card name based on
 * a model instance.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICardNameSelector {

  /**
   * Gets the card name to use to present the model.
   * 
   * @param model
   *          the model to back the view.
   * @return the card name to look up the view.
   */
  String getCardNameForModel(Object model);

}
