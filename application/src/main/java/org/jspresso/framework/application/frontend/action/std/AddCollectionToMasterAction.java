/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Creates and adds an entity to the selected master detail collection.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
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
      varContext = new HashMap<String, Object>();
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
   *          the elementEntityDescriptor to set.
   */
  public void setElementEntityDescriptor(
      IComponentDescriptor<IEntity> elementEntityDescriptor) {
    this.elementEntityDescriptor = elementEntityDescriptor;
    if (elementEntityDescriptor != null) {
      setIconImageURL(elementEntityDescriptor.getIconImageURL());
    }
  }

  /**
   * Gets the elementEntityDescriptor.
   * 
   * @param context
   *          the action context.
   * @return the elementEntityDescriptor.
   */
  protected IComponentDescriptor<IEntity> getElementEntityDescriptor(
      Map<String, Object> context) {
    return elementEntityDescriptor;
  }

}
