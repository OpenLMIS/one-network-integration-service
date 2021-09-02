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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonPropertyOrder({
    "#Demand Time",
    "Item Name",
    "Item Enterprise Name",
    "Quantity",
    "Quantity UOM",
    "Site Name",
    "Site Enterprise Name",
    "Site Organization Name"
})
@Getter
@RequiredArgsConstructor
public class ConsumptionForCsv {

  private static final String MANAGING_NAME = "OpenLMIS Demo";
  private static final String EACH = "EACH";

  @JsonProperty("#Demand Time")
  private final String demandTime;

  @JsonProperty("Item Name")
  private final String itemName;

  @JsonProperty("Item Enterprise Name")
  private final String itemEnterpriseName = MANAGING_NAME;

  @JsonProperty("Quantity")
  private final String quantity;

  @JsonProperty("Quantity UOM")
  private final String quantityUom = EACH;

  @JsonProperty("Site Name")
  private final String siteName;

  @JsonProperty("Site Enterprise Name")
  private final String siteEnterpriseName = MANAGING_NAME;

  @JsonProperty("Site Organization Name")
  private final String siteOrganizationName = MANAGING_NAME;
}
