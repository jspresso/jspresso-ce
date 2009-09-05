/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.security;

import javax.security.auth.Subject;

/**
 * Implemented by classes that can be injected with a JAAS subject.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISubjectAware {

  /**
   * Sets the JAAS subject.
   * 
   * @param subject
   *          the JAAS subject to set.
   */
  void setSubject(Subject subject);
}
