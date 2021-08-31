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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.dto.referencedata.OrderableWrapper;
import org.openlmis.onenetwork.integration.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class OrderableDataServiceTest {

  @Mock
  private AuthService authService;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private OrderableDataService orderableDataService;

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
            eq(OrderableWrapper.class)))
            .thenReturn(ResponseEntity.noContent().build());

    List<Orderable> list = orderableDataService.getAllOrderables();

    assertThat(list).isEmpty();

  }

  @Test
  public void shouldReturnListOfOrderableWhenResponseIsAvailable() {
    Orderable orderable = new Orderable.Builder().build();
    OrderableWrapper orderableWrapper = new OrderableWrapper();
    orderableWrapper.setContent(Collections.singletonList(orderable));
    when(restTemplate.exchange(
            any(URI.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(OrderableWrapper.class)))
        .thenReturn(ResponseEntity.of(Optional.of(orderableWrapper)));

    List<Orderable> list = orderableDataService.getAllOrderables();

    assertThat(list).isNotEmpty();
    assertThat(list.contains(orderable)).isTrue();
  }
}
