/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.onenetwork.integration.service;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicQueue<T> extends AbstractQueue<T> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private ConcurrentLinkedQueue<T> queue;

  public BasicQueue() {
    this.queue = new ConcurrentLinkedQueue<>();
  }

  @Override
  public Iterator<T> iterator() {
    return queue.iterator();
  }

  @Override
  public int size() {
    return queue.size();
  }

  @Override
  public boolean offer(T t) {
    synchronized (queue) {
      if (t == null) {
        return false;
      }
      return queue.offer(t);
    }
  }

  @Override
  public T poll() {
    return queue.poll();
  }

  @Override
  public T peek() {
    synchronized (queue) {
      return queue.peek();
    }
  }

  /**
   * Gets list from queue, and then clears this list.
   *
   * @return {@link List} of {@link T} Returns list.
   */
  public List<T> getList() {
    synchronized (queue) {
      logger.debug("Getting list from queue and clearing list");
      List<T> result = new ArrayList<>(queue);
      queue.clear();

      return result;
    }
  }
}
