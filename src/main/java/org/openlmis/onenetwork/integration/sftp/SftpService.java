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

package org.openlmis.onenetwork.integration.sftp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.openlmis.onenetwork.integration.domain.Orderable;
import org.openlmis.onenetwork.integration.service.OrderableCsvService;
import org.openlmis.onenetwork.integration.service.OrderableQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SftpService {

  private final SftpClient sftpClient;
  private final OrderableCsvService csvService;
  private OrderableQueue orderableQueue;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * SftpService constructor.
   *
   */
  @Autowired
  public SftpService(SftpClient sftpClient, OrderableCsvService csvService,
                     OrderableQueue orderableQueue) {
    this.sftpClient = sftpClient;
    this.csvService = csvService;
    this.orderableQueue = orderableQueue;
  }

  public void send() {
    sendProducts();
  }

  private void sendProducts() {
    try {
      List<Orderable> orderables = orderableQueue.getList();
      if (orderables.size() != 0 && !orderables.equals(null)) {
        File csv = csvService.createCsvFile(
            orderables,
            new File("products.csv"));
        sftpClient.putFileToSftp(Files.readAllBytes(csv.toPath()), "products.csv");
      } else {
        logger.debug("Orderables list was empty");
      }
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
