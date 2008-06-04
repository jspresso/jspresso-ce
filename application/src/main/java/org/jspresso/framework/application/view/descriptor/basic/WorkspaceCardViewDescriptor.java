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
package org.jspresso.framework.application.view.descriptor.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.AbstractCardViewDescriptor;


/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the workspace structure.
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
public class WorkspaceCardViewDescriptor extends AbstractCardViewDescriptor {

  private static final String ELEMENT_SUFFIX = ".element";

  /**
   * Constructs a new <code>WorkspaceCardViewDescriptor</code> instance.
   * 
   * @param workspace
   *            the workspace.
   * @param moduleDescriptorViewFactory
   *            the view descriptor factory used to create (or decorate) the
   *            modules projected views.
   */
  public WorkspaceCardViewDescriptor(Workspace workspace,
      IModuleViewDescriptorFactory moduleDescriptorViewFactory) {
    Map<String, IViewDescriptor> moduleCards = new HashMap<String, IViewDescriptor>();
    prepareModuleCards(moduleCards, workspace.getModules(),
        moduleDescriptorViewFactory);
    setCardViewDescriptors(moduleCards);
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof BeanModule
        && (((BeanModule) model).getParent()) instanceof BeanCollectionModule) {
      return (((BeanModule) model).getParent()).getName() + ELEMENT_SUFFIX;
    } else if (model instanceof Module) {
      return ((Module) model).getName();
    }
    return null;
  }

  private void prepareModuleCards(Map<String, IViewDescriptor> moduleCards,
      List<Module> modules,
      IModuleViewDescriptorFactory moduleDescriptorViewFactory) {
    if (modules != null) {
      for (Module module : modules) {
        if (module.getProjectedViewDescriptor() != null) {
          moduleCards.put(module.getName(), moduleDescriptorViewFactory
              .createProjectedViewDescriptor(module));
          if (module instanceof BeanCollectionModule) {
            BeanModule fakeBeanModule = new BeanModule();
            fakeBeanModule
                .setProjectedViewDescriptor(((BeanCollectionModule) module)
                    .getElementViewDescriptor());
            fakeBeanModule
                .setComponentDescriptor(((BeanCollectionModule) module)
                    .getElementComponentDescriptor());
            moduleCards.put(module.getName() + ELEMENT_SUFFIX,
                moduleDescriptorViewFactory
                    .createProjectedViewDescriptor(fakeBeanModule));
          }
        }
        prepareModuleCards(moduleCards, module.getSubModules(),
            moduleDescriptorViewFactory);
      }
    }
  }
}
