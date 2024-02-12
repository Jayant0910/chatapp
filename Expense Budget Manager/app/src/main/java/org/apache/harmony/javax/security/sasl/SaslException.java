

package org.apache.harmony.javax.security.sasl;

import java.io.IOException;

public class SaslException extends IOException {

    private static final long serialVersionUID = 4579784287983423626L;


    private Throwable _exception;

    public SaslException() {
        super();
    }

    public SaslException(String detail) {
        super(detail);
    }

    public SaslException(String detail, Throwable ex) {
        super(detail);
        if (ex != null) {
            super.initCause(ex);
            _exception = ex;
        }
    }

    @Override
    public Throwable getCause() {
        return _exception;
    }

    @Override
    public Throwable initCause(Throwable cause) {
        super.initCause(cause);
        _exception = cause;
        return this;
    }

    @Override
    public String toString() {
        if (_exception == null) {
            return super.toString();
        }
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", caused by: ");
        sb.append(_exception.toString());
        return sb.toString();
    }
}
