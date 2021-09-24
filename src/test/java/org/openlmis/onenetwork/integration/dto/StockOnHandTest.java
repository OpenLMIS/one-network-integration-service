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

import org.junit.Test;

public class StockOnHandTest {

  private static final String PRODUCT_CODE = "productCode";
  private static final String FACILITY = "facility";
  private static final String FACILITY_CODE = "facilityCode";
  private static final String SOH = "10";

  @Test
  public void shouldConvertToSohForCsv() {
    StockOnHand stockOnHand = new StockOnHand(PRODUCT_CODE, FACILITY, FACILITY_CODE, SOH);

    StockOnHandForCsv stockOnHandForCsv = stockOnHand.toSohForCsv();
    assertThat(stockOnHandForCsv).isNotNull();
    assertThat(stockOnHandForCsv.getItemName()).isEqualTo(PRODUCT_CODE);
    assertThat(stockOnHandForCsv.getSiteName()).isEqualTo(FACILITY + "-" + FACILITY_CODE);
    assertThat(stockOnHandForCsv.getOnHand()).isEqualTo(SOH);
  }
}
