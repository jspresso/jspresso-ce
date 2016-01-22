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

import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * This is the abstract base descriptor for all relationship properties.
 * relationship properties include :
 * <ul>
 * <li><i>reference</i> properties, i.e. &quot;N to 1&quot; or &quot;1 to
 * 1&quot; properties</li>
 * <li><i>collection</i> properties, i.e. &quot;1 to N&quot; or &quot;N to
 * N&quot; properties</li>
 * </ul>
 * Other type of properties are named <i>scalar</i> properties.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BasicRelationshipEndPropertyDescriptor extends
    BasicPropertyDescriptor implements IRelationshipEndPropertyDescriptor {

  private Boolean                            composition;
  private String                             fkName;
  private boolean                            leadingPersistence = true;

  private IRelationshipEndPropertyDescriptor reverseRelationEnd;
  private IRelationshipEndPropertyDescriptor tempReverseRelationEnd;

  private EFetchType                         fetchType          = EFetchType.SELECT;
  private Integer                            batchSize;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicRelationshipEndPropertyDescriptor clone() {
    BasicRelationshipEndPropertyDescriptor clonedDescriptor = (BasicRelationshipEndPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicRelationshipEndPropertyDescriptor createQueryDescriptor() {
    BasicRelationshipEndPropertyDescriptor queryDescriptor = (BasicRelationshipEndPropertyDescriptor) super
        .createQueryDescriptor();
    queryDescriptor.reverseRelationEnd = null;
    return queryDescriptor;
  }

  /**
   * Gets the fkName.
   *
   * @return the fkName.
   */
  public String getFkName() {
    return fkName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRelationshipEndPropertyDescriptor getReverseRelationEnd() {
    return reverseRelationEnd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComposition() {
    if (composition != null) {
      return composition;
    }
    return getDefaultComposition();
  }

  /**
   * Instructs the framework that this property has to be treated as a
   * <i>composition</i>, in the UML terminology. This implies that reachable
   * entities that are referenced by this property follow the owning entity
   * lifecycle. For instance, when the owning entity is deleted, the referenced
   * entities in composition properties are also deleted.
   * <p>
   * Whenever this property is not explicitly set by the developer, Jspresso
   * uses sensible defaults :
   * <ul>
   * <li><i>collection properties</i> are compositions <b>unless</b> they are
   * bidirectional &quot;N to N&quot;</li>
   * <li><i>reference properties</i> are not composition</li>
   * </ul>
   * <p>
   * This property is strictly behavioural and does not impact the domain state
   * itself.
   *
   * @param composition
   *          the composition to set.
   */
  public void setComposition(boolean composition) {
    this.composition = composition;
  }

  /**
   * Gives the developer the opportunity to customize the generated foreign key
   * (if any) name.
   *
   * @param fkName
   *          the fkName to set.
   */
  public void setFkName(String fkName) {
    this.fkName = fkName;
  }

  /**
   * Allows to make a relationship bi-directional. By default, when a
   * relationship end is defined, it is only navigable from the owning component
   * to the described end (default value is {@code null}). Assigning a
   * reverse relationship ends instructs the framework that the relationship is
   * bi-directional. This implies several complementary features :
   * <ul>
   * <li>When one of the relationship ends is updated, the other side is
   * automatically maintained by Jspresso, i.e. you never have to worry about
   * reverse state. For instance, considering a {@code Invoice} -
   * {@code InvoiceLine} bi-directional relationship,
   * {@code InvoiceLine.setInvoice(Invoice)} and
   * {@code Invoice.addToInvoiceLines(InvoiceLine)} are strictly
   * equivalent.</li>
   * <li>You can qualify a &quot;N-N&quot; relationship (thus creating an
   * association table in the data store behind the scene) by assigning 2
   * collection property descriptors as reverse relation ends of each other.</li>
   * <li>You can qualify a &quot;1-1&quot; relationship (thus enforcing some
   * unicity constraint in the data store behind the scene) by assigning 2
   * reference property descriptors as reverse relation ends of each other.</li>
   * </ul>
   * Setting the reverse relation end operation is commutative so that it
   * automatically assigns bot ends as reverse, i.e. you only have to set the
   * property on one side of the relationship.
   *
   * @param reverseRelationEnd
   *          the reverseRelationEnd to set.
   */
  @Override
  public void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd) {
    if (getName() == null) {
      // store it until we have a name available.
      tempReverseRelationEnd = reverseRelationEnd;
      return;
    }
    // We only ant to actually update reverse relation end if it is an 'actual'
    // property descriptor, e.g. not a compound one used only for the view.
    if (this.reverseRelationEnd != reverseRelationEnd) {
      if (!getName().contains(".")) {
        if (this.reverseRelationEnd != null) {
          this.reverseRelationEnd.setReverseRelationEnd(null);
        }
      }
      if (reverseRelationEnd == null) {
        // it is set uni-directional.
        leadingPersistence = true;
      } else {
        leadingPersistence = reverseRelationEnd.getReverseRelationEnd() != this;
      }
      this.reverseRelationEnd = reverseRelationEnd;
      if (!getName().contains(".")) {
        if (this.reverseRelationEnd != null) {
          this.reverseRelationEnd.setReverseRelationEnd(this);
        }
      }
    }
  }

  /**
   * Assign reverse temp relation end if set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setName(String name) {
    super.setName(name);
    if (tempReverseRelationEnd != null) {
      setReverseRelationEnd(tempReverseRelationEnd);
      tempReverseRelationEnd = null;
    }
  }

  /**
   * Gets the leadingPersistence.
   *
   * @return the leadingPersistence.
   */
  public boolean isLeadingPersistence() {
    return leadingPersistence;
  }

  /**
   * Gets the default composition of a relationship end.
   *
   * @return the default composition of a relationship end.
   */
  protected abstract boolean getDefaultComposition();

  /**
   * Gets the fetchType.
   *
   * @return the fetchType.
   */
  public EFetchType getFetchType() {
    return fetchType;
  }

  /**
   * This property allows to finely tune fetching strategy of the ORM on this
   * relationship end. This is either a value of the {@code EFetchType}
   * enum or its equivalent string representation :
   * <ul>
   * <li>{@code SELECT} for default 2nd select strategy (lazy)</li>
   * <li>{@code SUBSELECT} for default 2nd select strategy (lazy) using an
   * IN clause</li>
   * <li>{@code JOIN} for a join select strategy (not lazy)</li>
   * </ul>
   * <p>
   * Default value is {@code EFetchType.SELECT}, i.e. 2nd select strategy.
   *
   * @param fetchType
   *          the fetchType to set.
   */
  public void setFetchType(EFetchType fetchType) {
    this.fetchType = fetchType;
  }

  /**
   * Gets the batchSize.
   *
   * @return the batchSize.
   */
  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   * This property allows to finely tune batching strategy of the ORM on this
   * relationship end. Whenever possible, the ORM will use a IN clause in order
   * to fetch multiple instances relationships at once. The batch size
   * determines the size of th IN clause.
   *
   * @param batchSize
   *          the batchSize to set.
   */
  public void setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
  }
}
