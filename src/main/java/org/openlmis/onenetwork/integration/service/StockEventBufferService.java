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
import java.util.List;
import org.openlmis.onenetwork.integration.dto.StockEvent;
import org.springframework.stereotype.Service;

@Service
public class StockEventBufferService implements BufferService<StockEvent> {

  private final List<StockEvent> buffer;

  public StockEventBufferService(List<StockEvent> buffer) {
    this.buffer = buffer;
  }

  @Override
  public boolean add(StockEvent stockEvent) {
    synchronized (buffer) {
      if (stockEvent == null) {
        return false;
      }
      return buffer.add(stockEvent);
    }
  }

  /**
   * Gets elements from buffer, and then clears this buffer.
   *
   * @return {@link List} of {@link StockEvent} list.
   */
  @Override
  public List<StockEvent> getAllAndClear() {
    synchronized (buffer) {
      List<StockEvent> result = new ArrayList<>(buffer);
      buffer.clear();
      System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~buffer: " + result.size());
      return result;
    }
  }
}
