package geometry;

import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * 结合计算工具
 * */
public class Geometry
{

	/**
	 * 获取轮廓上点的集合
	 * */
	public static Vector<Point> getContourPoints(Vector<Point> points, double width, boolean isTransition)
	{
		Vector<Point> contourPoints = new Vector<Point>();
		Point contourPoint;
		float part = 20;
		double ratio;

		// beginning
		ratio = isTransition ? 0.1 : 1;
		contourPoint = getContourPointAtBegin(points.get(0), points.get(1), width * ratio);
		contourPoints.add(contourPoint);

		// middle
		for (int i = 1; i < points.size() - 1; i++)
		{
			if (isTransition)
			{
				if (i < points.size() / part)
				{
					ratio = i * part / points.size();
				}
				else if (i > points.size() * (part - 1) / part)
				{
					ratio = (points.size() - i) * part / points.size();
				}
				else
				{
					ratio = 1;
				}
			}
			else
			{
				ratio = 1;
			}

			ratio = Math.sin(ratio * Math.PI / 2);
			contourPoint = getContourPoint(points.get(i - 1), points.get(i), points.get(i + 1), ratio * width);
			contourPoints.add(contourPoint);

		}

		// end
		ratio = isTransition ? 0.1 : 1;
		contourPoint = getContourPointAtEnd(points.get(points.size() - 1), points.get(points.size() - 2), width * ratio);
		contourPoints.add(contourPoint);

		return contourPoints;
	}

	/**
	 * 获取轮廓上的点（中间位置）
	 * */
	public static Point getContourPoint(Point lastPoint, Point currPoint, Point nextPoint, double width)
	{
		Point diffPoint1 = currPoint.sub(lastPoint);
		Point diffPoint2 = currPoint.sub(nextPoint);

		double angle = getAngle(diffPoint1, diffPoint2);

		double halfAngle = angle / 2;
		double distance = width / Math.sin(halfAngle);

		Point rotatePoint = getRotatePoint(diffPoint1, halfAngle);
		rotatePoint = rotatePoint.div(rotatePoint.length()).mul((float) distance);

		return currPoint.add(rotatePoint);

	}

	/**
	 * 获取旋转后的向量
	 * */
	public static Point getRotatePoint(Point point, double angle)
	{
		double x = point.x * Math.cos(angle) - point.y * Math.sin(angle);
		double y = point.x * Math.sin(angle) + point.y * Math.cos(angle);

		return new Point((float) (x), (float) (y));
	}

	/**
	 * cos值
	 * */
	public static double getCos(Point diffPoint1, Point diffPoint2)
	{
		return (diffPoint1.x * diffPoint2.x + diffPoint1.y * diffPoint2.y) / (diffPoint1.length() * diffPoint2.length());
	}

	/**
	 * cos值
	 * */
	public static double getCos(Point diffPoint1)
	{
		return (diffPoint1.x) / diffPoint1.length();
	}

	/**
	 * cos值
	 * */
	public static double getSin(Point diffPoint1)
	{
		return (diffPoint1.y) / diffPoint1.length();
	}

	/**
	 * 获取旋转后的向量
	 * */
	public static Point getRotatePoint(Point point, Point diffPoint1)
	{
		double cos = (diffPoint1.x) / diffPoint1.length();
		double sin = (diffPoint1.y) / diffPoint1.length();
		double x = point.x * cos - point.y * sin;
		double y = point.x * sin + point.y * cos;

		return new Point((float) (x), (float) (y));
	}

	/**
	 * 获取角度
	 * */
	public static double getAngle(Point diffPoint1, Point diffPoint2)
	{
		double cos = getCos(diffPoint1, diffPoint2);
		if (cos > 1)
		{
			cos = 1;
		}
		if (cos < -1)
		{
			cos = -1;
		}
		return Math.acos(cos);
	}

	/**
	 * 获取角度
	 * */
	public static double getAngle(Point diffPoint1)
	{
		return getAngle(diffPoint1, new Point(1, 0));
	}

	/**
	 * 获取起始处轮廓坐标
	 * */
	public static Point getContourPointAtBegin(Point endPoint, Point neighbourPoint, double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);

		Point normalPoint = new Point(diffPoint.y, -diffPoint.x);

		double length = normalPoint.length();

		normalPoint = normalPoint.div(length).mul((float) width);

