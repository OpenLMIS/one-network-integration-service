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

package org.openlmis.onenetwork.integration.web;

import java.util.List;
import java.util.stream.Collectors;

import org.openlmis.onenetwork.integration.configuration.SchedulerConfiguration;
import org.openlmis.onenetwork.integration.domain.Orderable;
import org.openlmis.onenetwork.integration.domain.OrderableForCsv;
import org.openlmis.onenetwork.integration.domain.SchedulerStatus;
import org.openlmis.onenetwork.integration.service.CsvService;
import org.openlmis.onenetwork.integration.service.referencedata.OrderableDataService;
import org.openlmis.onenetwork.integration.sftp.SftpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for displaying and setting scheduler information.
 */
@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);

  private final SchedulerConfiguration schedulerConfiguration;
  private final OrderableDataService orderableService;
  private final CsvService csvService;

  @Autowired
  private SftpService sftpService;

  /**
   * xxxxxx the schedule.
   *
   */
  @Autowired
  public SchedulerController(SchedulerConfiguration schedulerConfiguration,
                             OrderableDataService orderableService,
                             CsvService csvService) {
    this.schedulerConfiguration = schedulerConfiguration;
    this.orderableService = orderableService;
    this.csvService = csvService;
  }

  /**
   * Enables the schedule.
   *
   * @return {SchedulerStatus} Returns scheduler status info.
   */
  @RequestMapping("/status")
  public SchedulerStatus schedulerStatus() {
    LOGGER.debug("Getting scheduler status info");
    return SchedulerStatus.builder()
            .schedulerEnabled(schedulerConfiguration.getEnable())
            .build();
  }

  /**
   * Enables the schedule.
   *
   * @return {SchedulerStatus} Returns scheduler status info.
   */
  @PutMapping("/enable")
  public SchedulerStatus enableScheduler() {
    LOGGER.debug("Enabling the scheduler");
    System.out.println("szukam orderabli");
    List<OrderableForCsv> objectsToCsvList = this.orderableService.getAllOrderables()
            .stream()
            .map(Orderable::toCsvObject)
            .collect(Collectors.toList());
    sftpService.send(objectsToCsvList, OrderableForCsv.class, this.csvService.getCsvName());
    this.schedulerConfiguration.setEnable(true);
    return SchedulerStatus.builder()
            .schedulerEnabled(schedulerConfiguration.getEnable())
            .build();
  }

  /**
   * Disables the schedule.
   *
   * @return {SchedulerStatus} Returns scheduler status info.
   */
  @PutMapping("/disable")
  public SchedulerStatus disableScheduler() {
    LOGGER.debug("Disabling the scheduler");
    this.schedulerConfiguration.setEnable(false);

    LOGGER.debug("Sending collected data while disabled scheduler");
    sftpService.send();

    return SchedulerStatus.builder()
            .schedulerEnabled(schedulerConfiguration.getEnable())
            .build();
  }
}
