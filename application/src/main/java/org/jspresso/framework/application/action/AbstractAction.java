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
package org.jspresso.framework.application.action;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.lang.StringUtils;

/**
 * This is the base class for all application actions. It establishes the
 * foundation of the Jspresso action framework, i.e. each action can link :
 * <ul>
 * <li>a <i>wrapped</i> action that will execute a return back to the caller
 * action</li>
 * <li>a <i>next</i> action that will execute after the caller</li>
 * </ul>
 * The action chaining described above supports the separation of concerns that
 * consists in splitting the actions in two distinct categories :
 * <ul>
 * <li><i>frontend</i> actions that deal with user interaction. They are
 * typically used to bootstrap a service request from th UI, update the UI
 * state, trigger the display of information, errors, ...</li>
 * <li><i>backend</i> actions that are faceless, UI agnostic and deal with the
 * manipulation of the domain model, backend services, ...</li>
 * </ul>
 * Conceptually, a frontend action can call a backend action or another frontend
 * action but a backend action should stay on the backend, thus should only
 * reference another backend action. In other words, the backend layer should
 * never explicitly reference the frontend layer.
 * <p>
 * That's the main reason for having a <i>wrapped</i> and a <i>next</i> action
 * in the action chain. The <i>wrapped</i> action is perfectly suited to call
 * the backend layer (a backend action) and give the flow back to the frontend
 * layer. The <i>next</i> action is perfectly suited to chain 2 actions of the
 * same type (i.e. 2 frontend actions or 2 backend actions). Using this scheme
 * helps keeping layer dependencies clear. Of course, this dual chaining
 * mechanism is completely recursive thus allowing to compose small (generic)
 * actions into larger composite ones and promoting reuse.
 * <p>
 * Actions execute on a context (an arbitrary map of objects keyed by
 * "well-known" character strings) that is initialized by the controller when
 * the action is triggered. The context passes along the action chain and is
 * thus the perfect medium for actions to loosely communicate between each
 * other. There are some framework standard elements placed in the action
 * context that depend on the application state, but nothing prevents action
 * developers to synchronize on arbitrary custom context elements that would be
 * produced/consumed to/from the context.
 * <p>
 * Regarding framework-standard context elements, an action developer can
 * (should) leverage the utility methods declared in the
 * {@code AbstractActionContextAware} super class instead of relying on the
 * element keys in the map. Take a look to the
 * {@code AbstractActionContextAware} documentation to get your hands on
 * action context exploration.
 * <p>
 * Last but not least, you should be aware that actions should be coded with
 * thread-safety in mind. Actual action instances are generally singleton-like
 * (unless explicitly stated which is extremely rare) and thus, might be used by
 * multiple sessions (threads) at once. You should <b>never</b> use action class
 * attributes for argument passing along the action chain, but use the
 * thread-owned action execution context instead. The action context is a data
 * structure that is local to an action chain execution thus not shared across
 * threads (users). It is the perfect place for exchanging data between actions.
 * Of course, different instances of the same action can be used and configured
 * differently through their class attributes (refer to it as static
 * configuration), but this is all for using in different places in the
 * application.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public abstract class AbstractAction extends AbstractActionContextAware
    implements IAction {

  /**
   * {@code ACTION_MODEL_NAME}.
   */
  protected static final String ACTION_MODEL_NAME = "ActionModel";
  private String                permId;

  private Collection<String>    grantedRoles;
  private IAction               nextAction;

  private IAction               wrappedAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (actionHandler.execute(getWrappedAction(context), context)) {
      return actionHandler.execute(getNextAction(context), context);
    }
    return false;
  }

  /**
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
  public String getPermId() {
    return permId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitly set if Spring XML is used.
   *
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }

  /**
   * Assigns the roles that are authorized to execute this action. It supports
   * &quot;<b>!</b>&quot; prefix to negate the role(s). This will directly
   * influence the UI behaviour since unauthorized actions won't be displayed.
   * Setting the collection of granted roles to {@code null} (default
   * value) disables role based authorization on this action. Note that this
   * authorization enforcement does not prevent programmatic access that is of
   * the developer responsibility.
   *
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Registers an action to be executed after this action and after the
   * <i>wrapped</i> one. This is perfectly suited to chain an action of the same
   * type (frontend or backend) as this one.
   *
   * @param nextAction
   *          the next action to execute.
   */
  public void setNextAction(IAction nextAction) {
    this.nextAction = nextAction;
  }

  /**
   * Registers an action to be executed after this action but before the
   * <i>next</i> one. This is perfectly suited to chain a backend action from a
   * frontend action since the control flow will return back to the calling
   * layer (the frontend).
   *
   * @param wrappedAction
   *          the wrappedAction to set.
   */
  public void setWrappedAction(IAction wrappedAction) {
    this.wrappedAction = wrappedAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).toString();
  }

  /**
   * Gets the controller (frontend or backend) out of the action context.
   *
   * @param context
   *          the action context.
   * @return the controller (frontend or backend).
   */
  protected abstract IController getController(Map<String, Object> context);

  /**
   * Gets the next action reference. If the next action has been configured
   * strongly through the setter method, it is directly returned. If not, it is
   * looked up into the action context.
   *
   * @param context
   *          the action context.
   * @return the next action to execute.
   * @see #setNextAction(IAction)
   */
  public IAction getNextAction(Map<String, Object> context) {
    return nextAction;
  }

  /**
   * Gets the wrapped action reference. If the wrapped action has been
   * configured strongly through the setter method, it is directly returned. If
   * not, it is looked up into the action context.
   *
   * @param context
   *          the action context.
   * @return the wrapped action to execute.
   * @see #setWrappedAction(IAction)
   */
  public IAction getWrappedAction(Map<String, Object> context) {
    return wrappedAction;
  }

  /**
   * Clones the action.
   * @return the action clone.
   */
  @Override
  public AbstractAction clone() {
    try {
      return (AbstractAction) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }
}
