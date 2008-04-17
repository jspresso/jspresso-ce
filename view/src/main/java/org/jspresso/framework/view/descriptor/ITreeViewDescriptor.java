/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

/**
 * This public interface is implemented by any tree view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITreeViewDescriptor extends IViewDescriptor {

  /**
   * Given a user object, this method qives the ability to the tree view
   * descriptor to return the url of an image used to render the user object.
   * This method may return null.
   * 
   * @param userObject
   *            the user object to render.
   * @return the url of the image to use for the renderer or null.
   */
  String getIconImageURLForUserObject(Object userObject);

  /**
   * It gets the maximum depth of the tree structure whichis mandatory in case
   * of a recursive one.
   * 
   * @return the maximum tree structure depth.
   */
  int getMaxDepth();

  /**
   * Gets the root tree level descriptor of this tree view.
   * 
   * @return the root tree level descriptor of this tree view.
   */
  ITreeLevelDescriptor getRootSubtreeDescriptor();

}
