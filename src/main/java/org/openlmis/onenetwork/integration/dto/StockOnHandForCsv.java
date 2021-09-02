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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonPropertyOrder({
    "#* Enterprise",
    "* Organization",
    "Name",
    "* Item Enterprise Name",
    "Item Partner Name",
    "* Item Name",
    "* Site Enterprise Name",
    "* Site Organization Name",
    "* Site Name",
    "Site Alias",
    "* Quantity Unit Of Measure (100-PACK, 10000GALLONTANKCAR, 1000BTU ...)",
    "* Ordering UOM (AM, AST, ASY ...)",
    "Inventory Status (AVAILABLE, BAD, DESTROYED ...)",
    "On Hand (#,###.###)",
    "Snapshot Onhand Date (yyyyMMdd'T'HH:mm:ss)",
    "In Transit (#,###.###)",
    "On Order Quantity (#,###.###)",
    "On Floor (#,###.###)",
    "On Hold (#,###.###)",
    "Other Qty (#,###.###)",
    "Average Consumption (#,###.###)",
    "Days Of Supply (#,###.###)",
    "Stock Out (0 or 1 [0=false, 1=true])",
    "Location Name",
    "Program Enterprise Name",
    "Program Name",
    "Customer Enterprise Name",
    "Customer Name",
    "Customer Organization Name",
    "Vendor Enterprise Name",
    "Vendor Name",
    "Vendor Organization Name",
    "Partner Item Enterprise Name",
    "Partner Item Name",
    "Lot Number",
    "Lot Owner",
    "Lot Item Enterprise Name",
    "LoItem Name"
})
@Getter
@RequiredArgsConstructor
public class StockOnHandForCsv {

  private static final String MANAGING_NAME = "OpenLMIS Demo";
  private static final String EMPTY = "";
  private static final String DEFAULT = "DEFAULT";
  private static final String EACH = "EACH";
  private static final String AVAILABLE = "AVAILABLE";

  @JsonProperty("#* Enterprise")
  private final String enterprise = MANAGING_NAME;

  @JsonProperty("* Organization")
  private final String organization = MANAGING_NAME;

  @JsonProperty("Name")
  private final String name = DEFAULT;

  @JsonProperty("* Item Enterprise Name")
  private final String itemEnterpriseName = MANAGING_NAME;

  @JsonProperty("Item Partner Name")
  private final String itemPartnerName = EMPTY;

  @JsonProperty("* Item Name")
  private final String itemName;

  @JsonProperty("* Site Enterprise Name")
  private final String siteEnterpriseName = MANAGING_NAME;

  @JsonProperty("* Site Organization Name")
  private final String siteOrganizationName = MANAGING_NAME;

  @JsonProperty("* Site Name")
  private final String siteName;

  @JsonProperty("Site Alias")
  private final String siteAlias = EMPTY;

  @JsonProperty("* Quantity Unit Of Measure (100-PACK, 10000GALLONTANKCAR, 1000BTU ...)")
  private final String quantityUnitOfMeasure = EACH;

  @JsonProperty("* Ordering UOM (AM, AST, ASY ...)")
  private final String orderingUom = EACH;

  @JsonProperty("Inventory Status (AVAILABLE, BAD, DESTROYED ...)")
  private final String inventoryStatus = AVAILABLE;

  @JsonProperty("On Hand (#,###.###)")
  private final String onHand;

  @JsonProperty("Snapshot Onhand Date (yyyyMMdd'T'HH:mm:ss)")
  private final String snapshotOnhandDate = EMPTY;

  @JsonProperty("In Transit (#,###.###)")
  private final String inTransit = EMPTY;

  @JsonProperty("On Order Quantity (#,###.###)")
  private final String onOrderQuantity = EMPTY;

  @JsonProperty("On Floor (#,###.###)")
  private final String onFloor = EMPTY;

  @JsonProperty("On Hold (#,###.###)")
  private final String onHold = EMPTY;

  @JsonProperty("Other Qty (#,###.###)")
  private final String otherQty = EMPTY;

  @JsonProperty("Average Consumption (#,###.###)")
  private final String averageConsumption = EMPTY;

  @JsonProperty("Days Of Supply (#,###.###)")
  private final String daysOfSupply = EMPTY;

  @JsonProperty("Stock Out (0 or 1 [0=false, 1=true])")
  private final String stockOut = EMPTY;

  @JsonProperty("Location Name")
  private final String locationName = EMPTY;

  @JsonProperty("Program Enterprise Name")
  private final String programEnterpriseName = EMPTY;

  @JsonProperty("Program Name")
  private final String programName = EMPTY;

  @JsonProperty("Customer Enterprise Name")
  private final String customerEnterpriseName = EMPTY;

  @JsonProperty("Customer Name")
  private final String customerName = EMPTY;

  @JsonProperty("Customer Organization Name")
  private final String customerOrganizationName = EMPTY;

  @JsonProperty("Vendor Enterprise Name")
  private final String vendorEnterpriseName = EMPTY;

  @JsonProperty("Vendor Name")
  private final String vendorName = EMPTY;

  @JsonProperty("Vendor Organization Name")
  private final String vendorOrganizationName = EMPTY;

  @JsonProperty("Partner Item Enterprise Name")
  private final String partnerItemEnterpriseName = EMPTY;

  @JsonProperty("Partner Item Name")
  private final String partnerItemName = EMPTY;

  @JsonProperty("Lot Number")
  private final String lotNumber = EMPTY;

  @JsonProperty("Lot Owner")
  private final String lotOwner = EMPTY;

  @JsonProperty("Lot Item Enterprise Name")
  private final String lotItemEnterpriseName = EMPTY;

  @JsonProperty("Lot Item Name")
  private final String lotItemName = EMPTY;
}
