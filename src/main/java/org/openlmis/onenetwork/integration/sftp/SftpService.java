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

import org.openlmis.onenetwork.integration.service.csv.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SftpService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final SftpClient sftpClient;
  private final CsvService csvService;

  @Autowired
  public SftpService(SftpClient sftpClient, CsvService csvService) {
    this.sftpClient = sftpClient;
    this.csvService = csvService;
  }

  /**
   * Sends the data of type {@code T} to SFTP server with provided file name.
   *
   * @param elements data to send.
   * @param type     of the data.
   * @param fileName name of the CSV file.
   * @param <T>      type of the data.
   */
  public <T> void send(List<T> elements, Class<T> type, String fileName) {
    try {
      if (!CollectionUtils.isEmpty(elements)) {
        File csv = csvService.writeDataToCsvFile(elements, type, new File(fileName));
        sftpClient.putFileToSftp(Files.readAllBytes(csv.toPath()), fileName);
      }
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
