package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Effects;

public class PrincipalController {

	@FXML
	private ImageView imgView1;
	@FXML
	private ImageView imgView2;
	@FXML
	TextField txtPorcentagemRed;
	@FXML
	TextField txtPorcentagemGreen;
	@FXML
	TextField txtPorcentagemBlue;

	private Image img1;
	private Image img2;

	@FXML
	public void abreImagem1() {
		img1 = abreImagem(imgView1, img1);
	}

	public void atualizaImagem() {
		imgView2.setImage(img2);
		imgView2.setFitWidth(img2.getWidth());
		imgView2.setFitHeight(img2.getHeight());
	}

	private Image abreImagem(ImageView imageView, Image image) {
		File f = selecionaImagem();
		if (f != null) {
			image = new Image(f.toURI().toString());
			imageView.setImage(image);
			imageView.setFitWidth(image.getWidth());
			imageView.setFitHeight(image.getHeight());
			return image;
		}
		return null;
	}

	private File selecionaImagem() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.JPG", "*.png",
				"*.PNG", "*.gif", "*.GIF", "*.bmp", "*.BMP"));
		fileChooser.setInitialDirectory(new File("imagens\\"));
		File imgSelec = fileChooser.showOpenDialog(null);
		try {
			if (imgSelec != null) {
				return imgSelec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	public void salvar() {
		if (img2 != null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("imagem", "*.png"));
			fileChooser.setInitialDirectory((new File("imagens processadas\\")));
			File file = fileChooser.showSaveDialog(null);
			if (file != null) {
				BufferedImage bImg = SwingFXUtils.fromFXImage(img2, null);
				try {
					ImageIO.write(bImg, "PNG", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	public void cinzaPonderada() {
		try {
			img2 = Effects.cinzaMediaAritmetica(img1, Integer.parseInt(txtPorcentagemRed.getText()),
					Integer.parseInt(txtPorcentagemGreen.getText()), Integer.parseInt(txtPorcentagemBlue.getText()));
			atualizaImagem();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void cinzaAritmetica() {
		try {
			img2 = Effects.cinzaMediaAritmetica(img1, 0, 0, 0);
			atualizaImagem();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void ruido3x3() {
		img2 = Effects.ruidos(img1, 1);
		atualizaImagem();
	}

	@FXML
	public void ruidoCruz() {
		img2 = Effects.ruidos(img1, 2);
		atualizaImagem();
	}

	@FXML
	public void ruidoX() {
		img2 = Effects.ruidos(img1, 3);
		atualizaImagem();
	}

	@FXML
	public void abreModalHistograma(ActionEvent event) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(true);
			// stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			// stage.setMaximized(true);
			stage.show();

			HistogramaController controller = (HistogramaController) loader.getController();

			if (img1 != null)
				Effects.getGrafico(img1, controller.grafico1);
			if (img2 != null)
				Effects.getGrafico(img2, controller.grafico2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void equalizacao() {
		img2 = Effects.equalizacaoHistograma(img1, true);
		atualizaImagem();
	}

	@FXML
	public void equalizacaoValida() {
		img2 = Effects.equalizacaoHistograma(img1, false);
		atualizaImagem();
	}
	
	@FXML
	public void gaussiano() {
		img2 = Effects.gaussiano(img1);
		atualizaImagem();
	}
	
	@FXML
	public void gabor() {
		Mat mat = Effects.image2Mat(img2);
		Image newImg2 = Effects.gabor(mat);
		
		atualizaImagem();
	}
}
