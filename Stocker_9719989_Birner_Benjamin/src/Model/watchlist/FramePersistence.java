package Model.watchlist;


/**
 * This class contains all the data for an individual frame that is required to restore
 * the frame according to its last state when the program starts.
 * Instances of this class are also required  in the program´s closing process to store the
 * frame data for the {@link # StockerMainFrame} and the {@link # Watchlist}.
 * 
 * @author Benjamin Birner
 *
 */
public class FramePersistence {
	
	private final int height;
	private final int width;
	private final int locX;
	private final int locY;
	private final int zOrder;
	
	public FramePersistence(int height, int width, int locX, int locY, int zOrder) {
		this.height = height;
		this.width = width;
		this.locX = locX;
		this.locY = locY;
		this.zOrder = zOrder;
	}
	
	
	
	/**
	 * gets the <code>height</code> of the frame.
	 * 
	 * @return the <code>height</code> of the frame.
	 */
	public int getHeight() {
		return height;
	}
	
	
	
	
	/**
	 * gets the <code>width</code> of the frame.
	 * 
	 * @return the <code>width</code> of the frame.
	 */
	public int getWidth() {
		return width;
	}
	
	
	
	
	/**
	 * gets the X-Location of the frame.
	 * 
	 * @return the X-Location of the frame.
	 */
	public int getLocX() {
		return locX;
	}
	
	
	
	

	/**
	 * gets the Y-Location of the frame.
	 * 
	 * @return the Y-Location of the frame.
	 */
	public int getLocY() {
		return locY;
	}
	
	
	
	
	/**
	 * gets the <code>zOrder</code> of the frame.
	 * 
	 * @return the <code>zOrder</code> of the frame.
	 */
	public int getZOrder() {
		return zOrder;
	}

}
