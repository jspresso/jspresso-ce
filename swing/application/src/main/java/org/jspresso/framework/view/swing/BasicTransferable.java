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
package org.jspresso.framework.view.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * Strongly inspired from javax.swing.plaf.basic.BasicTransferable.
 *
 * @author Vincent Vandenschrick
 */
public class BasicTransferable implements Transferable, ClipboardOwner {

  private static final Logger LOG = LoggerFactory.getLogger(BasicTransferable.class);

  private final String plainData;
  private final String htmlData;

  private static final DataFlavor[] HTML_FLAVORS;
  private static final DataFlavor[] STRING_FLAVORS;
  private static final DataFlavor[] PLAIN_FLAVORS;

  static {
    HTML_FLAVORS = new DataFlavor[3];
    PLAIN_FLAVORS = new DataFlavor[3];
    STRING_FLAVORS = new DataFlavor[2];
    try {
      HTML_FLAVORS[0] = new DataFlavor("text/html;class=java.lang.String");
      HTML_FLAVORS[1] = new DataFlavor("text/html;class=java.io.Reader");
      HTML_FLAVORS[2] = new DataFlavor(
          "text/html;charset=unicode;class=java.io.InputStream");

      PLAIN_FLAVORS[0] = new DataFlavor("text/plain;class=java.lang.String");
      PLAIN_FLAVORS[1] = new DataFlavor("text/plain;class=java.io.Reader");
      PLAIN_FLAVORS[2] = new DataFlavor(
          "text/plain;charset=unicode;class=java.io.InputStream");

      STRING_FLAVORS[0] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
          + ";class=java.lang.String");
      STRING_FLAVORS[1] = DataFlavor.stringFlavor;

    } catch (ClassNotFoundException cle) {
      LOG.error("Error initializing javax.swing.plaf.basic.BasicTransferable", cle);
    }
  }

  /**
   * Constructs a new {@code BasicTransferable} instance.
   *
   * @param plainData
   *     plain data.
   * @param htmlData
   *     HTML data.
   */
  public BasicTransferable(String plainData, String htmlData) {
    this.plainData = plainData;
    this.htmlData = htmlData;
  }

  /**
   * Returns an array of DataFlavor objects indicating the flavors the data can
   * be provided in. The array should be ordered according to preference for
   * providing the data (from most richly descriptive to least descriptive).
   *
   * @return an array of data flavors in which this data can be transferred
   */
  @Override
  public DataFlavor[] getTransferDataFlavors() {
    DataFlavor[] richerFlavors = getRicherFlavors();
    int nRicher = 0;
    if (richerFlavors != null) {
      nRicher = richerFlavors.length;
    }
    int nHTML = 0;
    if (isHTMLSupported()) {
      nHTML = HTML_FLAVORS.length;
    }
    int nPlain = 0;
    if (isPlainSupported()) {
      nPlain = PLAIN_FLAVORS.length;
    }
    int nString = 0;
    if (isPlainSupported()) {
      nString = STRING_FLAVORS.length;
    }
    int nFlavors = nRicher + nHTML + nPlain + nString;
    DataFlavor[] flavors = new DataFlavor[nFlavors];

    // fill in the array
    int nDone = 0;
    if (nRicher > 0) {
      System.arraycopy(richerFlavors, 0, flavors, nDone, nRicher);
      nDone += nRicher;
    }
    if (nHTML > 0) {
      System.arraycopy(HTML_FLAVORS, 0, flavors, nDone, nHTML);
      nDone += nHTML;
    }
    if (nPlain > 0) {
      System.arraycopy(PLAIN_FLAVORS, 0, flavors, nDone, nPlain);
      nDone += nPlain;
    }
    if (nString > 0) {
      System.arraycopy(STRING_FLAVORS, 0, flavors, nDone, nString);
      nDone += nString;
    }
    return flavors;
  }

  /**
   * Returns whether or not the specified data flavor is supported for this
   * object.
   *
   * @param flavor
   *     the requested flavor for the data
   * @return boolean indicating whether or not the data flavor is supported
   */
  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    DataFlavor[] flavors = getTransferDataFlavors();
    for (DataFlavor flavor1 : flavors) {
      if (flavor1.equals(flavor)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns an object which represents the data to be transferred. The class of
   * the object returned is defined by the representation class of the flavor.
   *
   * @param flavor
   *     the requested flavor for the data
   * @return the transferred object.
   *
   * @throws IOException
   *     if the data is no longer available in the requested flavor.
   * @throws UnsupportedFlavorException
   *     if the requested data flavor is not supported.
   * @see DataFlavor#getRepresentationClass
   */
  @Override
  public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException, IOException {
    if (isRicherFlavor(flavor)) {
      return getRicherData(flavor);
    }
    if (isHTMLFlavor(flavor)) {
      String data = getHTMLData();
      if (data == null) {
        data = "";
      }
      if (String.class.equals(flavor.getRepresentationClass())) {
        return data;
      } else if (Reader.class.equals(flavor.getRepresentationClass())) {
        return new StringReader(data);
      } else if (InputStream.class.equals(flavor.getRepresentationClass())) {
        return new ByteArrayInputStream(data.getBytes());
      }
      // fall through to unsupported
    } else if (isPlainFlavor(flavor)) {
      String data = getPlainData();
      if (data == null) {
        data = "";
      }
      if (String.class.equals(flavor.getRepresentationClass())) {
        return data;
      } else if (Reader.class.equals(flavor.getRepresentationClass())) {
        return new StringReader(data);
      } else if (InputStream.class.equals(flavor.getRepresentationClass())) {
        return new ByteArrayInputStream(data.getBytes());
      }
      // fall through to unsupported

    } else if (isStringFlavor(flavor)) {
      String data = getPlainData();
      if (data == null) {
        data = "";
      }
      return data;
    }
    throw new UnsupportedFlavorException(flavor);
  }

  // --- richer subclass flavors ----------------------------------------------

  /**
   * Tests whether the passed in flavor is richer than this.
   *
   * @param flavor
   *     the flavor to test.
   * @return @{code true} if the flavor is richer than this.
   */
  protected boolean isRicherFlavor(DataFlavor flavor) {
    DataFlavor[] richerFlavors = getRicherFlavors();
    if (richerFlavors != null) {
      for (DataFlavor richerFlavor : richerFlavors) {
        if (richerFlavor.equals(flavor)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Some subclasses will have flavors that are more descriptive than HTML or
   * plain text. If this method returns a non-null value, it will be placed at
   * the start of the array of supported flavors.
   *
   * @return the richer compatible flavors.
   */
  protected DataFlavor[] getRicherFlavors() {
    return null;
  }

  /**
   * Gets richer data.
   *
   * @param flavor
   *     the flavor to get the data.
   * @throws UnsupportedFlavorException
   *     whenever this flavor is not supported.
   * @return the richer data.
   */
  @SuppressWarnings({"unused", "UnusedParameters"})
  protected Object getRicherData(DataFlavor flavor)
      throws UnsupportedFlavorException {
    return null;
  }

  // --- html flavors ----------------------------------------------------------

  /**
   * Returns whether or not the specified data flavor is an HTML flavor that is
   * supported.
   *
   * @param flavor
   *     the requested flavor for the data
   * @return boolean indicating whether or not the data flavor is supported
   */
  protected boolean isHTMLFlavor(DataFlavor flavor) {
    DataFlavor[] flavors = HTML_FLAVORS;
    for (DataFlavor flavor1 : flavors) {
      if (flavor1.equals(flavor)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Should the HTML flavors be offered? If so, the method getHTMLData should be
   * implemented to provide something reasonable.
   *
   * @return true if HTML is supported.
   */
  protected boolean isHTMLSupported() {
    return htmlData != null;
  }

  /**
   * Fetch the data in a text/html format.
   *
   * @return HTML data.
   */
  protected String getHTMLData() {
    return htmlData;
  }

  // --- plain text flavors ----------------------------------------------------

  /**
   * Returns whether or not the specified data flavor is an plain flavor that is
   * supported.
   *
   * @param flavor
   *     the requested flavor for the data
   * @return boolean indicating whether or not the data flavor is supported
   */
  protected boolean isPlainFlavor(DataFlavor flavor) {
    DataFlavor[] flavors = PLAIN_FLAVORS;
    for (DataFlavor flavor1 : flavors) {
      if (flavor1.equals(flavor)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Should the plain text flavors be offered? If so, the method getPlainData
   * should be implemented to provide something reasonable.
   *
   * @return true if plain flavor is supported.
   */
  protected boolean isPlainSupported() {
    return plainData != null;
  }

  /**
   * Fetch the data in a text/plain format.
   *
   * @return plain data.
   */
  protected String getPlainData() {
    return plainData;
  }

  // --- string flavors
  // --------------------------------------------------------

  /**
   * Returns whether or not the specified data flavor is a String flavor that is
   * supported.
   *
   * @param flavor
   *     the requested flavor for the data
   * @return boolean indicating whether or not the data flavor is supported
   */
  protected boolean isStringFlavor(DataFlavor flavor) {
    DataFlavor[] flavors = STRING_FLAVORS;
    for (DataFlavor flavor1 : flavors) {
      if (flavor1.equals(flavor)) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
    // Don't do anything
  }

}
