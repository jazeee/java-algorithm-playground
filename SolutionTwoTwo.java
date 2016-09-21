import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//test cases:
//2^63 9223372036854775808
//2^100 1267650600228229401496703205376
/**
 * Input (stdin) 2 9223372036854775808 1267650600228229401496703205376 Your Output 8 13
 **/
public class SolutionTwoTwo {
	private static final int LEN = 801;

	static class BitArray {
		boolean[] bits;
		boolean isOverflow = false;
		boolean isInitializedWithOdd = false;

		public BitArray(boolean[] bits) {
			this.bits = bits;
		}

		public BitArray(char digit) {
			bits = new boolean[LEN];
			switch (digit) {
			case '0':
				break;
			case '1':
				bits[LEN - 1] = true;
				isInitializedWithOdd = true;
				break;
			case '2':
				bits[LEN - 2] = true;
				break;
			case '3':
				bits[LEN - 1] = true;
				bits[LEN - 2] = true;
				isInitializedWithOdd = true;
				break;
			case '4':
				bits[LEN - 3] = true;
				break;
			case '5':
				bits[LEN - 1] = true;
				bits[LEN - 3] = true;
				isInitializedWithOdd = true;
				break;
			case '6':
				bits[LEN - 2] = true;
				bits[LEN - 3] = true;
				break;
			case '7':
				bits[LEN - 1] = true;
				bits[LEN - 2] = true;
				bits[LEN - 3] = true;
				isInitializedWithOdd = true;
				break;
			case '8':
				bits[LEN - 4] = true;
				break;
			case '9':
				bits[LEN - 1] = true;
				bits[LEN - 4] = true;
				isInitializedWithOdd = true;
				break;
			default:
				throw new IllegalArgumentException("0-9 only");
			}
		}

		public boolean multiplyByTen() {
			// System.out.print(this + " x ten = ");
			isOverflow |= timesTen(bits);
			// System.out.println(this);
			return isOverflow;
		}

		public boolean add(BitArray bitArray) {
			boolean[] operandBits = bitArray.bits;
			isOverflow = add(bits, operandBits);
			if (bitArray.isOverflow) {
				isOverflow = true;
			}
			this.isOverflow |= isOverflow;
			return this.isOverflow;
		}

		public static boolean timesTen(boolean[] bits) {
			boolean[] bitsTimesTwo = new boolean[LEN];
			for (int i = 0; i < LEN; ++i) {
				boolean bit = bits[i];
				if (i < 3) {
					if (bit) {
						// Overflow
						return true;
					}
				} else {
					if (i < LEN) {
						bits[i - 3] = bit;
					}
				}
				if (i >= 1) {
					bitsTimesTwo[i - 1] = bit;
				}
			}
			bits[LEN - 3] = false;
			bits[LEN - 2] = false;
			bits[LEN - 1] = false;
			return add(bits, bitsTimesTwo);

		}

		public static boolean add(boolean[] leftBits, boolean[] rightBits) {
			boolean carry = false;
			boolean isOverflow = false;
			for (int i = LEN - 1; i >= 0; --i) {
				int bitCount = carry ? 1 : 0;
				if (rightBits[i]) {
					bitCount++;
				}
				if (leftBits[i]) {
					bitCount++;
				}
				if (bitCount == 2 || bitCount == 3) {
					leftBits[i] = (bitCount == 3);
					carry = true;
				} else {
					leftBits[i] = (bitCount == 1);
					carry = false;
				}
			}
			if (carry) {
				isOverflow = true;
			}
			return isOverflow;
		}

		public boolean isPowerOfTwoAndNotOverflow() {
			if (isOverflow) {
				return false;
			}
			// Should only have one bit turned on.
			boolean foundBit = false;
			for (int i = 0; i < LEN; i++) {
				if (bits[i]) {
					if (foundBit) {
						return false;
					}
					foundBit = true;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			boolean foundBit = false;
			String out = "";
			for (int i = 0; i < LEN; i++) {
				if (bits[i]) {
					foundBit = true;
				}
				if (foundBit) {
					out += bits[i] ? "1" : "0";
				}
			}
			if (out.isEmpty()) {
				out = "0";
			}
			return out;
		}
	}

	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		boolean isReadingFile = false;
		if (args.length == 1) {
			inputStream = new FileInputStream(new File(args[0]));
			isReadingFile = true;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		Integer testCaseCount = Integer.parseInt(br.readLine());
		long startTime = System.nanoTime();
		for (int testCase = 0; testCase < testCaseCount; testCase++) {
			long powerOfTwoCount = 0;
			String line = br.readLine();
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == '0') {
					continue;
				}
				BitArray bitArray = new BitArray(line.charAt(i));
				if (bitArray.isPowerOfTwoAndNotOverflow()) {
					powerOfTwoCount++;
				}
				for (int j = i + 1; j < line.length(); j++) {
					BitArray nextBitArray = new BitArray(line.charAt(j));
					if (bitArray.multiplyByTen()) {
						break;
					}
					if (bitArray.add(nextBitArray)) {
						break;
					}
					if (nextBitArray.isInitializedWithOdd) {
						continue;
					}
					if (bitArray.isPowerOfTwoAndNotOverflow()) {
						// System.out.println(testDigits + " " + bitArray +" ");
						// System.out.print(true);
						powerOfTwoCount++;
					}
					// System.out.println();
				}
			}
			System.out.println(powerOfTwoCount);
		}
		if (isReadingFile) {
			System.out.println("Process time (msec): " + (System.nanoTime() - startTime) / 1000_000l);
		}
	}
}
// 126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376
// 126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376126765060022822940149670320537612676506002282294014967032053761267650600228229401496703205376