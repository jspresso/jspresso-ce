/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.wings.components.plaf;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.wings.SCellRendererPane;
import org.wings.SClickable;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SListSelectionModel;
import org.wings.STable;
import org.wings.io.Device;
import org.wings.io.StringBuilderDevice;
import org.wings.plaf.CGManager;
import org.wings.plaf.TableCG;
import org.wings.plaf.Update;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.AbstractUpdate;
import org.wings.plaf.css.UpdateHandler;
import org.wings.plaf.css.Utils;
import org.wings.session.SessionManager;
import org.wings.table.SDefaultTableCellRenderer;
import org.wings.table.STableCellRenderer;
import org.wings.table.STableColumn;
import org.wings.table.STableColumnModel;

/**
 * A clickable header table CG. Since default TableCG is final, it can't be
 * overriden, so I had to copy the code to override the
 * <code>writeHeaderCell()</code> method.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("all")
public class ClickableHeaderTableCG extends AbstractComponentCG implements
    TableCG {

  private static final long serialVersionUID     = 1L;
  protected SIcon           editIcon;
  protected String          fixedTableBorderWidth;
  protected String          selectionColumnWidth = "30";

  int                       horizontalOversize   = 4;

  /**
   * Initialize properties from config
   */
  public ClickableHeaderTableCG() {
    final CGManager manager = SessionManager.getSession().getCGManager();
    setFixedTableBorderWidth((String) manager.getObject(
        "TableCG.fixedTableBorderWidth", String.class));
    setEditIcon(manager.getIcon("TableCG.editIcon"));
    selectionColumnWidth = (String) manager.getObject(
        "TableCG.selectionColumnWidth", String.class);
  }

  public static void printClickability(final Device device,
      final SComponent component, final String eventValue,
      final boolean formComponent) throws IOException {
    device.print(" onclick=\"return wingS.table.cellClick(");
    device.print("event,this,");
    device.print(formComponent + ",");
    device.print(!component.isReloadForced() + ",'");
    device.print(Utils.event(component));
    device.print("','");
    device.print(eventValue == null ? "" : eventValue);
    device.print("'");
    device.print(");\"");
  }

  public Update getEditCellUpdate(STable table, int row, int column) {
    return new EditCellUpdate(table, row, column);
  }

  /**
   * @return Returns the icon used to indicated an editable cell (if content is
   *         not direct clickable).
   */
  public SIcon getEditIcon() {
    return editIcon;
  }

  /**
   * Tweak property. Declares a deprecated BORDER=xxx attribute on the HTML
   * TABLE element.
   */
  public String getFixedTableBorderWidth() {
    return fixedTableBorderWidth;
  }

  public int getHorizontalOversize() {
    return horizontalOversize;
  }

  public Update getRenderCellUpdate(STable table, int row, int column) {
    return new RenderCellUpdate(table, row, column);
  }

  /**
   * @return The width of the (optional) row selection column in px
   */
  public String getSelectionColumnWidth() {
    return selectionColumnWidth;
  }

  public Update getSelectionUpdate(STable table, List deselectedIndices,
      List selectedIndices) {
    return new SelectionUpdate(table, deselectedIndices, selectedIndices);
  }

  public Update getTableScrollUpdate(STable table, Rectangle newViewport,
      Rectangle oldViewport) {
    // return new TableScrollUpdate(table);
    return new ComponentUpdate(this, table);
  }

  public String getToggleSortParameter(int col) {
    return "o" + col;
  }

  public void installCG(final SComponent comp) {
    super.installCG(comp);

    final STable table = (STable) comp;
    final CGManager manager = table.getSession().getCGManager();
    Object value;

    value = manager.getObject("STable.defaultRenderer",
        STableCellRenderer.class);
    if (value != null) {
      table.setDefaultRenderer((STableCellRenderer) value);
      if (value instanceof SDefaultTableCellRenderer) {
        SDefaultTableCellRenderer cellRenderer = (SDefaultTableCellRenderer) value;
        cellRenderer.setEditIcon(editIcon);
      }
    }

    value = manager
        .getObject("STable.headerRenderer", STableCellRenderer.class);
    if (value != null) {
      table.setHeaderRenderer((STableCellRenderer) value);
    }

    value = manager.getObject("STable.rowSelectionRenderer",
        org.wings.table.STableCellRenderer.class);
    if (value != null) {
      table.setRowSelectionRenderer((org.wings.table.STableCellRenderer) value);
    }

    if (isMSIE(table))
      table.putClientProperty("horizontalOversize", new Integer(
          horizontalOversize));
  }

  /**
   * Sets the icon used to indicated an editable cell (if content is not direct
   * clickable).
   */
  public void setEditIcon(SIcon editIcon) {
    this.editIcon = editIcon;
  }

  /**
   * Tweak property. Declares a deprecated BORDER=xxx attribute on the HTML
   * TABLE element.
   */
  public void setFixedTableBorderWidth(String fixedTableBorderWidth) {
    this.fixedTableBorderWidth = fixedTableBorderWidth;
  }

  public void setHorizontalOversize(int horizontalOversize) {
    this.horizontalOversize = horizontalOversize;
  }

  /**
   * The width of the (optional) row selection column in px
   * 
   * @param selectionColumnWidth
   *          The width of the (optional) row selection column with unit
   */
  public void setSelectionColumnWidth(String selectionColumnWidth) {
    this.selectionColumnWidth = selectionColumnWidth;
  }

  public void uninstallCG(SComponent component) {
    super.uninstallCG(component);
    final STable table = (STable) component;
    table.setHeaderRenderer(null);
    table.setDefaultRenderer(null);
    table.setRowSelectionRenderer(null);
  }

  public final void writeInternal(final Device device, final SComponent _c)
      throws IOException {
    final STable table = (STable) _c;

    device.print("<table");
    Utils.writeAllAttributes(device, table);
    writeTableAttributes(device, table);
    device.print("><thead>");
    Utils.printNewline(device, table);

    Rectangle currentViewport = table.getViewportSize();
    Rectangle maximalViewport = table.getScrollableViewportSize();
    int startX = 0;
    int endX = table.getVisibleColumnCount();
    int startY = 0;
    int endY = table.getRowCount();
    int emptyIndex = maximalViewport != null ? maximalViewport.height : endY;

    if (currentViewport != null) {
      startX = currentViewport.x;
      endX = startX + currentViewport.width;
      startY = currentViewport.y;
      endY = startY + currentViewport.height;
    }

    writeColumnWidths(device, table, startX, endX);
    writeHeader(device, table, startX, endX);

    device.print("</thead>");
    Utils.printNewline(device, table);
    device.print("<tbody>");

    writeBody(device, table, startX, endX, startY, endY, emptyIndex);

    device.print("</tbody></table>");
  }

  /**
   * write a specific cell to the device
   */
  protected void renderCellContent(final Device device, final STable table,
      final SCellRendererPane rendererPane, final int row, final int col)
      throws IOException {
    final boolean isEditingCell = table.isEditing()
        && row == table.getEditingRow() && col == table.getEditingColumn();
    final boolean editableCell = table.isCellEditable(row, col);
    final boolean selectableCell = table.getSelectionMode() != SListSelectionModel.NO_SELECTION
        && !table.isEditable() && table.isSelectable();

    final SComponent component;
    if (isEditingCell) {
      component = table.getEditorComponent();
    } else {
      component = table.prepareRenderer(table.getCellRenderer(row, col), row,
          col);
    }

    final boolean isClickable = component instanceof SClickable;

    device.print("<td col=\"");
    device.print(col);
    device.print("\"");

    if (component == null) {
      device.print("></td>");
      return;
    }
    Utils.printTableCellAlignment(device, component, SConstants.LEFT,
        SConstants.TOP);
    Utils.optAttribute(device, "oversize", horizontalOversize);

    String parameter = null;
    if (table.isEditable() && editableCell)
      parameter = table.getEditParameter(row, col);
    else if (selectableCell)
      parameter = table.getToggleSelectionParameter(row, col);

    if (parameter != null && (selectableCell || editableCell) && !isClickable) {
      printClickability(device, table, parameter, table
          .getShowAsFormComponent());
      device.print(isEditingCell ? " editing=\"true\"" : " editing=\"false\"");
      device.print(isEditingCell ? " class=\"cell\""
          : " class=\"cell clickable\"");
    } else
      device.print(" class=\"cell\"");
    device.print(">");

    rendererPane.writeComponent(device, component, table);

    device.print("</td>");
    Utils.printNewline(device, component);
  }

  protected void writeBody(Device device, STable table, int startX, int endX,
      int startY, int endY, int emptyIndex) throws IOException {
    final SListSelectionModel selectionModel = table.getSelectionModel();

    StringBuilder selectedArea = Utils.inlineStyles(table
        .getDynamicStyle(STable.SELECTOR_SELECTED));
    StringBuilder evenArea = Utils.inlineStyles(table
        .getDynamicStyle(STable.SELECTOR_EVEN_ROWS));
    StringBuilder oddArea = Utils.inlineStyles(table
        .getDynamicStyle(STable.SELECTOR_ODD_ROWS));
    final SCellRendererPane rendererPane = table.getCellRendererPane();
    STableColumnModel columnModel = table.getColumnModel();

    for (int r = startY; r < endY; ++r) {
      writeTableRow(device, table, columnModel, selectionModel, rendererPane,
          r, startX, endX, emptyIndex, selectedArea, oddArea, evenArea);
    }
  }

  protected void writeHeaderCell(final Device device, final STable table,
      final SCellRendererPane rendererPane, final int col) throws IOException {
    // final SComponent comp = table.prepareHeaderRenderer(table
    // .getHeaderRenderer(col), col);
    //
    // device.print("<th col=\"");
    // device.print(col);
    // device.print("\" class=\"head\"");
    //
    // Utils.printTableCellAlignment(device, comp, SConstants.CENTER,
    // SConstants.CENTER);
    // device.print(">");
    //
    // rendererPane.writeComponent(device, comp, table);
    //
    // device.print("</th>");
    // Utils.printNewline(device, comp);
    final SComponent comp = table.prepareHeaderRenderer(table
        .getHeaderRenderer(col), col);

    device.print("<th col=\"");
    device.print(col);
    device.print("\"");

    Utils.printTableCellAlignment(device, comp, SConstants.CENTER,
        SConstants.CENTER);

    String parameter = getToggleSortParameter(col);

    Utils.printClickability(device, table, parameter, true, table
        .getShowAsFormComponent());
    device.print(" class=\"clickable head\"");

    device.print(">");

    rendererPane.writeComponent(device, comp, table);

    device.print("</th>");
    Utils.printNewline(device, comp);
  }

  /**
   * Renders the row sometimes needed to allow row selection.
   */
  protected void writeSelectionBody(final Device device, final STable table,
      final SCellRendererPane rendererPane, final int row) throws IOException {
    final STableCellRenderer rowSelectionRenderer = table
        .getRowSelectionRenderer();
    if (isSelectionColumnVisible(table)) {
      final SComponent comp = rowSelectionRenderer
          .getTableCellRendererComponent(table, table
              .getToggleSelectionParameter(row, -1), table.isRowSelected(row),
              row, -1);
      final String columnStyle = Utils.joinStyles(comp, "num");

      device.print("<td valign=\"top\" align=\"right\"");
      Utils.optAttribute(device, "width", selectionColumnWidth);

      String value = table.getToggleSelectionParameter(row, -1);
      if (table.getSelectionMode() != SListSelectionModel.NO_SELECTION) {
        printClickability(device, table, value, table.getShowAsFormComponent());
        device.print(" class=\"clickable ");
        device.print(columnStyle);
        device.print("\"");
      } else {
        device.print(" class=\"");
        device.print(columnStyle);
        device.print("\"");
      }
      device.print(">");

      // Renders the content of the row selection row
      rendererPane.writeComponent(device, comp, table);

      device.print("</td>");
    }
  }

  protected void writeSelectionHeader(Device device, STable table)
      throws IOException {
    if (isSelectionColumnVisible(table)) {
      device.print("<th valign=\"middle\" class=\"num\"");
      Utils.optAttribute(device, "width", selectionColumnWidth);
      device.print("></th>");
    }
  }

  protected void writeTableRow(Device device, STable table,
      STableColumnModel columnModel, SListSelectionModel selectionModel,
      SCellRendererPane rendererPane, final int rowIndex, int startX, int endX,
      int emptyIndex, StringBuilder selectedArea, StringBuilder oddArea,
      StringBuilder evenArea) throws IOException {
    if (rowIndex >= emptyIndex) {
      int colspan = endX - startX;
      device.print("<tr>\n");
      if (isSelectionColumnVisible(table)) {
        device.print("  <td></td>\n");
      }
      device.print("  <td class=\"empty\" colspan=\"" + colspan
          + "\">&nbsp;</td>\n");
      device.print("</tr>\n");
      return;
    }

    String rowStyle = table.getRowStyle(rowIndex);
    StringBuilder rowClass = new StringBuilder(rowStyle != null ? rowStyle
        + " " : "");
    device.print("<tr");
    if (selectionModel.isSelectedIndex(rowIndex)) {
      Utils.optAttribute(device, "style", selectedArea);
      rowClass.append("selected ");
    } else if (rowIndex % 2 != 0)
      Utils.optAttribute(device, "style", oddArea);
    else
      Utils.optAttribute(device, "style", evenArea);

    rowClass.append(rowIndex % 2 != 0 ? "odd" : "even");
    Utils.optAttribute(device, "class", rowClass);
    device.print(">");

    writeSelectionBody(device, table, rendererPane, rowIndex);

    for (int c = startX; c < endX; ++c) {
      STableColumn column = columnModel.getColumn(c);
      if (!column.isHidden())
        renderCellContent(device, table, rendererPane, rowIndex, c);
      else
        ++endX;
    }

    device.print("</tr>");
    Utils.printNewline(device, table);
  }

  private boolean atLeastOneColumnWidthIsNotNull(STableColumnModel columnModel) {
    int columnCount = columnModel.getColumnCount();
    for (int i = 0; i < columnCount; i++)
      if (columnModel.getColumn(i).getWidth() != null) return true;
    return false;
  }

  private int columnInView(STable table, int column) {
    int viewColumn = 0;
    for (int i = 0; i < column; i++) {
      STableColumn tableColumn = table.getColumnModel().getColumn(i);
      if (!tableColumn.isHidden()) viewColumn++;
    }
    return viewColumn;
  }

  private boolean isSelectionColumnVisible(STable table) {
    if (table.getRowSelectionRenderer() != null
        && table.getSelectionModel().getSelectionMode() != SListSelectionModel.NO_SELECTION)
      return true;
    return false;
  }

  private void writeCol(Device device, String width) throws IOException {
    device.print("<col");
    Utils.optAttribute(device, "width", width);
    device.print("/>");
  }

  private void writeColumnWidths(Device device, STable table, int startX,
      int endX) throws IOException {
    STableColumnModel columnModel = table.getColumnModel();
    if (columnModel != null && atLeastOneColumnWidthIsNotNull(columnModel)) {
      device.print("<colgroup>");
      if (isSelectionColumnVisible(table))
        writeCol(device, selectionColumnWidth);

      for (int i = startX; i < endX; ++i) {
        STableColumn column = columnModel.getColumn(i);
        if (!column.isHidden())
          writeCol(device, column.getWidth());
        else
          ++endX;
      }
      device.print("</colgroup>");
      Utils.printNewline(device, table);
    }
  }

  private void writeHeader(Device device, STable table, int startX, int endX)
      throws IOException {
    if (!table.isHeaderVisible()) return;

    final SCellRendererPane rendererPane = table.getCellRendererPane();
    STableColumnModel columnModel = table.getColumnModel();

    StringBuilder headerArea = Utils.inlineStyles(table
        .getDynamicStyle(STable.SELECTOR_HEADER));
    device.print("<tr class=\"header\"");
    Utils.optAttribute(device, "style", headerArea);
    device.print(">");

    Utils.printNewline(device, table, 1);
    writeSelectionHeader(device, table);

    for (int i = startX; i < endX; ++i) {
      STableColumn column = columnModel.getColumn(i);
      if (!column.isHidden())
        writeHeaderCell(device, table, rendererPane, i);
      else
        ++endX;
    }
    device.print("</tr>");
    Utils.printNewline(device, table);
  }

  private void writeTableAttributes(Device device, STable table)
      throws IOException {
    final SDimension intercellPadding = table.getIntercellPadding();
    final SDimension intercellSpacing = table.getIntercellSpacing();
    Utils.writeEvents(device, table, null);

    // TODO: border="" should be obsolete
    // TODO: cellspacing and cellpadding may be in conflict with border-collapse
    /* Tweaking: CG configured to have a fixed border="xy" width */
    Utils.optAttribute(device, "border", fixedTableBorderWidth);
    Utils.optAttribute(device, "cellspacing", ((intercellSpacing != null) ? ""
        + intercellSpacing.getWidthInt() : null));
    Utils.optAttribute(device, "cellpadding", ((intercellPadding != null) ? ""
        + intercellPadding.getHeightInt() : null));
  }

  protected class SelectionUpdate extends AbstractUpdate<STable> {

    private List<Integer> deselectedIndices;
    private List<Integer> selectedIndices;

    public SelectionUpdate(STable component, List<Integer> deselectedIndices,
        List<Integer> selectedIndices) {
      super(component);
      this.deselectedIndices = deselectedIndices;
      this.selectedIndices = selectedIndices;
    }

    public Handler getHandler() {
      int indexOffset = 0;
      if (component.isHeaderVisible()) {
        ++indexOffset;
      }

      UpdateHandler handler = new UpdateHandler("selectionTable");
      handler.addParameter(component.getName());
      handler.addParameter(new Integer(indexOffset));
      handler.addParameter(Utils.listToJsArray(deselectedIndices));
      handler.addParameter(Utils.listToJsArray(selectedIndices));
      if (isSelectionColumnVisible(component)) {
        String exception = null;
        List<String> deselectedBodies = new ArrayList<String>();
        List<String> selectedBodies = new ArrayList<String>();
        exception = writeSelectionBodies(deselectedIndices, deselectedBodies);
        exception = writeSelectionBodies(selectedIndices, selectedBodies);
        handler.addParameter(Utils.listToJsArray(deselectedBodies));
        handler.addParameter(Utils.listToJsArray(selectedBodies));
        if (exception != null) {
          handler.addParameter(exception);
        }
      }
      return handler;
    }

    private String writeSelectionBodies(List<Integer> indices,
        List<String> bodies) {
      try {
        final StringBuilderDevice htmlDevice = new StringBuilderDevice(512);
        final SCellRendererPane rendererPane = component.getCellRendererPane();
        for (Integer index : indices) {
          writeSelectionBody(htmlDevice, component, rendererPane, index);
          bodies.add(htmlDevice.toString());
          htmlDevice.reset();
        }
      } catch (Throwable t) {
        return t.getClass().getName();
      }
      return null;
    }
  }

  protected class TableScrollUpdate extends AbstractUpdate {

    public TableScrollUpdate(SComponent component) {
      super(component);
    }

    public Handler getHandler() {
      STable table = (STable) component;

      Rectangle currentViewport = table.getViewportSize();
      Rectangle maximalViewport = table.getScrollableViewportSize();
      int startX = 0;
      int endX = table.getVisibleColumnCount();
      int startY = 0;
      int endY = table.getRowCount();
      int emptyIndex = maximalViewport != null ? maximalViewport.height : endY;

      if (currentViewport != null) {
        startX = currentViewport.x;
        endX = startX + currentViewport.width;
        startY = currentViewport.y;
        endY = startY + currentViewport.height;
      }

      String htmlCode = "";
      String exception = null;

      try {
        StringBuilderDevice htmlDevice = new StringBuilderDevice();
        writeBody(htmlDevice, table, startX, endX, startY, endY, emptyIndex);
        htmlCode = htmlDevice.toString();
      } catch (Throwable t) {
        exception = t.getClass().getName();
      }

      UpdateHandler handler = new UpdateHandler("tableScroll");
      handler.addParameter(table.getName());
      handler.addParameter(htmlCode);
      if (exception != null) {
        handler.addParameter(exception);
      }
      return handler;
    }
  }

  private class EditCellUpdate extends AbstractUpdate<STable> {

    private int column;
    private int row;

    public EditCellUpdate(STable table, int row, int column) {
      super(table);
      this.row = row;
      this.column = column;
    }

    public Handler getHandler() {
      STable table = this.component;

      String htmlCode = "";
      String exception = null;

      try {
        StringBuilderDevice device = new StringBuilderDevice();
        /*
         * Utils.printTableCellAlignment(device, component, SConstants.LEFT,
         * SConstants.TOP); Utils.optAttribute(device, "oversize",
         * horizontalOversize); device.print(" class=\"cell\">");
         */
        SComponent component = table.getEditorComponent();
        table.getCellRendererPane().writeComponent(device, component, table);
        htmlCode = device.toString();
      } catch (Throwable t) {
        exception = t.getClass().getName();
      }

      row = table.isHeaderVisible() ? this.row + 1 : this.row;
      column = columnInView(table, column);
      column = isSelectionColumnVisible(table) ? column + 1 : column;

      Rectangle currentViewport = table.getViewportSize();
      if (currentViewport != null) {
        row -= currentViewport.y;
        column -= currentViewport.x;
      }

      UpdateHandler handler = new UpdateHandler("tableCell");
      handler.addParameter(table.getName());
      handler.addParameter(row);
      handler.addParameter(column);
      handler.addParameter(true);
      handler.addParameter(htmlCode);
      if (exception != null) {
        handler.addParameter(exception);
      }
      return handler;
    }
  }

  private class RenderCellUpdate extends AbstractUpdate<STable> {

    private int column;
    private int row;

    public RenderCellUpdate(STable table, int row, int column) {
      super(table);
      this.row = row;
      this.column = column;
    }

    public boolean equals(Object object) {
      if (!super.equals(object)) return false;

      RenderCellUpdate other = (RenderCellUpdate) object;

      if (this.row != other.row) return false;
      if (this.column != other.column) return false;

      return true;
    }

    public Handler getHandler() {
      STable table = this.component;

      String htmlCode = "";
      String exception = null;

      try {
        StringBuilderDevice device = new StringBuilderDevice(256);
        /*
         * Utils.printTableCellAlignment(device, component, SConstants.LEFT,
         * SConstants.TOP); Utils.optAttribute(device, "oversize",
         * horizontalOversize); device.print(" class=\"cell\">");
         */
        SComponent component = table.prepareRenderer(table.getCellRenderer(row,
            column), row, column);
        table.getCellRendererPane().writeComponent(device, component, table);
        htmlCode = device.toString();
      } catch (Throwable t) {
        exception = t.getClass().getName();
      }

      row = table.isHeaderVisible() ? this.row + 1 : this.row;
      column = columnInView(table, column);
      column = isSelectionColumnVisible(table) ? column + 1 : column;

      Rectangle currentViewport = table.getViewportSize();
      if (currentViewport != null) {
        row -= currentViewport.y;
        column -= currentViewport.x;
      }

      UpdateHandler handler = new UpdateHandler("tableCell");
      handler.addParameter(table.getName());
      handler.addParameter(row);
      handler.addParameter(column);
      handler.addParameter(false);
      handler.addParameter(htmlCode);
      if (exception != null) {
        handler.addParameter(exception);
      }
      return handler;
    }

    public int hashCode() {
      int hashCode = super.hashCode();
      int dispersionFactor = 37;

      hashCode = hashCode * dispersionFactor + row;
      hashCode = hashCode * dispersionFactor + column;

      return hashCode;
    }
  }
}
