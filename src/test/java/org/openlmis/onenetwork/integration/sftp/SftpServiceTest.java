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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openlmis.onenetwork.integration.domain.OrderableForCsv;
import org.openlmis.onenetwork.integration.service.csv.CsvService;

@RunWith(MockitoJUnitRunner.class)
public class SftpServiceTest {

  @Mock
  private SftpClient sftpClient;

  @Mock
  private CsvService csvService;

  @InjectMocks
  private SftpService sftpService;


  @Test
  public void shouldSkipSendingWhenElementsListIsEmpty() throws Exception {

    List<OrderableForCsv> elements = Collections.emptyList();

    sftpService.send(elements, OrderableForCsv.class, "dummy.csv");

    verify(sftpClient, times(0)).putFileToSftp(any(), any());
    verify(csvService, times(0)).generateCsv(any(), any());

  }

}
