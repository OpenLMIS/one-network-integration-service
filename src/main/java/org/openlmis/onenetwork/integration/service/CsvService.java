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

package org.openlmis.onenetwork.integration.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CsvService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String PREFIX_CSV_NAME = "products-";
  private static final String SUFFIX_CSV_NAME = ".csv";

  /**
   * Creates csv file.
   */
  public <T> File createCsvFile(List<T> elements, Class<T> type, File csvFile)
      throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    csvMapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
    CsvSchema csvSchema = csvMapper
        .schemaFor(type)
        .withHeader();
    ObjectWriter csvWriter = csvMapper.writer(csvSchema.withLineSeparator("\n"));
    csvWriter.writeValue(csvFile, elements);
    logger.debug("Csv file created");
    return csvFile;
  }

  /**
   * Returns name of csv file.
   */
  public String getCsvName() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    return PREFIX_CSV_NAME + dateFormat.format(timestamp) + SUFFIX_CSV_NAME;
  }
}
