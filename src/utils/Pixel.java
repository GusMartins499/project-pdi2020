package utils;

public class Pixel {

	public double r;
	public double g;
	public double b;
	public int x;
	public int y;
	public Pixel[] vizX = new Pixel[4];
	public Pixel[] vizC = new Pixel[4];
	public Pixel[] viz3 = new Pixel[8];

	public Pixel(double r, double g, double b, int x, int y) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.x = x;
		this.y = y;
	}
}
