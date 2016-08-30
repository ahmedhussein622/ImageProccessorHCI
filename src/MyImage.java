import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MyImage extends JPanel {

	public BufferedImage image, backGround;
	public MyState currentState;
	JScrollPane scrollPane;
	JFrame parentFrame;

	public MyImage(BufferedImage image, final JFrame parentFrame) {
		this.image = image;
		currentState = new MyState(image, this);
		this.parentFrame = parentFrame;

		setFocusable(true);

		currentState.rect.visible = true;

		try {
			backGround = ImageIO.read(new File("backGround.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				requestFocus();

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void reset() {
		initialize(image);
	}

	public void initialize(BufferedImage image) {
		this.image = image;
		currentState.image = image;

		StateData d = new StateData();
		d.width = image.getWidth();
		d.hight = image.getHeight();
		d.angle = 0;
		d.sx1 = 0;
		d.sy1 = 0;
		d.sx2 = d.width;
		d.sy2 = d.hight;

		currentState.setData(d);
		repaint();

	}

	@Override
	public void paintComponent(Graphics gg) {

		setSize(currentState.PWidth + 15, currentState.Phigth + 15);
		// requestFocus();

		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle r = new Rectangle(0, 0, 100, 80);
		g.setPaint(new TexturePaint(backGround, r));
		Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
		g.fill(rect);

		if (image == null)
			return;

		AffineTransform matrix = g.getTransform(); // Backup
		g.translate(currentState.points[2].x, currentState.points[2].y);
		g.rotate(currentState.angle);

		g.drawImage(image, 0, 0, currentState.width, currentState.hight,
				currentState.sx1, currentState.sy1, currentState.sx2,
				currentState.sy2, this);
		currentState.rect.paintComponent(g);

		g.setTransform(matrix); // Restore

	}

	@Override
	@Transient
	public Dimension getPreferredSize() {

		return new Dimension(this.getWidth(), this.getHeight());
	}

	public void crop() {

		int oldW = currentState.sx2 - currentState.sx1;
		int oldH = currentState.sy2 - currentState.sy1;

		double wr = (double) oldW / currentState.width;
		double hr = (double) oldH / currentState.hight;

		int sx1New = (int) (currentState.sx1 + wr
				* currentState.rect.points[2].x);
		int sx2New = (int) (currentState.sx1 + wr
				* currentState.rect.points[0].x);

		int sy1New = (int) (currentState.sy1 + hr
				* currentState.rect.points[2].y);
		int sy2New = (int) (currentState.sy1 + hr
				* currentState.rect.points[0].y);

		currentState.sx1 = sx1New;
		currentState.sy1 = sy1New;
		currentState.sx2 = sx2New;
		currentState.sy2 = sy2New;

		int nW = currentState.sx2 - currentState.sx1;
		int nH = currentState.sy2 - currentState.sy1;

		currentState.width = nW;
		currentState.hight = nH;

		currentState.rect.reset();

		currentState.rotate(currentState.angle);
		requestFocus();
		parentFrame.repaint();

	}

	public BufferedImage getSaveImage() {

		BufferedImage ans = image.getSubimage(currentState.sx1,
				currentState.sy1, currentState.sx2 - currentState.sx1,
				currentState.sy2 - currentState.sy1);

		double angle = currentState.angle;
		AffineTransform tran = new AffineTransform();
		tran.rotate(angle, -0.3 * ans.getWidth(), -0.3 * ans.getHeight());
		AffineTransformOp operator = new AffineTransformOp(tran,
				AffineTransformOp.TYPE_BILINEAR);
		ans = operator.filter(ans, null);
		return ans;
	}

}
