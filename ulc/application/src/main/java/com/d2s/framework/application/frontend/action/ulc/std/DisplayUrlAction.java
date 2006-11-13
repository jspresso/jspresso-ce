/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.std;

import java.io.IOException;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.util.resources.IResource;
import com.d2s.framework.util.resources.UrlResource;
import com.d2s.framework.util.ulc.resource.ResourceManager;

/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayUrlAction extends AbstractUlcAction {

  private String mimeType;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    String urlSpec = (String) context.get(ActionContextConstants.ACTION_PARAM);

    try {
      IResource resource = new UrlResource(mimeType, urlSpec);
      String resourceId = ResourceManager.getInstance().register(resource);
      ResourceManager.getInstance().showDocument(resourceId);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
    return true;
  }

  /**
   * Sets the mimeType.
   *
   * @param mimeType
   *          the mimeType to set.
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}
