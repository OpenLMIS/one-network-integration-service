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
        "#ManagingEntName",
        "ManagingOrgName",
        "ItemName",
        "DisplayName",
        "Description",
        "Active"
})
@Getter
@RequiredArgsConstructor
public class OrderableForCsv {

  private static final String MANAGING_NAME = "OpenLMIS Demo";

  @JsonProperty("#ManagingEntName")
  private final String managingEntName = MANAGING_NAME;

  @JsonProperty("ManagingOrgName")
  private final String managingOrgName = MANAGING_NAME;

  @JsonProperty("ItemName")
  private final String productCode;

  @JsonProperty("Description")
  private final String fullProductName;

  @JsonProperty("DisplayName")
  private final String displayName;

  @JsonProperty("Active")
  private final String active;
}
