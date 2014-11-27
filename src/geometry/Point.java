package geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class Point
{
	// x����
	public double x;

	// y����
	public double y;

	/**
	 * ���캯��
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * ���캯��
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * ���캯��
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Point()
	{
		this.x = 0;
		this.y = 0;
	}

	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * ��ֱ
	 */
	public Point perpendicularPoint()
	{
		return new Point(-y, x);
	}

	/**
	 * ��ȡ��λ����
	 * */
	public Point normalizePoint()
	{
		if (length() == 0)
		{
			return new Point();
		}
		else
		{
			return div(length());
		}
	}

	/**
	 * ����һ����
	 * 
	 * @param point
	 * */
	public Point add(Point point)
	{
		return new Point(this.x + point.x, this.y + point.y);
	}

	/**
	 * ��ȥһ����
	 * 
	 * @param point
	 * */
	public Point sub(Point point)
	{
		return new Point(this.x - point.x, this.y - point.y);
	}

	/**
	 * ����һ��ֵ
	 * 
	 * @param m
	 * */
	public Point mul(float m)
	{
		return new Point(m * this.x, m * this.y);
	}

	/**
	 * ����һ��ֵ
	 * 
	 * @param m
	 * */
	public Point div(double m)
	{
		return new Point(this.x / m, this.y / m);
	}

	/**
	 * ��ӡ
	 * */
	public void print()
	{
		System.out.println("x: " + x + " y: " + y);
	}

	/**
	 * ����
	 * 
	 * @param grphics2d
	 * @param color
	 * @param size
	 * */
	public void drawPoint(Graphics2D graphics2d, Color color)
	{
		drawPoint(graphics2d, color, 4);
	}

	/**
	 * ����
	 * 
	 * @param grphics2d
	 * @param color
	 * @param size
	 * */
	public void drawPoint(Graphics2D graphics2d, Color color, int size)
	{
		print();

		graphics2d.setColor(color);
		graphics2d.fillRect((int) x - size / 2, (int) y - size / 2, size, size);
	}

	/**
	 * ��ȡ������֮��ľ���
	 * 
	 * @param point1
	 * @param point2
	 * */
	public static double getDistance(Point point1, Point point2)
	{
		return point1.sub(point2).length();
	}

	/**
	 * ��ȡ������֮����յ�
	 * 
	 * @param point1
	 * @param point2
	 * */
	public static Point getMidPoint(Point point1, Point point2)
	{
		return new Point((point1.x + point2.x) / 2.0f, (point1.y + point2.y) / 2.0f);
	}

	/**
	 * ��ȡ������֮���ĳһ��
	 * 
	 * @param point1
	 * @param point2
	 * @param percetage1
	 *            point1ռ�ݵı���
	 * */
	public static Point getPointBetweenTweenPoint(Point point1, Point point2, float percentage1)
	{
		float percentage2 = 1 - percentage1;
		double x = point1.x * percentage1 + point2.x * percentage2;
		double y = point1.y * percentage1 + point2.y * percentage2;
		return new Point(x, y);
	}

	/**
	 * ��ȡ������е�ĶԳƵ�
	 * 
	 * @param midPoint
	 * @param point
	 * */
	public static Point getSymmetryPoint(Point midPoint, Point point)
	{
		return midPoint.mul(2).sub(point);
	}

	/**
	 * ��ȡһ����x����
	 * 
	 * @param points
	 * */
	public static int[] getPointX(Vector<Point> points)
	{
		int[] pointX = new int[points.size()];

		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = (int) points.get(i).x;
		}
		return pointX;
	}

	/**
	 * ��ȡһ����y����
	 * 
	 * @param points
	 * */
	public static int[] getPointY(Vector<Point> points)
	{
		int[] pointY = new int[points.size()];

		for (int i = 0; i < pointY.length; i++)
		{
			pointY[i] = (int) points.get(i).y;
		}
		return pointY;
	}

}
