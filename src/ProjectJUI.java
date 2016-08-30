import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Stack;


public class ProjectJUI {

	private JFrame frame;
	MyImage image;
	Stack<StateData> previous, next;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectJUI window = new ProjectJUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProjectJUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("image processor");
		frame.setBounds(100, 100, 523, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		previous = new Stack<StateData>();
		next = new Stack<StateData>();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter jpg = new FileNameExtensionFilter("jpg", "jpg");
		FileNameExtensionFilter png = new FileNameExtensionFilter("png", "png");
//		FileNameExtensionFilter jpeg = new FileNameExtensionFilter("jpeg", "jpeg");
		FileNameExtensionFilter allImages = new FileNameExtensionFilter("All", "jpg","png","png","bmp");

		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(jpg);
		fc.setFileFilter(png);
//		fc.setFileFilter(jpeg);
		fc.setFileFilter(allImages);
		
		final JFileChooser fcSave = new JFileChooser();
		fcSave.setAcceptAllFileFilterUsed(false);
		fcSave.setFileFilter(jpg);
		fcSave.setFileFilter(png);
//		fcSave.setFileFilter(jpeg);
		
		
		
		
		
		try {
			image = new MyImage(ImageIO.read(new File("hci.jpg")), frame);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		int diff = 2;
		JButton btnLoad = new JButton();
		setupBotton(btnLoad, "load.png","load");
		menuBar.add(btnLoad);
		menuBar.add(Box.createHorizontalStrut(diff));

		
		JButton btnSave = new JButton();
		setupBotton(btnSave, "save.png", "save");
		menuBar.add(btnSave);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnReset = new JButton();
		setupBotton(btnReset, "reset.png","reset");
		menuBar.add(btnReset);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnUndo = new JButton();
		setupBotton(btnUndo, "undo.png","undo");
		menuBar.add(btnUndo);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnRedo = new JButton();
		setupBotton(btnRedo, "redo.png", "redo");
		menuBar.add(btnRedo);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnCrop = new JButton();
		setupBotton(btnCrop, "crop.png", "crop");
		menuBar.add(btnCrop);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnRotate = new JButton();
		setupBotton(btnRotate, "rotate.png", "rotate");
		menuBar.add(btnRotate);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		
		final JTextField txtAngle = new JTextField("0");
		txtAngle.setMaximumSize(new Dimension(50, 70));
		txtAngle.setEditable(true);
		txtAngle.setToolTipText("angle");
		menuBar.add(txtAngle);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		JButton btnHelp = new JButton();
		setupBotton(btnHelp, "help.png", "help");
		menuBar.add(btnHelp);
		menuBar.add(Box.createHorizontalStrut(diff));
		
		
		Scroller scrollPane = new Scroller(image);
		scrollPane.setBounds(0, 0, 1000, 1000);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				  int ans = fc.showOpenDialog(frame);
				  if (ans == JFileChooser.APPROVE_OPTION) {
					  File file = fc.getSelectedFile();
					  try {
						image.initialize(ImageIO.read(file));
						frame.repaint();
						System.out.println("done");
					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
						System.out.println("failed to find the image");
					}
				  }
				
			}
		});
		
		
		btnCrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				previous.add(image.currentState.getData());
				next.clear();
				image.crop();
				image.requestFocus();
				frame.repaint();
			}
		});
		
		btnUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(previous.size() == 0) {
					return;
				}
				next.add(image.currentState.getData());
				image.currentState.setData(previous.pop());
				image.requestFocus();
				frame.repaint();
			}
		});
		
		btnRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(next.size() == 0) {
					return;
				}
				previous.add(image.currentState.getData());
				image.currentState.setData(next.pop());
				image.requestFocus();
				frame.repaint();
				
			}
		});
		
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previous.add(image.currentState.getData());
				next.clear();
				image.reset();
				image.requestFocus();
				frame.repaint();
			}
		});
		
		btnRotate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int angle = 0;
				try {
					angle = Integer.parseInt(txtAngle.getText());
				}catch(Exception e) {
					String t = String.format("%d", (int) image.currentState.angle);
					return;
				}
				
				double ang = angle * Math.PI / 180;
				ang = ((ang % (2 *Math.PI))+(2 * Math.PI))% (2 * Math.PI);
				txtAngle.setText(""+(int)(ang * 180 / Math.PI));
				image.currentState.rotate(ang);
				image.requestFocus();
				frame.repaint();
			}
		});
		
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fcSave.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					  File saveFile = fcSave.getSelectedFile();
					  saveFile = new File(saveFile.getPath()+"."+fcSave.getFileFilter().getDescription());
					  
					  try {
						ImageIO.write(image.getSaveImage(), fcSave.getFileFilter().getDescription(), saveFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String help = " zoom : mouse scroll\n rotate : put angle then click rotate or click 'a' & 'd\n" +
						" crop : move the four black squares at the corners then click crop\n" +
						" undo ,redo and reset : works for crop only";
				JOptionPane.showMessageDialog(frame, help, "A plain message", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		image.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				image.currentState.zoom((int) ((-e.getUnitsToScroll()) % (2 * Math.PI)));
				frame.repaint();
			}
		});
		
		image.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A) {
					double angle = image.currentState.angle - Math.PI / 180%(2 * Math.PI);
					angle = ((angle % (2 *Math.PI))+(2 * Math.PI))% (2 * Math.PI);
					String t = String.format("%d",  (int)(angle * 180 / Math.PI));
					txtAngle.setText(t);
					image.currentState.rotate(angle);
					frame.repaint();
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					
					double angle = (image.currentState.angle + Math.PI / 180) %(2 * Math.PI);
					angle = ((angle % (2 *Math.PI))+(2 * Math.PI))% (2 * Math.PI);
					String t = String.format("%d", (int)(angle * 180 / Math.PI));
					txtAngle.setText(t);
					image.currentState.rotate(angle);
					frame.repaint();
				}
			}
		});
		
		
	}
	
	
	
	
	public static class Scroller extends JScrollPane {
		
		public BufferedImage backGround;
		
		
		public Scroller(MyImage image) {
			
			
			super(image);
			
			try {
				backGround = ImageIO.read(new File("backGround.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}




		@Override
		protected void paintComponent(Graphics gg) {
			// TODO Auto-generated method stub
			super.paintComponent(gg);
			
			
			Graphics2D g = (Graphics2D) gg;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
				
			
			Rectangle r = new Rectangle(0, 0, 100, 80);
		    g.setPaint(new TexturePaint(backGround, r));
		    Rectangle rect = new Rectangle(0,0,getWidth(),getHeight());
		    g.fill(rect);
		}
		
		
		
	}
	
	
	public static void setupBotton(JButton b, String file, String tip) {
		
		try {
		    Image img = ImageIO.read(new File(file));
		    img =  img.getScaledInstance(40, 40, Image.SCALE_DEFAULT);
		    b.setIcon(new ImageIcon(img));
		    b.setMargin(new Insets(0, 0, 0, 0));
		    b.setToolTipText(tip);
		  } catch (IOException ex) {
		}
	}
	
	

}








