/*
 * security-test - Copyright (C) 2006 David Roden
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package main;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;

/**
 * Main class for the security test demonstration. This class sets a
 * {@link PluginPolicy custom policy} and a {@link SecurityManager} and then
 * loads a {@link Plugin} from two different JAR files. By restricting the
 * rights of the second plugin in
 * {@link PluginPolicy#getPermissions(java.security.CodeSource)} the second
 * plugin is not actually allowed to access the â€œuser.homeâ€� system property and
 * will thus throw a {@link SecurityException} while the first plugin can
 * execute that code without any problems.
 * 
 * @author David Roden &lt;droden@gmail.com&gt;
 * @version $Id$
 */
public class Main {

	/**
	 * Program main entry point, called by the VM.
	 * 
	 * @param arguments
	 *            The command-line arguments (ignored)
	 * @throws Throwable
	 *             if any error occurs
	 */
	public static void main(String[] arguments) throws Throwable {
		new Main();
	}

	/**
	 * Main program. Sets the Policy, installs a SecurityManager and then loads
	 * the two plugins and executes them.
	 * 
	 * @throws Throwable
	 *             if any error occurs
	 */
	public Main() throws Throwable {
		Policy.setPolicy(new PluginPolicy());
		System.setSecurityManager(new SecurityManager());

		File authorizedJarFile = new File("authorized.jar");
		ClassLoader authorizedLoader = URLClassLoader.newInstance(new URL[] { authorizedJarFile.toURL() });
		
		System.out.println(authorizedLoader.getClass().toString());
		Plugin authorizedPlugin = (Plugin) authorizedLoader.loadClass("plugins.authorized.Authorized").newInstance();
		authorizedPlugin.run();

		File unauthorizedJarFile = new File("rogue.jar");
		ClassLoader unauthorizedLoader = URLClassLoader.newInstance(new URL[] { unauthorizedJarFile.toURL() });
		Plugin unauthorizedPlugin = (Plugin) unauthorizedLoader.loadClass("plugins.rogue.Rogue").newInstance();
//		unauthorizedPlugin.run();
	}

}
