package gui;

import geometry.Geometry;
import geometry.ImgUtil;
import geometry.PixelGrabber;
import geometry.Point;
import geometry.Rotate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private Vector<Point> dirPoints;
	
	private Vector<Point> middlePoints;
	
	private Vector<Float> dirAngles;
	
	private Vector<Point> points;
	private Vector<Point> leftContourPoints;
	private Vector<Point> rightCountPoints;

	private BufferedImage image;

	public DrawingPanel()
	{
		image = ImgUtil.getImg("res/1.jpg");
		this.setBounds(GuiConfig.DRAWING_RECTANGLE);
		this.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void clear()
	{
		getGraphics().clearRect(0, 0, 1280, 720);
	}

	public void initPoint()
	{
		points = new Vector<Point>();
	}

	public void drawSpine(Graphics2D graphics2d)
	{
		// TODO Auto-generated method stub
		
		points = Geometry.normalize(points, 8);
		points = Geometry.removeClose(points, 8);
		
		dirPoints = Geometry.getDirPoints(points);
		
		//middlePoints = Geometry.getMiddlePoints(points);
		//dirPoints = Geometry.getDirPoints(points);
		leftContourPoints = Geometry.getContourPoints(points, 20.0f, false);
		rightCountPoints = Geometry.getContourPoints(points, -20.0f, false);

		drawRects(graphics2d);
		drawPoints(graphics2d);
		drawPolylines(graphics2d);
		save();
		saveRotate();
	}
	
	private void drawRects(Graphics2D graphics2d)
	{
		graphics2d.setColor(Color.BLUE);
		for (int i = 0; i < points.size()-1; i++)
		{
			int halfSize = 40;
			Vector<Point> rectPoints = new Vector<Point>();
			rectPoints.add(new Point(halfSize,halfSize));
			rectPoints.add(new Point(-halfSize,halfSize));
			rectPoints.add(new Point(-halfSize,-halfSize));
			rectPoints.add(new Point(halfSize,-halfSize));
			
			int [] pointX = new int[4];
			int [] pointY = new int[4];
			Point dirPoint = dirPoints.get(i);
			for (int j = 0; j < rectPoints.size(); j++)
			{
				Point point = Geometry.getRotatePoint(rectPoints.get(j), dirPoint);
				point = point.add(points.get(i));
				rectPoints.set(j, point);
				pointX[j] = (int) point.x;
				pointY[j] = (int) point.y;
			}
			graphics2d.drawPolygon(pointX, pointY, 4);
			
			BufferedImage outputImage = Geometry.getRotateImage(image, rectPoints, Math.atan2(dirPoint.y,dirPoint.x));
			
			savePatch(outputImage,i);
		}
	}
	
	private void drawPoints(Graphics2D graphics2d)
	{
		///graphics2d.setColor(Color.BLACK);
		for (int i = 0; i < points.size(); i++)
		{
			graphics2d.setColor(Color.RED);
			graphics2d.fillRect((int) points.get(i).x - 2, (int) points.get(i).y - 2, 4, 4);
		}
		for (int i = 0; i < leftContourPoints.size(); i++)
		{
			//graphics2d.setColor(Color.GREEN);
			//graphics2d.fillRect((int) leftContourPoints.get(i).x - 2, (int) leftContourPoints.get(i).y - 2, 4, 4);
		}

		for (int i = 0; i < rightCountPoints.size(); i++)
		{
			//graphics2d.setColor(Color.BLUE);
			//graphics2d.fillRect((int) rightCountPoints.get(i).x - 2, (int) rightCountPoints.get(i).y - 2, 4, 4);
		}
	}
	
	private void drawPolylines(Graphics2D graphics2d)
	{
		graphics2d.drawPolyline(Point.getPointX(points), Point.getPointY(points), points.size());
		
	}
	
	
	private void saveRotate()
	{
		BufferedImage rotateImage = Rotate.getImage(image, 1.2);
		File outputFile = new File("res/rotate.jpg");
		try
		{
			ImageIO.write(rotateImage, "JPG", outputFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void savePatch(BufferedImage outputImage,int index)
	{
		File outputFile = new File("res/patch" + index + ".jpg");
		try
		{
			ImageIO.write(outputImage, "JPG", outputFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private void save()
	{
		BufferedImage outputImage = PixelGrabber.getIamge(image, leftContourPoints, rightCountPoints);

		File outputFile = new File("res/2.jpg");
		try
		{
			ImageIO.write(outputImage, "JPG", outputFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g)
	{
		// TODO Auto-generated method stub
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.drawImage(image, 0, 0, null);
	}

	public void addPoint(int x, int y)
	{
		if (!points.isEmpty() && (points.lastElement().x == x && points.lastElement().y == y))
		{
			return;
		}

		if (points.size() > 1 && (points.get(points.size() - 2).x == x && points.get(points.size() - 2).y == y))
		{
			return;
		}

		points.add(new Point(x, y));

	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		// TODO Auto-generated method stub
		addPoint(event.getX(), event.getY());

	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		// TODO Auto-generated method stub
		initPoint();
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		// TODO Auto-generated method stub
		drawSpine((Graphics2D) getGraphics());
	}

}
