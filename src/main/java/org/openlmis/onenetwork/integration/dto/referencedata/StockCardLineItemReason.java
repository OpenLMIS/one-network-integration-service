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
import lombok.Getter;

@Getter
@JsonDeserialize(builder = StockCardLineItemReason.Builder.class)
public class StockCardLineItemReason {

  private String name;

  private String description;

  private ReasonType reasonType;

  /**
   * Constructor of {@link StockCardLineItemReason}.
   */
  public StockCardLineItemReason(String name, String description, ReasonType reasonType) {
    this.name = name;
    this.description = description;
    this.reasonType = reasonType;
  }

  @JsonPOJOBuilder
  public static class Builder {

    String name;
    String description;
    ReasonType reasonType;

    public StockCardLineItemReason.Builder withName(String name) {
      this.name = name;
      return this;
    }

    public StockCardLineItemReason.Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public StockCardLineItemReason.Builder withReasonType(ReasonType reasonType) {
      this.reasonType = reasonType;
      return this;
    }

    public StockCardLineItemReason build() {
      return new StockCardLineItemReason(name, description, reasonType);
    }
  }
}
