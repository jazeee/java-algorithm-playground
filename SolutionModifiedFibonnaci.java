import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class SolutionModifiedFibonnaci {

	public static BigInteger nextModifiedFibonacci(BigInteger tMinus2, BigInteger tMinus1) {
		return tMinus2.add(tMinus1.multiply(tMinus1));
	}

	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		if (args.length == 1) {
			inputStream = new FileInputStream(new File(args[0]));
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		Integer testCaseCount = 1;
		for (int test = 0; test < testCaseCount; test++) {
			String[] parameters = br.readLine().split(" ");
			BigInteger t1 = new BigInteger(parameters[0]);
			BigInteger t2 = new BigInteger(parameters[1]);
			int n = Integer.parseInt(parameters[2]);
			int startIndex = 2;
			BigInteger tMinus2 = t1;
			BigInteger tMinus1 = t2;
			BigInteger t = null;
			for (int i = startIndex; i < n; i++) {
				t = nextModifiedFibonacci(tMinus2, tMinus1);
				tMinus2 = tMinus1;
				tMinus1 = t;
			}
			System.out.println(t.toString());
		}
	}
}