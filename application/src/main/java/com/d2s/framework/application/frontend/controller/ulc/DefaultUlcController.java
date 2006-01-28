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
import com.d2s.framework.view.descriptor.projection.IProjectionViewDescriptor;
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
  private Map<String, ULCInternalFrame> projectionInternalFrames;

  /**
   * Creates the initial view from the root view descriptor, then a JFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start(IBackendController backendController, Locale locale) {
    super.start(backendController, locale);
    controllerFrame = createControllerFrame();
    controllerFrame.pack();
    controllerFrame.setSize(1024, 768);
    controllerFrame.setVisible(true);
  }

  private void displayProjection(String rootProjectionId) {
    if (projectionInternalFrames == null) {
      projectionInternalFrames = new HashMap<String, ULCInternalFrame>();
    }
    ULCInternalFrame projectionInternalFrame = projectionInternalFrames
        .get(rootProjectionId);
    if (projectionInternalFrame == null) {
      IProjectionViewDescriptor projectionViewDescriptor = getRootProjectionViewDescriptor(rootProjectionId);
      IView<ULCComponent> projectionView = createProjectionView(
          rootProjectionId, projectionViewDescriptor);
      projectionInternalFrame = createULCInternalFrame(projectionView);
      projectionInternalFrames.put(rootProjectionId, projectionInternalFrame);
      controllerFrame.getContentPane().add(projectionInternalFrame);
      getMvcBinder().bind(projectionView.getConnector(),
          getBackendController().getRootProjectionConnector(rootProjectionId));
      projectionInternalFrame.pack();
    }
    projectionInternalFrame.setVisible(true);
    if (projectionInternalFrame.isIcon()) {
      projectionInternalFrame.setIcon(false);
    }
    projectionInternalFrame.setMaximum(true);
    projectionInternalFrame.moveToFront();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getSelectedRootProjectionId() {
    for (Map.Entry<String, ULCInternalFrame> projectionIdAndFrame : projectionInternalFrames
        .entrySet()) {
      if (projectionIdAndFrame.getValue() != null
          && projectionIdAndFrame.getValue().isSelected()) {
        return projectionIdAndFrame.getKey();
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedRootProjectionId(@SuppressWarnings("unused")
  String selectedRootProjectionId) {
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
    applicationMenuBar.add(getProjectionMenu());
    return applicationMenuBar;
  }

  private ULCMenu getProjectionMenu() {
    ULCMenu projectionMenu = new ULCMenu(getLabelTranslator().getTranslation(
        "Projections", getLocale()));
    for (String rootProjectionId : getRootProjectionIds()) {
      IProjectionViewDescriptor projectionViewDescriptor = getRootProjectionViewDescriptor(rootProjectionId);
      ULCMenuItem projectionMenuItem = new ULCMenuItem(
          new ProjectionSelectionAction(rootProjectionId,
              projectionViewDescriptor));
      projectionMenu.add(projectionMenuItem);
    }
    return projectionMenu;
  }

  private final class ProjectionSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 3469745193806038352L;
    private String            rootProjectionId;

    /**
     * Constructs a new <code>ProjectionSelectionAction</code> instance.
     * 
     * @param rootProjectionId
     * @param projectionViewDescriptor
     */
    public ProjectionSelectionAction(String rootProjectionId,
        IProjectionViewDescriptor projectionViewDescriptor) {
      this.rootProjectionId = rootProjectionId;
      putValue(com.ulcjava.base.application.IAction.NAME, getLabelTranslator()
          .getTranslation(projectionViewDescriptor.getName(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          getDescriptionTranslator().getTranslation(
              projectionViewDescriptor.getDescription(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SMALL_ICON,
          getIconFactory().getIcon(projectionViewDescriptor.getIconImageURL(),
              IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected projection.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      displayProjection(rootProjectionId);
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
    internalFrame.setFrameIcon(getIconFactory().getIcon(
        view.getDescriptor().getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    internalFrame.getContentPane().add(view.getPeer());
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
  }
}
