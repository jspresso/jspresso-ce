/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing.jgoodies;

import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.swing.DefaultSwingViewFactory;
import com.jgoodies.uif_lite.component.UIFSplitPane;
import com.jgoodies.uif_lite.panel.SimpleInternalFrame;

/**
 * This view factory uses JGoodies components to enhance the user interface.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JGoodiesSwingViewFactory extends DefaultSwingViewFactory {

  /**
   * Constructs a new <code>JGoodiesSwingViewFactory</code> instance.
   */
  public JGoodiesSwingViewFactory() {
    super();
  }

  /**
   * Constructs a new <code>JGoodiesSwingViewFactory</code> instance.
   * 
   * @param lookAndFeel
   *          the look and feel to use.
   */
  public JGoodiesSwingViewFactory(String lookAndFeel) {
    super(lookAndFeel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void decorateWithBorder(IView<JComponent> view, Locale locale) {
    if (view.getDescriptor().getBorderType() == IViewDescriptor.TITLED) {
      SimpleInternalFrame sif = new SimpleInternalFrame(getLabelTranslator()
          .getTranslation(getTitleKey(view.getDescriptor()),
              locale));
      if (view.getDescriptor().getIconImageURL() != null) {
        sif.setFrameIcon(getIconFactory()
            .getIcon(view.getDescriptor().getIconImageURL(),
                IIconFactory.TINY_ICON_SIZE));
      }
      // if (view.getPeer() instanceof JPanel) {
      // JToolBar toolBar = null;
      // for (Component child : view.getPeer().getComponents()) {
      // if (child instanceof JToolBar) {
      // toolBar = (JToolBar) child;
      // }
      // }
      // if (toolBar != null) {
      // sif.setToolBar(toolBar);
      //        }
      //      }
      sif.add(view.getPeer());
      view.setPeer(sif);
    } else {
      super.decorateWithBorder(view, locale);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JSplitPane createJSplitPane() {
    return new UIFSplitPane();
  }

}
