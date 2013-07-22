package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader
{

	// поля для хранения
	// private static String _propertiesPath = null;

	public static Properties GetProperties (String filename) throws Exception,
			IOException
	{
		Properties defaultProps = new Properties ();
		String path = _GetPropertiesPath (filename);
		_CheckPropertiesFile (path);

		FileInputStream in = new FileInputStream (path);
		defaultProps.load (in);
		in.close ();
		return defaultProps;
	}

	private static void _CheckPropertiesFile (String fullpath)
			throws Exception, IOException
	{
		File configFile = new File (fullpath);
		// проверка есть ли файл настроек и есть ли права на его чтение
		if (!configFile.exists ())
			throw new IOException ("Unable to find properties file at " + fullpath);

		// проверка есть ли права на чтение файла настроек
		if (!configFile.canRead ())
			throw new IOException ("Unable to read properties file at " + fullpath);
	}

//	private static String _GetPropertiesPath (String filename) throws Exception
//	{
//		return "C:/Projects/primavera/"+ filename;
//	}
	
	private final static String	_PrimaviewAltPropLocation	= System.getProperty ("user.home") + File.separator + ".primaview";

	private static String _GetPropertiesPath (String filename) throws Exception
	{
		/* Apache Tomcat dependent part */
		String appServerPath = System.getProperty ("catalina.home");
		if (appServerPath != null)
			return appServerPath + File.separator + "conf" + File.separator + filename;

		/* /Apache Tomcat dependent part */

		// ищем настройки в запасном месте
		File path = new File (_PrimaviewAltPropLocation);
		if (!path.exists ()){
			return "C:/Projects/primavera/"+ filename;
		}
		return path + File.separator + filename;
	}

}
