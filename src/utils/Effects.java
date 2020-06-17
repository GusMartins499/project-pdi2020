package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Effects {

	public static Image cinzaMediaAritmetica(Image imagem, int pcR, int pcG, int pcB) {

		try {
			int w = (int) imagem.getWidth();
			int h = (int) imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color corA = pr.getColor(i, j);
					double media = (corA.getRed() + corA.getGreen() + corA.getBlue()) / 3;
					if (pcR != 0 || pcG != 0 || pcB != 0)
						media = (corA.getRed() * pcR + corA.getGreen() * pcG + corA.getBlue() * pcB) / 100;
					Color corN = new Color(media, media, media, corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			return wi;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image ruidos(Image imagem, int tipoVizinhos) {
		try {
			int w = (int) imagem.getWidth();
			int h = (int) imagem.getHeight();

			PixelReader pr = imagem.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = 1; i < w - 1; i++) {
				for (int j = 1; j < h - 1; j++) {
					Color corA = pr.getColor(i, j);
					Pixel p = new Pixel(corA.getRed(), corA.getGreen(), corA.getBlue(), i, j);
					buscavizinhos(imagem, p);
					Pixel vetor[] = null;
					if (tipoVizinhos == Constantes.VIZINHOS3X3)
						vetor = p.viz3;
					if (tipoVizinhos == Constantes.VIZINHOSCRUZ)
						vetor = p.vizC;
					if (tipoVizinhos == Constantes.VIZINHOSX)
						vetor = p.vizX;
					double red = mediana(vetor, Constantes.CANALR);
					double green = mediana(vetor, Constantes.CANALG);
					double blue = mediana(vetor, Constantes.CANALB);
					Color corN = new Color(red, green, blue, corA.getOpacity());
					pw.setColor(i, j, corN);
				}
			}
			return wi;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static double mediana(Pixel[] pixels, int canal) {
		double v[] = new double[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			if (canal == Constantes.CANALR) {
				v[i] = pixels[i].r;
			}
			if (canal == Constantes.CANALG) {
				v[i] = pixels[i].g;
			}
			if (canal == Constantes.CANALB) {
				v[i] = pixels[i].b;
			}
		}
		v = ordenaVetor(v);
		return v[v.length / 2];
	}

	private static void buscavizinhos(Image imagem, Pixel p) {
		p.vizX = buscaVizinhosX(imagem, p);
		p.vizC = buscaVizinhosC(imagem, p);
		p.viz3 = buscaVizinhos3(imagem, p);
	}

	private static Pixel[] buscaVizinhosX(Image imagem, Pixel p) {
		Pixel[] vizX = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		Color cor = pr.getColor(p.x - 1, p.y + 1);
		vizX[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y + 1);
		cor = pr.getColor(p.x + 1, p.y - 1);
		vizX[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y - 1);
		cor = pr.getColor(p.x - 1, p.y - 1);
		vizX[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y - 1);
		cor = pr.getColor(p.x + 1, p.y + 1);
		vizX[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y + 1);
		vizX[4] = p;
		return vizX;
	}

	private static Pixel[] buscaVizinhos3(Image imagem, Pixel p) {
		Pixel[] viz3 = new Pixel[9];
		PixelReader pr = imagem.getPixelReader();
		Color cor = pr.getColor(p.x - 1, p.y - 1);
		viz3[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y - 1);
		cor = pr.getColor(p.x, p.y - 1);
		viz3[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y - 1);
		cor = pr.getColor(p.x + 1, p.y - 1);
		viz3[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y - 1);
		cor = pr.getColor(p.x + 1, p.y);
		viz3[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y);
		cor = pr.getColor(p.x + 1, p.y + 1);
		viz3[4] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y + 1);
		cor = pr.getColor(p.x, p.y + 1);
		viz3[5] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y + 1);
		cor = pr.getColor(p.x - 1, p.y + 1);
		viz3[6] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y + 1);
		cor = pr.getColor(p.x - 1, p.y);
		viz3[7] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y);
		viz3[8] = p;
		return viz3;
	}

	private static Pixel[] buscaVizinhosC(Image imagem, Pixel p) {
		Pixel[] vizC = new Pixel[5];
		PixelReader pr = imagem.getPixelReader();
		Color cor = pr.getColor(p.x, p.y - 1);
		vizC[0] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y - 1);
		cor = pr.getColor(p.x + 1, p.y);
		vizC[1] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x + 1, p.y);
		cor = pr.getColor(p.x, p.y + 1);
		vizC[2] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x, p.y + 1);
		cor = pr.getColor(p.x - 1, p.y);
		vizC[3] = new Pixel(cor.getRed(), cor.getGreen(), cor.getBlue(), p.x - 1, p.y);
		vizC[4] = p;
		return vizC;
	}

	private static double[] ordenaVetor(double[] v) {
		double aux = 0;
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v.length; j++) {
				if (v[i] < v[j]) {
					aux = v[i];
					v[i] = v[j];
					v[j] = aux;
				}
			}
		}
		return v;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getGrafico(Image imagem, BarChart<String, Number> grafico) {
		// HISTOGRAMA ÚNICO
		// int[] histograma = histogramaUnico(imagem);
		// XYChart.Series valor = new XYChart.Series();
		// for (int i = 0; i < histograma.length; i++) {
		// valor.getData().add(new XYChart.Data(i + "", histograma[i]));
		// }
		// grafico.getData().addAll(valor);

		// HISTOGRAMA POR CANAL
		int[] histR = histograma(imagem, 1);
		int[] histG = histograma(imagem, 2);
		int[] histB = histograma(imagem, 3);
		XYChart.Series vlrR = new XYChart.Series();
		XYChart.Series vlrG = new XYChart.Series();
		XYChart.Series vlrB = new XYChart.Series();

		for (int i = 0; i < 256; i++) {
			vlrR.getData().add(new XYChart.Data(i + "", histR[i]));
			vlrG.getData().add(new XYChart.Data(i + "", histG[i]));
			vlrB.getData().add(new XYChart.Data(i + "", histB[i]));
		}
		grafico.getData().addAll(vlrR, vlrG, vlrB);

		for (Node n : grafico.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill:red;");
		}
		for (Node n : grafico.lookupAll(".default-color1.chart-bar")) {
			n.setStyle("-fx-bar-fill:green;");
		}
		for (Node n : grafico.lookupAll(".default-color2.chart-bar")) {
			n.setStyle("-fx-bar-fill:blue;");
		}

	}

	public static int[] histograma(Image imagem, int i) {
		int valorCanal[] = new int[256];
		PixelReader pr = imagem.getPixelReader();
		double w = (int) imagem.getWidth();
		double h = (int) imagem.getHeight();

		if (i == 1) {
			for (int j = 1; j < w; j++) {
				for (int k = 1; k < h; k++) {
					valorCanal[(int) (pr.getColor(j, k).getRed() * 255)]++;
				}
			}
		}
		if (i == 2) {
			for (int j = 1; j < w; j++) {
				for (int k = 1; k < h; k++) {
					valorCanal[(int) (pr.getColor(j, k).getGreen() * 255)]++;
				}
			}
		}
		if (i == 3) {
			for (int j = 1; j < w; j++) {
				for (int k = 1; k < h; k++) {
					valorCanal[(int) (pr.getColor(j, k).getBlue() * 255)]++;
				}
			}
		}
		return valorCanal;
	}

	public static Image equalizacaoHistograma(Image imagem, boolean todos) {
		// equalizacao valida
		int w = (int) imagem.getWidth();
		int h = (int) imagem.getHeight();
		PixelReader pr = imagem.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();

		int[] hR = histograma(imagem, 1); // 1=red 2=green 3=blue
		int[] hG = histograma(imagem, 2);
		int[] hB = histograma(imagem, 3);

		int[] histAcR = histogramaAc(hR); // não tem código, implementar o histograma acumulado
		int[] histAcG = histogramaAc(hG);
		int[] histAcB = histogramaAc(hB);

		int qtTonsRed = qtTons(hR); // valors válidos, contas quantos são 0 dentro do vetor de cada canal e diminuir
									// de 255
		int qtTonsGreen = qtTons(hG);
		int qtTonsBlue = qtTons(hB);

		double minR = pontoMin(hR);
		double minG = pontoMin(hG);
		double minB = pontoMin(hB);

		// equalização
		if (todos) {
			qtTonsRed = 255;
			qtTonsGreen = 255;
			qtTonsBlue = 255;
			minR = 0;
			minG = 0;
			minB = 0;
		}

		double n = w * h; // n=quantidade total de pixels na imagem

		for (int i = 1; i < w; i++) {
			for (int j = 1; j < h; j++) {
				Color oldColor = pr.getColor(i, j);

				double acR = histAcR[(int) (oldColor.getRed() * 255)];
				double acG = histAcG[(int) (oldColor.getGreen() * 255)];
				double acB = histAcB[(int) (oldColor.getBlue() * 255)];

				double pxR = ((qtTonsRed - 1) / n) * acR;
				double pxG = ((qtTonsGreen - 1) / n) * acG;
				double pxB = ((qtTonsBlue - 1) / n) * acB;

				double corR = (minR + pxR) / 255;
				double corG = (minG + pxG) / 255;
				double corB = (minB + pxB) / 255;

				Color newColor = new Color(corR, corG, corB, oldColor.getOpacity());
				pw.setColor(i, j, newColor);
			}
		}
		return wi;
	}

	public static int[] histogramaAc(int[] histograma) {
		int[] histogramaAcumulado = new int[256];
		histogramaAcumulado[0] = histograma[0];

		for (int i = 1; i < 256; i++) {
			histogramaAcumulado[i] = histogramaAcumulado[i - 1] + histograma[i];
		}
		return histogramaAcumulado;
	}

	public static int pontoMin(int[] hist) {
		for (int i = 0; i < hist.length; i++) {
			if (hist[i] > 0)
				return i;
		}
		return 0;
	}

	public static int qtTons(int[] histograma) {
		int qt = 0;
		for (int i = 0; i < 256; i++) {
			if (histograma[i] > 0) {
				qt++;
			}
		}
		return qt;
	}

	public static Image gaussiano(Image imagem) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			Mat mat = image2Mat(imagem);
			Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0);
			imagem = mat2Image(mat);
			return imagem;
		} catch (Exception e) {
			return null;
		}
	}

	private static Image mat2Image(Mat frame) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (frame.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = frame.channels() * frame.cols() * frame.rows();
		byte[] b = new byte[bufferSize];
		frame.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(frame.cols(), frame.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return SwingFXUtils.toFXImage(image, null);

	}

	public static Mat bufferedImage2Mat(BufferedImage in) {
		Mat out;
		byte[] data;
		int r, g, b;
		int height = in.getHeight();
		int width = in.getWidth();
		if (in.getType() == BufferedImage.TYPE_INT_RGB || in.getType() == BufferedImage.TYPE_INT_ARGB) {
			out = new Mat(height, width, CvType.CV_8UC3);
			data = new byte[height * width * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
			for (int i = 0; i < dataBuff.length; i++) {
				data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
				data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
				data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
			}
		} else {
			out = new Mat(height, width, CvType.CV_8UC1);
			data = new byte[height * width * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
			for (int i = 0; i < dataBuff.length; i++) {
				r = (byte) ((dataBuff[i] >> 16) & 0xFF);
				g = (byte) ((dataBuff[i] >> 8) & 0xFF);
				b = (byte) ((dataBuff[i] >> 0) & 0xFF);
				data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); // luminosity
			}
		}
		out.put(0, 0, data);
		return out;
	}

	public static Mat image2Mat(Image image) {

		BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

		return bufferedImage2Mat(bImage);

	}
	
	public static Mat gabor(Mat myImg){

	      // prepare the output matrix for filters
	    Mat gabor1 = new Mat (myImg.width(), myImg.height(), CvType.CV_8UC1);
	    Mat gabor2 = new Mat (myImg.width(), myImg.height(), CvType.CV_8UC1);
	    Mat gabor3 = new Mat (myImg.width(), myImg.height(), CvType.CV_8UC1);
	    Mat gabor4 = new Mat (myImg.width(), myImg.height(), CvType.CV_8UC1);
	    Mat enhanced = new Mat (myImg.width(), myImg.height(), CvType.CV_8UC1);

	      //predefine parameters for Gabor kernel 
	    Size kSize = new Size(31,31);

	    double theta1 = 0;
	    double theta2 = 45;
	    double theta3 = 90;
	    double theta4 = 135;

	    double lambda = 30;
	    double sigma = 24;  
	    double gamma = 1;
	    double psi =  0;

	       // the filters kernel
	    Mat kernel1 = Imgproc.getGaborKernel(kSize, sigma, theta1, lambda, gamma, psi, CvType.CV_32F);
	    Mat kernel2 = Imgproc.getGaborKernel(kSize, sigma, theta2, lambda, gamma, psi, CvType.CV_32F);
	    Mat kernel3 = Imgproc.getGaborKernel(kSize, sigma, theta3, lambda, gamma, psi, CvType.CV_32F);
	    Mat kernel4 = Imgproc.getGaborKernel(kSize, sigma, theta4, lambda, gamma, psi, CvType.CV_32F);

	      // apply filters on my image. The result is stored in gabor1...4
	    Imgproc.filter2D(myImg, gabor1, -1, kernel1);
	    Imgproc.filter2D(myImg, gabor2, -1, kernel2);
	    Imgproc.filter2D(myImg, gabor3, -1, kernel3);
	    Imgproc.filter2D(myImg, gabor4, -1, kernel4);

	    //enhanced = gabor1+gabor2+gabor3+gabor4 - something like that
	    myImg.convertTo(myImg, CvType.CV_32F);
	    return myImg;
	}
}
