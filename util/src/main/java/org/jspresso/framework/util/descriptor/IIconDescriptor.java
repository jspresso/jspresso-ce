/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.descriptor;

/**
 * This interface is implemented by anything which is graphically identifiable
 * by an icon.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconDescriptor extends IDescriptor {

  /**
   * Gets the URL of the image used by the icon. For Swing view factory a
   * special kind of URL is supported in the form of
   * <code>classpath:directory/image.ext</code> to be able to load images as
   * classpath resource streams.
   * 
   * @return the image URL.
   */
  String getIconImageURL();

}
