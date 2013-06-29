package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.util.Date;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.primavera.ServerException;
import com.primavera.integration.client.ClientException;
import com.primavera.integration.client.RMIURL;
import com.primavera.integration.client.Session;
import com.primavera.integration.common.DatabaseInstance;
import com.primavera.integration.network.NetworkException;

public class PrimaveraConnector {
	public final static Logger logger = Logger
			.getLogger(PrimaveraConnector.class);

	// имя файла настроек
	private final static String _PrimaviewPropertiesFile = "primaviewData.properties";

	// поля файла _PrimaviewPropertiesFile
	public final static String pfPrimaveraLogin = "pv_username";
	public final static String pfPrimaveraPassword = "pv_password";
	public final static String pfPrimaveraDBLogin = "db_username";
	public final static String pfPrimaveraDBPassword = "db_password";

	public final static String EOL = System.getProperty("line.separator");
	private long _creationId; // id экземпляра класса
	private static String _pv_login;
	private static String _pv_password;

	// объект коннектора
	private static PrimaveraConnector PrimaveraConnectorInstance;

	private int attemptCount;
	// массив соединений с БД.
	private DatabaseInstance dbInstances[];
	// текущая сессия
	private static Session _sessionInstance = null;

	private static Properties _properties = null;

	/**
	 * метод-фабрика
	 * 
	 * @return PrimaveraConnector
	 * @throws Exception
	 */
	public static PrimaveraConnector getInstance() throws Exception {
		// logger.info ("static getInstance()");

		if (PrimaveraConnectorInstance == null) {
			PrimaveraConnectorInstance = new PrimaveraConnector();
			if (logger.isDebugEnabled())
				logger.debug("Created new PrimaveraConnectorInstance");
		}

		return PrimaveraConnectorInstance;
	}

	/**
	 * Конструктор
	 */
	protected PrimaveraConnector() {
		this._creationId = new Date().getTime();

		try {
			_properties = ConfigReader.GetProperties(_PrimaviewPropertiesFile);
			_pv_login = _properties.getProperty(pfPrimaveraLogin);
			_pv_password = _properties.getProperty(pfPrimaveraPassword);
		} catch (Exception e) {
			writeLog(
					new StringBuilder("PrimaveraConnector construct ").append(e
							.getMessage()), Level.ERROR);
			e.printStackTrace();
		}

		// writeLog ("Construct. Reading config OK");

		attemptCount = 0;
		_resolveDatabases();
		// writeLog ("DB resolved, proceed to get session");
		getSession();
	}

	/**
	 * надо не забыть разлогиниться, когда нас уничтожают
	 */
	public void finalize() throws Throwable {
		writeLog("finalize() WTF", Level.FATAL);
		_logout();

		super.finalize();
	}

	public Session getSession() {
		// writeLog (new StringBuilder ("getSession begin attempt ").append
		// (attemptCount), Level.DEBUG);
		try {
			if (_sessionInstance == null || !_sessionInstance.isValid())
				_login(_pv_login, _pv_password);
			// writeLog ("getSession got it", Level.DEBUG);

			return _sessionInstance;
		} catch (Exception e) {
			System.out.println(e);
			writeLog(new StringBuilder("getSession Exception ").append(e
					.getMessage()), Level.ERROR);
		}

		if (attemptCount <= 3) {
			_logout();
			_login(_pv_login, _pv_password);
			attemptCount++;

			return getSession();
		} else {
			attemptCount = 0;
			writeLog("getSession too much attempts", Level.FATAL);

			return null;
		}
	}

