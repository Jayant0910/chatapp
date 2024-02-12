

package org.apache.harmony.javax.security.auth;

public interface Refreshable {

    void refresh() throws RefreshFailedException;

    boolean isCurrent();

}
