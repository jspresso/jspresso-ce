/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.format;

import java.text.Format;
import java.text.ParseException;

/**
 * An adapter for the <code>Format</code> jdk objects.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FormatAdapter implements IFormatter {

  private Format format;

  /**
   * Constructs a new <code>FormatAdapter</code> instance.
   * 
   * @param format
   *            the format to adapt.
   */
  public FormatAdapter(Format format) {
    super();
    this.format = format;
  }

  /**
   * {@inheritDoc}
   */
  public String format(Object value) {
    if (value != null) {
      return format.format(value);
    }
    return null;
  }

  /**
   * Gets the wrapped format instance.
   * 
   * @return the wrapped format instance.
   */
  public Format getFormat() {
    return format;
  }

  /**
   * {@inheritDoc}
   */
  public Object parse(String source) throws ParseException {
    return format.parseObject(source);
  }

}
