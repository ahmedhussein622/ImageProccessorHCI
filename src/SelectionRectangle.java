import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class SelectionRectangle implements MouseMotionListener, MouseListener{

	
	SelectionPoint points[];
	boolean visible;
	boolean latch;
	int latchedPoint;
	MyState state;
	
	int xMax, yMax;
	
	
	public SelectionRectangle(int width, int higth, MyState state) {
		points = new SelectionPoint[4];
		
		
		
		this.state = state;
		visible = false;
		
		xMax = state.width;
		yMax = state.hight;
		
		reset();
	}
	
	
	
	public void reset() {
		
		xMax = state.width;
		yMax = state.hight;
		
		points[0] = new SelectionPoint(xMax, yMax);
		points[1] = new SelectionPoint(0, yMax);
		points[2] = new SelectionPoint(0, 0);
		points[3] = new SelectionPoint(xMax, 0);
	}
	
	
	public void paintComponent(Graphics2D g) {
		
		Color myColor = new Color(0,0,0, .7f);
		g.setColor(myColor);
		
		g.fillRect(0, 0, points[1].x, yMax);
		g.fillRect(points[3].x, 0, xMax - points[3].x, yMax);
		g.fillRect(points[2].x, 0, points[3].x - points[2].x, points[3].y);
		g.fillRect(points[1].x, points[1].y, points[0].x - points[1].x, yMax - points[0].y);
		
		
		g.setColor(Color.black);
		
		g.drawLine(points[0].x, points[0].y, points[1].x, points[1].y);
		g.drawLine(points[0].x, points[0].y, points[3].x, points[3].y);
		g.drawLine(points[2].x, points[2].y, points[1].x, points[1].y);
		g.drawLine(points[2].x, points[2].y, points[3].x, points[3].y);
		
		
		for(int i = 0; i < points.length; i++) {
			points[i].paintComponent(g);
		}
		
	}
	
	
	
	public void update(int index, int x, int y ) {
		
		if(x > xMax || x < 0 || y > yMax || y < 0)
			return;
		
		if(index == 0) {
			
			if(x < points[1].x || y < points[3].y)
				return;
			
			points[0].x = x;
			points[0].y = y;
			
			points[1].y = points[0].y;
			points[3].x = points[0].x;
		}
		else if(index == 1) {
			
			if(x > points[0].x || y < points[2].y)
				return;
			
			points[1].x = x;
			points[1].y = y;
			
			points[0].y = points[1].y;
			points[2].x = points[1].x;
		}
		else if(index == 2) {
			
			if(x > points[3].x || y > points[1].y)
				return;
			
			points[2].x = x;
			points[2].y = y;
			
			points[3].y = points[2].y;
			points[1].x = points[2].x;
		}
        else if(index == 3) {
        	
        	if(x < points[2].x || y > points[0].y)
				return;
        	
        	points[3].x = x;
			points[3].y = y;
			
			points[2].y = points[3].y;
			points[0].x = points[3].x;
			
		}
		
	}


	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
		if(visible) {
			Point p = state.transform(arg0.getX(), arg0.getY());
			
			if(!latch) {
				for(int i = 0; i < 4 ; i++) {
					if(points[i].contains(p.x, p.y)) {
						latch = true;
						latchedPoint = i;
					}
				}
				
			}
			else {
				latch = false;
			}
			
		}
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(latch) {
			Point p = state.transform(arg0.getX(), arg0.getY());
			update(latchedPoint, p.x, p.y);
			state.panel.repaint();
		}
		
	}
	
	
	public void zoom(int z) {
		
		
		xMax = state.width;
		yMax = state.hight;
		
		int hNew = points[0].y + z;
		int wNew = (int) (hNew *(((double)points[0].x) / points[0].y));
		
		points[0].x = wNew;
		points[0].y = hNew;
		
		points[1].y = points[0].y;
		points[3].x = points[0].x;
		
		
		
	}
	
	
	
	
	
	
	
	
}






