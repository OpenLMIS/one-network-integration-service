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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openlmis.onenetwork.integration.dto.Facility;
import org.openlmis.onenetwork.integration.dto.FacilityWrapper;
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
public class FacilityDataService {

  @Value("${service.url}")
  private String referencedataUrl;

  private static final String SERVICE_URL = "/api/facilities/";

  private final AuthService authService;
  private final RestTemplate restTemplate;

  @Autowired
  public FacilityDataService(AuthService authService,
                              RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.authService = authService;
  }

  /**
   * Fetch all facilities from referencedata service.
   *
   * @return List of {@link Facility} or empty list if
   *     referencedata service returned empty content.
   */
  public List<Facility> getAllFacilities() {
    HttpHeaders headers = new HttpHeaders();
    String url = referencedataUrl + SERVICE_URL;
    headers.setBearerAuth(authService.obtainAccessToken());

    Optional<FacilityWrapper> facilityWrapperOptional = Optional.ofNullable(restTemplate.exchange(
            RequestHelper.createUri(url),
            HttpMethod.GET,
            new HttpEntity<>(headers),
            FacilityWrapper.class)
            .getBody());

    if (facilityWrapperOptional.isPresent()) {
      return facilityWrapperOptional.get().getContent();
    }
    return Collections.emptyList();
  }
}
