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

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class StockCardSummariesTest {

  private static final String JSON = "[\n"
      + " {\n"
      + "   \"orderable\" : {\n"
      + "     \"id\" : \"d602d0c6-4052-456c-8ccd-61b4ad77bece\",\n"
      + "     \"href\" : \"http://192.168.1.68/api/orderables/d602d0c6-4052-456c-8ccd-61b4ad77bece\",\n"
      + "     \"versionNumber\": 1\n"
      + "   },\n"
      + "   \"stockOnHand\": 9\n"
      + " }\n"
      + "]";

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void shouldMapFromJsonObjectToStockCardSummariesList() throws Exception {
    List<StockCardSummaries> stockCardSummariesList = objectMapper
        .readValue(JSON, objectMapper
            .getTypeFactory()
            .constructCollectionType(List.class, StockCardSummaries.class));

    assertThat(stockCardSummariesList).isNotNull();
    assertThat(stockCardSummariesList.size()).isEqualTo(1);
    assertThat(stockCardSummariesList.get(0).getOrderable().getId())
        .isEqualTo(UUID.fromString("d602d0c6-4052-456c-8ccd-61b4ad77bece"));
  }
}
