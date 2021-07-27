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

package org.openlmis.onenetwork.integration.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = Orderable.Builder.class)
public class Orderable {

  private final String managingEntName;

  private final String managingOrgName;

  private final String productCode;

  private final String fullProductName;

  private final String displayName;

  private Orderable(String productCode, String fullProductName) {
    this.productCode = productCode;
    this.fullProductName = fullProductName;
    this.displayName = productCode + "-" + fullProductName;
    this.managingOrgName = "OpenLMIS Demo";
    this.managingEntName = "OpenLMIS Demo";
  }

  /**
   * Converts the {@link Orderable} to {@link OrderableForCsv} object.
   * @return {@link OrderableForCsv}
   */
  public OrderableForCsv toOrderableForCsv() {
    return new OrderableForCsv(
            this.managingEntName,
            this.managingOrgName,
            this.productCode,
            this.displayName,
            this.fullProductName);
  }

  @JsonPOJOBuilder
  public static class Builder {

    String productCode;
    String fullProductName;

    public Builder withProductCode(String productCode) {
      this.productCode = productCode;
      return this;
    }

    public Builder withFullProductName(String fullProductName) {
      this.fullProductName = fullProductName;
      return this;
    }

    public Orderable build() {
      return new Orderable(productCode, fullProductName);
    }
  }

}
