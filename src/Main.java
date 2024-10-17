import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable, MouseListener, KeyListener {

	private JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	private int WIDTH = 1080, HEIGHT = 720;
	private BufferedImage canva;

	private List<Point> points;
	private Bezier line;
	private int pointCount = 0;

	public static int exibMode = 0;
	private int frameRate = 15;

	public Main() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		this.addKeyListener(this);

		canva = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		points = new LinkedList<>();

		initFrame();
	}

	private void initFrame() {
		frame = new JFrame("Bezier");
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	private synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(isRunning) {
			render();
			tick();

			try {
				Thread.sleep(1000/frameRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {return;}
	@Override
	public void mouseExited(MouseEvent e) {return;}
	@Override
	public void mousePressed(MouseEvent e) {return;}
	@Override
	public void mouseReleased(MouseEvent e) {return;}

	@Override
	public void keyReleased(KeyEvent e) {return;}
	@Override
	public void keyTyped(KeyEvent e) {return;}

	public static void main(String[] args) {
		Main a = new Main();
		a.start();
	}	

	/*###########################################################*/

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = canva.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		for(int i = 0; i < points.size(); i++) {
			points.get(i).render(g);
		}

		for(int i = 0; i < points.size()-1; i++) {
			Point cPoint = points.get(i);
			Point nPoint = points.get(i+1);
			g.drawLine(cPoint.getX(), cPoint.getY(), nPoint.getX(), nPoint.getY());
		}

		if(line != null) {
			line.render(g);
		}

		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, 20));
		if(line instanceof Bezier3) {
			g.drawString("Exibition modes 0 - 3", 5, 25);
		} else if(line instanceof Bezier4) {
			g.drawString("Exibition modes 0 - 4", 5, 25);
		}
		g.drawString("Press 'UP' and 'DOWN' to control the velocity.", 5, HEIGHT-5);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(canva, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}

	private void tick() {
		if(line != null) {
			line.tick();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		addPoint(e);
	}

	private void addPoint(MouseEvent e) {
		if(pointCount < 4) {
			points.add(new Point(e.getX(), e.getY()));
			pointCount++;
		}

		if(pointCount == 3) {
			line = new Bezier3(points.get(points.size()-3), points.get(points.size()-2), points.get(points.size()-1));
		}

		if(pointCount == 4) {
			exibMode = 0;
			line = new Bezier4(points.get(points.size()-4), points.get(points.size()-3), points.get(points.size()-2), points.get(points.size()-1));
			pointCount++;
		} else if(pointCount == 5) {
			points.remove(0);
			points.add(new Point(e.getX(), e.getY()));
			line = new Bezier4(points.get(points.size()-4), points.get(points.size()-3), points.get(points.size()-2), points.get(points.size()-1));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_0) {
			exibMode = 0;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_1) {
			exibMode = 1;
		}

		if(e.getKeyCode() == KeyEvent.VK_2) {
			exibMode = 2;
		}

		if(e.getKeyCode() == KeyEvent.VK_3) {
			exibMode = 3;
		}

		if(e.getKeyCode() == KeyEvent.VK_4) {
			exibMode = 4;
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			stop();
		}

		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(frameRate > 5) {
				frameRate-=5;
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(frameRate <= 95) {
				frameRate+=5;
			}
		}
	}

}
