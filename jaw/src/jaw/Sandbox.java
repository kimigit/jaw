/* Custom implementation of Java SecurityManager
 * 
 */

package jaw;

import java.awt.AWTPermission;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.PropertyPermission;

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
