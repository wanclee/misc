package co.sf.install.exception;

/**
 * The custom runtime exception for wrapping any specific runtime exceptions (unchecked) 
 * or checked exceptions encountered during program execution.
 * @author wan
 *
 */
@SuppressWarnings("serial")
public class InstallMgrException extends RuntimeException {

	public InstallMgrException() {}
	
	public InstallMgrException(String message) {
		super(message);
	}
	
	public InstallMgrException(String message, Exception ex) {
		super(message, ex);
	}
}
