/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.ulc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.descriptor.module.IModuleDescriptor;
import com.ulcjava.base.application.AbstractAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
import com.ulcjava.base.application.ULCFrame;
import com.ulcjava.base.application.ULCInternalFrame;
import com.ulcjava.base.application.ULCMenu;
import com.ulcjava.base.application.ULCMenuBar;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * Default implementation of a swing frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultUlcController extends
    AbstractFrontendController<ULCComponent> {

  private ULCFrame                      controllerFrame;
  private Map<String, ULCInternalFrame> moduleInternalFrames;

  /**
   * Creates the initial view from the root view descriptor, then a JFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale locale) {
    if (super.start(backendController, locale)) {
      controllerFrame = createControllerFrame();
      controllerFrame.pack();
      controllerFrame.setSize(1024, 768);
      controllerFrame.setVisible(true);
      return true;
    }
    return false;
  }

  private void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, ULCInternalFrame>();
    }
    ULCInternalFrame moduleInternalFrame = moduleInternalFrames.get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IView<ULCComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor);
      moduleInternalFrame = createULCInternalFrame(moduleView);
      moduleInternalFrame.setFrameIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(),
          getBackendController().getModuleConnector(moduleId));
      moduleInternalFrame.pack();
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIcon()) {
      moduleInternalFrame.setIcon(false);
    }
    moduleInternalFrame.setMaximum(true);
    moduleInternalFrame.moveToFront();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getSelectedModuleId() {
    for (Map.Entry<String, ULCInternalFrame> moduleIdAndFrame : moduleInternalFrames
        .entrySet()) {
      if (moduleIdAndFrame.getValue() != null
          && moduleIdAndFrame.getValue().isSelected()) {
        return moduleIdAndFrame.getKey();
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleId(@SuppressWarnings("unused")
  String selectedModuleId) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IIconFactory<ULCIcon> getIconFactory() {
    return (IIconFactory<ULCIcon>) super.getIconFactory();
  }

  private ULCFrame createControllerFrame() {
    ULCFrame frame = new ULCFrame(getLabelTranslator().getTranslation(
        getName(), getLocale()));
    frame.setContentPane(new ULCDesktopPane());
    frame.setIconImage(getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    frame.setDefaultCloseOperation(ULCFrame.TERMINATE_ON_CLOSE);
    frame.setMenuBar(getApplicationMenuBar());
    return frame;
  }

  private ULCMenuBar getApplicationMenuBar() {
    ULCMenuBar applicationMenuBar = new ULCMenuBar();
    applicationMenuBar.add(getModulesMenu());
    return applicationMenuBar;
  }

  private ULCMenu getModulesMenu() {
    ULCMenu modulesMenu = new ULCMenu(getLabelTranslator().getTranslation(
        "Modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      ULCMenuItem moduleMenuItem = new ULCMenuItem(new ModuleSelectionAction(
          moduleId, moduleDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    return modulesMenu;
  }

  private final class ModuleSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 3469745193806038352L;
    private String            moduleId;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     * 
     * @param moduleId
     * @param moduleDescriptor
     */
    public ModuleSelectionAction(String moduleId,
        IModuleDescriptor moduleDescriptor) {
      this.moduleId = moduleId;
      putValue(com.ulcjava.base.application.IAction.NAME, getLabelTranslator()
          .getTranslation(moduleDescriptor.getName(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          getDescriptionTranslator().getTranslation(
              moduleDescriptor.getDescription(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SMALL_ICON,
          getIconFactory().getIcon(moduleDescriptor.getIconImageURL(),
              IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      displayModule(moduleId);
    }
  }

  /**
   * Creates a new ULCInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private ULCInternalFrame createULCInternalFrame(IView<ULCComponent> view) {
    ULCInternalFrame internalFrame = new ULCInternalFrame(getLabelTranslator()
        .getTranslation(view.getDescriptor().getName(), getLocale()), true,
        false, true, true);
    internalFrame.getContentPane().add(view.getPeer());
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
  }
}
