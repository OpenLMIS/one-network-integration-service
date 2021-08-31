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
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.onenetwork.integration.dto.referencedata.StockEvent;

public class StockEventTest {

  private static final String JSON = "{\n"
      + "    \"programId\" : \"c7298536-4cd9-46f3-bf9f-3690473a26fe\",\n"
      + "    \"facilityId\" : \"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce\",\n"
      + "    \"lineItems\" : [\n"
      + "      {\n"
      + "        \"orderableId\" : \"880cf2eb-7b68-4450-a037-a0dec1a17987\",\n"
      + "        \"lotId\" : null,\n"
      + "        \"quantity\":3,\n"
      + "        \"reasonId\" : \"c1fc3cf3-da18-44b0-a220-77c985202e06\",\n"
      + "        \"reasonFreeText\" : null,\n"
      + "        \"destinationId\" : \"e89eaf68-50c1-47f2-b83a-5b51ffa2206e\",\n"
      + "        \"destinationFreeText\" : null\n"
      + "      },\n"
      + "      {\n"
      + "        \"orderableId\" : \"d602d0c6-4052-456c-8ccd-61b4ad77bece\",\n"
      + "        \"lotId\" : null,\n"
      + "        \"quantity\" : 1,\n"
      + "        \"reasonId\" : \"c1fc3cf3-da18-44b0-a220-77c985202e06\",\n"
      + "        \"reasonFreeText\" : null,\n"
      + "        \"destinationId\" : \"e89eaf68-50c1-47f2-b83a-5b51ffa2206e\",\n"
      + "        \"destinationFreeText\" : null\n"
      + "      }\n"
      + "      ]\n"
      + "    }";

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void shouldMapFromJsonObjectToStockEvent() throws Exception {
    StockEvent stockEvent = objectMapper.readValue(JSON, StockEvent.class);

    assertThat(stockEvent).isNotNull();
    assertThat(stockEvent.getProgramId())
        .isEqualTo(UUID.fromString("c7298536-4cd9-46f3-bf9f-3690473a26fe"));
    assertThat(stockEvent.getFacilityId())
        .isEqualTo(UUID.fromString("e6799d64-d10d-4011-b8c2-0e4d4a3f65ce"));
    assertThat(stockEvent.getLineItems().size()).isEqualTo(2);
    assertThat(stockEvent.getLineItems().get(0).getOrderableId())
        .isEqualTo(UUID.fromString("880cf2eb-7b68-4450-a037-a0dec1a17987"));
    assertThat(stockEvent.getLineItems().get(0).getQuantity()).isEqualTo(3);
  }
}
