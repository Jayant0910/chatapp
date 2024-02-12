

package org.apache.harmony.javax.security.sasl;

import java.security.Provider;
import java.security.Security;

import org.apache.harmony.auth.internal.nls.Messages;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashSet;
import java.util.Iterator;

public class Sasl {

	private static final String CLIENTFACTORYSRV = "SaslClientFactory";


	private static final String SERVERFACTORYSRV = "SaslServerFactory";

	public static final String POLICY_NOPLAINTEXT = "javax.security.sasl.policy.noplaintext";

	public static final String POLICY_NOACTIVE = "javax.security.sasl.policy.noactive";

	public static final String POLICY_NODICTIONARY = "javax.security.sasl.policy.nodictionary";

	public static final String POLICY_NOANONYMOUS = "javax.security.sasl.policy.noanonymous";

	public static final String POLICY_FORWARD_SECRECY = "javax.security.sasl.policy.forward";

	public static final String POLICY_PASS_CREDENTIALS = "javax.security.sasl.policy.credentials";

	public static final String MAX_BUFFER = "javax.security.sasl.maxbuffer";

	public static final String RAW_SEND_SIZE = "javax.security.sasl.rawsendsize";

	public static final String REUSE = "javax.security.sasl.reuse";

	public static final String QOP = "javax.security.sasl.qop";

	public static final String STRENGTH = "javax.security.sasl.strength";

	public static final String SERVER_AUTH = "javax.security.sasl.server.authentication";


	public static final String CREDENTIALS = "javax.security.sasl.credentials";


	private Sasl() {
		super();
	}


	private static Object newInstance(String factoryName, Provider prv)
			throws SaslException {
		String msg = Messages.getString("auth.31");
		Object factory;
		ClassLoader cl = prv.getClass().getClassLoader();
		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}
		try {
			factory = (Class.forName(factoryName, true, cl)).newInstance();
			return factory;
		} catch (IllegalAccessException e) {
			throw new SaslException(msg + factoryName, e);
		} catch (ClassNotFoundException e) {
			throw new SaslException(msg + factoryName, e);
		} catch (InstantiationException e) {
			throw new SaslException(msg + factoryName, e);
		}
	}


	private static Collection<?> findFactories(String service) {
		HashSet<Object> fact = new HashSet<Object>();
		Provider[] pp = Security.getProviders();
		if ((pp == null) || (pp.length == 0)) {
			return fact;
		}
		HashSet<String> props = new HashSet<String>();
		for (int i = 0; i < pp.length; i++) {
			String prName = pp[i].getName();
			Enumeration<Object> keys = pp[i].keys();
			while (keys.hasMoreElements()) {
				String s = (String) keys.nextElement();
				if (s.startsWith(service)) {
					String prop = pp[i].getProperty(s);
					try {
						if (props.add(prName.concat(prop))) {
							fact.add(newInstance(prop, pp[i]));
						}
					} catch (SaslException e) {

						e.printStackTrace();
					}
				}
			}
		}
		return fact;
	}

	@SuppressWarnings("unchecked")
	public static Enumeration<SaslClientFactory> getSaslClientFactories() {
		Collection<SaslClientFactory> res = (Collection<SaslClientFactory>) findFactories(CLIENTFACTORYSRV);
		return Collections.enumeration(res);

	}

	@SuppressWarnings("unchecked")
	public static Enumeration<SaslServerFactory> getSaslServerFactories() {
		Collection<SaslServerFactory> res = (Collection<SaslServerFactory>) findFactories(SERVERFACTORYSRV);
		return Collections.enumeration(res);
	}

	public static SaslServer createSaslServer(String mechanism,
			String protocol, String serverName, Map<String, ?> prop,
			CallbackHandler cbh) throws SaslException {
		if (mechanism == null) {
			throw new NullPointerException(Messages.getString("auth.32"));
		}
		Collection<?> res = findFactories(SERVERFACTORYSRV);
		if (res.isEmpty()) {
			return null;
		}

		Iterator<?> iter = res.iterator();
		while (iter.hasNext()) {
			SaslServerFactory fact = (SaslServerFactory) iter.next();
			String[] mech = fact.getMechanismNames(null);
			boolean is = false;
			if (mech != null) {
				for (int j = 0; j < mech.length; j++) {
					if (mech[j].equals(mechanism)) {
						is = true;
						break;
					}
				}
			}
			if (is) {
				SaslServer saslS = fact.createSaslServer(mechanism, protocol,
						serverName, prop, cbh);
				if (saslS != null) {
					return saslS;
				}
			}
		}
		return null;
	}

	public static SaslClient createSaslClient(String[] mechanisms,
			String authanticationID, String protocol, String serverName,
			Map<String, ?> prop, CallbackHandler cbh) throws SaslException {
		if (mechanisms == null) {
			throw new NullPointerException(Messages.getString("auth.33"));
		}
		Collection<?> res = findFactories(CLIENTFACTORYSRV);
		if (res.isEmpty()) {
			return null;
		}

		Iterator<?> iter = res.iterator();
		while (iter.hasNext()) {
			SaslClientFactory fact = (SaslClientFactory) iter.next();
			String[] mech = fact.getMechanismNames(null);
			boolean is = false;
			if (mech != null) {
				for (int j = 0; j < mech.length; j++) {
					for (int n = 0; n < mechanisms.length; n++) {
						if (mech[j].equals(mechanisms[n])) {
							is = true;
							break;
						}
					}
				}
			}
			if (is) {
				SaslClient saslC = fact.createSaslClient(mechanisms,
						authanticationID, protocol, serverName, prop, cbh);
				if (saslC != null) {
					return saslC;
				}
			}
		}
		return null;
	}
}
