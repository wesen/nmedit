package nomad.plugin.cache;

/**
 * @author Christian Schneider
 */
public class UICacheException extends Exception {

	public UICacheException() {
		super();
	}

	public UICacheException(String message) {
		super(message);
	}

	public UICacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public UICacheException(Throwable cause) {
		super(cause);
	}

}
