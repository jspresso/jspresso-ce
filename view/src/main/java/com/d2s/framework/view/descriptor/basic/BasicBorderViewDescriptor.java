/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.IBorderViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a border view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicBorderViewDescriptor extends BasicCompositeViewDescriptor
    implements IBorderViewDescriptor {

  private IViewDescriptor centerViewDescriptor;
  private IViewDescriptor eastViewDescriptor;
  private IViewDescriptor northViewDescriptor;
  private IViewDescriptor southViewDescriptor;
  private IViewDescriptor westViewDescriptor;

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getCenterViewDescriptor() {
    return centerViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getEastViewDescriptor() {
    return eastViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getNorthViewDescriptor() {
    return northViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getSouthViewDescriptor() {
    return southViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getWestViewDescriptor() {
    return westViewDescriptor;
  }

  /**
   * Sets the centerViewDescriptor.
   * 
   * @param centerViewDescriptor
   *            the centerViewDescriptor to set.
   */
  public void setCenterViewDescriptor(IViewDescriptor centerViewDescriptor) {
    this.centerViewDescriptor = centerViewDescriptor;
  }

  /**
   * Sets the eastViewDescriptor.
   * 
   * @param eastViewDescriptor
   *            the eastViewDescriptor to set.
   */
  public void setEastViewDescriptor(IViewDescriptor eastViewDescriptor) {
    this.eastViewDescriptor = eastViewDescriptor;
  }

  /**
   * Sets the northViewDescriptor.
   * 
   * @param northViewDescriptor
   *            the northViewDescriptor to set.
   */
  public void setNorthViewDescriptor(IViewDescriptor northViewDescriptor) {
    this.northViewDescriptor = northViewDescriptor;
  }

  /**
   * Sets the southViewDescriptor.
   * 
   * @param southViewDescriptor
   *            the southViewDescriptor to set.
   */
  public void setSouthViewDescriptor(IViewDescriptor southViewDescriptor) {
    this.southViewDescriptor = southViewDescriptor;
  }

  /**
   * Sets the westViewDescriptor.
   * 
   * @param westViewDescriptor
   *            the westViewDescriptor to set.
   */
  public void setWestViewDescriptor(IViewDescriptor westViewDescriptor) {
    this.westViewDescriptor = westViewDescriptor;
  }
}
