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

package org.openlmis.onenetwork.integration.sftp;

import static org.openlmis.onenetwork.integration.i18n.MessageKeys.ERROR_SFTP_CHANNEL_OPEN;
import static org.openlmis.onenetwork.integration.i18n.MessageKeys.ERROR_SFTP_CONNECT;
import static org.openlmis.onenetwork.integration.i18n.MessageKeys.ERROR_SFTP_PUT_FILE;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.openlmis.onenetwork.integration.configuration.SftpConfiguration;
import org.openlmis.onenetwork.integration.exception.SftpClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SftpClient {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final SftpConfiguration sftpConfiguration;
  private Session session;
  private ChannelSftp channelSftp;

  @Autowired
  public SftpClient(SftpConfiguration sftpConfiguration) {
    this.sftpConfiguration = sftpConfiguration;
  }

  /**
   * This scheduler will run tasks to send CSV files to the SFTP server.
   */
  public void putFileToSftp(byte[] bytes, String fileName) {
    try {
      connect();
      openSftpChannel(session);
      final String filePath = sftpConfiguration.getRemoteDir() + fileName;
      putCsvFileOnSftpServer(new ByteArrayInputStream(bytes), filePath);
      disconnect();
    } catch (SftpClientException e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void connect() throws SftpClientException {
    try {
      this.session = createSession();
      logger.info("Connected to SFTP");
    } catch (JSchException | IOException e) {
      throw new SftpClientException(
              ERROR_SFTP_CONNECT, e);
    }
  }

  private void disconnect() {
    channelSftp.disconnect();
    session.disconnect();
    logger.info("Disconnected to SFTP");
  }

  private void putCsvFileOnSftpServer(
          final ByteArrayInputStream inputStream, final String location)
          throws SftpClientException {
    try {
      channelSftp.put(inputStream, location);
    } catch (SftpException e) {
      throw new SftpClientException(
              ERROR_SFTP_PUT_FILE, e);
    }
  }

  private void openSftpChannel(final Session session) throws SftpClientException {
    try {
      channelSftp = (ChannelSftp) session.openChannel(sftpConfiguration.getChannelType());
      channelSftp.connect();
    } catch (JSchException e) {
      throw new SftpClientException(
              ERROR_SFTP_CHANNEL_OPEN, e);
    }
  }

  private Session createSession() throws JSchException, IOException {
    final JSch jsch = new JSch();
    jsch.getHostKeyRepository().add(provideHostKey(), null);
    session = jsch.getSession(sftpConfiguration.getUsername(), sftpConfiguration.getHost());
    session.setPassword(sftpConfiguration.getPassword());
    session.setPort(sftpConfiguration.getPort());
    session.connect();
    return session;
  }

  private HostKey provideHostKey() throws IOException, JSchException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sftpHostKey.txt");
    byte[] bytes = new byte[inputStream.available()];
    inputStream.read(bytes);
    final byte[] key = Base64.getDecoder().decode(bytes);
    return new HostKey(sftpConfiguration.getHost(), key);
  }
}
