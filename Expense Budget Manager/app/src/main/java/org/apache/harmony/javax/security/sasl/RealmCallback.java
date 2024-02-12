

package org.apache.harmony.javax.security.sasl;

import org.apache.harmony.javax.security.auth.callback.TextInputCallback;

public class RealmCallback extends TextInputCallback {

    private static final long serialVersionUID = -4342673378785456908L;

    public RealmCallback(String prompt) {
        super(prompt);
    }

    public RealmCallback(String prompt, String defaultRealmInfo) {
        super(prompt, defaultRealmInfo);
    }
}
