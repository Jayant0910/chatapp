

package org.apache.harmony.javax.security.sasl;

import org.apache.harmony.javax.security.auth.callback.ChoiceCallback;

public class RealmChoiceCallback extends ChoiceCallback {

    private static final long serialVersionUID = -8588141348846281332L;

    public RealmChoiceCallback(String prompt, String[] choices, int defaultChoice,
            boolean multiple) {
        super(prompt, choices, defaultChoice, multiple);
    }
}
