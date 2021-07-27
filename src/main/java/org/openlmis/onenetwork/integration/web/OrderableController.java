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

package org.openlmis.onenetwork.integration.web;

import org.openlmis.onenetwork.integration.configuration.SchedulerConfiguration;
import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.service.OrderableQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration")
public class OrderableController {

  private OrderableQueue orderableQueue;
  private SchedulerConfiguration schedulerConfiguration;

  @Autowired
  public OrderableController(OrderableQueue orderableQueue,
                             SchedulerConfiguration schedulerConfiguration) {
    this.orderableQueue = orderableQueue;
    this.schedulerConfiguration = schedulerConfiguration;
  }

  /**
   * Receives product related data and writes them to the buffer.
   *
   * @return {@link Orderable}
   */
  @PostMapping("/orderable")
  public ResponseEntity<Orderable> getUpdatedOrderable(
          @RequestBody Orderable orderable) {
    if (this.schedulerConfiguration.getEnable()) {
      orderableQueue.add(orderable);
    }
    return ResponseEntity.ok()
            .body(orderable);
  }
}
