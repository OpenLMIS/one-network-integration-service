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
import java.util.stream.Collectors;

import org.openlmis.onenetwork.integration.domain.Orderable;
import org.openlmis.onenetwork.integration.domain.OrderableForCsv;
import org.openlmis.onenetwork.integration.service.CsvService;
import org.openlmis.onenetwork.integration.service.OrderableQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SftpService {

  private final SftpClient sftpClient;
  private final CsvService csvService;
  private OrderableQueue orderableQueue;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * SftpService constructor.
   *
   */
  @Autowired
  public SftpService(SftpClient sftpClient, CsvService csvService,
                     OrderableQueue orderableQueue) {
    this.sftpClient = sftpClient;
    this.csvService = csvService;
    this.orderableQueue = orderableQueue;
  }

  /**
   * SftpService constructor.
   *
   */
  public void send() {
    List<OrderableForCsv> list = orderableQueue.getList().stream()
            .map(Orderable::toCsvObject)
            .collect(Collectors.toList());
    send(list, OrderableForCsv.class, this.csvService.getCsvName());
  }

  /**
   * SftpService constructor.
   *
   */
  public <T> void send(List<T> elements, Class<T> type, String fileName) {
    try {
      if (!CollectionUtils.isEmpty(elements)) {
        File csv = csvService.createCsvFile(
            elements, type, new File(fileName));
        sftpClient.putFileToSftp(Files.readAllBytes(csv.toPath()), fileName);
      }
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
