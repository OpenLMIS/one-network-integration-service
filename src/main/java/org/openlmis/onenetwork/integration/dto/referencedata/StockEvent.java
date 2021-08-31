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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
@JsonDeserialize(builder = StockEvent.Builder.class)
public class StockEvent {

  private final UUID facilityId;
  private final UUID programId;
  private final List<StockEventLineItemDto> lineItems;

  private StockEvent(UUID facilityId, UUID programId, List<StockEventLineItemDto> lineItems) {
    this.facilityId = facilityId;
    this.programId = programId;
    this.lineItems = lineItems;
  }

  @JsonPOJOBuilder
  public static class Builder {

    UUID facilityId;
    UUID programId;
    List<StockEventLineItemDto> lineItems;

    public StockEvent.Builder withFacilityId(UUID facilityId) {
      this.facilityId = facilityId;
      return this;
    }

    public StockEvent.Builder withProgramId(UUID programId) {
      this.programId = programId;
      return this;
    }

    public StockEvent.Builder withLineItems(List<StockEventLineItemDto> lineItems) {
      this.lineItems = lineItems;
      return this;
    }

    public StockEvent build() {
      return new StockEvent(facilityId, programId, lineItems);
    }
  }
}
