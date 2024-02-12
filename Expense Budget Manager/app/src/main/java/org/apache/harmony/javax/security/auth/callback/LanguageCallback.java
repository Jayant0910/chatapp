

package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;
import java.util.Locale;

public class LanguageCallback implements Callback, Serializable {

    private static final long serialVersionUID = 2019050433478903213L;

    private Locale locale;

    public LanguageCallback() {
        super();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
