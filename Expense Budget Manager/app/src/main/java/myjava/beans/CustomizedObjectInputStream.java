

package myjava.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;

import org.apache.harmony.beans.internal.nls.Messages;


class CustomizedObjectInputStream extends ObjectInputStream {

    private ClassLoader cls;

    public CustomizedObjectInputStream(InputStream in, ClassLoader cls)
            throws IOException {
        super(in);
        this.cls = cls;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {
        String className = desc.getName();

        if (className.startsWith("[")) {
            int idx = className.lastIndexOf("[");
            String prefix = className.substring(0, idx + 1);
            int[] dimensions = new int[prefix.length()];
            for (int i = 0; i < dimensions.length; ++i) {
                dimensions[i] = 0;
            }

            String postfix = className.substring(idx + 1);
            Class<?> componentType = null;
            if (postfix.equals("Z")) {
                componentType = boolean.class;
            } else if (postfix.equals("B")) {
                componentType = byte.class;
            } else if (postfix.equals("C")) {
                componentType = char.class;
            } else if (postfix.equals("D")) {
                componentType = double.class;
            } else if (postfix.equals("F")) {
                componentType = float.class;
            } else if (postfix.equals("I")) {
                componentType = int.class;
            } else if (postfix.equals("L")) {
                componentType = long.class;
            } else if (postfix.equals("S")) {
                componentType = short.class;
            } else if (postfix.equals("V")) {

            } else if (postfix.startsWith("L")) {
                componentType = cls.loadClass(postfix.substring(1, postfix
                        .length() - 1));
            } else {
                throw new IllegalArgumentException(Messages.getString(
                        "beans.1E", className));
            }
            return Array.newInstance(componentType, dimensions).getClass();
        }
        return Class.forName(className, true, cls);
    }
}
