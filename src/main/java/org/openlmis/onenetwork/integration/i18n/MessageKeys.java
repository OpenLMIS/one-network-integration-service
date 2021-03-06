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

package org.openlmis.onenetwork.integration.i18n;

import java.util.Arrays;

public abstract class MessageKeys {
  private static final String DELIMITER = ".";
  private static final String SERVICE_PREFIX = "onenetwork.integration";
  private static final String ERROR_PREFIX = join(SERVICE_PREFIX, "error");
  private static final String SFTP = "sftp";

  public static final String ERROR_SFTP_CHANNEL_OPEN =
          join(ERROR_PREFIX, SFTP, "channelOpen");
  public static final String ERROR_SFTP_PUT_FILE =
          join(ERROR_PREFIX, SFTP, "putFile");
  public static final String ERROR_SFTP_CONNECT =
          join(ERROR_PREFIX, SFTP, "connect");

  private MessageKeys() {
    throw new UnsupportedOperationException();
  }

  private static String join(String... params) {
    return String.join(DELIMITER, Arrays.asList(params));
  }
}