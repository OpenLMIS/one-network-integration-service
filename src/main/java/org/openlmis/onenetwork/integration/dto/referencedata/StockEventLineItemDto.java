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

package org.openlmis.onenetwork.integration.dto.referencedata;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public class StockEventLineItemDto {

  private UUID orderableId;
  private UUID lotId;
  private Integer quantity;
  private Map<String, String> extraData;
  private LocalDate occurredDate;
  private UUID reasonId;
  private UUID sourceId;
  private UUID destinationId;

  public boolean hasReasonId() {
    return this.reasonId != null;
  }

  public boolean hasDestinationId() {
    return this.destinationId != null;
  }
}
