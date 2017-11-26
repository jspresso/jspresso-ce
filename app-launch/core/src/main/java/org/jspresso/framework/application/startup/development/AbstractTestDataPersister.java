/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.startup.development;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.tuple.Pair;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * The type Abstract test data persister.
 */
public abstract class AbstractTestDataPersister {

  private static String[] LOREM_IPSUM = ("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
      + "Aenean commodo ligula eget dolor. Aenean massa. Cum sociis "
      + "natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. "
      + "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. "
      + "Nulla consequat massa quis enim. Donec pede justo, fringilla vel, "
      + "aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, "
      + "venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. " + "Tincidunt. Cras dapibus.").split(" ");

  /**
   * Creates an entity instance.
   *
   * @param <T>
   *     the actual entity type.
   * @param entityContract
   *     the entity contract.
   * @return the created entity.
   */
  protected <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return getEntityFactory().createEntityInstance(entityContract);
  }

  protected IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * Gets the entityFactory.
   *
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return getBackendController().getEntityFactory();
  }

  /**
   * Create random entity
   * <p>
   * Uses pairs of field name and field values
   * - If pair is an array, select of value randomly
   * - If pair value is anything else, simply use this value
   * <p>
   *
   * @param entityContract
   *     The entity contract
   * @param initializations
   *     The list of pairs
   * @return The created entity
   */
  protected <T extends IEntity> T createRandomEntity(Class<T> entityContract, Pair<String, Object>... initializations)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

    T entity = createEntityInstance(entityContract);

    IAccessorFactory accessorFactory = getEntityFactory().getAccessorFactory();

    for (Pair<String, Object> init : initializations) {

      Object value = init.getValue();
      if (value instanceof Object[]) {
        value = randomSelection((Object[]) value);
      }

      IAccessor propertyAccessor = accessorFactory.createPropertyAccessor(init.getLeft(), entityContract);
      propertyAccessor.setValue(entity, value);
    }

    return entity;

  }

  /**
   * Select random value from array
   * <p>
   *
   * @param values
   *     The array values
   * @return The random value
   */
  protected <T> T randomSelection(T[] values) {
    int index = randomInt(0, values.length);
    return values[index];
  }

  /**
   * Creates random date
   * <p>
   *
   * @param origin
   *     The origin
   * @param bound
   *     The bound
   * @return The random value
   */
  protected Date randomDate(Date origin, Date bound) {
    long l = randomLong(origin.getTime(), bound.getTime());
    return new Date(l);
  }

  /**
   * Creates random long
   * <p>
   *
   * @param origin
   *     The origin
   * @param bound
   *     The bound
   * @return The random value
   */
  protected long randomLong(long origin, long bound) {
    return ThreadLocalRandom.current().nextLong(origin, bound);
  }

  /**
   * Creates random int
   * <p>
   *
   * @param origin
   *     The origin
   * @param bound
   *     The bound
   * @return The random value
   */
  protected int randomInt(int origin, int bound) {
    return ThreadLocalRandom.current().nextInt(origin, bound);
  }

  /**
   * Creates random lorem upsum string
   * <p>
   *
   * @param minLength
   *     The string min length
   * @param maxLength
   *     The string max length
   * @return The random value
   */
  protected String randomString(int minLength, int maxLength) {

    return randomString(minLength, maxLength, false, -1);
  }

  /**
   * Creates random lorem upsum string
   * <p>
   *
   * @param minLength
   *     The string min length
   * @param maxLength
   *     The string max length
   * @param carriageReturnFrequency
   *     Carriagr return frequency
   * @return The random value
   */
  protected String randomString(int minLength, int maxLength, boolean ponctuation, int carriageReturnFrequency) {

    int targetLength = randomInt(minLength, maxLength - 1);

    StringBuilder sb = new StringBuilder();

    String word = randomSelection(LOREM_IPSUM);

    int nextCarriageReturn = carriageReturnFrequency > 0 ? randomInt(1, carriageReturnFrequency) : -1;

    boolean nextUpperCase = true;
    while (sb.length() + word.length() < targetLength) {

      if (nextCarriageReturn >= 0) {
        if (nextCarriageReturn == 0) {
          sb.append('\n');
          nextCarriageReturn = randomInt(1, carriageReturnFrequency);
        } else {
          nextCarriageReturn--;
        }
      }

      if (nextUpperCase) {
        char prefix = Character.toUpperCase(word.charAt(0));
        word = word.length() == 1 ? String.valueOf(prefix) : String.valueOf(prefix) + word.substring(1);

        nextUpperCase = false;
      }
      sb.append(word);

      char c = sb.charAt(sb.length() - 1);
      if (ponctuation) {
        sb.append(' ');
        if (c == '.') {
          nextUpperCase = true;
        }
      } else {
        if (c == ',' || c == '.') {
          sb.deleteCharAt(sb.length() - 1).append(' ');
        }
        sb.append(' ');
      }

      word = randomSelection(LOREM_IPSUM);
    }

    String s = sb.toString().trim();
    if (!s.endsWith(".")) {
      s += '.';
    }

    return s;
  }

  /**
   * Return a pair of String, Object
   * <p>
   *
   * @param propertyName
   *     The property name
   * @param propertyValue
   *     The property value
   * @return
   */
  protected Pair<String, Object> init(String propertyName, Object propertyValue) {
    return Pair.of(propertyName, propertyValue);
  }
}
