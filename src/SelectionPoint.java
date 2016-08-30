import java.awt.Graphics2D;
import java.awt.Point;


public class SelectionPoint {
	
	static int margin = 5;
	static int width = 10;
	int x, y;
	
	
	public SelectionPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void paintComponent(Graphics2D g) {
		
		int xn = x - width / 2;
		int yn = y - width / 2;
		
		g.fillRect(xn, yn, width, width);
		
	}
	
	
	public boolean contains(int px, int py) {
		if(Math.abs(x - px) <= margin && Math.abs(y - py) <= margin)
			return true;
		return false;
	}
	
	

}






