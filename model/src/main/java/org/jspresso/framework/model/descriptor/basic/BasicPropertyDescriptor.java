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
package org.jspresso.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.bean.integrity.IPropertyProcessor;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.lang.StringUtils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * This is the abstract base class for all property descriptors. It mainly
 * serves for factorizing all commons properties for property descriptors.
 * <p/>
 * A property descriptor is used for describing a component/entity/interface
 * property (<i>Java Beans</i> semantic).
 * <p/>
 * You will never use {@code BasicPropertyDescriptor} as such but rather
 * use its concrete descendants.
 * <p/>
 * Please note that {@code BasicPropertyDescriptor} enforces its name to
 * start with a lower case letter, following the JavaBean convention. So even if
 * you name it &quot;MyProperty&quot;, it will actually end up to
 * &quot;myProperty&quot;.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BasicPropertyDescriptor extends DefaultIconDescriptor
    implements IPropertyDescriptor, BeanFactoryAware {

  private BeanFactory                    beanFactory;
  private boolean                        computed;
  private Class<?>                       delegateClass;
  private String                         delegateClassName;
  private Boolean                        delegateWritable;
  private Collection<String>             grantedRoles;
  private List<String>                   integrityProcessorBeanNames;
  private List<String>                   integrityProcessorClassNames;
  private List<IPropertyProcessor<?, ?>> integrityProcessors;
  private Boolean                        mandatory;
  private Integer                        preferredWidth;
  private Collection<IGate>              readabilityGates;
  private Boolean                        readOnly;
  private Boolean                        sortable;
  private boolean                        cacheable;
  private String                         sqlName;
  private String                         unicityScope;
  private boolean                        versionControl;
  private Collection<IGate>              writabilityGates;
  private Boolean                        filterComparable;

  /**
   * Constructs a new {@code BasicPropertyDescriptor} instance.
   */
  public BasicPropertyDescriptor() {
    computed = false;
    versionControl = true;
    cacheable = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPropertyDescriptor clone() {
    return (BasicPropertyDescriptor) super.clone();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPropertyDescriptor createQueryDescriptor() {
    BasicPropertyDescriptor queryPropertyDescriptor = clone();
    queryPropertyDescriptor.setMandatory(false);
    queryPropertyDescriptor.setReadOnly(false);
    queryPropertyDescriptor.integrityProcessors = null;
    queryPropertyDescriptor.setIntegrityProcessorClassNames(null);
    queryPropertyDescriptor.writabilityGates = null;
    queryPropertyDescriptor.readabilityGates = null;
    queryPropertyDescriptor.preferredWidth = null;
    return queryPropertyDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getDelegateClass() {
    if (delegateClass == null) {
      String className = getDelegateClassName();
      if (className != null) {
        try {
          delegateClass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
          throw new NestedRuntimeException(ex);
        }
      }
    }
    return delegateClass;
  }

  /**
   * Sets the delegate class name.
   *
   * @return The class name of the extension delegate used to compute this
   * property.
   */
  @Override
  public String getDelegateClassName() {
    return delegateClassName;
  }

  /**
   * Gets the grantedRoles.
   *
   * @return the grantedRoles.
   */
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IPropertyProcessor<?, ?>> getIntegrityProcessors() {
    registerIntegrityProcessorsIfNecessary();
    return integrityProcessors;
  }

  /**
   * Gets the preferredWidth.
   *
   * @return the preferredWidth.
   */
  @Override
  public Integer getPreferredWidth() {
    return preferredWidth;
  }

  /**
   * Gets the readabilityGates.
   *
   * @return the readabilityGates.
   */
  @Override
  public Collection<IGate> getReadabilityGates() {
    return readabilityGates;
  }

  /**
   * Gets the sqlName.
   *
   * @return the sqlName.
   */
  public String getSqlName() {
    return sqlName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnicityScope() {
    return unicityScope;
  }

  /**
   * Gets the writabilityGates.
   *
   * @return the writabilityGates.
   */
  @Override
  public Collection<IGate> getWritabilityGates() {
    return writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object interceptSetter(Object component, Object newValue) {
    Object interceptedValue = newValue;
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return interceptedValue;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      interceptedValue = ((IPropertyProcessor<Object, Object>) processor).interceptSetter(component, interceptedValue);
    }
    return interceptedValue;
  }

  /**
   * Returns true if a delegate class is registered to compute the property
   * value. A property can be made {@code computed} even if its delegate
   * class is null by calling {@code setComputed(true)}. This way, the
   * property should be ignored by the ORM.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean isComputed() {
    return getDelegateClassName() != null || computed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMandatory() {
    if (mandatory != null) {
      return mandatory;
    }
    return getDefaultMandatory();
  }

  /**
   * Gets the default value of the mandatory attribute.
   *
   * @return {@code false}.
   */
  protected boolean getDefaultMandatory() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isModifiable() {
    if (getDelegateClassName() == null) {
      return true;
    }
    if (delegateWritable != null) {
      return delegateWritable;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    if (readOnly != null) {
      return readOnly;
    }
    return !isModifiable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSortable() {
    if (sortable != null) {
      return sortable;
    }
    return getDefaultSortablility();
  }

  /**
   * Default property sortability.
   *
   * @return {@code true} by default unless overridden in subclasses.
   */
  protected boolean getDefaultSortablility() {
    return true;
  }

  /**
   * Gets the versionControl.
   *
   * @return the versionControl.
   */
  public boolean isVersionControl() {
    return versionControl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public void postprocessSetter(Object component, Object oldValue, Object newValue) {
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      ((IPropertyProcessor<Object, Object>) processor).postprocessSetter(component, oldValue, newValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public void preprocessSetter(final Object component, Object newValue) {
    // Mandatory checking should only happen on save. See bug #776.
    // if (isMandatory()
    // && (newValue == null || newValue instanceof Collection<?>
    // && ((Collection<?>) newValue).isEmpty())) {
    // throw new MandatoryPropertyException(this, component);
    // }
    List<IPropertyProcessor<?, ?>> processors = getIntegrityProcessors();
    if (processors == null) {
      return;
    }
    for (IPropertyProcessor<?, ?> processor : processors) {
      ((IPropertyProcessor<Object, Object>) processor).preprocessSetter(component, newValue);
    }
  }

  /**
   * Sets the beanFactory.
   *
   * @param beanFactory
   *     the beanFactory to set.
   * @internal
   */
  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * Forces a property to be considered as a computed property by the framework.
   * A computed property will be completely ignored by the persistence layer and
   * its management is left to the developer.
   * <p/>
   * Properties declared with a delegate computing class are considered computed
   * by default so there is no need to explicitly set them
   * {@code computed=true}. However, there is sometimes a need to declare a
   * property at some level (e.g. in an interface descriptor) and let lower
   * level implementation decide how to handle this common property concretely
   * (either computing it or handling it as a real persistent property). In that
   * case, you can declare this property {@code computed=true} in the super
   * type and refine the actual implementation (computed or not) in the
   * sub-types.
   * <p/>
   * Default value is {@code false}.
   *
   * @param computed
   *     the computed to set.
   */
  public void setComputed(boolean computed) {
    this.computed = computed;
  }

  /**
   * Instructs the framework that this property is computed by a delegate
   * attached to the owning component. The {@code delegateClassName}
   * property must be set with the fully qualified class name of the delegate
   * instance to use.
   * <p/>
   * Delegate instances are stateful. This allows for some caching of computing
   * intensive properties. There is exactly one delegate of a certain class per
   * owning component instance. Delegate instances are lazily created when
   * needed, i.e. whe the computed property is accessed either programmatically
   * or by the binding layer.
   * <p/>
   * The delegate class must implement the
   * {@code IComponentExtension&lt;T&gt;} interface (where &lt;T&gt; is
   * assignable from the owning component class) and provide a public
   * constructor taking exactly 1 parameter : the component instance. Jspresso
   * provides an adapter class to inherit from :
   * {@code AbstractComponentExtension&lt;T&gt;}. This helper class
   * provides the methods to access the enclosing component from the delegate
   * implementation as well as the Spring context it comes from, when needed.
   * <p/>
   * A delegate-computed property is most of the time read-only but it can be
   * made writable by setting the property descriptor
   * {@code delegateWritable=true}. In that case the delegate class must
   * also provide a setter for the computed property.
   *
   * @param delegateClassName
   *     The class name of the extension delegate used to compute this
   *     property.
   */
  public void setDelegateClassName(String delegateClassName) {
    this.delegateClassName = delegateClassName;
  }

  /**
   * Instructs the framework that a delegate-computed property is writable. Most
   * of the time, a computed property is read-only. Whenever a computed property
   * is made writable through the use of {@code delegateWritable=true}, the
   * delegate class must also provide a setter for the computed property.
   * <p/>
   * Default value is {@code false}.
   *
   * @param delegateWritable
   *     the delegateWritable to set.
   */
  public void setDelegateWritable(boolean delegateWritable) {
    this.delegateWritable = delegateWritable;
  }

  /**
   * Assigns the roles that are authorized to manipulate the property backed by
   * this descriptor. It supports &quot;<b>!</b>&quot; prefix to negate the
   * role(s). This will directly influence the UI behaviour and even composition
   * (e.g. show/hide columns or fields). Setting the collection of granted roles
   * to {@code null} (default value) disables role based authorization on
   * this property level. Note that this authorization enforcement does not
   * prevent programmatic access that is of the developer responsibility.
   *
   * @param grantedRoles
   *     the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Registers a list of property processor instances that will be triggered on
   * the different phases of the property modification, i.e. :
   * <ul>
   * <li><i>before</i> the property is modified, usually for controlling the
   * incoming value</li>
   * <li><i>while</i> (actually just before the actual assignment) the property
   * is modified, allowing to intercept and change the incoming value</li>
   * <li><i>after</i> the property is modified, allowing to trigger some
   * post-modification behaviour (e.g. tracing, domain integrity management,
   * ...)</li>
   * </ul>
   * This property must be set with Spring bean names (i.e. Spring ids). When
   * needed, Jspresso will query the Spring application context to retrieve the
   * processor instances. This property is equivalent to setting
   * {@code integrityProcessorClassNames} except that it allows to register
   * processor instances that are configured externally in the Spring context.
   * <p/>
   * Property processor instances must implement the
   * {@code IPropertyProcessor&lt;E, F&gt;} interface where &lt;E, F&gt;
   * represent respectively the type of the owning component and the type of the
   * property. Since there are 3 methods to implement in the interface (1 for
   * each of the phase described above), Jspresso provides an adapter class with
   * empty implementations to override :
   * {@code EmptyPropertyProcessor&lt;E, F&gt;}.
   * <p/>
   * Whenever the underlying property is a collection property, the interface to
   * implement is {@code ICollectionPropertyProcessor&lt;E, F&gt;} (or
   * extend {@code EmptyCollectionPropertyProcessor&lt;E, F&gt;}) with 4
   * new phases introduced :
   * <ul>
   * <li><i>before</i> an element is <i>added</i> to the collection property</li>
   * <li><i>after</i> an element is <i>added</i> to the collection property</li>
   * <li><i>before</i> an element is <i>removed</i> from the collection property
   * </li>
   * <li><i>after</i> an element is <i>removed</i> from the collection property</li>
   * </ul>
   *
   * @param integrityProcessorBeanNames
   *     the integrityProcessorBeanNames to set.
   */
  public void setIntegrityProcessorBeanNames(List<String> integrityProcessorBeanNames) {
    this.integrityProcessorBeanNames = StringUtils.ensureSpaceFree(integrityProcessorBeanNames);
  }

  /**
   * Much the same as {@code integrityProcessorBeanNames} except that
   * instead of providing a list of Spring bean names, you provide a list of
   * fully qualified class names. These classes must :
   * <ul>
   * <li>provide a default constructor</li>
   * <li>implement the {@code ILifecycleInterceptor&lt;E&gt;} interface.</li>
   * </ul>
   * When needed, Jspresso will create lifecycle interceptor instances.
   *
   * @param integrityProcessorClassNames
   *     the integrityProcessorClassNames to set.
   */
  public void setIntegrityProcessorClassNames(List<String> integrityProcessorClassNames) {
    this.integrityProcessorClassNames = StringUtils.ensureSpaceFree(integrityProcessorClassNames);
  }

  /**
   * Declare a property as mandatory. This will enforce mandatory checks when
   * the owning component is persisted as well as when the property is updated
   * individually. Moreover, this information allows the views bound to the
   * property to be configured accordingly, e.g. display the property with a
   * slightly modified label indicating it is mandatory. This constraint is also
   * enforced programmatically.
   * <p/>
   * Default value is false.
   *
   * @param mandatory
   *     the mandatory to set.
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  /**
   * This property allows for setting an indication of width for representing
   * this property in a view.
   * <p/>
   * Default value is {@code null}, so that the view factory will make its
   * decision based on the type and/or other characteristics of the property
   * (e.g. max length).
   *
   * @param preferredWidth
   *     the preferredWidth to set.
   */
  public void setPreferredWidth(Integer preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  /**
   * Sets the readabilityGates.
   *
   * @param readabilityGates
   *     the readabilityGates to set.
   * @internal
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Enforces a property to be read-only. This is only enforced at the UI level,
   * i.e. the property can still be updated programmatically. The UI may take
   * decisions like changing text fields into labels if it knows the underlying
   * property is read-only.
   *
   * @param readOnly
   *     the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Enforces a property sortability. This is only enforced at the UI level,
   * i.e. the property can still be used for sorting programmatically.
   *
   * @param sortable
   *     the sortable to set.
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  /**
   * Instructs Jspresso to use this name when translating this property name to
   * the data store namespace. This includes , but is not limited to, database
   * column names.
   * <p/>
   * Default value is {@code null} so that Jspresso uses its default naming
   * policy.
   *
   * @param sqlName
   *     the sqlName to set.
   */
  public void setSqlName(String sqlName) {
    this.sqlName = sqlName;
  }

  /**
   * Makes this property part of a unicity scope. All tuples of properties
   * belonging to the same unicity scope are enforced to be unique in the
   * component type scope. This concretely translates to unique constraints in
   * the data store spanning the properties composing the unicity scope.
   * <p/>
   * Note that, for performance reasons, unicity scopes are only enforced by the
   * persistence layer.
   *
   * @param unicityScope
   *     the unicityScope to set.
   */
  public void setUnicityScope(String unicityScope) {
    this.unicityScope = unicityScope;
  }

  /**
   * This property allows to fine tune whether this component property
   * participates in optimistic versioning. It mainly allows to declare some
   * properties that should be ignored regarding optimistic versioning thus
   * lowering the risk of version conflicts between concurrent users. Of course,
   * this feature has to be used with care since it may generate phantom updates
   * to the data store.
   * <p/>
   * Default value is {@code true} so that any change in the described
   * property increases the owning component version.
   *
   * @param versionControl
   *     the versionControl to set.
   */
  public void setVersionControl(boolean versionControl) {
    this.versionControl = versionControl;
  }

  /**
   * Assigns a collection of gates to determine property <i>writability</i>. A
   * property will be considered writable if and only if all gates are open.
   * This mechanism is mainly used for dynamic UI authorization based on model
   * state, e.g. a validated invoice should not be editable anymore.
   * <p/>
   * Descriptor assigned gates will be cloned for each property instance created
   * and backed by this descriptor. So basically, each property instance will
   * have its own, unshared collection of writability gates.
   * <p/>
   * Jspresso provides a useful set of gate types, like the binary property gate
   * that open/close based on the value of a boolean property of owning
   * component.
   * <p/>
   * By default, property descriptors are not assigned any gates collection,
   * i.e. there is no writability restriction. Note that gates do not enforce
   * programmatic writability of a property; only UI is impacted.
   *
   * @param writabilityGates
   *     the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

  private void registerIntegrityProcessor(IPropertyProcessor<?, ?> integrityProcessor) {
    if (integrityProcessors == null) {
      integrityProcessors = new ArrayList<>();
    }
    integrityProcessors.add(integrityProcessor);
  }

  private synchronized void registerIntegrityProcessorsIfNecessary() {
    if (integrityProcessorClassNames != null) {
      // process creation of integrity processors.
      for (String integrityProcessorClassName : integrityProcessorClassNames) {
        try {
          registerIntegrityProcessor((IPropertyProcessor<?, ?>) Class.forName(integrityProcessorClassName)
                                                                     .newInstance());
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ex) {
          throw new DescriptorException(ex);
        }
      }
      integrityProcessorClassNames = null;
    }
    if (integrityProcessorBeanNames != null && beanFactory != null) {
      // process creation of integrity processors.
      for (String integrityProcessorBeanName : integrityProcessorBeanNames) {
        registerIntegrityProcessor(beanFactory.getBean(integrityProcessorBeanName, IPropertyProcessor.class));
      }
      integrityProcessorBeanNames = null;
    }
  }

  /**
   * Enforces its name to start with a lower case letter, following the JavaBean
   * convention. So even if you name it &quot;MyProperty&quot;, it will actually
   * end up to &quot;myProperty&quot;.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setName(String name) {
    if (name != null && name.length() > 0) {
      super.setName(name.substring(0, 1).toLowerCase() + name.substring(1));
    } else {
      super.setName(name);
    }
  }

  /**
   * Returns sqlName.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getPersistenceFormula() {
    return getSqlName();
  }

  /**
   * Returns the property name.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getPermId() {
    return getName();
  }

  /**
   * A property permanent id is forced to be its name. Trying to set it to
   * another value will raise an exception.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setPermId(String permId) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the cacheable.
   *
   * @return the cacheable.
   */
  @Override
  public boolean isCacheable() {
    return cacheable;
  }

  /**
   * Configures the fact that this property can be cached. This is only used for
   * computed properties. Note that the cached value will be reset whenever a
   * firePropertyChange regarding this property is detected to be fired.
   * <p/>
   * Default value is {@code false} in order to prevent un-desired
   * side-effects if computed property change notification is not correctly
   * wired.
   *
   * @param cacheable
   *     the cacheable to set.
   */
  public void setCacheable(boolean cacheable) {
    this.cacheable = cacheable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModelTypeName() {
    Class<?> modelType = getModelType();
    if (modelType.isArray()) {
      return getModelType().getComponentType() + "[]";
    }
    return modelType.getName();
  }

  /**
   * Is filter comparable.
   *
   * @return the boolean
   */
  @Override
  public boolean isFilterComparable() {
    if (filterComparable == null) {
      return isDefaultFilterComparable();
    }
    return filterComparable;
  }

  /**
   * Sets filter comparable.
   *
   * @param filterComparable the filter comparable
   */
  public void setFilterComparable(Boolean filterComparable) {
    this.filterComparable = filterComparable;
  }

  /**
   * Gets whether this type of property descriptor is comparable by default in filter views. Can be overridden on a
   * per-instance basis using the settFilterComparable setter.
   *
   * @return the {@code false} by default.
   */
  protected boolean isDefaultFilterComparable() {
    return false;
  }
}
