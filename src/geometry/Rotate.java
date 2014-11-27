package geometry;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Rotate
{
	public static BufferedImage getImage(BufferedImage sourceImage,int size,double angle)
	{
		BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2d = outputImage.createGraphics();
		
		graphics2d.translate(size/2-sourceImage.getWidth()/2, size/2-sourceImage.getHeight()/2);
		graphics2d.rotate(angle,sourceImage.getWidth()/2,sourceImage.getHeight()/2);
		
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(angle);
		
		graphics2d.drawImage(sourceImage, null, null);
		
		return outputImage;
	}
	
	
	public static BufferedImage getImage(BufferedImage sourceImage,double angle)
	{
		BufferedImage outputImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2d = outputImage.createGraphics();
		
		graphics2d.rotate(angle,sourceImage.getWidth()/2,sourceImage.getHeight()/2);
		
		graphics2d.drawImage(sourceImage, null, null);
		
		return outputImage;
	}
}
