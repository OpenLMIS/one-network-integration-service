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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openlmis.onenetwork.integration.dto.Facility;
import org.openlmis.onenetwork.integration.dto.FacilityForCsv;
import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.dto.OrderableForCsv;
import org.openlmis.onenetwork.integration.dto.SohForCsv;
import org.openlmis.onenetwork.integration.dto.StockEvent;
import org.openlmis.onenetwork.integration.dto.StockOnHand;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardSummaries;
import org.openlmis.onenetwork.integration.service.referencedata.FacilityDataService;
import org.openlmis.onenetwork.integration.service.referencedata.OrderableDataService;
import org.openlmis.onenetwork.integration.service.referencedata.StockCardSummariesService;
import org.openlmis.onenetwork.integration.sftp.SftpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProcessingService {

  private static final String SUFFIX_CSV_NAME = ".csv";
  private static final String ORDERABLE_PREFIX_CSV_NAME = "products-";
  private static final String FACILITY_PREFIX_CSV_NAME = "facilities-";
  private static final String SOH_PREFIX_CSV_NAME = "SOH-";

  @Value("${time.zoneIdOneNetwork}")
  private String timeZoneId;

  @Value("${country}")
  private String country;

  private final OrderableDataService orderableDataService;
  private final FacilityDataService facilityDataService;
  private final SftpService sftpService;
  private final OrderableBufferService orderableBufferService;
  private final StockEventBufferService stockEventBufferService;
  private final StockCardSummariesService stockCardSummariesService;

  /**
   * ProcessingService constructor.
   */
  @Autowired
  public ProcessingService(OrderableDataService orderableDataService,
                           FacilityDataService facilityDataService,
                           OrderableBufferService orderableBufferService,
                           StockEventBufferService stockEventBufferService,
                           StockCardSummariesService stockCardSummariesService,
                           SftpService sftpService) {
    this.orderableDataService = orderableDataService;
    this.facilityDataService = facilityDataService;
    this.orderableBufferService = orderableBufferService;
    this.stockEventBufferService = stockEventBufferService;
    this.stockCardSummariesService = stockCardSummariesService;
    this.sftpService = sftpService;
  }

  /**
   * Gets the data from the buffer and send them to SFTP server.
   */
  public void processBufferedData() {
    processOrderableBufferedData();
    processSohBufferedData();
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
            .map(f -> f.toFacilityForCsv(timeZoneId, country))
            .collect(Collectors.toList());
    sftpService.send(objectsToCsvList, FacilityForCsv.class,
            generateCsvName(FACILITY_PREFIX_CSV_NAME));
  }

  /**
   * Fetches orderable data from buffer and send them to SFTP server.
   */
  public void processOrderableBufferedData() {
    List<OrderableForCsv> list = orderableBufferService.getAllAndClear()
        .stream()
        .map(Orderable::toOrderableForCsv)
        .collect(Collectors.toList());
    sftpService.send(list, OrderableForCsv.class, generateCsvName(ORDERABLE_PREFIX_CSV_NAME));
  }

  /**
   * Fetches stockOnHand data from buffer and send them to SFTP server.
   */
  public void processSohBufferedData() {
    List<StockEvent> stockEventList = stockEventBufferService.getAllAndClear();
    List<SohForCsv> list = fetchSohData(stockEventList);
    sftpService.send(list, SohForCsv.class, generateCsvName(SOH_PREFIX_CSV_NAME));
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

  /**
   * Fetches {@link SohForCsv} list from {@link StockEvent} list.
   *
   * @return list of {@link SohForCsv}.
   */
  public List<SohForCsv> fetchSohData(List<StockEvent> stockEventList) {
    List<StockOnHand> sohList = new ArrayList<>();
    for (StockEvent stockEvent : stockEventList) {
      Facility facilityClass = facilityDataService.findWithId(stockEvent.getFacilityId());
      String facility = facilityClass.getName();
      String facilityCode = facilityClass.getCode();
      List<StockCardSummaries> stockCardSummariesList = stockCardSummariesService
          .getStockCardSummaries(stockEvent.getFacilityId(), stockEvent.getProgramId());
      for (StockCardSummaries stockCardSummaries : stockCardSummariesList) {
        Orderable orderable = orderableDataService
            .findWithId(stockCardSummaries.getOrderable().getId());
        String product = orderable.getFullProductName();
        String productCode = orderable.getProductCode();
        String soh = stockCardSummaries.getStockOnHand().toString();
        StockOnHand stockOnHand = new StockOnHand(product,
            productCode, facility, facilityCode, soh);
        sohList.add(stockOnHand);
      }
    }
    return sohList.stream().map(StockOnHand::toSohForCsv).collect(Collectors.toList());
  }
}
