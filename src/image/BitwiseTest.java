package image;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class BitwiseTest {

	@Test
	public void Bitwise_turnOnLSBtest() {
		// Arrange
		char test = 2;
		// Act
		test = Bitwise.turnOnLSB(test);
		// Assert
		assertEquals(3, test);
	}

	@Test
	public void Bitwise_turnOffLSBtest() {
		// Arrange
		char test = 1;
		// Act
		test = Bitwise.turnOffLSB(test);
		// Assert
		assertEquals(0, test);

	}

	@Test
	public void Bitwise_statusOfLSBtest() {
		// Arrange
		char test1 = 1;
		char test2 = 3;
		char test3 = 149;
		char test4 = 157;
		char test5 = 2;
		char test6 = 64;
		char test7 = 82;
		char test8 = 12;

		// Act
		// Assert
		assertEquals(true, Bitwise.statusOfLSB(test1));
		assertEquals(true, Bitwise.statusOfLSB(test2));
		assertEquals(true, Bitwise.statusOfLSB(test3));
		assertEquals(true, Bitwise.statusOfLSB(test4));
		assertEquals(false, Bitwise.statusOfLSB(test5));
		assertEquals(false, Bitwise.statusOfLSB(test6));
		assertEquals(false, Bitwise.statusOfLSB(test7));
		assertEquals(false, Bitwise.statusOfLSB(test8));
	}

	@Test
	public void Bitwise_statusOfBitAtIndexTest() {
		// Arrange
		char test1 = 1;
		char test2 = 3;
		char test3 = 149;
		char test4 = 157;
		char test5 = 2;
		char test6 = 64;
		char test7 = 82;
		char test8 = 12;

		// Act
		// Assert
		assertEquals(false, Bitwise.statusOfBitAtIndex(test1, 3));
		assertEquals(true, Bitwise.statusOfBitAtIndex(test2, 2));
		assertEquals(false, Bitwise.statusOfBitAtIndex(test3, 4));
		assertEquals(true, Bitwise.statusOfBitAtIndex(test4, 4));
		assertEquals(false, Bitwise.statusOfBitAtIndex(test5, 1));
		assertEquals(true, Bitwise.statusOfBitAtIndex(test6, 7));
		assertEquals(false, Bitwise.statusOfBitAtIndex(test7, 1));
		assertEquals(true, Bitwise.statusOfBitAtIndex(test8, 3));

	}

	@Test
	public void Bitwise_encodeBitTest() {
		// Arrange
		char s = 'S';

		char[] chars = { 1, 3, 149, 157, 2, 64, 82, 12 };

		// Act
		int j = 0;
		for (int i = 8; i > 0; i--) {
			chars[j] = Bitwise.encodeBit(s, chars[j], i);
			j++;
		}

		// Assert
		assertEquals(false, Bitwise.statusOfLSB(chars[0]));
		assertEquals(true, Bitwise.statusOfLSB(chars[1]));
		assertEquals(false, Bitwise.statusOfLSB(chars[2]));
		assertEquals(true, Bitwise.statusOfLSB(chars[3]));
		assertEquals(false, Bitwise.statusOfLSB(chars[4]));
		assertEquals(false, Bitwise.statusOfLSB(chars[5]));
		assertEquals(true, Bitwise.statusOfLSB(chars[6]));
		assertEquals(true, Bitwise.statusOfLSB(chars[7]));
	}

	@Test
	public void PPMImageEx_messageTest() {
		// Arrange
		String message = "Hello, my name is !$M% (^_>^)%875%(^<_^) !@%$";
		String result;
		PPMImageEx ppm = new PPMImageEx(new File("dog.ppm"));
		// Act
		ppm.hideMessage(message);
		result = ppm.recoverMessage();
		
		System.out.println(message);
		
		System.out.println(result);
		
		// Arrange
		message = message.concat("\0"); //account for terminating char
		assertEquals(result, message);
	}

}
