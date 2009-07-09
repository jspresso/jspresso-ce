/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.qooxdoo.rpc;

import net.sf.qooxdoo.rpc.RemoteCallUtils;

/**
 * Subclass of Qooxdoo rpc servlet to handle JSON <-> Java more finely.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RpcServlet extends net.sf.qooxdoo.rpc.RpcServlet {

  private static final long serialVersionUID = -3112390742440209121L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected RemoteCallUtils getRemoteCallUtils() {
    return new org.jspresso.framework.qooxdoo.rpc.RemoteCallUtils();
  }
}
