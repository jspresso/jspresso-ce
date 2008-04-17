/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding;

/**
 * This public interfacehas to be implemented by classes wishing to keep track
 * of of connectors modifications. This is typically used when binding two
 * connectors together (in a MVC relationship for instance) where the 2
 * connectors will listen for eachother value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorValueChangeListener {

  /**
   * This method is triggered whenever a connector detects that its peer value
   * changed. Ideally this method will only fire when <code>oldValue</code>
   * differs from <code>newValue</code>.
   * 
   * @param evt
   *            The event representing the change. This event will have :
   *            <li><code>source</code> set to the connector which initiated
   *            the event.
   *            <li><code>oldValue</code> set to the old value of the source
   *            connector.
   *            <li><code>newValue</code> set to the new value of the source
   *            connector.
   */
  void connectorValueChange(ConnectorValueChangeEvent evt);

}
