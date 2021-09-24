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

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.openlmis.onenetwork.integration.dto.Consumption;
import org.openlmis.onenetwork.integration.dto.ConsumptionForCsv;
import org.openlmis.onenetwork.integration.dto.Facility;
import org.openlmis.onenetwork.integration.dto.FacilityForCsv;
import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.dto.OrderableForCsv;
import org.openlmis.onenetwork.integration.dto.StockOnHand;
import org.openlmis.onenetwork.integration.dto.StockOnHandForCsv;
import org.openlmis.onenetwork.integration.dto.referencedata.ReasonType;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardSummaries;
import org.openlmis.onenetwork.integration.dto.referencedata.StockEvent;
import org.openlmis.onenetwork.integration.dto.referencedata.StockEventLineItemDto;
import org.openlmis.onenetwork.integration.service.referencedata.FacilityDataService;
import org.openlmis.onenetwork.integration.service.referencedata.OrderableDataService;
import org.openlmis.onenetwork.integration.service.referencedata.StockCardLineItemReasonDataService;
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
  private static final String CONSUMPTION_PREFIX_CSV_NAME = "consumption-";

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
  private final StockCardLineItemReasonDataService stockCardLineItemReasonDataService;

  /**
   * ProcessingService constructor.
   */
  @Autowired
  public ProcessingService(OrderableDataService orderableDataService,
                           FacilityDataService facilityDataService,
                           OrderableBufferService orderableBufferService,
                           StockEventBufferService stockEventBufferService,
                           StockCardSummariesService stockCardSummariesService,
                           StockCardLineItemReasonDataService stockCardLineItemReasonDataService,
                           SftpService sftpService) {
    this.orderableDataService = orderableDataService;
    this.facilityDataService = facilityDataService;
    this.orderableBufferService = orderableBufferService;
    this.stockEventBufferService = stockEventBufferService;
    this.stockCardSummariesService = stockCardSummariesService;
    this.stockCardLineItemReasonDataService = stockCardLineItemReasonDataService;
    this.sftpService = sftpService;
  }

  /**
   * Gets the data from the buffer and send them to SFTP server.
   */
  public void processBufferedData() {
    processOrderableBufferedData();
    processStockEventBufferedData();
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
  public void processStockEventBufferedData() {
    List<StockEvent> stockEventList = stockEventBufferService.getAllAndClear();
    List<StockOnHandForCsv> list = fetchSohData(stockEventList);
    List<ConsumptionForCsv> consumptionList = fetchConsumptionData(stockEventList);
    sftpService.send(list, StockOnHandForCsv.class, generateCsvName(SOH_PREFIX_CSV_NAME));
    sftpService.send(consumptionList,
        ConsumptionForCsv.class,
        generateCsvName(CONSUMPTION_PREFIX_CSV_NAME));
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
   * Fetches {@link StockOnHandForCsv} list from {@link StockEvent} list.
   *
   * @return list of {@link StockOnHandForCsv}.
   */
  private List<StockOnHandForCsv> fetchSohData(List<StockEvent> stockEventList) {
    List<StockOnHand> sohList = new ArrayList<>();
    for (StockEvent stockEvent : stockEventList) {
      Facility facilityClass = facilityDataService.findWithId(stockEvent.getFacilityId());
      String facility = facilityClass.getName();
      String facilityCode = facilityClass.getCode();
      List<StockCardSummaries> stockCardSummariesList = stockCardSummariesService
          .getStockCardSummaries(stockEvent.getFacilityId(), stockEvent.getProgramId());
      List<UUID> orderableIdList = stockEvent.getLineItems()
          .stream()
          .map(li -> li.getOrderableId())
          .collect(Collectors.toList());
      for (StockCardSummaries stockCardSummaries : stockCardSummariesList) {
        if (orderableIdList.contains(stockCardSummaries.getOrderable().getId())) {
          Orderable orderable = orderableDataService
              .findWithId(stockCardSummaries.getOrderable().getId());

          String productCode = orderable.getProductCode();
          DecimalFormat formatter = new DecimalFormat("###,###.###");
          String soh = formatter.format((double) stockCardSummaries.getStockOnHand());
          StockOnHand stockOnHand = new StockOnHand(productCode, facility, facilityCode, soh);
          sohList.add(stockOnHand);
        }
      }
    }
    return sohList.stream().map(StockOnHand::toSohForCsv).collect(Collectors.toList());
  }

  /**
   * Fetches {@link ConsumptionForCsv} list from {@link StockEvent} list.
   *
   * @return list of {@link ConsumptionForCsv}.
   */
  private List<ConsumptionForCsv> fetchConsumptionData(List<StockEvent> stockEventList) {
    List<Consumption> consumptionList = new ArrayList<>();

    for (StockEvent stockEvent : stockEventList) {
      Facility facilityClass = facilityDataService.findWithId(stockEvent.getFacilityId());
      String facility = facilityClass.getName();
      String facilityCode = facilityClass.getCode();
      for (StockEventLineItemDto lineItemDto : stockEvent.getLineItems()) {
        Orderable orderable = orderableDataService
             .findWithId(lineItemDto.getOrderableId());
        String productCode = orderable.getProductCode();
        String consumptionValue = "";
        if (lineItemDto.hasReasonId()) {
          ReasonType reasonType = stockCardLineItemReasonDataService
              .findWithId(lineItemDto.getReasonId()).getReasonType();
          consumptionValue = reasonType == ReasonType.DEBIT
              ? String.valueOf(lineItemDto.getQuantity() * -1)
              : lineItemDto.getQuantity().toString();
        } else if (!lineItemDto.hasReasonId()) {
          consumptionValue = lineItemDto.hasDestinationId()
              ? String.valueOf(lineItemDto.getQuantity() * -1)
              : lineItemDto.getQuantity().toString();
        }
        ZonedDateTime lastUpdate = lineItemDto.getOccurredDate();

        Consumption consumption = new Consumption(productCode,
            facility, facilityCode, consumptionValue, lastUpdate);
        consumptionList.add(consumption);
      }
    }

    return consumptionList
        .stream()
        .map(Consumption::toConsumptionForCsv)
        .collect(Collectors.toList());
  }
}
