/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.d2s.framework.application.ControllerException;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.WaitCursorTimer;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.descriptor.module.IModuleDescriptor;

import foxtrot.Job;

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
public class DefaultSwingController extends
    AbstractFrontendController<JComponent> {

  private JFrame                      controllerFrame;
  private Map<String, JInternalFrame> moduleInternalFrames;

  private WaitCursorTimer             waitTimer;

  /**
   * Creates the initial view from the root view descriptor, then a JFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start(IBackendController backendController, Locale locale) {
    super.start(backendController, locale);
    // Toolkit.getDefaultToolkit().getSystemEventQueue().push(new
    // WaitCursorEventQueue(500));
    waitTimer = new WaitCursorTimer(500);
    waitTimer.setDaemon(true);
    waitTimer.start();
    controllerFrame = createControllerFrame();
    controllerFrame.pack();
    controllerFrame.setSize(1100, 800);
    controllerFrame.setVisible(true);
  }

  private void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, JInternalFrame>();
    }
    JInternalFrame moduleInternalFrame = moduleInternalFrames
        .get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IView<JComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor);
      moduleInternalFrame = createJInternalFrame(moduleView);
      moduleInternalFrame.setFrameIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(),
          IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addInternalFrameListener(new ModuleInternalFrameListener(
              moduleId));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(),
          getBackendController().getModuleConnector(moduleId));
      moduleInternalFrame.pack();
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIcon()) {
      try {
        moduleInternalFrame.setIcon(false);
      } catch (PropertyVetoException ex) {
        throw new ControllerException(ex);
      }
    }
    try {
      moduleInternalFrame.setMaximum(true);
    } catch (PropertyVetoException ex) {
      throw new ControllerException(ex);
    }
    moduleInternalFrame.toFront();
  }

  private final class ModuleInternalFrameListener extends
      InternalFrameAdapter {

    private String moduleId;

    /**
     * Constructs a new <code>ModuleInternalFrameListener</code>
     * instance.
     * 
     * @param moduleId
     *          the root module identifier this listener is attached to.
     */
    public ModuleInternalFrameListener(String moduleId) {
      this.moduleId = moduleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameActivated(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IIconFactory<Icon> getIconFactory() {
    return (IIconFactory<Icon>) super.getIconFactory();
  }

  private JFrame createControllerFrame() {
    JFrame frame = new JFrame(getLabelTranslator().getTranslation(getName(),
        getLocale()));
    frame.setContentPane(new JDesktopPane());
    frame.setIconImage(((ImageIcon) getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE)).getImage());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setJMenuBar(getApplicationMenuBar());
    frame.setGlassPane(createHermeticGlassPane());
    return frame;
  }

  private JMenuBar getApplicationMenuBar() {
    JMenuBar applicationMenuBar = new JMenuBar();
    applicationMenuBar.add(getModulesMenu());
    return applicationMenuBar;
  }

  private JMenu getModulesMenu() {
    JMenu modulesMenu = new JMenu(getLabelTranslator().getTranslation(
        "Modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      JMenuItem moduleMenuItem = new JMenuItem(
          new ModuleSelectionAction(moduleId,
              moduleDescriptor));
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
      putValue(Action.NAME, getLabelTranslator().getTranslation(
          moduleDescriptor.getName(), getLocale()));
      putValue(Action.SHORT_DESCRIPTION, getDescriptionTranslator()
          .getTranslation(moduleDescriptor.getDescription(),
              getLocale()));
      putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      displayModule(moduleId);
    }
  }

  /**
   * Creates a new JInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private JInternalFrame createJInternalFrame(IView<JComponent> view) {
    JInternalFrame internalFrame = new JInternalFrame(getLabelTranslator()
        .getTranslation(view.getDescriptor().getName(), getLocale()), true,
        false, true, true);
    internalFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);
    internalFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    internalFrame.setGlassPane(createHermeticGlassPane());
    return internalFrame;
  }

  private JComponent createHermeticGlassPane() {
    JPanel glassPane = new JPanel();
    glassPane.setOpaque(false);
    glassPane.addMouseListener(new MouseAdapter() {
      // No-op
      });
    glassPane.addKeyListener(new KeyAdapter() {
      // No-op
      });
    return glassPane;
  }

  /**
   * This method has been overriden to take care of long-running operations not
   * to have the swing gui blocked. It uses the foxtrot library to achieve this.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected final Map<String, Object> executeBackend(final IAction action) {
    if (action.isLongOperation()) {
      return (Map<String, Object>) SwingUtil.performLongOperation(new Job() {

        /**
         * Decorates the super implementation with the foxtrot job.
         * <p>
         * {@inheritDoc}
         */
        @Override
        public Object run() {
          return protectedExecuteBackend(action);
        }
      });
    }
    return protectedExecuteBackend(action);
  }

  private Map<java.lang.String, Object> protectedExecuteBackend(IAction action) {
    return super.executeBackend(action);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected final Map<String, Object> executeFrontend(final IAction action) {
    // return (Map<String, Object>) SwingUtil.performLongOperation(new Job() {
    //
    // /**
    // * Decorates the super implementation with the foxtrot job.
    // * <p>
    // * {@inheritDoc}
    // */
    // @Override
    // public Object run() {
    // return protectedExecuteFrontend(action);
    // }
    // });
    return protectedExecuteFrontend(action);
  }

  private Map<java.lang.String, Object> protectedExecuteFrontend(IAction action) {
    return super.executeFrontend(action);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IAction action) {
    if (action == null) {
      return null;
    }
    JComponent sourceComponent = (JComponent) action.getContext().get(
        ActionContextConstants.SOURCE_COMPONENT);
    Component windowOrInternalFrame = null;
    if (sourceComponent != null) {
      windowOrInternalFrame = SwingUtil
          .getWindowOrInternalFrame(sourceComponent);
    }
    if (windowOrInternalFrame instanceof JFrame) {
      ((JFrame) windowOrInternalFrame).getGlassPane().setVisible(true);
    } else if (windowOrInternalFrame instanceof JInternalFrame) {
      ((JInternalFrame) windowOrInternalFrame).getGlassPane().setVisible(true);
    } else if (windowOrInternalFrame instanceof JDialog) {
      ((JDialog) windowOrInternalFrame).getGlassPane().setVisible(true);
    }
    waitTimer.startTimer(sourceComponent);
    try {
      return super.execute(action);
    } finally {
      if (windowOrInternalFrame instanceof JFrame) {
        ((JFrame) windowOrInternalFrame).getGlassPane().setVisible(false);
      } else if (windowOrInternalFrame instanceof JInternalFrame) {
        ((JInternalFrame) windowOrInternalFrame).getGlassPane().setVisible(
            false);
      } else if (windowOrInternalFrame instanceof JDialog) {
        ((JDialog) windowOrInternalFrame).getGlassPane().setVisible(false);
      }
      waitTimer.stopTimer();
    }
  }
}
