/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.std;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.ActionWrapper;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.i18n.ITranslationProvider;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;

/**
 * Creates and adds an entity to the selected master detail collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class AddCollectionToMasterAction<E, F, G> extends
    ActionWrapper<E, F, G> {

  private IComponentDescriptor<IEntity> elementEntityDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (super.equals(obj)) {
      if (elementEntityDescriptor != null
          && obj instanceof AddCollectionToMasterAction) {
        return elementEntityDescriptor
            .equals(((AddCollectionToMasterAction<?, ?, ?>) obj).elementEntityDescriptor);
      }
      return true;
    }
    return false;
  }

  /**
   * Completes the action context with the element entity descriptor
   * parametrized.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Map<String, Object> varContext = context;
    if (varContext == null) {
      varContext = new HashMap<String, Object>();
    }
    varContext.put(ActionContextConstants.ELEMENT_DESCRIPTOR,
        elementEntityDescriptor);
    return super.execute(actionHandler, varContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (elementEntityDescriptor != null) {
      return translationProvider.getTranslation("add.element.description",
          new String[] {elementEntityDescriptor.getI18nName(
              translationProvider, locale)}, locale);
    }
    return super.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    if (elementEntityDescriptor != null) {
      return translationProvider.getTranslation("add.element.name",
          new String[] {elementEntityDescriptor.getI18nName(
              translationProvider, locale)}, locale);
    }
    return super.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (elementEntityDescriptor != null) {
      hash += elementEntityDescriptor.hashCode();
    }
    return hash;
  }

  /**
   * Sets the elementEntityDescriptor. Entities of this type (which must be a
   * subclass of the collection element) are created and added to the detail
   * collection.
   * 
   * @param elementEntityDescriptor
   *            the elementEntityDescriptor to set.
   */
  public void setElementEntityDescriptor(
      IComponentDescriptor<IEntity> elementEntityDescriptor) {
    this.elementEntityDescriptor = elementEntityDescriptor;
    if (elementEntityDescriptor != null) {
      setIconImageURL(elementEntityDescriptor.getIconImageURL());
    }
  }

}
