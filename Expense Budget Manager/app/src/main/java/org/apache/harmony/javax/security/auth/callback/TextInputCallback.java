

package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;

import org.apache.harmony.auth.internal.nls.Messages;

public class TextInputCallback implements Callback, Serializable {

    private static final long serialVersionUID = -8064222478852811804L;

    private String defaultText;

    private String prompt;

    private String inputText;

    private void setPrompt(String prompt) {
        if (prompt == null || prompt.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.14"));
        }
        this.prompt = prompt;
    }

    private void setDefaultText(String defaultText) {
        if (defaultText == null || defaultText.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.15"));
        }
        this.defaultText = defaultText;
    }

    public TextInputCallback(String prompt) {
        super();
        setPrompt(prompt);
    }

    public TextInputCallback(String prompt, String defaultText) {
        super();
        setPrompt(prompt);
        setDefaultText(defaultText);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getText() {
        return inputText;
    }

    public void setText(String text) {
        this.inputText = text;
    }
}
