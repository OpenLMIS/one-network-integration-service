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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicQueue<T> {
  private Queue queue;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public BasicQueue() {
    this.queue = QueueUtils.synchronizedQueue(new CircularFifoQueue());
  }

  /**
   * Get list from queue.
   *
   * @return {@link List} of {@link T} Returns list.
   */
  public List<T> getList() {
    synchronized (queue) {
      logger.debug("Getting list from queue");
      List<T> result = new ArrayList<>();
      Iterator i = queue.iterator();

      while (i.hasNext()) {
        result.add((T) i.next());
      }
      queue.clear();

      return result;
    }
  }

  /**
   * Add element to queue.
   */
  public void add(T element) {
    synchronized (queue) {
      logger.debug("Adding to the queue");
      queue.add(element);
    }
  }
}
