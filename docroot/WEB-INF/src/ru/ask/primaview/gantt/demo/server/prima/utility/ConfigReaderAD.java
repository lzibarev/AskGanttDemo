package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReaderAD
{
	// поля файла _PrimaviewPropertiesFile
	public final static String	pf							= "pv_username";
	public final static String	pfPrimaveraPassword			= "pv_password";
	public final static String	pfPrimaveraDBLogin			= "db_username";
	public final static String	pfPrimaveraDBPassword		= "db_password";

	private final static String	_PrimaviewPropertiesFile	= "ldap_ad.properties";
	private final static String	_PrimaviewAltPropLocation	= System.getProperty ("user.home") + File.separator + ".primaveiew";

	// поля для хранения
	private static String		_propertiesPath				= null;

	public static Properties GetProperties () throws Exception, IOException
	{
		ConfigReaderAD.CheckPropertiesFile ();

		return ConfigReaderAD.LoadProperties ();
	}

	private static Properties LoadProperties () throws IOException
	{
		Properties defaultProps = new Properties ();
		FileInputStream in = new FileInputStream (_propertiesPath);
		defaultProps.load (in);
		in.close ();
		return defaultProps;
	}

	private static void CheckPropertiesFile () throws Exception, IOException
	{
		_propertiesPath = _GetPropertiesPath ();
		File configFile = new File (_propertiesPath);
		// проверка есть ли файл настроек и есть ли права на его чтение
		if (!configFile.exists ())
			throw new IOException ("Unable to find Primavera properties file at " + _propertiesPath);

		// проверка есть ли права на чтение файла настроек
		if (!configFile.canRead ())
			throw new IOException ("Unable to read Primavera properties file at " + _propertiesPath);
	}

	private static String _GetPropertiesPath () throws Exception
	{
		/* Apache Tomcat dependent part */
		String appServerPath = System.getProperty ("catalina.home");
		if (appServerPath != null) { return appServerPath + File.separator + "conf" + File.separator + _PrimaviewPropertiesFile; }
		/* /Apache Tomcat dependent part */

		// ищем настройки в запасном месте
		File path = new File (_PrimaviewAltPropLocation);
		if (!path.exists ())
			throw new Exception ("Primavera data services currently can work in Apache Tomcat app server. To work in another app server modify ru.pm_partner.primaview.data.primavera.utility.ConfigReader class to search it's config in different environment");

		return path + File.separator + _PrimaviewPropertiesFile;
	}
}
