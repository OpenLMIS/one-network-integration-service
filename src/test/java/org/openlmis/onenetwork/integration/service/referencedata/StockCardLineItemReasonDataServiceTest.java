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
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardLineItemReason;
import org.openlmis.onenetwork.integration.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class StockCardLineItemReasonDataServiceTest {

  @Mock
  private AuthService authService;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private StockCardLineItemReasonDataService stockCardLineItemReasonDataService;

  @Before
  public void setUp() {
    when(authService.obtainAccessToken()).thenReturn("testValue");
  }

  @Test
  public void shouldReturnNewInstanceWhenResponseIsUnavailable() {
    UUID reasonId = UUID.fromString("6e63b5b2-7c93-404b-973c-8d7db077c498");

    when(restTemplate.exchange(
        any(URI.class),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(StockCardLineItemReason.class)))
        .thenReturn(ResponseEntity.noContent().build());

    StockCardLineItemReason returned = stockCardLineItemReasonDataService.findWithId(reasonId);

    assertThat(returned).isInstanceOf(StockCardLineItemReason.class);
  }

  @Test
  public void shouldReturnStockCardLineItemReasonWhenResponseIsAvailable() {
    UUID reasonId = UUID.fromString("6e63b5b2-7c93-404b-973c-8d7db077c498");
    StockCardLineItemReason stockCardLineItemReason = new StockCardLineItemReason.Builder().build();

    when(restTemplate.exchange(
        any(URI.class),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(StockCardLineItemReason.class)))
        .thenReturn(ResponseEntity.of(Optional.of(stockCardLineItemReason)));

    StockCardLineItemReason returned = stockCardLineItemReasonDataService.findWithId(reasonId);

    assertThat(returned).isEqualTo(stockCardLineItemReason);
  }
}
