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

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.onenetwork.integration.dto.referencedata.StockEvent;

public class StockEventBufferServiceTest {

  StockEventBufferService stockEventBufferService;

  private StockEvent stockEvent = new StockEvent.Builder()
      .withFacilityId(UUID.fromString("d602d0c6-4052-456c-8ccd-61b4ad77bece"))
      .withProgramId(UUID.fromString("dce17f2e-af3e-40ad-8e00-3496adef44c3"))
      .build();

  @Before
  public void setUp() {
    stockEventBufferService = new StockEventBufferService();
  }

  @Test
  public void shouldReturnEmptyList() {
    stockEventBufferService.add(null);
    assertEquals(0, stockEventBufferService.getAllAndClear().size());
  }

  @Test
  public void shouldAddStockEventToListAndReturnList() {
    stockEventBufferService.add(stockEvent);
    List resultList = stockEventBufferService.getAllAndClear();
    StockEvent resultStockEvent = (StockEvent) resultList.get(0);

    assertEquals(stockEvent, resultStockEvent);
    assertEquals(UUID.fromString("d602d0c6-4052-456c-8ccd-61b4ad77bece"),
        resultStockEvent.getFacilityId());
    assertEquals(UUID.fromString("dce17f2e-af3e-40ad-8e00-3496adef44c3"),
        resultStockEvent.getProgramId());
  }

  @Test
  public void shouldClearQueue() {
    stockEventBufferService.add(stockEvent);
    List resultList = stockEventBufferService.getAllAndClear();
    List secondResultList = stockEventBufferService.getAllAndClear();

    assertEquals(1, resultList.size());
    assertEquals(0, secondResultList.size());
  }
}