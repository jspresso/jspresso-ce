/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.i18n.ITranslationProvider;

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

  private boolean              longOperation;
  private Map<String, Object>  initialContext;
  private ITranslationProvider translationProvider;

  /**
   * Retrieves the locale the action has to use to execute from its context
   * using a well-known key.
   * 
   * @param context
   *          the action context.
   * @return the locale the action executes in.
   */
  public abstract Locale getLocale(Map<String, Object> context);


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

  /**
   * Gets the initialContext.
   * 
   * @return the initialContext.
   */
  public Map<String, Object> getInitialContext() {
    return initialContext;
  }

  /**
   * {@inheritDoc}
   */
  public void putInitialContext(String key, Object value) {
    if (initialContext == null) {
      initialContext = new HashMap<String, Object>();
    }
    initialContext.put(key, value);
  }

  /**
   * Gets the parent module selected indices from the context. it uses the
   * <code>ActionContextConstants.PARENT_MODULE_SELECTED_INDICES</code> key.
   * 
   * @param context
   *          the action context.
   * @return the selected indices if any.
   */
  public int[] getParentModuleSelectedIndices(Map<String, Object> context) {
    return ((ICollectionConnectorProvider) ((ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_VIEW_CONNECTOR))
        .getParentConnector()).getCollectionConnector().getSelectedIndices();
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  protected ITranslationProvider getTranslationProvider() {
    return translationProvider;
  }
  
  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
