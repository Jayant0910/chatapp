

package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;

import org.apache.harmony.auth.internal.nls.Messages;

public class ChoiceCallback implements Callback, Serializable {

    private static final long serialVersionUID = -3975664071579892167L;

    private int defaultChoice;

    private String prompt;

    private boolean multipleSelectionsAllowed;

    private String[] choices;

    private int[] selections;

    private void setChoices(String[] choices) {
        if (choices == null || choices.length == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.1C"));
        }
        for (int i = 0; i < choices.length; i++) {
            if (choices[i] == null || choices[i].length() == 0) {
                throw new IllegalArgumentException(Messages.getString("auth.1C"));
            }
        }

        this.choices = choices;

    }

    private void setPrompt(String prompt) {
        if (prompt == null || prompt.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.14"));
        }
        this.prompt = prompt;
    }

    private void setDefaultChoice(int defaultChoice) {
        if (0 > defaultChoice || defaultChoice >= choices.length) {
            throw new IllegalArgumentException(Messages.getString("auth.1D"));
        }
        this.defaultChoice = defaultChoice;
    }

    public ChoiceCallback(String prompt, String[] choices, int defaultChoice,
            boolean multipleSelectionsAllowed) {
        super();
        setPrompt(prompt);
        setChoices(choices);
        setDefaultChoice(defaultChoice);
        this.multipleSelectionsAllowed = multipleSelectionsAllowed;
    }

    public boolean allowMultipleSelections() {
        return multipleSelectionsAllowed;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getDefaultChoice() {
        return defaultChoice;
    }

    public String getPrompt() {
        return prompt;
    }

    public int[] getSelectedIndexes() {
        return selections;
    }

    public void setSelectedIndex(int selection) {
        this.selections = new int[1];
        this.selections[0] = selection;
    }

    public void setSelectedIndexes(int[] selections) {
        if (!multipleSelectionsAllowed) {
            throw new UnsupportedOperationException();
        }
        this.selections = selections;



    }
}
