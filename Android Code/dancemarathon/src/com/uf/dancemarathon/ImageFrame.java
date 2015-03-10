package com.uf.dancemarathon;

/**
 * Objects of this class represent a certain inner section of an image based on ratios with the original 
 * image pixels and the section's x and y locations in pixels.
 * @author Chris Whitten
 *
 */
public class ImageFrame {
	
	//These are all ratios not locations//
	private double minRatio_X;
	private double minRatio_Y;
	private double maxRatio_X;
	private double maxRatio_Y;
	
	
	public ImageFrame(double min_X, double min_Y, double max_X, double max_Y) {
		this.minRatio_X = min_X;
		this.minRatio_Y = min_Y;
		this.maxRatio_X = max_X;
		this.maxRatio_Y = max_Y;
	}
	
	public ImageFrame(double minX_coord, double minY_coord, double maxX_coord, double maxY_coord, double width, double height)
	{
		this.minRatio_X = minX_coord / width;
		this.minRatio_Y = minY_coord / height;
		this.maxRatio_X = maxX_coord / width;
		this.maxRatio_Y = maxY_coord / height;
	}
	
	protected double getMinXCoord(double imgWidth)
	{
		return minRatio_X * imgWidth;
	}
	
	protected double getMaxXCoord(double imgWidth)
	{
		return maxRatio_X * imgWidth;
	}
	
	protected double getMinYCoord(double imgHeight)
	{
		return minRatio_Y * imgHeight;
	}
	
	protected double getMaxYCoord(double imgHeight)
	{
		return maxRatio_Y * imgHeight;
	}
	
	protected double getWidth(double imgWidth)
	{
		return maxRatio_X *imgWidth - minRatio_X * imgWidth;
	}
	
	protected double getHeight(double imgHeight)
	{
		return maxRatio_Y *imgHeight - minRatio_Y * imgHeight;
	}
	
	/**
	 * Check to see if a point is in this frame. All values in pixels.
	 * @param x The x value of the point
	 * @param y The y value of the point
	 * @param imageWidth The width of the image
	 * @param imageHeight The height of the image
	 * @return true if point is in the frame.
	 */
	protected boolean isPointInFrame(double x, double y, double imageWidth, double imageHeight)
	{
		double minX_loc = minRatio_X * imageWidth;
		double minY_loc = minRatio_Y * imageHeight;
		double maxX_loc = maxRatio_X * imageWidth;
		double maxY_loc = maxRatio_Y * imageHeight;
		
		if(x >= minX_loc && x <= maxX_loc)
		{
			if( y >= minY_loc && y <= maxY_loc)
				return true;
		}
		
		return false;
	}
	
	
	
	
	
	
	
	/**
	 * @return the min_X
	 */
	public double getMin_X() {
		return minRatio_X;
	}
	/**
	 * @param min_X the min_X to set
	 */
	public void setMin_X(double min_X) {
		this.minRatio_X = min_X;
	}
	/**
	 * @return the min_Y
	 */
	public double getMin_Y() {
		return minRatio_Y;
	}
	/**
	 * @param min_Y the min_Y to set
	 */
	public void setMin_Y(double min_Y) {
		this.minRatio_Y = min_Y;
	}
	/**
	 * @return the max_X
	 */
	public double getMax_X() {
		return maxRatio_X;
	}
	/**
	 * @param max_X the max_X to set
	 */
	public void setMax_X(double max_X) {
		this.maxRatio_X = max_X;
	}
	/**
	 * @return the max_Y
	 */
	public double getMax_Y() {
		return maxRatio_Y;
	}
	/**
	 * @param max_Y the max_Y to set
	 */
	public void setMax_Y(double max_Y) {
		this.maxRatio_Y = max_Y;
	}
	
	
	
}
