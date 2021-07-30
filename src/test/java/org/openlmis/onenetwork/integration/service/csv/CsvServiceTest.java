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

package org.openlmis.onenetwork.integration.service.csv;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openlmis.onenetwork.integration.dto.OrderableForCsv;

public class CsvServiceTest {

  private static final String CONTENT =
          "ManagingEntName,ItemName,Description,Active\n"
          + "\"OpenLMIS Demo\",TEST001,TEST001,true\n"
          + "\"OpenLMIS Demo\",TEST002,TEST002,true\n";

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private final CsvService csvService = new CsvService();

  @Test
  public void shouldWriteDataToCsvFile() throws Exception {
    List<OrderableForCsv> data = provideCsvData();

    byte[] fileByte = csvService.generateCsv(data, OrderableForCsv.class);
    byte[] convertedContent = CONTENT.getBytes(StandardCharsets.UTF_8);

    assertThat(fileByte).isEqualTo(convertedContent);
  }

  private List<OrderableForCsv> provideCsvData() {
    return Stream.of(
            new OrderableForCsv(
                    "TEST001",
                    "TEST001",
                    true
            ),
            new OrderableForCsv(
                    "TEST002",
                    "TEST002",
                    true
            )
    ).collect(Collectors.toList());
  }

}
