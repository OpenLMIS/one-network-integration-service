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

package org.openlmis.onenetwork.integration.service.referencedata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardSummaries;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardSummariesWrapper;
import org.openlmis.onenetwork.integration.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class StockCardSummariesServiceTest {

  @Mock
  private AuthService authService;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private StockCardSummariesService stockCardSummariesService;

  @Before
  public void setUp() {
    when(authService.obtainAccessToken()).thenReturn("testValue");
  }

  @Test
  public void shouldReturnEmptyListWhenNoResponseIsAvailable() {

    when(restTemplate.exchange(
        any(URI.class),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(StockCardSummariesWrapper.class)))
        .thenReturn(ResponseEntity.noContent().build());

    List<StockCardSummaries> list = stockCardSummariesService
        .getStockCardSummaries(UUID.randomUUID(), UUID.randomUUID());

    assertThat(list).isEmpty();
  }

  @Test
  public void shouldReturnListOfStockCardSummariesWhenResponseIsAvailable() {
    UUID facilityId = UUID.fromString("e129703c-d4f5-4e62-9357-1e30a48ea4e5");
    UUID programId = UUID.fromString("172dc1a3-0822-464f-8e0e-461d8332c5dd");
    StockCardSummaries stockCardSummaries = new StockCardSummaries.Builder().build();
    StockCardSummariesWrapper stockCardSummariesWrapper = new StockCardSummariesWrapper();
    stockCardSummariesWrapper.setContent(Collections.singletonList(stockCardSummaries));
    when(restTemplate.exchange(
        any(URI.class),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(StockCardSummariesWrapper.class)))
        .thenReturn(ResponseEntity.of(Optional.of(stockCardSummariesWrapper)));

    List<StockCardSummaries> list = stockCardSummariesService
        .getStockCardSummaries(facilityId, programId);

    assertThat(list).isNotEmpty();
    assertThat(list.contains(stockCardSummaries)).isTrue();
  }
}
