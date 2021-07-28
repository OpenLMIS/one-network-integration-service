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

import org.openlmis.onenetwork.integration.configuration.IntegrationConfiguration;
import org.openlmis.onenetwork.integration.dto.IntegrationStatus;
import org.openlmis.onenetwork.integration.scheduler.ScheduledTasks;
import org.openlmis.onenetwork.integration.service.ProcessingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for displaying and setting integration information.
 */
@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationController.class);

  private final IntegrationConfiguration integrationConfiguration;
  private final ProcessingService processingService;

  @Autowired
  public IntegrationController(IntegrationConfiguration integrationConfiguration,
                               ProcessingService processingService) {
    this.integrationConfiguration = integrationConfiguration;
    this.processingService = processingService;
  }

  /**
   * Returns the {@link ScheduledTasks} status.
   *
   * @return {@link IntegrationStatus} info.
   */
  @RequestMapping("/status")
  public IntegrationStatus getIntegrationStatus() {
    LOGGER.debug("Getting scheduler status info");
    return IntegrationStatus.builder()
            .enabled(integrationConfiguration.getEnable())
            .build();
  }

  /**
   * Enables integration and sends full data.
   */
  @PutMapping("/enable")
  @ResponseStatus(HttpStatus.OK)
  public void enableIntegration() {
    LOGGER.debug("Sending full data");
    processingService.processFullCsvData();

    LOGGER.debug("Enabling the scheduler");
    this.integrationConfiguration.setEnable(true);
  }

  /**
   * Disables integration and sends unprocessed data.
   */
  @PutMapping("/disable")
  @ResponseStatus(HttpStatus.OK)
  public void disableIntegration() {
    LOGGER.debug("Disabling the scheduler");
    this.integrationConfiguration.setEnable(false);

    LOGGER.debug("Sending unprocessed data");
    processingService.processQueueData();
  }
}
