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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

public class ConsumptionTest {

  private static final String MANAGING_NAME = "OpenLMIS Demo";
  private static final String EACH = "EACH";

  @Test
  public void shouldConvertToConsumptionForCsv() {
    String productCode = "productCode";
    String facility = "facility";
    String facilityCode = "facilityCode";
    String consumpt = "10";
    ZonedDateTime lastUpdate = ZonedDateTime.now();
    Consumption consumption = new Consumption(productCode,
        facility,
        facilityCode,
        consumpt,
        lastUpdate);

    ConsumptionForCsv consumptionForCsv = consumption.toConsumptionForCsv();
    assertThat(consumptionForCsv).isNotNull();
    assertThat(consumptionForCsv.getDemandTime()).isEqualTo(lastUpdate
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    assertThat(consumptionForCsv.getItemName()).isEqualTo(productCode);
    assertThat(consumptionForCsv.getItemEnterpriseName()).isEqualTo(MANAGING_NAME);
    assertThat(consumptionForCsv.getQuantity()).isEqualTo(consumpt);
    assertThat(consumptionForCsv.getQuantityUom()).isEqualTo(EACH);
    assertThat(consumptionForCsv.getSiteName()).isEqualTo(facility + "-" + facilityCode);
    assertThat(consumptionForCsv.getSiteEnterpriseName()).isEqualTo(MANAGING_NAME);
    assertThat(consumptionForCsv.getSiteOrganizationName()).isEqualTo(MANAGING_NAME);
  }
}
