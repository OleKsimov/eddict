package ua.eddict;

import java.io.Console;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Eddict {

	private static final Console console = System.console();
	private static final DictionaryParser dictionaryParser = new DictionaryParser();

	public static void main(String[] args) {
		Eddict eddict = new Eddict();
		eddict.start();
	}

	public void start() {
		File jarFile = new File(getClassPath());
		Path dictPath = Path.of(jarFile.getParentFile().getPath(), "dict.txt");
		Map<String, String> dictionary = dictionaryParser.parse(dictPath);
		if (dictionary.size() == 0) {
			System.err.println("No words with \"word - definition\" pattern found in dictionary!");
			return;
		}

		List<Entry<String,String>> wordsMain = new ArrayList<>(dictionary.entrySet());
		Collections.shuffle(wordsMain);
		
		int wordsAmount = askWordsNumberToTrain();
		wordsAmount = Math.min(wordsAmount, wordsMain.size());

		List<Entry<String,String>> wordsMissed = showWords(wordsMain, wordsAmount);
		while (wordsMissed.size() > 0) {
			wordsAmount += wordsMissed.size();
			System.out.println("-----------");
			System.out.println("Let's practice some words again: ");
			wordsMissed = showWords(wordsMissed);
		}

		System.out.println("-----------");
		System.out.println("Today you've practiced " + wordsAmount + " words.");
	}


	private List<Entry<String,String>> showWords(List<Entry<String,String>> words) {
		return showWords(words, words.size());
	}

	private List<Entry<String,String>> showWords(List<Entry<String,String>> words, int wordsToPracticeAmount) {
		if (words.size() == 0 || wordsToPracticeAmount == 0) {
			return Collections.emptyList();
		}

		int step = 0;
		List<Entry<String,String>> wordsMissed = new ArrayList<>();
		for (var wordDef : words) {
			step++;
			System.out.print(step + ") " + wordDef.getKey());
			waitAnyInput();
			System.out.println(wordDef.getValue());
			String input = waitAnyInput();
			if (input.equals("-")) {
				wordsMissed.add(wordDef);
			}
			if (step == wordsToPracticeAmount) {
				break;
			}
		}
		return wordsMissed;
	}

	private String getClassPath() {
		return this.getClass()
			.getProtectionDomain()
			.getCodeSource()
			.getLocation()
			.getPath();
	} 

	private int askWordsNumberToTrain() {
		System.out.print("Enter amount of words to practice: ");
		String input = console.readLine();
		return Integer.parseInt(input);
	}

	private String waitAnyInput() {
		try {
			return console.readLine();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}