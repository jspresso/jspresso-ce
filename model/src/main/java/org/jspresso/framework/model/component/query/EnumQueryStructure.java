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
package org.jspresso.framework.model.component.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * A simple query structure which holds selection tick, a value and a
 * translation.
 *
 * @author Vincent Vandenschrick
 */
public class EnumQueryStructure extends AbstractPropertyChangeCapable {

  private final IEnumerationPropertyDescriptor sourceDescriptor;
  private ITranslationProvider           translationProvider;
  private Locale                         locale;
  private Set<EnumValueQueryStructure>   enumerationValues;

  /**
   * Constructs a new {@code EnumValueQueryStructure} instance.
   *
   * @param propertyDescriptor
   *          the enumeration property descriptor to create the query structure
   *          for.
   */
  public EnumQueryStructure(IEnumerationPropertyDescriptor propertyDescriptor) {
    this.sourceDescriptor = propertyDescriptor;
    PropertyChangeListener toStringListener = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(EnumQueryStructureDescriptor.TO_STRING, null,
            getToString());
      }
    };
    this.enumerationValues = new LinkedHashSet<>();
    if (!propertyDescriptor.isMandatory()) {
      EnumValueQueryStructure nullValueQueryStructure = new EnumValueQueryStructure();
      nullValueQueryStructure.setValue(null);
      nullValueQueryStructure.addPropertyChangeListener(toStringListener);
      enumerationValues.add(nullValueQueryStructure);
    }
    for (String value : propertyDescriptor.getEnumerationValues()) {
      EnumValueQueryStructure enumValueQueryStructure = new EnumValueQueryStructure();
      enumValueQueryStructure.setValue(value);
      enumValueQueryStructure.addPropertyChangeListener(toStringListener);
      enumerationValues.add(enumValueQueryStructure);
    }
  }

  /**
   * Gets the enumerationValues.
   *
   * @return the enumerationValues.
   */
  public Set<EnumValueQueryStructure> getEnumerationValues() {
    return enumerationValues;
  }

  /**
   * Sets the enumerationValues.
   *
   * @param enumerationValues
   *          the enumerationValues to set.
   */
  protected void setEnumerationValues(
      Set<EnumValueQueryStructure> enumerationValues) {
    this.enumerationValues = enumerationValues;
  }

  /**
   * Returns the selected enumeration value query structures.
   *
   * @return the selected enumeration value query structures.
   */
  public Set<EnumValueQueryStructure> getSelectedEnumerationValues() {
    Set<EnumValueQueryStructure> selectedEnumerationValues = new LinkedHashSet<>();
    for (EnumValueQueryStructure enumValueQueryStructure : getEnumerationValues()) {
      if (enumValueQueryStructure.isSelected()) {
        selectedEnumerationValues.add(enumValueQueryStructure);
      }
    }
    return selectedEnumerationValues;
  }

  /**
   * Sets the selected enumeration value query structures.
   *
   * @param selectedEnumerationValues the selected enumeration value query structures.
   */
  public void setSelectedEnumerationValues(Set<EnumValueQueryStructure> selectedEnumerationValues) {
    for (EnumValueQueryStructure enumValueQueryStructure : getEnumerationValues()) {
      if (selectedEnumerationValues != null) {
        enumValueQueryStructure.setSelected(selectedEnumerationValues.contains(enumValueQueryStructure));
      } else {
        enumValueQueryStructure.setSelected(false);
      }
    }
  }

  /**
   * Computes the toString().
   *
   * @return the toString().
   */
  public String getToString() {
    Set<EnumValueQueryStructure> selectedEnumerationValues = getSelectedEnumerationValues();
    if (selectedEnumerationValues.isEmpty()) {
      return "";
    }
    if (selectedEnumerationValues.size() > 3) {
      return "***";
    }
    String sep = ", ";
    StringBuilder buf = new StringBuilder();
    for (Iterator<EnumValueQueryStructure> ite = selectedEnumerationValues
        .iterator(); ite.hasNext();) {
      EnumValueQueryStructure enumValue = ite.next();
      if (getTranslationProvider() != null && getLocale() != null) {
        buf.append(sourceDescriptor.getI18nValue(enumValue.getValue(),
            getTranslationProvider(), getLocale()));
      } else {
        buf.append(enumValue.getValue());
      }
      if (ite.hasNext()) {
        buf.append(sep);
      }
    }
    return buf.toString();
  }

  /**
   * Clears all selection.
   */
  public void clear() {
    for (EnumValueQueryStructure value : getEnumerationValues()) {
      value.setSelected(false);
    }
  }

  /**
   * Returns {@code true} if this query structure does not have any
   * selected value.
   *
   * @return {@code true} if this query structure does not have any
   *         selected value.
   */
  public boolean isEmpty() {
    return getSelectedEnumerationValues().isEmpty();
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
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
    translateValues();
  }

  /**
   * Gets the locale.
   *
   * @return the locale.
   */
  protected Locale getLocale() {
    return locale;
  }

  /**
   * Sets the locale.
   *
   * @param locale
   *          the locale to set.
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
    translateValues();
  }

  /**
   * Gets the sourceDescriptor.
   *
   * @return the sourceDescriptor.
   */
  protected IEnumerationPropertyDescriptor getSourceDescriptor() {
    return sourceDescriptor;
  }

  private void translateValues() {
    if (getTranslationProvider() != null && getLocale() != null) {
      for (EnumValueQueryStructure valueQueryStructure : getEnumerationValues()) {
        valueQueryStructure.setI18nValue(getSourceDescriptor().getI18nValue(valueQueryStructure.getValue(),
            getTranslationProvider(), getLocale()));
      }
    }
  }
}
