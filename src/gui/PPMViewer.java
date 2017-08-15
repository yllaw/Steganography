package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import image.PPMImageEx;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class PPMViewer extends Application {
	Stage primaryStage;
	Scene scene;
	Pane p = new Pane();
	BorderPane bp = new BorderPane();

	// current file path
	File selectedImageFilePath;
	// unedited, original PPMImage
	PPMImageEx image;
	// current image that is being displayed
	Image displayImage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		// File Menu
		Menu fileMenu = new Menu("_File");

		// File Menu Items
		MenuItem load = new MenuItem("Load Image...");
		load.setOnAction(e -> {
			try {
				loadFromFile();
				if (this.selectedImageFilePath != null) {
					p.getChildren().clear();
					primaryStage.setHeight(500);
					primaryStage.setHeight(800);
					this.image = new PPMImageEx(this.selectedImageFilePath);
					this.displayImage = PPMViewer.getImageFromFile(this.selectedImageFilePath);
					this.loadImage(this.displayImage);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		fileMenu.getItems().add(load);

		MenuItem save = new MenuItem("Save Image...");
		save.setOnAction(e -> saveToFile());
		fileMenu.getItems().add(save);
		fileMenu.getItems().add(new SeparatorMenuItem());

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> System.exit(0));
		fileMenu.getItems().add(exit);

		Menu stagMenu = new Menu("_Steganography");

		// Help Menu Items
		MenuItem hideMessage = new MenuItem("Hide A Secret Message!...");
		hideMessage.setOnAction(e -> {
			try {
				hideMessage();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		MenuItem recoverMessage = new MenuItem("Recover A Secret Message!...");
		recoverMessage.setOnAction(e -> {
			try {
				recoverMessage();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		stagMenu.getItems().addAll(hideMessage, recoverMessage);

		// Color Menu
		Menu colorMenu = new Menu("_Color");

		// Color Menu Items

		// grayscale
		MenuItem grayscale = new MenuItem("Grayscale");
		grayscale.setOnAction(e -> {
			try {

				imageColorToGrayscale();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		colorMenu.getItems().add(grayscale);

		// negative
		MenuItem negative = new MenuItem("Negative");
		negative.setOnAction(e -> {
			try {
				imageColorToNegative();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		colorMenu.getItems().add(negative);

		// sepia
		MenuItem sepia = new MenuItem("Sepia");
		sepia.setOnAction(e -> {
			try {
				imageColorToSepia();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		colorMenu.getItems().add(sepia);
		colorMenu.getItems().add(new SeparatorMenuItem());

		// revert to original file
		MenuItem revert = new MenuItem("Revert Unsaved Changes...");
		revert.setOnAction(e -> {
			try {
				if (this.selectedImageFilePath == null) {
					fileErrorDialog();
					return;
				}

				if (this.warningDialog()) {
					revertImageToOrginalFile();
				}
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		colorMenu.getItems().add(revert);

		// Help Menu
		Menu helpMenu = new Menu("_Help");

		// Help Menu Items
		MenuItem about = new MenuItem("About...");
		// TODO: about.setOnAction(e -> aboutDialog());
		helpMenu.getItems().add(about);

		// Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, stagMenu, colorMenu, helpMenu);

		// BorderPane
		this.bp.setTop(menuBar);
		this.bp.setCenter(this.p);

		this.scene = new Scene(bp, 800, 500);
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("editorIcon.png")));
		this.primaryStage.setScene(scene);
		this.primaryStage.setTitle("PPM Image Editor");
		this.primaryStage.show();

	}

	// change image to grayscale
	private void imageColorToGrayscale() throws NullPointerException, IOException {
		if (this.selectedImageFilePath == null) {
			fileErrorDialog();
			return;
		}
		this.image.grayscale();
		updateImageViewSource(this.image);

	}

	// change image to negative
	private void imageColorToNegative() throws NullPointerException, IOException {
		if (this.selectedImageFilePath == null) {
			fileErrorDialog();
			return;
		}
		this.image.negative();
		updateImageViewSource(this.image);

	}

	// change image to sepia
	private void imageColorToSepia() throws NullPointerException, IOException {
		if (this.selectedImageFilePath == null) {
			fileErrorDialog();
			return;
		}
		this.image.sepia();
		updateImageViewSource(this.image);

	}

	// revert image to orginal file
	private void revertImageToOrginalFile() throws NullPointerException, IOException {
		this.displayImage = PPMViewer.getImageFromFile(this.selectedImageFilePath);
		loadImage(this.displayImage);
		// restore tempImage to original file
		this.image = new PPMImageEx(this.selectedImageFilePath);
	}

	// helper method to dynamically update imageView
	private void updateImageViewSource(PPMImageEx image) throws NullPointerException, IOException {
		// create temporary file
		File tempFile = File.createTempFile("image", ".ppm");
		// clear pane
		p.getChildren().clear();
		primaryStage.setHeight(500);
		primaryStage.setHeight(800);
		// write to tempFile
		image.writeImage(tempFile.getPath());
		// load edited image
		Image imageToDisplay = PPMViewer.getImageFromFile(tempFile);
		loadImage(imageToDisplay);
		// delete tempFile
		tempFile.delete();
		tempFile.deleteOnExit();
	}

	// load image to pane
	private void loadImage(Image imageToDisplay) {
		// TODO: account for multiple screen resolutions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Load colors
		ImageView img = new ImageView(imageToDisplay);
		if (!primaryStage.isMaximized()) {

			if (imageToDisplay.getHeight() > (screenSize.getHeight() - 30)) {
				img.setFitHeight(screenSize.getHeight() - 30);
				primaryStage.setHeight(img.getFitHeight());
			} else {
				primaryStage.setHeight(imageToDisplay.getHeight() + 65);
			}
			img.setPreserveRatio(true);

			if (imageToDisplay.getWidth() > 800 && imageToDisplay.getWidth() < screenSize.getWidth()) {
				// img.setFitWidth(screenSize.getWidth());
				primaryStage.setWidth(imageToDisplay.getWidth());
			} else {
				primaryStage.setWidth(imageToDisplay.getWidth() - screenSize.getWidth());
			}
		}

		p.getChildren().add(img);

	}

	// hide message
	private void hideMessage() throws IOException {
		if (this.selectedImageFilePath == null) {
			fileErrorDialog();
			return;
		}
		TextInputDialog dialog = new TextInputDialog("Type hidden message here");
		dialog.setTitle("Hide Message");
		dialog.setHeaderText("Hide a message into the .PPM image");
		dialog.setContentText("Please enter your secret message:");
		dialog.initOwner(this.primaryStage);

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(message -> this.image.hideMessage(message));
	}

	// recover message
	private void recoverMessage() throws IOException {
		if (this.selectedImageFilePath == null) {
			fileErrorDialog();
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Recover Message");
		alert.setHeaderText("Hidden Message Recovered:");
		alert.setContentText("Hint: If there is gibberish, then no message was found.");
		TextArea textArea = new TextArea();
		textArea.appendText(this.image.recoverMessage());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		
		alert.initOwner(this.primaryStage);
		alert.showAndWait();
	}

	// load stage
	private void loadStage() throws NullPointerException, IOException {
		loadFromFile();
		if (this.selectedImageFilePath != null) {
			p.getChildren().clear();
			primaryStage.setHeight(500);
			primaryStage.setHeight(800);
			this.image = new PPMImageEx(this.selectedImageFilePath);
			this.displayImage = PPMViewer.getImageFromFile(this.selectedImageFilePath);
			this.loadImage(this.displayImage);
		}
	}

	// file error with resolution
	private void fileErrorDialog() throws IOException {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("No File Found");
		alert.setHeaderText("File Not Found Error");
		alert.setContentText("Load a .ppm image to continue.");
		alert.initOwner(this.primaryStage);
		alert.showAndWait();
		this.loadStage();
	}

	// warning dialog
	private boolean warningDialog() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Revert Changes Warning");
		alert.setHeaderText("Are You Sure You Want To Revert Changes?");
		alert.setContentText("Click 'OK' to revert changes, otherwise click 'Cancel' ");
		alert.initOwner(this.primaryStage);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	// save to file
	private void saveToFile() {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new FileNameExtensionFilter("PPMImage Files", "ppm"));
		fc.showSaveDialog(null);
		if (fc.getSelectedFile().getPath().toLowerCase().endsWith(".ppm")) {
			this.image.writeImage(fc.getSelectedFile().getPath());
		} else {
			this.image.writeImage(fc.getSelectedFile().getPath() + ".ppm");
		}
	}

	// load from file
	private File loadFromFile() throws IOException {
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter(new FileNameExtensionFilter("PPMImage Files", "ppm"));
		fc.setDialogTitle("Select an Image.ppm ");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int retVal = fc.showOpenDialog(null);

		if (retVal == JFileChooser.APPROVE_OPTION) {
			this.selectedImageFilePath = fc.getSelectedFile();
			return fc.getSelectedFile();
		}
		return null;
	}

	// read file and convert to usable JavaFX image
	private static Image getImageFromFile(File img) throws NullPointerException, IOException {
		if (img == null) {
			throw new NullPointerException("File to render is null");
		}
		Image image = SwingFXUtils.toFXImage(ImageIO.read(img), null);
		return image;
	}

	// TODO: NOTE: The color manipulation functions should not be called on an
	// image that has the hidden message. This will corrupt the message because
	// the values of the pixels will be changed.

	public static void main(String[] args) {
		launch(args);
	}
}
