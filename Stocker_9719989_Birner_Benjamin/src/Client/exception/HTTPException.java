package Client.exception;





/**
 * An object of this Exception class is created if there is an error occurred 
 * regarding the HTTP-Request with a corresponding HTTP-error-code.
 * An instance of this class contains this code.
 * 
 * @author Benjamin Birner
 *
 */
public class HTTPException extends Exception {

	private int code;
	
	public HTTPException(int code){
		this.code = code;
	}
	
	

	/**
	 * returns the HTTP-error-code.
	 * 
	 * @return the HTTP-error-code.
	 */
	public String getMessage() {

		switch(code) {
		case 400:
			return "HTTP-Fehlercode 400: Bad Request. Die Anfrage-Nachricht ist fehlerhaft aufgebaut!";
			
		case 401:
			return "HTTP-Fehlercode 401: Unauthorized. Authentifizierung ist Fehlerhaft!";
			
        case 404:
        	return "HTTP-Fehlercode 404: Not Found. Ressource wurde nicht gefunden!";
			
		case 422:
			return "HTTP-Fehlercode 422: Unprocessable Entity.";
				
        case 500:
        	return "HTTP-Fehlercode 500: Internal Server Error.";
			
		}
		return "HTTP-Fehlercode: " + code;
	}
}
