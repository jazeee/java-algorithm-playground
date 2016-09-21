import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SolutionPangram {

	public static void main(String[] args) throws IOException {
		Pattern pattern = Pattern.compile("[A-Z]");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = in.readLine();
		Map<String, Long> characterFrequency = Arrays.stream(input.toUpperCase().split("")).collect(Collectors.groupingBy(c -> c, Collectors.counting()));
		long letterCount = characterFrequency.keySet().stream().map(s -> s).filter(s -> pattern.matcher(s).matches()).count();
		System.out.println((letterCount == 26 ? "" : "not ") + "pangram");
	}
}