	private void _resolveDatabases() {
		try {
			// writeLog ("resolveDatabases begin", Level.DEBUG);
			// writeLog
			// ("resolveDatabases RMIURL.getRmiUrl(RMIURL.LOCAL_SERVICE)=" +
			// RMIURL.getRmiUrl (0));
			String rmiurl = RMIURL.getRmiUrl(0);
			dbInstances = Session.getDatabaseInstances(rmiurl);

			// всего лишь запись в лог несущественной информации
			// writeLog ("resolveDatabases got them", Level.DEBUG);
			// if (dbInstances != null)
			// {
			// writeLog ("resolveDatabases dbInstances.length = " +
			// dbInstances.length);
			// for (int i = 0; i < dbInstances.length; i++)
			// writeLog ("resolveDatabases " + i + " " + dbInstances
			// [i].toString ());
			// }
			// else
			// writeLog ("resolveDatabases FUCK");
		} catch (ServerException e) {
			writeLog(
					new StringBuilder("resolveDatabases ServerException ").append(e
							.getMessage()), Level.ERROR);
			e.printStackTrace();
		} catch (NetworkException e) {
			writeLog(
					new StringBuilder("resolveDatabases NetworkException ").append(e
							.getMessage()), Level.ERROR);
			e.printStackTrace();
		} catch (ClientException e) {
			writeLog(
					new StringBuilder("resolveDatabases ClientException ").append(e
							.getMessage()), Level.ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			writeLog(new StringBuilder("resolveDatabases Exception ").append(e
					.getMessage()), Level.ERROR);
			e.printStackTrace();
		}
		// writeLog ("resolveDatabases end", Level.DEBUG);
	}

	private void _login(String login, String password) {
		// writeLog ("login() begin", Level.DEBUG);
		try {
			// writeLog (new StringBuilder
			// ("login() isLocalModeAvailable = ").append
			// (Session.isLocalModeAvailable ()), Level.DEBUG);
			// writeLog (new StringBuilder
			// ("login() isRemoteModeAvailable = ").append
			// (Session.isRemoteModeAvailable ()), Level.DEBUG);

			if (_sessionInstance != null && !_sessionInstance.isValid()) {
				writeLog("login() will logout for relogin", Level.WARN);
				_sessionInstance.logout();
			}

			// writeLog ("login " + login + "password " + password);
			// writeLog (RMIURL.getRmiUrl (0));
			// writeLog ("login() getDatabaseId = " + dbInstances
			// [0].getDatabaseId ());
			// writeLog ("login() getDatabaseName = " + dbInstances
			// [0].getDatabaseName ());
			// writeLog ("login() getActualDatabaseName = " + dbInstances
			// [0].getActualDatabaseName ());
			// writeLog ("login() getDatabaseUrl = " + dbInstances
			// [0].getDatabaseUrl ());

			_sessionInstance = Session.login(RMIURL.getRmiUrl(0),
					dbInstances[0].getDatabaseId(), login, password);
			// writeLog ("login() got Session: " + _sessionInstance.toString (),
			// Level.DEBUG);
			attemptCount = 0;
		} catch (ServerException e) {
			writeLog(new StringBuilder("login() ServerException ").append(e
					.getMessage()), Level.ERROR);
		} catch (NetworkException e) {
			writeLog(new StringBuilder("login() NetworkException ").append(e
					.getMessage()), Level.ERROR);
		} catch (ClientException e) {
			writeLog(new StringBuilder("login() ClientException ").append(e
					.getMessage()), Level.ERROR);
		}
	}

	private void _logout() {
		// writeLog ("logout () called");
		try {
			if (_sessionInstance != null && _sessionInstance.isValid())
				_sessionInstance.logout();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		_sessionInstance = null;
	}

	public void writeLog(String message) {
		logger.info(new StringBuilder().append(this._creationId).append(" ")
				.append(message));
	}

	public void writeLog(String message, Level level) {
		logger.log(PrimaveraConnector.class.getName(), level,
				new StringBuilder().append(this._creationId).append(" ")
						.append(message), null);
	}

	public void writeLog(StringBuilder message) {
		message.insert(0, " ");
		message.insert(0, this._creationId);

		logger.info(message);
	}

	public void writeLog(StringBuilder message, Level level) {
		message.insert(0, " ");
		message.insert(0, this._creationId);

		logger.log(PrimaveraConnector.class.getName(), level, message, null);
	}

	public static void writeLog(String message, Exception e) {
		StringBuilder sbMessage = new StringBuilder(message).append(" ")
				.append(e.getMessage()).append(EOL);
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++)
			sbMessage.append(stackTrace[i].toString()).append(EOL);

		logger.fatal(sbMessage.toString());

	}
}
