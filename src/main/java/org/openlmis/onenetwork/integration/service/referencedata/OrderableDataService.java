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

import java.util.ArrayList;
import java.util.List;

import org.openlmis.onenetwork.integration.domain.Orderable;
import org.openlmis.onenetwork.integration.domain.OrderableWrapper;
import org.openlmis.onenetwork.integration.service.AuthService;
import org.openlmis.onenetwork.integration.service.request.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderableDataService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${service.url}")
  private String referencedataUrl;

  private static final String SERVICE_URL = "/api/orderables/";

  private final AuthService authService;

  @Autowired
  public OrderableDataService(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Fetch all orderables.
   */
  public List<Orderable> getAllOrderables() {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String url = referencedataUrl + SERVICE_URL;
    try {
      headers.setBearerAuth(authService.obtainAccessToken());

      OrderableWrapper responseEntity = restTemplate.exchange(
              RequestHelper.createUri(url),
              HttpMethod.GET,
              new HttpEntity<>(headers),
              OrderableWrapper.class).getBody();

      System.out.println("to i :" + responseEntity.getContent().get(0).getManagingEntName());
      System.out.println("to y :" + responseEntity.getContent().get(0).getDisplayName());
      System.out.println("to h :" + responseEntity.getContent().get(0).getProductCode());
      return responseEntity.getContent();
    } catch (HttpStatusCodeException ex) {
      logger.error(
              "Unable to fetch orderables from referencedata service "
                      + "Error code: {}, response message: {}",
              ex.getStatusCode(), ex.getMessage()
      );
    }
    return new ArrayList<>();
  }
}
