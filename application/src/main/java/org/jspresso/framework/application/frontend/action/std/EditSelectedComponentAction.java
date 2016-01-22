/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.frontend.action.std;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.CloseDialogAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This action should be installed on collection views. It takes the selected
 * component and edit it in a modal dialog. Editing happens in a &quot;Unit of
 * Work&quot; meaning that it can be rolled-back when canceling.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class EditSelectedComponentAction<E, F, G> extends
    AbstractEditComponentAction<E, F, G> {

  private FrontendAction<E, F, G> cancelAction;
  private FrontendAction<E, F, G> okAction;

  /**
   * Gets the cancelAction.
   *
   * @return the cancelAction.
   */
  @Override
  protected IDisplayableAction getCancelAction() {
    return cancelAction;
  }

  /**
   * Gets the okAction.
   *
   * @return the okAction.
   */
  @Override
  protected IDisplayableAction getOkAction() {
    return okAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (!getBackendController(context).isUnitOfWorkActive()) {
      getBackendController(context).beginUnitOfWork();
    }
    try {
      return super.execute(actionHandler, context);
    } catch (RuntimeException ex) {
      getBackendController(context).rollbackUnitOfWork();
      throw ex;
    }
  }

  /**
   * Gets the selected model.
   *
   * @param context
   *          the action context.
   * @return the model.
   */
  @Override
  protected Object getComponentToEdit(Map<String, Object> context) {
    IEntity uowEntity = getBackendController(context).cloneInUnitOfWork(
        (IEntity) getSelectedModel(context));
    return uowEntity;
  }

  /**
   * Sets the cancelAction.
   *
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(FrontendAction<E, F, G> cancelAction) {
    this.cancelAction = new UowRollbackerAction<>(cancelAction);
  }

  /**
   * Sets the okAction.
   *
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(FrontendAction<E, F, G> okAction) {
    this.okAction = new UowRollbackerAction<>(okAction);
  }

  /**
   * Default OK action.
   *
     * @author Vincent Vandenschrick
   * @param <E>
   *          the actual gui component type used.
   * @param <F>
   *          the actual icon type used.
   * @param <G>
   *          the actual action type used.
   */
  public static class DefaultOkAction<E, F, G> extends
      CloseDialogAction<E, F, G> {

    /**
     * Merges eagerly the edited entity.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      IEntity entityClone = getModel(context);
      getBackendController(context).merge(entityClone, EMergeMode.MERGE_EAGER);
      return super.execute(actionHandler, context);
    }
  }

  /**
   * A wrapper action that roll backs the current UOW before delegating to its
   * delegate.
   *
     * @author Vincent Vandenschrick
   * @param <E>
   *          the actual gui component type used.
   * @param <F>
   *          the actual icon type used.
   * @param <G>
   *          the actual action type used.
   */
  public static class UowRollbackerAction<E, F, G> extends
      FrontendAction<E, F, G> {

    private final FrontendAction<E, F, G> delegate;

    /**
     * Constructs a new {@code UowRollbackerAction} instance.
     *
     * @param delegate
     *          the action to finally delegate to.
     */
    public UowRollbackerAction(FrontendAction<E, F, G> delegate) {
      this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler,
        Map<String, Object> context) {
      if (getBackendController(context).isUnitOfWorkActive()) {
        getBackendController(context).rollbackUnitOfWork();
      }
      return actionHandler.execute(delegate, context);
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getAcceleratorAsString() {
      return delegate.getAcceleratorAsString();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Collection<IGate> getActionabilityGates() {
      return delegate.getActionabilityGates();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return delegate.getDescription();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getI18nDescription(ITranslationProvider translationProvider,
        Locale locale) {
      return delegate.getI18nDescription(translationProvider, locale);
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getI18nName(ITranslationProvider translationProvider,
        Locale locale) {
      return delegate.getI18nName(translationProvider, locale);
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Icon getIcon() {
      return delegate.getIcon();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getMnemonicAsString() {
      return delegate.getMnemonicAsString();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return delegate.getName();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getPermId() {
      return delegate.getPermId();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getGrantedRoles() {
      return delegate.getGrantedRoles();
    }

    /**
     * Forwards to the delegate.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String getStyleName() {
      return delegate.getStyleName();
    }
  }
}
