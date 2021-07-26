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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openlmis.onenetwork.integration.domain.OrderableForCsv;

public class CsvServiceTest {

  private static final String MANAGING_NAME = "OpenLMIS Demo";
  private static final String CONTENT =
          "ManagingEntName,ManagingOrgName,ItemName,DisplayName,Description\n"
          + "\"OpenLMIS Demo\",\"OpenLMIS Demo\",TEST001,TEST001-TEST001,TEST001\n"
          + "\"OpenLMIS Demo\",\"OpenLMIS Demo\",TEST002,TEST002-TEST002,TEST002\n";

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private final CsvService csvService = new CsvService();

  @Test
  public void shouldWriteDataToCsvFile() throws Exception {
    List<OrderableForCsv> data = provideCsvData();
    File tempFile = tempFolder.newFile("temp.csv");

    csvService.writeDataToCsvFile(data, OrderableForCsv.class, tempFile);

    String fileContent = FileUtils.readFileToString(tempFile, StandardCharsets.UTF_8);

    assertThat(fileContent).isNotBlank();
    assertThat(CONTENT).isEqualTo(fileContent);

  }

  private List<OrderableForCsv> provideCsvData() {
    return Stream.of(
            new OrderableForCsv(
                    MANAGING_NAME,
                    MANAGING_NAME,
                    "TEST001",
                    "TEST001-TEST001",
                    "TEST001"
            ),
            new OrderableForCsv(
                    MANAGING_NAME,
                    MANAGING_NAME,
                    "TEST002",
                    "TEST002-TEST002",
                    "TEST002"
            )
    ).collect(Collectors.toList());
  }

}
