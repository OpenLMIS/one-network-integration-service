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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.openlmis.onenetwork.integration.dto.Facility;
import org.openlmis.onenetwork.integration.dto.FacilityForCsv;
import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.dto.OrderableForCsv;
import org.openlmis.onenetwork.integration.service.referencedata.FacilityDataService;
import org.openlmis.onenetwork.integration.service.referencedata.OrderableDataService;
import org.openlmis.onenetwork.integration.sftp.SftpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessingService {

  private static final String SUFFIX_CSV_NAME = ".csv";
  private static final String ORDERABLE_PREFIX_CSV_NAME = "products-";
  private static final String FACILITY_PREFIX_CSV_NAME = "facilities-";

  private final OrderableDataService orderableDataService;
  private final FacilityDataService facilityDataService;
  private final SftpService sftpService;
  private final OrderableBufferService orderableBufferService;

  /**
   * ProcessingService constructor.
   */
  @Autowired
  public ProcessingService(OrderableDataService orderableDataService,
                           FacilityDataService facilityDataService,
                           OrderableBufferService orderableBufferService,
                           SftpService sftpService) {
    this.orderableDataService = orderableDataService;
    this.facilityDataService = facilityDataService;
    this.orderableBufferService = orderableBufferService;
    this.sftpService = sftpService;
  }

  /**
   * Gets the data from the buffer and send them to SFTP server.
   */
  public void processBufferedData() {
    List<OrderableForCsv> list = orderableBufferService.getAllAndClear()
            .stream()
            .map(Orderable::toOrderableForCsv)
            .collect(Collectors.toList());
    sftpService.send(list, OrderableForCsv.class, generateCsvName(ORDERABLE_PREFIX_CSV_NAME));
  }

  /**
   * Fetches the data from external services and send them to SFTP server.
   */
  public void processFullCsvData() {
    processFullOrderableData();
    processFullFacilityData();
  }

  private void processFullOrderableData() {
    List<OrderableForCsv> objectsToCsvList = orderableDataService.getAllOrderables()
            .stream()
            .map(Orderable::toOrderableForCsv)
            .collect(Collectors.toList());
    sftpService.send(objectsToCsvList, OrderableForCsv.class,
            generateCsvName(ORDERABLE_PREFIX_CSV_NAME));
  }

  private void processFullFacilityData() {
    List<FacilityForCsv> objectsToCsvList = facilityDataService.getAllFacilities()
            .stream()
            .map(Facility::toFacilityForCsv)
            .collect(Collectors.toList());
    sftpService.send(objectsToCsvList, FacilityForCsv.class,
            generateCsvName(FACILITY_PREFIX_CSV_NAME));
  }

  /**
   * Generates the name for the CSV file.
   *
   * @return CSV file name.
   */
  private String generateCsvName(String prefix) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
    LocalDateTime timestamp = LocalDateTime.now();
    return prefix + dateFormat.format(timestamp) + SUFFIX_CSV_NAME;
  }

}
