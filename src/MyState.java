import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class MyState {

	public double angle;
	int sx1, sy1, sx2, sy2;
	Point[] points;
	int width, hight;
	int PWidth, Phigth;
	public BufferedImage image;
	SelectionRectangle rect;
	JPanel panel;
	
	public MyState(BufferedImage image, JPanel panel) {
		
		
		
		points = new Point[4];
		this.panel = panel;
		
		if(image != null) {
			this.width = image.getWidth();
			this.hight = image.getHeight();
		}
		else {
			this.width = 100;
			this.hight = 100;
		}
		
		sx1 = 0;
		sy1 = 0;
		sx2 = width;
		sy2 = hight;
		
		
		rotate(angle);
		rect = new SelectionRectangle(width, hight, this);
		
		panel.addMouseListener(rect);
		panel.addMouseMotionListener(rect);
		
	}
	
	
	
	public void rotate(double angle) {
		
		this.angle = angle;
		
		int x = width / 2;
		int y = hight / 2;
		
		points[0] = rotatePoint(x, y, angle);
		points[1]  = rotatePoint(- x, y, angle);
		points[2]  = rotatePoint(- x, - y, angle);
		points[3]  = rotatePoint(x, - y, angle);
		
		int Xmin = Math.min(Math.min(points[0].x, points[1].x), Math.min(points[2].x, points[3].x));
		int Xmax = Math.max(Math.max(points[0].x, points[1].x), Math.max(points[2].x, points[3].x));
		
		int Ymin = Math.min(Math.min(points[0].y, points[1].y), Math.min(points[2].y, points[3].y));
		int Ymax = Math.max(Math.max(points[0].y, points[1].y), Math.max(points[2].y, points[3].y));
		
		int xShift = Math.abs(Xmin);
		int yShift = Math.abs(Ymin);
		for(int i = 0; i < points.length ; i++) {
			points[i].x += xShift;
			points[i].y += yShift;
		}
		
		PWidth = Xmax - Xmin;
		Phigth = Ymax - Ymin;
		
//		System.out.println(PWidth+" "+Phigth);
//		for(int i = 0; i < 4; i++)
//			System.out.println(points[i]+" "+i+" "+angle+" "+width+" "+hight);
//		System.out.println(PWidth+" "+Phigth+" "+angle);
//		System.out.println("===================================================");
			
	}
	
	
	public void zoom(int z) {
		
		
		int hNew = hight + z;
		int wNew = (int) (hNew *(((double)width) / hight));
		
		hight = hNew;
		width = wNew;
		rotate(angle);
		
		rect.zoom(z);
		
	}
	
	
	public Point transform(int x, int y) {
		
		x -= points[2].x;
		y -= points[2].y;
		
		Point result = rotatePoint(x, y, -angle);

		
		return result;
	}
	
	
	
	
	public static Point rotatePoint(int x, int y, double angle) {
		
		double theta;
		if(x != 0)
			theta = Math.atan(Math.abs(y)/ (double)(Math.abs(x)));
		else
			theta = Math.PI / 2;
		
		if(x < 0 && y < 0) {
			theta += Math.PI;
		}
		else if(x < 0) {
			theta = Math.PI - theta;
		}
		else if(y < 0) {
			theta = 2 * Math.PI - theta;
		}
		
		
		
		theta += angle;
		double r = Math.sqrt(Math.pow(x, 2)+ Math.pow(y, 2));
		x = (int) (r * Math.cos(theta));
		y = (int) (r * Math.sin(theta));
		
		return new Point(x, y);
	}
	

	
	
	
	
	public StateData getData() {		
		StateData d = new StateData();
		d.angle = angle;
		d.sx1 = sx1;
		d.sy1 = sy1;
		d.sx2 = sx2;
		d.sy2 = sy2;
		d.width = width;
		d.hight = hight;
		return d;
	}
	
	
	public void setData(StateData data){
		angle = data.angle;
		sx1 = data.sx1;
		sy1 = data.sy1;
		sx2 = data.sx2;
		sy2 = data.sy2;
		width = data.width;
		hight = data.hight;
		
		rotate(angle);
		rect.reset();
	}
	
	
	
}











