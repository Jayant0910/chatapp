

package org.apache.harmony.javax.security.sasl;

public interface SaslServer {

    void dispose() throws SaslException;

    byte[] evaluateResponse(byte[] response) throws SaslException;

    String getAuthorizationID();

    String getMechanismName();

    Object getNegotiatedProperty(String propName);

    boolean isComplete();

    byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException;

    byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException;
}
