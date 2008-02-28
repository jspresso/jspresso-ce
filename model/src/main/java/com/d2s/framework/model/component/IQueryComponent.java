/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.component;

import java.util.List;
import java.util.Map;

/**
 * A simple adapter to wrap a component used as selection criteria and a list of
 * components. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IQueryComponent extends Map<String, Object> {

  /**
   * "queriedComponents" string constant.
   */
  String QUERIED_COMPONENTS = "queriedComponents";

  /**
   * Gets the list of components result of the query.
   * 
   * @return the list of components result of the query.
   */
  List<? extends IComponent> getQueriedComponents();

  /**
   * Sets the list of components result of the query.
   * 
   * @param queriedComponents
   *            the list of components result of the query.
   */
  void setQueriedComponents(List<? extends IComponent> queriedComponents);
  
  /**
   * Gets the contract of the components to query.
   * 
   * @return the contract of the components to query.
   */
  Class<?> getQueryContract();
}
