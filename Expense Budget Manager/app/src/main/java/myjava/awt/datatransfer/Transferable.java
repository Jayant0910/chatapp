

package myjava.awt.datatransfer;

import java.io.IOException;

public interface Transferable {
    Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException;

    boolean isDataFlavorSupported(DataFlavor flavor);

    DataFlavor[] getTransferDataFlavors();
}
