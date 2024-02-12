

package myjava.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.harmony.beans.internal.nls.Messages;



public class Beans {

    private static boolean designTime = false;

    private static boolean guiAvailable = true;

    
    public Beans() {

    }

    @SuppressWarnings("unchecked")
    private static Object internalInstantiate(ClassLoader cls, String beanName, Object initializer) throws IOException,
            ClassNotFoundException {

        String beanResourceName = getBeanResourceName(beanName);
        InputStream is = (cls == null) ? ClassLoader
                .getSystemResourceAsStream(beanResourceName) : cls
                .getResourceAsStream(beanResourceName);

        IOException serializationException = null;
        Object result = null;
        if (is != null) {
            try {
                ObjectInputStream ois = (cls == null) ? new ObjectInputStream(
                        is) : new CustomizedObjectInputStream(is, cls);
                result = ois.readObject();
            } catch (IOException e) {

                serializationException = e;
            }
        }


        boolean deserialized = true;
        ClassLoader classLoader = cls == null ? ClassLoader
                .getSystemClassLoader() : cls;
        if (result == null) {
            deserialized = false;
            try {
                result = Class.forName(beanName, true, classLoader)
                        .newInstance();
            } catch (Exception e) {
                if (serializationException != null) {
                    throw serializationException;
                }
                throw new ClassNotFoundException(e.getClass() + ": "
                        + e.getMessage());
            }
        }

        return result;
    }

    
    public static Object instantiate(ClassLoader loader, String name)
            throws IOException, ClassNotFoundException {
        return internalInstantiate(loader, name, null);
    }



    
    public static Object getInstanceOf(Object bean, Class<?> targetType) {
        return bean;
    }

    
    public static boolean isInstanceOf(Object bean, Class<?> targetType) {
        if (bean == null) {
            throw new NullPointerException(Messages.getString("beans.1D"));
        }

        return targetType == null ? false : targetType.isInstance(bean);
    }

    
    public static synchronized void setGuiAvailable(boolean isGuiAvailable)
            throws SecurityException {
        checkPropertiesAccess();
        guiAvailable = isGuiAvailable;
    }
    
    
    public static void setDesignTime(boolean isDesignTime)
            throws SecurityException {
        checkPropertiesAccess();
        synchronized(Beans.class){
            designTime = isDesignTime;
        }
    }

    
    public static synchronized boolean isGuiAvailable() {
        return guiAvailable;
    }

    
    public static synchronized boolean isDesignTime() {
        return designTime;
    }

    private static void checkPropertiesAccess() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();

        if (sm != null) {
            sm.checkPropertiesAccess();
        }
    }

    private static String getBeanResourceName(String beanName) {
        return beanName.replace('.', '/') + ".ser";
    }
    

	private static URL safeURL(String urlString) throws ClassNotFoundException {
		try {
			return new URL(urlString);
		} catch (MalformedURLException exception) {
			throw new ClassNotFoundException(exception.getMessage());
		}
	}

}
