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

package org.openlmis.onenetwork.integration.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class FacilityTest {

  private static final String MANAGING_NAME = "OpenLMIS Demo";

  private static final String JSON = "{\n"
          + "    \"id\" : \"c7298536-4cd9-46f3-bf9f-3690473a26fe\",\n"
          + "    \"name\" : \"Aafin Primary Health Care\",\n"
          + "    \"code\" : \"G108\",\n"
          + "    \"active\" : true,\n"
          + "    \"enabled\" : true\n"
          + "    }";

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void shouldMapFromJsonObjectToFacility() throws Exception {
    Facility facility = objectMapper.readValue(JSON, Facility.class);

    assertThat(facility).isNotNull();
    assertThat(facility.getCode()).isEqualTo("G108");
    assertThat(facility.getName()).isEqualTo("Aafin Primary Health Care");
    assertThat(facility.getActive()).isEqualTo(true);
  }

  @Test
  public void shouldConvertToFacilityForCsv() throws Exception {
    Facility facility = objectMapper.readValue(JSON, Facility.class);
    String timeZoneId = "mw";
    String country = "MW";

    FacilityForCsv facilityForCsv = facility.toFacilityForCsv(timeZoneId, country);
    assertThat(facilityForCsv).isNotNull();
    assertThat(facilityForCsv.getManagingEntName()).isEqualTo(MANAGING_NAME);
    assertThat(facilityForCsv.getManagingOrgName()).isEqualTo(MANAGING_NAME);
    assertThat(facilityForCsv.getSiteName()).isEqualTo("Aafin Primary Health Care-G108");
    assertThat(facilityForCsv.getActive()).isEqualTo("1");
  }
}
