package org.jspresso.framework.application.frontend.action.lov;

import java.util.Arrays;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;

/**
 * The type Create entity from lov action.
 *
 * @param <E>
 *     the type parameter
 * @param <F>
 *     the type parameter
 * @param <G>
 *     the type parameter
 */
public class CreateEntityFromLovAction<E, F, G> extends EditComponentAction<E, F, G> {

  /**
   * Gets component to edit.
   *
   * @param context
   *     the context
   * @return the component to edit
   */
  @Override
  protected Object getComponentToEdit(Map<String, Object> context) {
    IEntityFactory entityFactory = getBackendController(context).getEntityFactory();
    IQueryComponent lovQueryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
    Class<IEntity> entityToCreateContract = lovQueryComponent.getQueryContract();

    IEntity entityInstance = entityFactory.createEntityInstance(entityToCreateContract);
    setActionParameter(Arrays.asList(entityInstance), context);
    return entityInstance;
  }

  /**
   * Gets view descriptor.
   *
   * @param context
   *     the context
   * @return the view descriptor
   */
  @Override
  protected IViewDescriptor getViewDescriptor(Map<String, Object> context) {
    IEntityFactory entityFactory = getBackendController(context).getEntityFactory();
    IQueryComponent lovQueryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
    Class<IEntity> entityToCreateContract = lovQueryComponent.getQueryContract();
    IComponentDescriptor<?> entityToCreateDescriptor = entityFactory.getComponentDescriptor(entityToCreateContract);

    BasicComponentViewDescriptor formViewDescriptor = new BasicComponentViewDescriptor();
    formViewDescriptor.setModelDescriptor(entityToCreateDescriptor);
    return formViewDescriptor;
  }
}
