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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class OrderableTest {

  private static final String MANAGING_NAME = "OpenLMIS Demo";

  private static final String JSON = "{\n"
          + "    \"id\" : \"c7298536-4cd9-46f3-bf9f-3690473a26fe\",\n"
          + "    \"productCode\" : \"0268-8000\",\n"
          + "    \"fullProductName\" : \"10 TREE MIX\",\n"
          + "    \"netContent\" : 53,\n"
          + "    \"packRoundingThreshold\" : 4,\n"
          + "    \"roundToZero\" : true\n"
          + "    }";

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void shouldMapFromJsonObjectToOrderable() throws Exception {
    Orderable orderable = objectMapper.readValue(JSON, Orderable.class);

    assertThat(orderable).isNotNull();
    assertThat(orderable.getProductCode()).isEqualTo("0268-8000");
    assertThat(orderable.getFullProductName()).isEqualTo("10 TREE MIX");
  }

  @Test
  public void shouldConvertToOrderableForCsv() throws Exception {
    Orderable orderable = objectMapper.readValue(JSON, Orderable.class);

    OrderableForCsv orderableForCsv = orderable.toOrderableForCsv();
    assertThat(orderableForCsv).isNotNull();
    assertThat(orderableForCsv.getManagingEntName()).isEqualTo(MANAGING_NAME);
    assertThat(orderableForCsv.getProductCode()).isEqualTo("0268-8000");
    assertThat(orderableForCsv.getFullProductName()).isEqualTo("10 TREE MIX");
    assertThat(orderableForCsv.getDisplayName()).isEqualTo("10 TREE MIX - 0268-8000");
    assertThat(orderableForCsv.getActive()).isEqualTo("1");
  }

}
