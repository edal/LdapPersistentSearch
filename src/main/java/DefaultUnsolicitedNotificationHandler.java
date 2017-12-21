/*
 * Copyright 2008-2011 UnboundID Corp. All Rights Reserved.
 */
/*
 * Copyright (C) 2008-2011 UnboundID Corp. This program is free
 * software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License (GPLv2 only) or the terms of the GNU
 * Lesser General Public License (LGPLv2.1 only) as published by the
 * Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses>.
 */

import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.UnsolicitedNotificationHandler;
import com.unboundid.util.LDAPCommandLineTool;

import static com.unboundid.util.Validator.ensureNotNull;


/**
 * A notification handler wherein the
 * {@code handleUnsolicitedNotification()} method logs a message using
 * {@link LDAPCommandLineTool#out(Object...)}.
 */
public class DefaultUnsolicitedNotificationHandler
  implements UnsolicitedNotificationHandler  {



  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DefaultUnsolicitedNotificationHandler{");

    sb.append('}');
    return sb.toString();
  }



  /**
   * {@inheritDoc}
   * <p/>
   * Logs a message that the extended result was received as an
   * unsolicited notification. The message printed contains the returned
   * object from {@link ExtendedResult#getDiagnosticMessage()}.
   */
  @Override
  public void handleUnsolicitedNotification(final LDAPConnection ldapConnection,
                                            final ExtendedResult extendedResult) {
    ensureNotNull(ldapConnection,extendedResult);

      final String msg =
        String.format("the server to which the client is connected has sent an " +
          "unsolicited notification including the following diagnostic message: " +
          "%s",extendedResult.getDiagnosticMessage());
        System.out.println(msg);
  }
  

}