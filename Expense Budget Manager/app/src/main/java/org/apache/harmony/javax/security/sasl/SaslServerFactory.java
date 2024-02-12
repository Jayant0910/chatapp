

package org.apache.harmony.javax.security.sasl;

import java.util.Map;

import org.apache.harmony.javax.security.auth.callback.CallbackHandler;

public interface SaslServerFactory {

    SaslServer createSaslServer(String mechanisms, String protocol, String serverName,
            Map<String, ?> props, CallbackHandler cbh) throws SaslException;

    String[] getMechanismNames(Map<String, ?> props);

}
