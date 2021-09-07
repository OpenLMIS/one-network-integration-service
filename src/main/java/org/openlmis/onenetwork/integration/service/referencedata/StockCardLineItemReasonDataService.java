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

import java.util.Optional;
import java.util.UUID;

import org.openlmis.onenetwork.integration.dto.Orderable;
import org.openlmis.onenetwork.integration.dto.referencedata.StockCardLineItemReason;
import org.openlmis.onenetwork.integration.service.AuthService;
import org.openlmis.onenetwork.integration.service.request.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockCardLineItemReasonDataService {

  @Value("${service.url}")
  private String referencedataUrl;

  private static final String SERVICE_URL = "/api/stockCardLineItemReasons/";

  private final AuthService authService;
  private final RestTemplate restTemplate;

  @Autowired
  public StockCardLineItemReasonDataService(AuthService authService,
                              RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.authService = authService;
  }

  /**
   * Fetch reason by id from referencedata service.
   *
   * @return {@link Orderable} or empty orderable if
   *     referencedata service returned empty content.
   */
  public StockCardLineItemReason findWithId(UUID reasonId) {
    HttpHeaders headers = new HttpHeaders();
    String url = referencedataUrl + SERVICE_URL + "/" + reasonId;
    headers.setBearerAuth(authService.obtainAccessToken());

    Optional<StockCardLineItemReason> stockCardLineItemReasonOptional
        = Optional.ofNullable(restTemplate.exchange(
            RequestHelper.createUri(url),
            HttpMethod.GET,
            new HttpEntity<>(headers),
            StockCardLineItemReason.class)
        .getBody());

    if (stockCardLineItemReasonOptional.isPresent()) {
      return stockCardLineItemReasonOptional.get();
    }
    return new StockCardLineItemReason.Builder().build();
  }
}
