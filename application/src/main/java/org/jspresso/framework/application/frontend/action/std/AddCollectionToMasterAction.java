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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AddComponentCollectionToMasterAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This action is designed to wrap a backend action that will create and add a
 * (collection of) component(s) to the model collection of the view it's
 * installed on. Its objective is to complete the action context with the
 * descriptor of the component (or entity) to be added so that the backend
 * action explicitly knows what to create. Moreover, the name, description and
 * icon used for the graphical representation are all computed out of the
 * configured {@code elementEntityDescriptor}.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AddCollectionToMasterAction<E, F, G> extends
    FrontendAction<E, F, G> {

  private IComponentDescriptor<IEntity> elementEntityDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (super.equals(obj)) {
      if (elementEntityDescriptor != null
          && obj instanceof AddCollectionToMasterAction<?, ?, ?>) {
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
      varContext = new HashMap<>();
    }
    varContext.put(AddComponentCollectionToMasterAction.ELEMENT_DESCRIPTOR,
        getElementEntityDescriptor(context));
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
          new String[] {
            elementEntityDescriptor.getI18nName(translationProvider, locale)
          }, locale);
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
          new String[] {
            elementEntityDescriptor.getI18nName(translationProvider, locale)
          }, locale);
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
   * Configures the descriptor of the entities that are to be added by the
   * action to the underlying model collection. Setting this property serves
   * multiple objectives :
   * <ul>
   * <li>complete the application context with the
   * {@code AddComponentCollectionToMasterAction.ELEMENT_DESCRIPTOR} key so
   * that the chained backend action knows what type of entity to create.</li>
   * <li>customize the name, description and icon used to represent the action
   * in the UI. All three are derived from the configured element entity
   * descriptor.</li>
   * </ul>
   *
   * @param elementEntityDescriptor
   *          the elementEntityDescriptor to set.
   */
  public void setElementEntityDescriptor(
      IComponentDescriptor<IEntity> elementEntityDescriptor) {
    this.elementEntityDescriptor = elementEntityDescriptor;
    if (elementEntityDescriptor != null) {
      setIcon(elementEntityDescriptor.getIcon());
    }
  }

  /**
   * Gets the elementEntityDescriptor.
   *
   * @param context
   *          the action context.
   * @return the elementEntityDescriptor.
   */
  @SuppressWarnings("UnusedParameters")
  protected IComponentDescriptor<IEntity> getElementEntityDescriptor(
      Map<String, Object> context) {
    return elementEntityDescriptor;
  }

}
