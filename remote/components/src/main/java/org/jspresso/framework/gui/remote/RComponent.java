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
package org.jspresso.framework.gui.remote;

import java.util.Set;

import gnu.trove.set.hash.THashSet;

import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.remote.RemotePeer;

/**
 * This class is the generic server peer of a client GUI component.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RComponent extends RemotePeer implements
    IRemoteStateOwner {

  private static final long serialVersionUID = 4728316436476683941L;

  private RActionList[]    actionLists;
  private RActionList[]    secondaryActionLists;
  private String           background;
  private RemoteValueState backgroundState;
  private String           borderType;
  private Font             font;
  private RemoteValueState fontState;
  private String           foreground;
  private RemoteValueState foregroundState;
  private RIcon            icon;
  private String           label;
  private RemoteValueState labelState;
  private Dimension        preferredSize;
  private RemoteValueState state;
  private String           toolTip;
  private RemoteValueState toolTipState;
  private String           styleName;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final transient Set<RAction> actionHardReferences;

  /**
   * Constructs a new {@code RComponent} instance.
   *
   * @param guid
   *          the guid.
   */
  public RComponent(String guid) {
    super(guid);
    actionHardReferences = new THashSet<>();
  }

  /**
   * Constructs a new {@code RComponent} instance. Only used for
   * serialization support.
   */
  public RComponent() {
    // For serialization support
    actionHardReferences = new THashSet<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object actualValue() {
    return getState().getValue();
  }

  /**
   * Gets the actionLists.
   *
   * @return the actionLists.
   */
  public RActionList[] getActionLists() {
    return actionLists;
  }

  /**
   * Gets the background.
   *
   * @return the background.
   */
  public String getBackground() {
    return background;
  }

  /**
   * Gets the borderType.
   *
   * @return the borderType.
   */
  public String getBorderType() {
    return borderType;
  }

  /**
   * Gets the font.
   *
   * @return the font.
   */
  public Font getFont() {
    return font;
  }

  /**
   * Gets the foreground.
   *
   * @return the foreground.
   */
  public String getForeground() {
    return foreground;
  }

  /**
   * Gets the icon.
   *
   * @return the icon.
   */
  public RIcon getIcon() {
    return icon;
  }

  /**
   * Gets the name.
   *
   * @return the name.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Gets the preferredSize.
   *
   * @return the preferredSize.
   */
  public Dimension getPreferredSize() {
    return preferredSize;
  }

  /**
   * Gets the state.
   *
   * @return the state.
   */
  @Override
  public RemoteValueState getState() {
    return state;
  }

  /**
   * Gets the description.
   *
   * @return the description.
   */
  public String getToolTip() {
    return toolTip;
  }

  /**
   * Sets the actionLists.
   *
   * @param actionLists
   *          the actionLists to set.
   */
  public void setActionLists(RActionList... actionLists) {
    this.actionLists = actionLists;
  }

  /**
   * Sets the background.
   *
   * @param background
   *          the background to set.
   */
  public void setBackground(String background) {
    this.background = background;
  }

  /**
   * Sets the borderType.
   *
   * @param borderType
   *          the borderType to set.
   */
  public void setBorderType(String borderType) {
    this.borderType = borderType;
  }

  /**
   * Sets the font.
   *
   * @param font
   *          the font to set.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * Sets the foreground.
   *
   * @param foreground
   *          the foreground to set.
   */
  public void setForeground(String foreground) {
    this.foreground = foreground;
  }

  /**
   * Sets the icon.
   *
   * @param icon
   *          the icon to set.
   */
  public void setIcon(RIcon icon) {
    this.icon = icon;
  }

  /**
   * Sets the label.
   *
   * @param label
   *          the label to set.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Sets the preferredSize.
   *
   * @param preferredSize
   *          the preferredSize to set.
   */
  public void setPreferredSize(Dimension preferredSize) {
    this.preferredSize = preferredSize;
  }

  /**
   * Sets the state.
   *
   * @param state
   *          the state to set.
   */
  public void setState(RemoteValueState state) {
    this.state = state;
  }

  /**
   * Sets the toolTip.
   *
   * @param toolTip
   *          the toolTip to set.
   */
  public void setToolTip(String toolTip) {
    this.toolTip = toolTip;
  }

  /**
   * Gets the toolTipState.
   *
   * @return the toolTipState.
   */
  public RemoteValueState getToolTipState() {
    return toolTipState;
  }

  /**
   * Sets the toolTipState.
   *
   * @param toolTipState
   *          the toolTipState to set.
   */
  public void setToolTipState(RemoteValueState toolTipState) {
    this.toolTipState = toolTipState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchRemoteState() {
    // Empty implementation
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueFromState(Object stateValue) {
    // Empty implementation.
  }

  /**
   * Gets the secondaryActionLists.
   *
   * @return the secondaryActionLists.
   */
  public RActionList[] getSecondaryActionLists() {
    return secondaryActionLists;
  }

  /**
   * Sets the secondaryActionLists.
   *
   * @param secondaryActionLists
   *          the secondaryActionLists to set.
   */
  public void setSecondaryActionLists(RActionList... secondaryActionLists) {
    this.secondaryActionLists = secondaryActionLists;
  }

  /**
   * Gets the styleName.
   *
   * @return the styleName.
   */
  public String getStyleName() {
    return styleName;
  }

  /**
   * Sets the styleName.
   *
   * @param styleName
   *          the styleName to set.
   */
  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  /**
   * Gets the backgroundState.
   *
   * @return the backgroundState.
   */
  public RemoteValueState getBackgroundState() {
    return backgroundState;
  }

  /**
   * Sets the backgroundState.
   *
   * @param backgroundState
   *          the backgroundState to set.
   */
  public void setBackgroundState(RemoteValueState backgroundState) {
    this.backgroundState = backgroundState;
  }

  /**
   * Gets the foregroundState.
   *
   * @return the foregroundState.
   */
  public RemoteValueState getForegroundState() {
    return foregroundState;
  }

  /**
   * Sets the foregroundState.
   *
   * @param foregroundState
   *          the foregroundState to set.
   */
  public void setForegroundState(RemoteValueState foregroundState) {
    this.foregroundState = foregroundState;
  }

  /**
   * Gets the fontState.
   *
   * @return the fontState.
   */
  public RemoteValueState getFontState() {
    return fontState;
  }

  /**
   * Sets the fontState.
   *
   * @param fontState
   *          the fontState to set.
   */
  public void setFontState(RemoteValueState fontState) {
    this.fontState = fontState;
  }

  /**
   * Gets label state.
   *
   * @return the label state
   */
  public RemoteValueState getLabelState() {
    return labelState;
  }

  /**
   * Sets label state.
   *
   * @param labelState the label state
   */
  public void setLabelState(RemoteValueState labelState) {
    this.labelState = labelState;
  }

  /**
   * Add referenced action to prevent it from being garbage collected.
   *
   * @param action the action
   */
  public void addReferencedAction(RAction action) {
    actionHardReferences.add(action);
  }

  /**
   * NO-OP
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setRemoteStateValueMapper(IRemoteStateValueMapper remoteStateValueMapper) {
    // NO OP
  }
}
