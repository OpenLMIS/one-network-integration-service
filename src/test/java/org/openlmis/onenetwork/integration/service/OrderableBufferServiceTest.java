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
import org.junit.Before;
import org.junit.Test;
import org.openlmis.onenetwork.integration.dto.Orderable;

public class OrderableBufferServiceTest {

  OrderableBufferService orderableBufferService;

  @Before
  public void setUp() {
    orderableBufferService = new OrderableBufferService();
  }

  private Orderable orderable = new Orderable.Builder()
          .withProductCode("testCode")
          .withFullProductName("testName")
          .build();

  @Test
  public void shouldReturnEmptyList() {
    orderableBufferService.add(null);
    assertEquals(0, orderableBufferService.getAllAndClear().size());
  }

  @Test
  public void shouldAddOrderableToListAndReturnList() {
    orderableBufferService.add(orderable);
    List resultList = orderableBufferService.getAllAndClear();
    Orderable resultOrderable = (Orderable) resultList.get(0);

    assertEquals(orderable, resultOrderable);
    assertEquals("testCode", resultOrderable.getProductCode());
    assertEquals("testName", resultOrderable.getFullProductName());
  }

  @Test
  public void shouldClearQueue() {
    orderableBufferService.add(orderable);
    List resultList = orderableBufferService.getAllAndClear();
    List secondResultList = orderableBufferService.getAllAndClear();

    assertEquals(1, resultList.size());
    assertEquals(0, secondResultList.size());
  }
}
