

package org.apache.harmony.javax.security.sasl;

import java.io.Serializable;

import org.apache.harmony.javax.security.auth.callback.Callback;

public class AuthorizeCallback implements Callback, Serializable {

    private static final long serialVersionUID = -2353344186490470805L;

    
    private final String authenticationID;

    
    private final String authorizationID;

    
    private String authorizedID;

    
    private boolean authorized;

    public AuthorizeCallback(String authnID, String authzID) {
        super();
        authenticationID = authnID;
        authorizationID = authzID;
        authorizedID = authzID;
    }

    public String getAuthenticationID() {
        return authenticationID;
    }

    public String getAuthorizationID() {
        return authorizationID;
    }

    public String getAuthorizedID() {
        return (authorized ? authorizedID : null);
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean ok) {
        authorized = ok;
    }

    public void setAuthorizedID(String id) {
        if (id != null) {
            authorizedID = id;
        }
    }
}
