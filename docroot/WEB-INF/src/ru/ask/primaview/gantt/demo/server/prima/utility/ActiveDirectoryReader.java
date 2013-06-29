package ru.ask.primaview.gantt.demo.server.prima.utility;


public class ActiveDirectoryReader
{
//	private static final int					ST_USER_INFO				= 1;
//
//	private static final String					FILTER_USER_PRINCIPAL_NAME	= "(userprincipalname={0}@*)";
//
//	private static ActiveDirectoryReader		instance					= null;
//
//	private static Hashtable<String, String>	environment					= null;
//	private static Properties					config						= null;
//	private static String						searchBase					= null;
//	private DirContext							ctx							= null;
//	private SearchControls						ctls						= null;
//	private static Logger						logger						= Logger.getRootLogger ();
//	private int									currentSearchType;
//	private SearchResult						sr;
//
//	public static final String []				userAttrIDs					= {
//			"sn", "givenName", "mail", "telephoneNumber", "mobile",
//			"homePhone", "title", "facsimileTelephoneNumber", "objectclass" };
//
//	/**
//	 * Конструктор, недоступный извне
//	 * 
//	 * @throws Exception
//	 * @throws IOException
//	 */
//	private ActiveDirectoryReader () throws IOException, Exception
//	{
//		setEnvironment ();
//		setSearch ();
//	}
//
//	public static ActiveDirectoryReader getInstance () throws IOException,
//			Exception
//	{
//		if (instance != null)
//			return instance;
//
//		instance = new ActiveDirectoryReader ();
//		return instance;
//	}
//
//	private void setEnvironment () throws IOException, Exception
//	{
//		if (environment != null)
//			return;
//
//		environment = new Hashtable<String, String> ();
//		logger.debug ("setEnvironment start");
//		config = ConfigReaderAD.GetProperties ();
//		logger.debug ("setEnvironment config read");
//		/*
//		 * File: ldap_ad.properties
//		 * ldap.url = ldap://dom.ru:389
//		 * ldap.searchbase = dc=dom,dc=ru
//		 * #ldap.user=DOM\\soft_user
//		 * #ldap.password=226688
//		 * ldap.user=DOM\\webserv
//		 * ldap.password=web123serv
//		 * ldap.factories.initctx = com.sun.jndi.ldap.LdapCtxFactory
//		 * ldap.factories.control = com.sun.jndi.ldap.ControlFactory
//		 */
//
//		searchBase = config.getProperty ("ldap.searchbase");
//		logger.debug ("setEnvironment searchBase=" + searchBase);
//
//		environment.put (LdapContext.CONTROL_FACTORIES, config.getProperty ("ldap.factories.control"));
//		environment.put (Context.INITIAL_CONTEXT_FACTORY, config.getProperty ("ldap.factories.initctx"));
//		environment.put (Context.PROVIDER_URL, config.getProperty ("ldap.url"));
//		environment.put (Context.SECURITY_AUTHENTICATION, "simple");
//
//		environment.put (Context.SECURITY_PRINCIPAL, config.getProperty ("ldap.user"));
//		environment.put (Context.SECURITY_CREDENTIALS, config.getProperty ("ldap.password"));
//		environment.put (Context.STATE_FACTORIES, "PersonStateFactory");
//		environment.put (Context.OBJECT_FACTORIES, "PersonObjectFactory");
//		environment.put ("java.naming.ldap.version", "3");
//		environment.put (Context.REFERRAL, "follow");
//		logger.debug ("setEnvironment finish");
//
//	}
//
//	private void setSearch () throws NamingException
//	{
//		ctx = new InitialDirContext (environment);
//		ctls = new SearchControls ();
//		ctls.setSearchScope (SearchControls.SUBTREE_SCOPE);
//	}
//
//	public HashMap<String, String> getUserInfo (String userName)
//			throws NamingException
//	{
//		if (ctx == null)
//			setSearch ();
//
//		if (currentSearchType != ST_USER_INFO)
//		{
//			logger.debug ("getUserInfo setReturningAttributes");
//			ctls.setReturningAttributes (ActiveDirectoryReader.userAttrIDs);
//			currentSearchType = ST_USER_INFO;
//		}
//
//		// String filter = "(userprincipalname=" + userName + "@*)";
//
//		logger.debug ("getUserInfo search execution start");
//		NamingEnumeration<SearchResult> answer = ctx.search (searchBase, String.format (FILTER_USER_PRINCIPAL_NAME, userName), ctls);
//		if (!answer.hasMore ())
//		{
//			logger.debug ("getUserInfo " + userName + " was empty");
//			return null;
//		}
//
//		HashMap<String, String> result = new HashMap<String, String> ();
//		logger.debug ("getUserInfo not empty, working");
//		while (answer.hasMore ())
//		{
//			sr = (SearchResult) answer.next ();
//			Attributes attrs = sr.getAttributes ();
//			// c.addItem (new DataKeyValuePair ("all", attrs.getAll (). ));
//			result.put ("lastname", attrs.get ("sn").get ().toString ());
//			result.put ("firstname", (attrs.get ("givenName")).get ().toString ());
//			result.put ("mail", attrs.get ("mail").get ().toString ());
//			result.put ("telephonenumber", attrs.get ("telephoneNumber").get ().toString ());
//			result.put ("mobile", attrs.get ("mobile").get ().toString ());
//			result.put ("homePhone", attrs.get ("homePhone").get ().toString ());
//			result.put ("jobtitle", attrs.get ("title").get ().toString ());
//			result.put ("fax", attrs.get ("facsimileTelephoneNumber").get ().toString ());
//			logger.debug ("getUserInfo answer iteration end");
//		}
//		return result;
//	}
//
//	public void closeSearchCtx ()
//	{
//		try
//		{
//			ctx.close ();
//		}
//		catch (NamingException e)
//		{
//			logger.debug ("closeSearchCtx fatal error: " + e.getStackTrace ());
//		}
//		ctx = null;
//	}
}
