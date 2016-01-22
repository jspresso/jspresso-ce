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
package org.jspresso.framework.model.component;

import java.lang.reflect.InvocationTargetException;

import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.bean.integrity.EmptyPropertyProcessor;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This interface is implemented by component property translations.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("EmptyMethod")
public interface IPropertyTranslation extends IComponent {


  /**
   * Constant value for propertyName.
   */
  String PROPERTY_NAME = "propertyName";

  /**
   * Gets the propertyName.
   *
   * @return the propertyName.
   */
  java.lang.String getPropertyName();

  /**
   * Sets the propertyName.
   *
   * @param propertyName
   *     the propertyName to set.
   */
  void setPropertyName(java.lang.String propertyName);


  /**
   * Constant value for language.
   */
  String LANGUAGE = "language";

  /**
   * Gets the language.
   *
   * @return the language.
   */
  java.lang.String getLanguage();

  /**
   * Sets the language.
   *
   * @param language
   *     the language to set.
   */
  void setLanguage(java.lang.String language);


  /**
   * Constant value for translatedValue.
   */
  String TRANSLATED_VALUE = "translatedValue";

  /**
   * Gets the translatedValue.
   *
   * @return the translatedValue.
   */
  java.lang.String getTranslatedValue();

  /**
   * Sets the translatedValue.
   *
   * @param translatedValue
   *     the translatedValue to set.
   */
  void setTranslatedValue(java.lang.String translatedValue);

  /**
   * The type Common post processor.
   */
  class CommonProcessor extends EmptyPropertyProcessor<IPropertyTranslation, String> {

    private IAccessorFactory  accessorFactory;
    private IComponentFactory componentFactory;
    private String            translationsPropertyName;
    private String            processedPropertyName;

    /**
     * Postprocess setter.
     *
     * @param target
     *     the target
     * @param newPropertyValue
     *     the new property value
     */
    @Override
    public void postprocessSetter(IPropertyTranslation target, String oldPropertyValue, String newPropertyValue) {
      IComponent owningComponent = target.getOwningComponent();
      if (owningComponent != null) {

        // We cannot just update the property translation component, but we have to replace it by a new one.
        IPropertyTranslation newPropertyTranslation = (IPropertyTranslation) componentFactory.createComponentInstance(
            target.getComponentContract());
        newPropertyTranslation.straightSetProperty(IPropertyTranslation.PROPERTY_NAME, target.getPropertyName());
        newPropertyTranslation.straightSetProperty(IPropertyTranslation.LANGUAGE, target.getLanguage());
        newPropertyTranslation.straightSetProperty(IPropertyTranslation.TRANSLATED_VALUE, target.getTranslatedValue());

        // and keep the original value on the old one so that it's hashcode is preserved and it can be removed from the
        // hash set
        target.straightSetProperty(processedPropertyName, oldPropertyValue);

        ICollectionAccessor translationsAccessor = accessorFactory.createCollectionPropertyAccessor(
            translationsPropertyName, owningComponent.getClass(), target.getComponentContract());
        try {
          translationsAccessor.removeFromValue(owningComponent, target);
          translationsAccessor.addToValue(owningComponent, newPropertyTranslation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new NestedRuntimeException(e);
        }
      }
    }

    /**
     * Sets component factory.
     *
     * @param componentFactory
     *     the component factory
     */
    public void setComponentFactory(IComponentFactory componentFactory) {
      this.componentFactory = componentFactory;
    }

    /**
     * Sets accessor factory.
     *
     * @param accessorFactory
     *     the accessor factory
     */
    public void setAccessorFactory(IAccessorFactory accessorFactory) {
      this.accessorFactory = accessorFactory;
    }

    /**
     * Sets translations property name.
     *
     * @param translationsPropertyName
     *     the translations property name
     */
    public void setTranslationsPropertyName(String translationsPropertyName) {
      this.translationsPropertyName = translationsPropertyName;
    }

    /**
     * Sets processed property name.
     *
     * @param processedPropertyName
     *     the processed property name
     */
    public void setProcessedPropertyName(String processedPropertyName) {
      this.processedPropertyName = processedPropertyName;
    }
  }
}
