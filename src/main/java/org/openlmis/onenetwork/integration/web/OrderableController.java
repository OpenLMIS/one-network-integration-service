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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.openlmis.onenetwork.integration.domain.Orderable;
import org.openlmis.onenetwork.integration.service.OrderableCsvService;
import org.openlmis.onenetwork.integration.sftp.SftpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for getting information about orderables.
 */
@RestController
@RequestMapping("/api/integration")
public class OrderableController {

  private final OrderableCsvService csvService;
  private final SftpClient sftpClient;

  @Autowired
  public OrderableController(OrderableCsvService csvService,
                             SftpClient sftpClient) {
    this.csvService = csvService;
    this.sftpClient = sftpClient;
  }

  /**
   * Receives product related data and writes them to the buffer.
   *
   * @return {Orderable} Returns Orderable.
   */
  @PostMapping("/orderable")
  public ResponseEntity<Orderable> orderableIntegration(
          @RequestBody Orderable orderable) throws IOException {
    File csv = csvService.createCsvFile(
            new Orderable(orderable.getProductCode(), orderable.getFullProductName()),
            new File("products.csv"));
    sftpClient.putFileToSftp(Files.readAllBytes(csv.toPath()), "products.csv");
    return ResponseEntity.ok()
            .body(orderable);
  }
}