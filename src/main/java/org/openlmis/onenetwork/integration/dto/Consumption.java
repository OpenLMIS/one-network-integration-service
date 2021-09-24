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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Consumption {
  private String itemName;
  private String siteName;
  private String quantity;
  private String demandTime;

  /**
   * {@link Consumption} constructor.
   */
  public Consumption(String productCode,
                     String facility,
                     String facilityCode,
                     String consumption,
                     ZonedDateTime lastUpdate) {
    this.itemName = productCode;
    this.siteName = facility + "-" + facilityCode;
    this.quantity = consumption;
    this.demandTime = lastUpdate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  /**
   * Converts the {@link StockOnHand} to {@link StockOnHandForCsv} object.
   * @return {@link StockOnHandForCsv}
   */
  public ConsumptionForCsv toConsumptionForCsv() {
    return new ConsumptionForCsv(this.demandTime, this.itemName, this.quantity, this.siteName);
  }
}
