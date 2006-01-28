/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.text.Format;
import java.text.ParseException;

/**
 * This class is an abstract base class for any connector which deals with
 * string representation of its value. Instances of this class must be provided
 * with a <code>Format</code> using the {@link #setFormat(Format)} method. If
 * not set, the string representation will be used "as is" as the connector
 * value.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTextConnector extends AbstractValueConnector {

  private Format format;

  /**
   * Constructs a new <code>AbstractTextConnector</code> instance.
   * 
   * @param id
   *          the connector identifier.
   */
  public AbstractTextConnector(String id) {
    super(id);
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   * 
   * @see #setFormat(Format)
   */
  @Override
  protected Object getConnecteeValue() {
    if (getFormat() != null) {
      try {
        return getFormat().parseObject(getConnectorValueAsString());
      } catch (ParseException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
    return getConnectorValueAsString();
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   * 
   * @see #setFormat(Format)
   */
  @Override
  public void setConnecteeValue(Object aValue) {
    setConnectorValueAsString(getFormat().format(aValue));
  }

  /**
   * Gets the connector value as a string. This string will be parsed to
   * determine the actual connector value.
   * 
   * @return the string representation of the connector value.
   */
  protected abstract String getConnectorValueAsString();

  /**
   * Sets the connector value as a string. This string has been formatted from
   * the actual connector value using the format set using .
   * 
   * @param connectorValueAsString
   *          the string representation of the connector value.
   */
  protected abstract void setConnectorValueAsString(
      String connectorValueAsString);

  /**
   * Gets the format.
   * 
   * @return the format.
   */
  private Format getFormat() {
    return format;
  }

  /**
   * Sets the format.
   * 
   * @param format
   *          the format to set.
   */
  public void setFormat(Format format) {
    this.format = format;
  }
}
