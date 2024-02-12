

package org.apache.harmony.javax.security.sasl;

public interface SaslClient {

    void dispose() throws SaslException;

    byte[] evaluateChallenge(byte[] challenge) throws SaslException;

    String getMechanismName();

    Object getNegotiatedProperty(String propName);

    boolean hasInitialResponse();

    boolean isComplete();

    byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException;

    byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException;
}
