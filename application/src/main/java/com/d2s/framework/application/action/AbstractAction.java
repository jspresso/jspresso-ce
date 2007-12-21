/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.IController;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Base class for all application actions. Takes care of the context reference
 * as well as the input context keys reference.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractAction implements IAction {

  private Collection<String>  grantedRoles;
  private Map<String, Object> initialContext;
  private boolean             longOperation;

  /**
   * {@inheritDoc}
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
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
   * Retrieves the locale the action has to use to execute from its context
   * using a well-known key.
   * 
   * @param context
   *            the action context.
   * @return the locale the action executes in.
   */
  public abstract Locale getLocale(Map<String, Object> context);

  /**
   * Gets the parent module selected indices from the context. it uses the
   * <code>ActionContextConstants.PARENT_MODULE_SELECTED_INDICES</code> key.
   * 
   * @param context
   *            the action context.
   * @return the selected indices if any.
   */
  public int[] getParentModuleSelectedIndices(Map<String, Object> context) {
    return ((ICollectionConnectorProvider) ((ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_VIEW_CONNECTOR))
        .getParentConnector()).getCollectionConnector().getSelectedIndices();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLongOperation() {
    return longOperation;
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
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *            the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

  /**
   * Sets the longOperation.
   * 
   * @param longOperation
   *            the longOperation to set.
   */
  public void setLongOperation(boolean longOperation) {
    this.longOperation = longOperation;
  }

  /**
   * Gets the controller (frontend or backend) out of the action context.
   * 
   * @param context
   *            the action context.
   * @return the controller (frontend or backend).
   */
  protected abstract IController getController(Map<String, Object> context);

  /**
   * Gets a translation provider out of the action context.
   * 
   * @param context
   *            the action context.
   * @return the translation provider.
   */
  protected ITranslationProvider getTranslationProvider(
      Map<String, Object> context) {
    return getController(context).getTranslationProvider();
  }
}
