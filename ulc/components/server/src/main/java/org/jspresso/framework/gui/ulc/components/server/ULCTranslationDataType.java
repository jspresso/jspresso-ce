/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.server;

import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.gui.ulc.components.shared.TranslationDataTypeConstants;

import com.ulcjava.base.application.ULCProxy;
import com.ulcjava.base.application.datatype.IDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype to provide translated renderers on ULCExtendedTable, ULCList,
 * ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 */
public class ULCTranslationDataType extends ULCProxy implements IDataType {

  private static final long   serialVersionUID = 5080679351626979457L;

  private Map<String, String> dictionary;

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance.
   */
  public ULCTranslationDataType() {
    this(null);
  }

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance. This
   * constructor has default visibility to prevent for direct instanciation.
   * 
   * @param dictionary
   *            the dictionary containing the translations.
   */
  ULCTranslationDataType(Map<String, String> dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ULCTranslationDataType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    ULCTranslationDataType rhs = (ULCTranslationDataType) obj;
    return new EqualsBuilder().append(dictionary, rhs.dictionary).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 23).append(dictionary).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    Vector<String> flatDictionary = new Vector<String>();
    for (Map.Entry<String, String> dictEntry : dictionary.entrySet()) {
      flatDictionary.add(dictEntry.getKey());
      flatDictionary.add(dictEntry.getValue());
    }

    saveState(a, TranslationDataTypeConstants.DICTIONARY, flatDictionary, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UITranslationDataType";
  }

}
