

package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;

import org.apache.harmony.auth.internal.nls.Messages;

public class TextOutputCallback implements Callback, Serializable {

    private static final long serialVersionUID = 1689502495511663102L;

    public static final int INFORMATION = 0;

    public static final int WARNING = 1;

    public static final int ERROR = 2;

    private String message;

    private int messageType;

    public TextOutputCallback(int messageType, String message) {
        if (messageType > ERROR || messageType < INFORMATION) {
            throw new IllegalArgumentException(Messages.getString("auth.16"));
        }
        if (message == null || message.length() == 0) {
            throw new IllegalArgumentException(Messages.getString("auth.1F"));
        }
        this.messageType = messageType;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }
}
