/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of color properties. Color are
 * stored as hex string in the form "0xFFFFFF" if alpha (transparency) is not
 * used and "0xFFFFFFFF" if alpha is used.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IColorPropertyDescriptor extends IScalarPropertyDescriptor {
  // Just a marker interface.
}
