package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.File;

/*
 import java.util.ArrayList;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

 import ru.pm_partner.primaview.data.primavera.PrimaveraService;

 import com.primavera.integration.client.bo.BOHelperImpl;
 import com.primavera.integration.client.bo.helper.OBSHelper;
 import com.primavera.integration.client.bo.object.OBS;
 */
//import ru.pm_partner.primaview.data.primavera.PrimaveraService;
//import ru.pm_partner.primaview.data.primavera.PrimaveraServiceConfig;

public class test
{

	public static void main (String [] args)
	{
		// String word = "ASK Prima (not for the monitor)";
		System.out.println ("Hello");
		File testFile = new File ("/test/file/filename.ext");
		System.out.println ("filename: " + testFile.getName ());
		System.out.println ("file's parent: " + testFile.getParent ());

		/*
		 * Pattern p = Pattern.compile (".*not for the monitor.*", Pattern.UNICODE_CASE);
		 * Matcher m = p.matcher (word);
		 * -*System.out.println (m.matches () ? "matches" : "does not match");
		 * /*
		 * String propertiesFile = test.class.getName ();
		 * String app_server_path = System.getProperty ("catalina.home");
		 * String path = app_server_path + "/conf";
		 * System.out.println (path);
		 */
		// PrimaveraService ps = new PrimaveraService ();
		// DataProject [] projects = ps.GetProjectsByPortfolio (PrimaveraServiceConfig.projectsListPortfolio, PrimaveraServiceConfig.projectsListWhere,
		// PrimaveraServiceConfig.projectsListOrder);
		/*
		 * for (int i = 0; i < projects.length; i++)
		 * {
		 * System.out.println (projects [i].getId () + " " + projects [i].getName ());
		 * }
		 */

		/*
		 * PrimaveraService ps = new PrimaveraService ();
		 * ArrayList<DataOBS> list = ps.GetOBS (null, true);
		 * for (int i = 0; i < list.size (); i++)
		 * System.out.println (list.get (i).toString ());
		 * // OBSHelper obsHelper = new OBSHelper ();
		 */
	}
}
