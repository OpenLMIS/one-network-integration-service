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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Getter;

@Getter
@JsonDeserialize(builder = Facility.Builder.class)
public class Facility {

  private final String code;

  private final String name;

  private final String siteName;

  private final Boolean active;

  private Facility(String code, String name, Boolean active) {
    this.code = code;
    this.name = name;
    this.active = active;
    this.siteName = name + "-" + code;
  }

  /**
   * Converts the {@link Facility} to {@link FacilityForCsv} object.
   * @return {@link FacilityForCsv}
   */
  public FacilityForCsv toFacilityForCsv(String timeZoneId, String country) {
    return new FacilityForCsv(
        this.siteName,
        this.name,
        timeZoneId,
        country,
        this.active);
  }

  @JsonPOJOBuilder
  public static class Builder {

    String code;
    String name;
    Boolean active;

    public Facility.Builder withCode(String code) {
      this.code = code;
      return this;
    }

    public Facility.Builder withName(String name) {
      this.name = name;
      return this;
    }

    public  Facility.Builder withActive(Boolean active) {
      this.active = active;
      return this;
    }

    public Facility build() {
      return new Facility(code, name, active);
    }
  }
}
