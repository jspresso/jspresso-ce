/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicRelationshipEndPropertyDescriptor extends
    BasicPropertyDescriptor implements IRelationshipEndPropertyDescriptor {

  private Boolean                            composition;
  private String                             fkName;
  private IRelationshipEndPropertyDescriptor reverseRelationEnd;

  private IRelationshipEndPropertyDescriptor tempReverseRelationEnd;

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
      return composition.booleanValue();
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
   * Whenever this property is not explicitely set by the developer, Jspresso
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
    this.composition = new Boolean(composition);
  }

  /**
   * Gives the developer the oportunity to customize the geneated foreign key
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
   * relationdhip end is defined, it is only navigable from the owning component
   * to the described end (default value is <code>null</code>). Assigning a
   * reverse relationship ends instructs the framework that the relationship is
   * bi-derectional. This implies several complementary features :
   * <ul>
   * <li>When one of the relationship ends is updated, the other side is
   * automatically maintained by Jspresso, i.e. you never have to worry about
   * reverse state. For instance, considering a <code>Invoice</code> -
   * <code>InvoiceLine</code> bi-directional relationship,
   * <code>InvoiceLine.setInvoice(Invoice)</code> and
   * <code>Invoice.addToInvoiceLines(InvoiceLine)</code> are strictly
   * equivalent.</li>
   * <li>You can qualify a &quot;N-N&quot; relationship (thus creating an
   * association table in the datastore behind the scene) by assigning 2
   * collection property decriptors as reverse relation ends of each other.</li>
   * <li>You can qualify a &quot;1-1&quot; relationship (thus enforcing some
   * unicity constraint in the datastore behind the scene) by assigning 2
   * reference property decriptors as reverse relation ends of each other.</li>
   * </ul>
   * Setting the reverse relation end operation is commmutative so that it
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
      if (getName().indexOf(".") < 0 && this.reverseRelationEnd != null) {
        this.reverseRelationEnd.setReverseRelationEnd(null);
      }
      this.reverseRelationEnd = reverseRelationEnd;
      if (getName().indexOf(".") < 0 && this.reverseRelationEnd != null) {
        this.reverseRelationEnd.setReverseRelationEnd(this);
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
   * Gets the default composition of a relationship end.
   * 
   * @return the default composition of a relationship end.
   */
  protected abstract boolean getDefaultComposition();
}
