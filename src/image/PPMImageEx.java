package image;

import java.io.File;

public class PPMImageEx extends PPMImage {
	char[] pixels;

	public PPMImageEx(File image) {
		super(image);
		this.pixels = super.getPixelData();

	}

	public void hideMessage(String messageToHide) {
		// concatenate terminating char '\0'
		messageToHide = messageToHide.concat("\0");
		int index = 0;
		// for each char in message
		for (int i = 0; i < messageToHide.length(); i++)
			// for each bit in char
			for (int n = 8; n > 0; n--) {
				// encode in RGB sub-pixels
				this.pixels[index] = Bitwise.encodeBit(messageToHide.charAt(i), this.pixels[index], n);
				index++;
			}
	}

	public String recoverMessage() {
		String binary = "";
		String message = "";
		int zeroCount = 0;
		int index = 0;
		// for each sub-pixel take LSB and write it to a string
		while (true) {
			if (Bitwise.statusOfLSB(pixels[index])) {
				binary += 1;
				zeroCount = 0;
			} else if (!Bitwise.statusOfLSB(pixels[index])) {
				binary += 0;
				zeroCount++;
			}
			index++;

			// check for terminating char
			if (zeroCount == 8) {
				break;
			} else if (index % 8 == 0) {// reset after 8 bits
				zeroCount = 0;
			}
		}
		// get byte from binary string and create char to add to message
		for (int i = 0; i < binary.length() / 8; i++) {
			int b = Integer.parseInt(binary.substring(8 * i, (i + 1) * 8), 2);
			message += (char) b;
		}
		return message;
	}

	public void grayscale() {
		for (int i = 0; i < this.pixels.length; i += 3) {
			double R = this.pixels[i] * .299;
			double G = this.pixels[i + 1] * .587;
			double B = this.pixels[i + 2] * .114;

			char grayscaleSum = (char) (R + G + B);

			this.pixels[i] = grayscaleSum;
			this.pixels[i + 1] = grayscaleSum;
			this.pixels[i + 2] = grayscaleSum;
		}
	}

	public void negative() {
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = (char) (255 - this.pixels[i]);
		}
	}

	public void sepia() {
		for (int i = 0; i < this.pixels.length; i += 3) {
			double R = this.pixels[i];
			double G = this.pixels[i + 1];
			double B = this.pixels[i + 2];

			double rSum = checkMax((R * .393) + (G * .769) + (B * .189));

			double gSum = checkMax((R * .349) + (G * .686) + (B * .168));

			double bSum = checkMax((R * .272) + (G * .534) + (B * .131));

			this.pixels[i] = (char) rSum;
			this.pixels[i + 1] = (char) gSum;
			this.pixels[i + 2] = (char) bSum;
		}
	}

	// sepia() helper method
	private double checkMax(double sum) {
		if (sum > 255) {
			return 255;
		}
		return sum;
	}

}
