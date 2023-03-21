package ua.eddict;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DictionaryParser {

	private static final String SEPARATOR = " - ";

	public Map<String, String> parse(Path path) {
		System.out.println("Dictionary will be parsed from file: " + path);
		return parseDictionaryFile(path);
	}

	private Map<String, String> parseDictionaryFile(Path path) {
		Map<String, String> dict = new HashMap<>();
		List<String> linesUnparsed = new ArrayList<>();
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				if (!line.contains(SEPARATOR)) {
					linesUnparsed.add(line);
				} else {
					String[] entry = line.split(" - ");
					String key = entry[0];
					String value = entry[1];
					dict.put(key, value.trim());
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (linesUnparsed.size() > 0) {
			System.err.println("Next lines cannot be parsed (wrong format): " + linesUnparsed);
		}
		return dict;
	}

}