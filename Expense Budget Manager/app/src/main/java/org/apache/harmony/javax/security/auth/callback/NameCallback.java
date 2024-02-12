

package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;

import org.apache.harmony.auth.internal.nls.Messages;

public class NameCallback implements Callback, Serializable {

    private static final long serialVersionUID = 3770938795909392253L;

    private String prompt;

    private String defaultName;

    private String inputName;

    private void setPrompt(String prompt) {
        if (prompt == null || prompt.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.14"));
        }
        this.prompt = prompt;
    }

    private void setDefaultName(String defaultName) {
        if (defaultName == null || defaultName.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.1E"));
        }
        this.defaultName = defaultName;
    }

    public NameCallback(String prompt) {
        super();
        setPrompt(prompt);
    }

    public NameCallback(String prompt, String defaultName) {
        super();
        setPrompt(prompt);
        setDefaultName(defaultName);
    }

    public String getPrompt() {
        return prompt;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setName(String name) {
        this.inputName = name;
    }

    public String getName() {
        return inputName;
    }
}
