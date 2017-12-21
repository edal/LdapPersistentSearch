import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.controls.PersistentSearchChangeType;
import com.unboundid.ldap.sdk.controls.PersistentSearchRequestControl;

import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws LDAPException{
        System.out.println("Init");
        App app = new App();
        app.performPersistentSearch("dc=example,dc=org");

    }


    public void performPersistentSearch(String dc) throws LDAPException {
        // Handles unsolicited notifications from the directory server.
        final UnsolicitedNotificationHandler unsolicitedNotificationHandler = new DefaultUnsolicitedNotificationHandler();

        final LDAPConnection connection = getConnection();
        final LDAPConnectionOptions ldapConnectionOptions = new LDAPConnectionOptions();
        ldapConnectionOptions.setUnsolicitedNotificationHandler(unsolicitedNotificationHandler);
        connection.setConnectionOptions(ldapConnectionOptions);

        SearchRequest persistentSearchRequest =
                                                new SearchRequest(asyncSearchListener, dc, SearchScope.SUB,
                                                                  Filter.createPresenceFilter("objectClass"));
        persistentSearchRequest.addControl(new PersistentSearchRequestControl(PersistentSearchChangeType.allChangeTypes(), // Notify change types.
                                                                              true, // Only return new changes, don't match existing entries.
                                                                              true)); // Include change notification controls in search entries.

        // Launch the persistent search as an asynchronous operation.
        AsyncRequestID persistentSearchRequestID = connection.asyncSearch(persistentSearchRequest);

        // Modify an entry that matches the persistent search criteria.  This
        // should cause the persistent search listener to be notified.
        //LDAPResult modifyResult =
        //                          connection.modify("uid=test.user,ou=People,dc=example,dc=org", new Modification(ModificationType.REPLACE,
        //                                                                                                          "description", "test"));

        // Verify that the persistent search listener was notified....


        
        Scanner s = new Scanner(System.in);
        System.out.println("Enter to abort...");
        s.next();
        System.out.println("Aborting...");
        s.close();
        // Since persistent search operations don't end on their own, we need to
        // abandon the search when we don't need it anymore.
        connection.abandon(persistentSearchRequestID);
    }

    protected LDAPConnection getConnection() throws LDAPException {
        String address = "ldap://localhost";
        int port = 389;
        String bindDN = "dc=example,dc=org";
        String password = "admin";
        return new LDAPConnection(address, port, bindDN, password);
    }

    @SuppressWarnings("serial")
    private final AsyncSearchResultListener asyncSearchListener = new AsyncSearchResultListener() {

            @Override
            public void searchEntryReturned(final SearchResultEntry searchResultEntry) {
                final StringBuilder builder =
                                              new StringBuilder(
                                                                ">>>>\nsearch entry returned\n");
                builder.append(String.format("%-12s %s\n", "DN:",
                                             searchResultEntry.getDN()));
                builder.append(String.format("%-12s %s\n", "searchResult:",
                                             searchResultEntry.toLDIFString()));
                System.out.println(builder.toString());
            }


            @Override
            public void searchReferenceReturned(final SearchResultReference searchReferenceReturned) {
                final String msg = "searchReferenceReturned not yet supported.";
                throw new UnsupportedOperationException(msg);
            }


            @Override
            public void searchResultReceived(final AsyncRequestID requestId,
                                             final SearchResult searchResult) {
                final StringBuilder builder =
                                              new StringBuilder(
                                                                "search result received\n");
                builder.append(String.format("%-12s %s\n", "requestId:", requestId));
                builder.append(String.format("%-12s %s\n", "searchResult:",
                                             searchResult));
                System.out.println(builder.toString());
            }

        };
}
