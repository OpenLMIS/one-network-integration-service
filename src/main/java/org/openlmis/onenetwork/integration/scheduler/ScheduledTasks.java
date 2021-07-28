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

package org.openlmis.onenetwork.integration.scheduler;

import org.openlmis.onenetwork.integration.configuration.IntegrationConfiguration;
import org.openlmis.onenetwork.integration.service.ProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

  private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

  private final ProcessingService processingService;
  private final IntegrationConfiguration integrationConfiguration;

  @Autowired
  public ScheduledTasks(IntegrationConfiguration integrationConfiguration,
                        ProcessingService processingService) {
    this.integrationConfiguration = integrationConfiguration;
    this.processingService = processingService;
  }

  /**
   * This scheduler will run tasks to send CSV files to the SFTP server.
   */
  @Scheduled(cron = "${onenetwork.integration.cron}", zone = "${time.zoneId}")
  public void runTask() {
    if (this.integrationConfiguration.getEnable()) {
      logger.info("SCHEDULER - Running task using cron job.");
      processingService.processQueueData();
    }
  }

}