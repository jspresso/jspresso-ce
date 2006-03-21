/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Base class for all application actions. Takes care of the context reference
 * as well as the input context keys reference.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAction implements IAction {

  private Map<String, Object> context;
  private Collection<String>  inputContextKeys;
  private boolean             longOperation;

  /**
   * Gets the context.
   * 
   * @return the context.
   */
  public Map<String, Object> getContext() {
    return context;
  }

  /**
   * Sets the context.
   * 
   * @param context
   *          the context to set.
   */
  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getInputContextKeys() {
    return inputContextKeys;
  }

  /**
   * Sets the inputContextKeys. These keys index the context values used by the
   * action to execute. The values are accessed using
   * <code>getContext().get(key)</code>.
   * 
   * @param inputContextKeys
   *          the inputContextKeys to set.
   */
  public void setInputContextKeys(Collection<String> inputContextKeys) {
    this.inputContextKeys = inputContextKeys;
  }

  /**
   * Retrieves the locale the action has to use to execute from its context
   * using a well-known key.
   * 
   * @return the locale the action executes in.
   */
  public Locale getLocale() {
    return (Locale) getContext().get(ActionContextConstants.LOCALE);
  }

  /**
   * Gets the parent module selected indices from the context. it uses the
   * <code>ActionContextConstants.PARENT_MODULE_SELECTED_INDICES</code> key.
   * 
   * @return the selected indices if any.
   */
  public int[] getParentModuleSelectedIndices() {
    return (int[]) getContext().get(
        ActionContextConstants.PARENT_MODULE_SELECTED_INDICES);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLongOperation() {
    return longOperation;
  }

  /**
   * Sets the longOperation.
   * 
   * @param longOperation
   *          the longOperation to set.
   */
  public void setLongOperation(boolean longOperation) {
    this.longOperation = longOperation;
  }
}