		return endPoint.sub(normalPoint);

	}

	/**
	 * 获取结束处轮廓坐标
	 * */
	public static Point getContourPointAtEnd(Point endPoint, Point neighbourPoint, double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);

		Point normalPoint = new Point(diffPoint.y, -diffPoint.x);

		double length = normalPoint.length();

		normalPoint = normalPoint.div(length).mul((float) width);

		return endPoint.add(normalPoint);

	}

	/**
	 * 将太紧的移除
	 * */
	public static Vector<Point> removeClose(Vector<Point> originPoints, double disatnce)
	{
		for (int i = 0; i < originPoints.size() - 1; i++)
		{

			if ((float) originPoints.get(i).sub(originPoints.get(i + 1)).length() < disatnce)
			{
				Point midPoint = Point.getMidPoint(originPoints.get(i), originPoints.get(i + 1));
				originPoints.remove(i);
				originPoints.remove(i);
				originPoints.insertElementAt(midPoint, i);
			}
		}
		return originPoints;
	}

	/**
	 * 标准化 让相邻的距离不超过max
	 * 
	 * @param maxDistance
	 * */
	public static Vector<Point> normalize(Vector<Point> originPoints, float maxDistance)
	{

		Vector<Point> outputPoints = new Vector<Point>();
		outputPoints.add(originPoints.firstElement());

		double length = 0;
		for (int i = 1; i < originPoints.size() - 1;)
		{

			int count = 0;
			Point point1 = outputPoints.lastElement();
			Point point2 = originPoints.get(i);
			double recentLength = point2.sub(point1).length();
			length = recentLength;

			while (length < maxDistance)
			{
				if (i + count + 1 >= originPoints.size())
				{
					i = originPoints.size();
					break;
				}

				point1 = point2;
				point2 = originPoints.get(i + count + 1);
				recentLength = point2.sub(point1).length();
				length += recentLength;

				count++;
			}

			if (length > maxDistance)
			{
				float percent = (float) ((length - maxDistance) / recentLength);
				Point newPoint = Point.getPointBetweenTweenPoint(point1, point2, percent);
				outputPoints.add(newPoint);

				i = i + count;

			}
			else if (length == maxDistance)
			{
				outputPoints.add(point2);

				i = i + count + 1;
			}
			outputPoints.lastElement().print();

		}

		outputPoints.add(originPoints.lastElement());
		return outputPoints;
	}

	/**
	 * 获取一组点的方向
	 * */
	public static Vector<Float> getDir(Vector<Point> points)
	{
		Vector<Float> dirPoints = new Vector<Float>();
		dirPoints.add((float) getAngle(points.get(0).sub(points.get(1))));
		for (int i = 1; i < points.size() - 1; i++)
		{
			Point middlePoint1 = Point.getMidPoint(points.get(i - 1), points.get(i));
			Point middlePoint2 = Point.getMidPoint(points.get(i + 1), points.get(i));
			float angle = (float) getAngle(middlePoint2.sub(middlePoint1));
			dirPoints.add(angle);
		}
		dirPoints.add((float) getAngle(points.get(points.size() - 1).sub(points.get(points.size() - 2))));
		return dirPoints;
	}

	/**
	 * 获取一组点的中点
	 * */
	public static Vector<Point> getDirPoints(Vector<Point> points)
	{
		Vector<Point> dirPoints = new Vector<Point>();
		dirPoints.add(points.get(1).sub(points.get(0)));
		for (int i = 1; i < points.size() - 1; i++)
		{
			Point middlePoint1 = Point.getMidPoint(points.get(i - 1), points.get(i));
			Point middlePoint2 = Point.getMidPoint(points.get(i + 1), points.get(i));
			dirPoints.add(middlePoint2.sub(middlePoint1));
		}
		dirPoints.add(points.lastElement().sub(points.get(points.size() - 1)));
		return dirPoints;
	}

	/**
	 * 获取一组点的中点
	 * */
	public static Vector<Point> getMiddlePoints(Vector<Point> points)
	{
		Vector<Point> middlePoints = new Vector<Point>();
		for (int i = 1; i < points.size(); i++)
		{
			middlePoints.add(Point.getMidPoint(points.get(i - 1), points.get(i)));
		}
		return middlePoints;
	}

	
	public static BufferedImage getRotateImage(BufferedImage sourceImage, Vector<Point> points,double angle)
	{
		
		System.out.println("Angle" + angle);
		
		int minX = 0,maxX = 0,minY = 0,maxY = 0;
		minX = maxX = (int) points.get(0).x;
		minY = maxY = (int) points.get(0).y;
		for (int i = 1; i < points.size(); i++)
		{
			if (minX>points.get(i).x)
			{
				minX = (int) points.get(i).x;	
			}
			
			if (maxX<points.get(i).x)
			{
				maxX = (int) points.get(i).x;
			}
			
			if (minY>points.get(i).y)
			{
				minY = (int) points.get(i).y;	
			}
			
			if (maxY<points.get(i).y)
			{
				maxY = (int) points.get(i).y;
			}
		}
		
		minX = minX<0?0:minX;
		minY = minY<0?0:minY;
		maxX = maxX>sourceImage.getWidth()-1?sourceImage.getWidth()-1:maxX;
		maxY = maxY>sourceImage.getHeight()-1?sourceImage.getHeight()-1:maxY;
		
		System.out.println("minx" + minX);
		System.out.println("minY" + minY);
		System.out.println("maxX" + maxX);
		System.out.println("maxY" + maxY);
		int size = (maxX - minX)>(maxY-minY)? (maxX - minX): (maxY-minY);
		

		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		
		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				image.setRGB(x,y, sourceImage.getRGB(x+minX, y+minY));
			}
		}
		
		BufferedImage rotateImage = Rotate.getImage(image, 80, angle);
		
		return rotateImage;
		
	}
	
}
