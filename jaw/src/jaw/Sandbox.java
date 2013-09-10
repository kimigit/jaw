/* Custom implementation of Java SecurityManager
 * 
 */

package jaw;

import java.awt.AWTPermission;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FilePermission;
import java.security.AccessControlContext;
import java.security.AccessController;
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
			
		for (StackTraceElement i : e)
			if (i.getClassName().indexOf("app.") == 0)
				throw new SecurityException("get the heck outta here :)");
	}
	
}
