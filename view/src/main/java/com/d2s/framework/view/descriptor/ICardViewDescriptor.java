/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.Map;

/**
 * This public interface is implemented by "Card" view descriptors. A typical
 * implementation of the described view could be a swing JPanel using a card
 * layout.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICardViewDescriptor extends ICompositeViewDescriptor {

  /**
   * <code>DEFAULT_CARD</code>.
   */
  String DEFAULT_CARD = "DEFAULT_CARD";

  /**
   * Gets the children views registered in the card view and indexed by their
   * card names.
   * 
   * @return the children view descriptors.
   */
  Map<String, IViewDescriptor> getChildViewDescriptors();

  /**
   * Gets the card name to use to present the model.
   * 
   * @param model
   *          the model to back the view.
   * @return the card name to look up the view.
   */
  String getCardNameForModel(Object model);
}
