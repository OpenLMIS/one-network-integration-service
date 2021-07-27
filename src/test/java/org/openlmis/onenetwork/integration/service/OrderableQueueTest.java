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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.openlmis.onenetwork.integration.dto.Orderable;

public class OrderableQueueTest {

  @InjectMocks
  OrderableQueue orderableQueue = new OrderableQueue();

  private Orderable orderable = new Orderable.Builder()
          .withProductCode("testCode")
          .withFullProductName("testName")
          .build();

  @Test
  public void shouldReturnEmptyList() {
    assertEquals(0, orderableQueue.getList().size());
  }

  @Test
  public void shouldAddOrderableToListAndReturnList() {
    orderableQueue.add(orderable);
    List result = orderableQueue.getList();
    Orderable resultOrderable = (Orderable) result.get(0);

    assertEquals(Arrays.asList(orderable), result);
    assertEquals("testCode", resultOrderable.getProductCode());
    assertEquals("testName", resultOrderable.getFullProductName());
  }
}
