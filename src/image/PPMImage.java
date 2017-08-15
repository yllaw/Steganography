package image;

import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class PPMImage {
	private String magicNumber;
	private int width;
	private int height;
	private int maxColorValue;
	private char[] pixelData;

	public PPMImage(File paramFile) {
		if (!paramFile.getName().toLowerCase().endsWith(".ppm")) {
			throw new RuntimeException("ERROR: File is not a PPM Image.");
		}

		loadImage(paramFile);
	}

	public char[] getPixelData() {
		return pixelData;
	}

	private void loadImage(File paramFile) {
		try {
			DataInputStream localDataInputStream = new DataInputStream(new java.io.FileInputStream(paramFile));

			String str = localDataInputStream.readLine();
			if (!str.equals("P6")) {
				throw new RuntimeException("PPMIMAGE READ ERROR: First line is not the magic number P6!");
			}
			magicNumber = str;

			String[] arrayOfString = localDataInputStream.readLine().split(" ");

			if (arrayOfString.length != 2) {
				throw new RuntimeException("PPMIMAGE READ ERROR: Cannot parse width and height values correctly");
			}
			width = Integer.parseInt(arrayOfString[0]);
			height = Integer.parseInt(arrayOfString[1]);

			maxColorValue = Integer.parseInt(localDataInputStream.readLine());

			pixelData = new char[width * height * 3];

			for (int i = 0; i < pixelData.length; i++) {
				pixelData[i] = ((char) localDataInputStream.readUnsignedByte());
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			System.err.println("ERROR: PPMImage not found!");
		} catch (IOException localIOException) {
			System.err.println("ERROR: PPMImage read error! Image probably not in the right format.");
		} catch (NumberFormatException localNumberFormatException) {
			System.err.println("ERROR: PPMImage read error! Image probably not in the right format.");
		} catch (RuntimeException localRuntimeException) {
			System.err.println(localRuntimeException.getMessage());
		}
	}

	public void writeImage(String paramString) {
		if (!paramString.toLowerCase().endsWith(".ppm")) {
			throw new RuntimeException("ERROR: File is not a PPM Image.");
		}

		File localFile = new File(paramString);
		try {
			BufferedWriter localBufferedWriter = new BufferedWriter(new java.io.FileWriter(localFile));
			localBufferedWriter.write("" + magicNumber);
			localBufferedWriter.write("\n");
			localBufferedWriter.write("" + width + " " + height);
			localBufferedWriter.write("\n");
			localBufferedWriter.write("" + maxColorValue);
			localBufferedWriter.write("\n");

			localBufferedWriter.flush();
			localBufferedWriter.close();

			DataOutputStream localDataOutputStream = new DataOutputStream(
					new java.io.FileOutputStream(localFile, true));

			for (int i = 0; i < pixelData.length; i++) {
				localDataOutputStream.writeByte(pixelData[i]);
			}

			localDataOutputStream.flush();
			localDataOutputStream.close();
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}
}