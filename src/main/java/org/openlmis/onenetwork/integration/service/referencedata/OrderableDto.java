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

package org.openlmis.onenetwork.integration.service.referencedata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode()
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderableDto {

  private String managingEntName;

  private String managingOrgName;

  private String productCode;

  private String fullProductName;

  private String displayName;

  /**
   * Creates orderable object.
   */
  public OrderableDto() {
    this.displayName = this.getProductCode() + "-" + this.getFullProductName();
    this.managingOrgName = "OpenLMIS Demo";
    this.managingEntName = "OpenLMIS Demo";
  }
}
