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
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.EDateType;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPercentPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * A simple query structure which holds a comparator, and inf value and a sup
 * value.
 *
 * @author Vincent Vandenschrick
 */
public class ComparableQueryStructure extends QueryComponent {

  private static final long serialVersionUID = 7831817429580706837L;

  private final IPropertyDescriptor  sourceDescriptor;
  private       ITranslationProvider translationProvider;
  private       Locale               locale;

  /**
   * Constructs a new {@code ComparableQueryStructure} instance.
   *
   * @param componentDescriptor
   *     the query componentDescriptor
   * @param componentFactory
   *     the component factory
   * @param propertyDescriptor
   *     the property descriptor
   */
  public ComparableQueryStructure(IComponentDescriptor<?> componentDescriptor, IComponentFactory componentFactory,
                                  IPropertyDescriptor propertyDescriptor) {
    super(componentDescriptor, componentFactory);
    this.sourceDescriptor = propertyDescriptor;
    setComparator(ComparableQueryStructureDescriptor.EQ);
    PropertyChangeListener toStringListener = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(ComparableQueryStructureDescriptor.TO_STRING, null, getToString());
      }
    };
    addPropertyChangeListener(ComparableQueryStructureDescriptor.COMPARATOR, toStringListener);
    addPropertyChangeListener(ComparableQueryStructureDescriptor.INF_VALUE, toStringListener);
    addPropertyChangeListener(ComparableQueryStructureDescriptor.SUP_VALUE, toStringListener);
  }

  /**
   * Gets the comparator.
   *
   * @return the comparator.
   */
  public String getComparator() {
    return (String) get(ComparableQueryStructureDescriptor.COMPARATOR);
  }

  /**
   * Sets the comparator.
   *
   * @param comparator
   *     the comparator to set.
   */
  public void setComparator(String comparator) {
    put(ComparableQueryStructureDescriptor.COMPARATOR, comparator);
  }

  /**
   * Gets the infValue.
   *
   * @return the infValue.
   */
  public Object getInfValue() {
    return get(ComparableQueryStructureDescriptor.INF_VALUE);
  }

  /**
   * Sets the infValue.
   *
   * @param infValue
   *     the infValue to set.
   */
  public void setInfValue(Object infValue) {
    put(ComparableQueryStructureDescriptor.INF_VALUE, infValue);
  }

  /**
   * Gets the supValue.
   *
   * @return the supValue.
   */
  public Object getSupValue() {
    return get(ComparableQueryStructureDescriptor.SUP_VALUE);
  }

  /**
   * Sets the supValue.
   *
   * @param supValue
   *     the supValue to set.
   */
  public void setSupValue(Object supValue) {
    put(ComparableQueryStructureDescriptor.SUP_VALUE, supValue);
  }

  /**
   * Whether the comparable query structure actually holds a restriction.
   *
   * @return {@code true} if the comparable query structure actually holds
   * a restriction.
   */
  @Override
  public boolean isRestricting() {
    return getComparator() != null && (getInfValue() != null || getSupValue() != null)
        || ComparableQueryStructureDescriptor.NU.equals(getComparator()) || ComparableQueryStructureDescriptor.NN
        .equals(getComparator());
  }

  /**
   * Whether the value passed as parameter matches the query structure.
   *
   * @param value
   *     the value to test.
   * @return {@code true} if the value passed as parameter matches the
   * query structure.
   */
  public boolean matches(Comparable<Object> value) {
    if (isRestricting()) {
      if (value == null) {
        return false;
      }
      String comparator = getComparator();
      Object infValue = getInfValue();
      Object supValue = getSupValue();
      Object compareValue = infValue;
      if (compareValue == null) {
        compareValue = supValue;
      }
      switch (comparator) {
        case ComparableQueryStructureDescriptor.EQ:
          return value.compareTo(compareValue) == 0;
        case ComparableQueryStructureDescriptor.GT:
          return value.compareTo(compareValue) > 0;
        case ComparableQueryStructureDescriptor.GE:
          return value.compareTo(compareValue) >= 0;
        case ComparableQueryStructureDescriptor.LT:
          return value.compareTo(compareValue) < 0;
        case ComparableQueryStructureDescriptor.LE:
          return value.compareTo(compareValue) <= 0;
        case ComparableQueryStructureDescriptor.NU:
          return compareValue == null;
        case ComparableQueryStructureDescriptor.NN:
          return compareValue != null;
        case ComparableQueryStructureDescriptor.BE:
          if (infValue != null && supValue != null) {
            return value.compareTo(infValue) >= 0 && value.compareTo(supValue) <= 0;
          } else if (infValue != null) {
            return value.compareTo(infValue) >= 0;
          } else {
            return value.compareTo(supValue) <= 0;
          }
        default:
          break;
      }
    }
    return true;
  }

  /**
   * Computes the toString().
   *
   * @return the toString().
   */
  public String getToString() {
    if (isRestricting()) {
      String comparator = getComparator();
      switch (comparator) {
        case ComparableQueryStructureDescriptor.NU:
          return "#";
        case ComparableQueryStructureDescriptor.NN:
          return "!#";
        default:
          Format format = getFormat();
          StringBuilder buf = new StringBuilder();
          Object infValue = getInfValue();
          Object supValue = getSupValue();
          Object compareValue = infValue;
          if (compareValue == null) {
            compareValue = supValue;
          }
          
          String formattedCompareValue;
          try {
            formattedCompareValue = format != null ? format.format(compareValue) : compareValue.toString();
          } catch (IllegalArgumentException e) {
            formattedCompareValue = compareValue.toString();
          }
          
          switch (comparator) {
            case ComparableQueryStructureDescriptor.EQ:
              buf.append("= ").append(formattedCompareValue);
              break;
            case ComparableQueryStructureDescriptor.GT:
              buf.append("> ").append(formattedCompareValue);
              break;
            case ComparableQueryStructureDescriptor.GE:
              buf.append(">= ").append(formattedCompareValue);
              break;
            case ComparableQueryStructureDescriptor.LT:
              buf.append("< ").append(formattedCompareValue);
              break;
            case ComparableQueryStructureDescriptor.LE:
              buf.append("<= ").append(formattedCompareValue);
              break;
            case ComparableQueryStructureDescriptor.BE:
              if (infValue != null && supValue != null) {
                buf.append(">= ").append(format != null ? format.format(infValue) : infValue.toString()).append(", <= ")
                   .append(format != null ? format.format(supValue) : supValue.toString());
              } else if (infValue != null) {
                buf.append(">= ").append(format != null ? format.format(infValue) : infValue.toString());
              } else if (supValue != null) {
                buf.append("<= ").append(format != null ? format.format(supValue) : supValue.toString());
              }
              break;
            default:
              break;
          }
          return buf.toString();
      }
    }
    return "";
  }

  /**
   * Gets format dependening on property descriptor type.
   * @return The format.
   */
  public Format getFormat() {
    Format format = null;
    IPropertyDescriptor sd = getSourceDescriptor();
    if (sd instanceof IDatePropertyDescriptor) {
      if (((IDatePropertyDescriptor) sd).getType() == EDateType.DATE_TIME) {
        if (((IDatePropertyDescriptor) sd).isMillisecondsAware()) {
          format = new SimpleDateFormat(
              getTranslationProvider().getDatePattern(getLocale()) + " " + getTranslationProvider()
                  .getLongTimePattern(getLocale()));
        } else if (((IDatePropertyDescriptor) sd).isSecondsAware()) {
          format = new SimpleDateFormat(
              getTranslationProvider().getDatePattern(getLocale()) + " " + getTranslationProvider()
                  .getTimePattern(getLocale()));
        } else {
          format = new SimpleDateFormat(
              getTranslationProvider().getDatePattern(getLocale()) + " " + getTranslationProvider()
                  .getShortTimePattern(getLocale()));
        }
      } else {
        format = new SimpleDateFormat(getTranslationProvider().getDatePattern(getLocale()));
      }
    } else if (sd instanceof ITimePropertyDescriptor) {
      if (((ITimePropertyDescriptor) sd).isSecondsAware()) {
        format = new SimpleDateFormat(getTranslationProvider().getTimePattern(getLocale()));
      } else {
        format = new SimpleDateFormat(getTranslationProvider().getShortTimePattern(getLocale()));
      }
    } else if (sd instanceof IIntegerPropertyDescriptor) {
      format = NumberFormat.getIntegerInstance(locale);
    } else if (sd instanceof IDecimalPropertyDescriptor) {
      if (sd instanceof IPercentPropertyDescriptor) {
        format = NumberFormat.getPercentInstance(locale);
      } else {
        format = NumberFormat.getNumberInstance(locale);
      }
      ((NumberFormat) format).setMaximumFractionDigits(((IDecimalPropertyDescriptor) sd).getMaxFractionDigit());
      ((NumberFormat) format).setMinimumFractionDigits(((IDecimalPropertyDescriptor) sd).getMaxFractionDigit());
    }
    return format;
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
   *     the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
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
   *     the locale to set.
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Gets the sourceDescriptor.
   *
   * @return the sourceDescriptor.
   */
  public IPropertyDescriptor getSourceDescriptor() {
    return sourceDescriptor;
  }
}
