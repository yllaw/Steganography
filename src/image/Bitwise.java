package image;

class Bitwise {

	private static final char MASK = 1 << 0;

	static char turnOnLSB(char character) {
		return character |= MASK;
	}

	static char turnOffLSB(char character) {
		return character &= ~(MASK);
	}

	static boolean statusOfLSB(char character) {
		if ((character & MASK) == 0) {
			return false;
		} else
			return true;
	}

	static boolean statusOfBitAtIndex(char character, int n) throws IllegalArgumentException {
		if ((n < 1) || (n > 8)) {
			throw new IllegalArgumentException("n must be between 1-8");
		}
		if ((character & (1 << (n - 1))) == 0) {
			return false;
		} else
			return true;
	}

	static char encodeBit(char character, char pixel, int n) {
		if (Bitwise.statusOfBitAtIndex(character, n) == Bitwise.statusOfLSB(pixel) == true) {
			return pixel;
		} else if ((Bitwise.statusOfBitAtIndex(character, n) == false) && (Bitwise.statusOfLSB(pixel) == true)) {
			pixel = Bitwise.turnOffLSB(pixel);
		} else if ((Bitwise.statusOfBitAtIndex(character, n) == true) && (Bitwise.statusOfLSB(pixel) == false)) {
			pixel = Bitwise.turnOnLSB(pixel);
		}
		return pixel;
	}
}
