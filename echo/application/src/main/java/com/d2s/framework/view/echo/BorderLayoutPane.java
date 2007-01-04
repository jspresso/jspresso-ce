/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.echo;

import java.awt.BorderLayout;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.layout.SplitPaneLayoutData;

/**
 * An echo border layout pane.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BorderLayoutPane extends SplitPane {

  private static final long  serialVersionUID = -3145790890189352222L;

  /**
   * <code>NORTH</code>.
   */
  public static final String NORTH            = "0";

  /**
   * <code>SOUTH</code>.
   */
  public static final String SOUTH            = "1";

  /**
   * <code>EAST</code>.
   */
  public static final String EAST             = "2";

  /**
   * <code>WEST</code>.
   */
  public static final String WEST             = "3";

  /**
   * <code>CENTER</code>.
   */
  public static final String CENTER           = "4";

  private static final int   I_NORTH           = 0;
  private static final int   I_SOUTH           = 1;
  private static final int   I_EAST            = 2;
  private static final int   I_WEST            = 3;
  private static final int   I_CENTER          = 4;

  private Component          northC;
  private Component          southC;
  private Component          centerC;
  private Component          westC;
  private Component          eastC;

  /**
   * Constructs a new <code>BorderLayoutPane</code> instance.
   */
  public BorderLayoutPane() {
    super(SplitPane.ORIENTATION_VERTICAL, new Extent(80, Extent.PX));

    setStyleName("DefaultResizable");

    SplitPaneLayoutData splitPaneLayoutData;

    splitPaneLayoutData = new SplitPaneLayoutData();
    splitPaneLayoutData.setInsets(new Insets(10));
    ContentPane north = new ContentPane();
    add(north);
    northC = new Column();
    north.add(north);
    add(new Label(), BorderLayout.NORTH);

    SplitPane splitPaneNorth = new SplitPane(
        SplitPane.ORIENTATION_HORIZONTAL_LEADING_TRAILING, new Extent(150));
    splitPaneNorth.setStyleName("DefaultResizable");
    add(splitPaneNorth);

    splitPaneLayoutData = new SplitPaneLayoutData();
    splitPaneLayoutData.setInsets(new Insets(10));
    Component west = new ContentPane();
    splitPaneNorth.add(west);
    westC = new Row();
    west.add(westC);
    add(new Label(), BorderLayout.WEST);

    SplitPane splitPaneWest = new SplitPane(
        SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(150));
    splitPaneWest.setStyleName("DefaultResizable");
    splitPaneNorth.add(splitPaneWest);

    splitPaneLayoutData = new SplitPaneLayoutData();
    splitPaneLayoutData.setInsets(new Insets(10));
    Component south = new ContentPane();
    splitPaneWest.add(south);
    southC = new Column();
    south.add(southC);
    add(new Label(), BorderLayout.SOUTH);

    SplitPane splitPaneSouth = new SplitPane(
        SplitPane.ORIENTATION_HORIZONTAL_TRAILING_LEADING, new Extent(150));
    splitPaneSouth.setStyleName("DefaultResizable");
    splitPaneWest.add(splitPaneSouth);

    splitPaneLayoutData = new SplitPaneLayoutData();
    splitPaneLayoutData.setInsets(new Insets(10));
    Component east = new ContentPane();
    splitPaneSouth.add(east);
    eastC = new Row();
    east.add(eastC);
    add(new Label(), BorderLayout.EAST);

    Component center = new ContentPane();
    splitPaneSouth.add(center);
    centerC = new ContentPane();
    center.add(centerC);
    add(new Label(), BorderLayout.CENTER);
  }

  /**
   * Adds a child component.
   *
   * @param c
   *          the component to add.
   * @param pos
   *          one of the allowed posisions.
   */
  public void add(Component c, String pos) {
    switch (Integer.parseInt(pos)) {
      case I_NORTH:
        northC.removeAll();
        northC.add(c);
        break;
      case I_SOUTH:
        southC.removeAll();
        southC.add(c);
        break;
      case I_EAST:
        eastC.removeAll();
        eastC.add(c);
        break;
      case I_WEST:
        westC.removeAll();
        westC.add(c);
        break;
      case I_CENTER:
        centerC.removeAll();
        centerC.add(c);
        break;
      default:
        throw new IllegalArgumentException(pos);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(Component c) {
    // don't care which pane it's in, try remove it from all of them
    northC.remove(c);
    southC.remove(c);
    eastC.remove(c);
    westC.remove(c);
    centerC.remove(c);
  }

}
