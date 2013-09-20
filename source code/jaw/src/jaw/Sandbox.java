/* Custom implementation of Java SecurityManager.
 * 
 * This class is called every time JAW or an app request some permission (for
 * example reading/writing files or opening a new HTTTP connection).
 * This class will make sure that only JAW has the rights to perform privileged
 * actions while apps run on a sandbox.
 */

package jaw;

import java.security.Permission;

public class Sandbox extends SecurityManager {
	
	public Sandbox() {
		super();
	}
	
	@Override
	public void checkPermission(Permission p) {

		if (Commons.allowedThreads.contains(Thread.currentThread().getId()))
			return;
		
		StackTraceElement [] e = Thread.currentThread().getStackTrace();
			
		for (StackTraceElement i : e) {
			if (i.toString().indexOf("jaw.Listener.") == 0)
				return;
			
			if (i.getClassName().indexOf("app.") == 0)
				throw new SecurityException("get the heck outta here :)");
		}
	}
	
}
