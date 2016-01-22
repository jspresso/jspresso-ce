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
package org.jspresso.framework.application.frontend.action.module;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.action.IActionHandlerAware;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.util.gate.AbstractGate;
import org.jspresso.framework.util.gate.IGate;

/**
 * This action is used to save module and sub-modules objects.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class SaveModuleObjectFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  private       boolean dirtyTrackingEnabled;
  private final IGate   dirtyModuleGate;

  /**
   * Instantiates a new Save module object front action.
   */
  public SaveModuleObjectFrontAction() {
    dirtyTrackingEnabled = false;
    dirtyModuleGate = new DirtyModuleGate();
  }

  /**
   * Augments the actionability gates with the internal gate tracking the module dirtiness.
   *
   * @return the augmented actionability gates
   */
  @Override
  public Collection<IGate> getActionabilityGates() {
    Collection<IGate> existingGates = super.getActionabilityGates();
    if (!isDirtyTrackingEnabled()) {
      return existingGates;
    }
    Collection<IGate> gates = new HashSet<>();
    gates.add(dirtyModuleGate);
    if (existingGates != null) {
      gates.addAll(existingGates);
    }
    return gates;
  }

  private static class DirtyModuleGate extends AbstractGate implements IActionHandlerAware {

    private IFrontendController<?, ?, ?> frontendController;
    private PropertyChangeListener       moduleListener;
    private boolean                      open;

    public DirtyModuleGate() {
      createModuleListener();
    }

    private void createModuleListener() {
      moduleListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          setOpen((Boolean) evt.getNewValue());
        }
      };
    }

    @Override
    public void setActionHandler(IActionHandler actionHandler) {
      if (actionHandler instanceof IFrontendController) {
        this.frontendController = (IFrontendController<?, ?, ?>) actionHandler;
        frontendController.addPropertyChangeListener(IFrontendController.SELECTED_MODULE, new PropertyChangeListener() {
          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            Module oldModule = (Module) evt.getOldValue();
            Module newModule = (Module) evt.getNewValue();
            if (oldModule != null) {
              oldModule.removePropertyChangeListener(Module.DIRTY, moduleListener);
            }
            if (newModule != null) {
              newModule.addWeakPropertyChangeListener(Module.DIRTY, moduleListener);
              setOpen(newModule.isDirty());
            } else {
              setOpen(false);
            }
          }
        });
      }
    }

    /**
     * Is open.
     *
     * @return the boolean
     */
    @Override
    public boolean isOpen() {
      return open;
    }

    /**
     * Sets open.
     *
     * @param open
     *     the open
     */
    public void setOpen(boolean open) {
      boolean oldOpen = this.open;
      this.open = open;
      firePropertyChange(IGate.OPEN_PROPERTY, oldOpen, open);
    }

    @Override
    public DirtyModuleGate clone() {
      DirtyModuleGate clone = (DirtyModuleGate) super.clone();
      clone.createModuleListener();
      return clone;
    }
  }

  /**
   * Is dirty tracking enabled.
   *
   * @return the boolean
   */
  protected boolean isDirtyTrackingEnabled() {
    return dirtyTrackingEnabled;
  }

  /**
   * Set dirty tracking enabled.
   *
   * @param dirtyTrackingEnabled the dirty tracking enabled
   */
  public void setDirtyTrackingEnabled(boolean dirtyTrackingEnabled) {
    this.dirtyTrackingEnabled = dirtyTrackingEnabled;
  }
}
