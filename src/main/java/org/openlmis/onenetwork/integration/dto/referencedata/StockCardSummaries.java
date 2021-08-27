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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = StockCardSummaries.Builder.class)
public class StockCardSummaries {

  private ObjectReferenceDto orderable;
  private Set<CanFulfillForMeEntryDto> canFulfillForMe;

  public StockCardSummaries(ObjectReferenceDto orderable,
                            Set<CanFulfillForMeEntryDto> canFulfillForMe) {
    this.orderable = orderable;
    this.canFulfillForMe = canFulfillForMe;
  }

  /**
   * Sums stock on hand values from all {@link CanFulfillForMeEntryDto} instances.
   * @return sum of all stock on hand values
   */
  public Integer getStockOnHand() {
    List<CanFulfillForMeEntryDto> fulfillEntries = isEmpty(canFulfillForMe) ? null : canFulfillForMe
        .stream()
        .filter(a -> a.getStockOnHand() != null)
        .collect(toList());

    return isEmpty(fulfillEntries) ? null : fulfillEntries.stream()
        .mapToInt(CanFulfillForMeEntryDto::getStockOnHand)
        .sum();
  }

  @JsonPOJOBuilder
  public static class Builder {

    ObjectReferenceDto orderable;
    Set<CanFulfillForMeEntryDto> canFulfillForMe;

    public StockCardSummaries.Builder withOrderable(ObjectReferenceDto orderable) {
      this.orderable = orderable;
      return this;
    }

    public StockCardSummaries.Builder withCanFulfillForMe(
        Set<CanFulfillForMeEntryDto> canFulfillForMe) {
      this.canFulfillForMe = canFulfillForMe;
      return this;
    }

    public StockCardSummaries build() {
      return new StockCardSummaries(orderable, canFulfillForMe);
    }
  }
}
