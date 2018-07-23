package com.buvaneshwarant.commandparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandParser {
	Scanner file;

	private List<String> list;

	public CommandParser() {
		super();

	}

	public void setFile(String string) {
		try {
			file = new Scanner(new File(string));
			storeCleanContentsInList(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void doProcessing() throws Exception {

		if (isValidLoopFile()) {

			while (getNumberOfLoopConstructs() != 0) {
				Integer[] openingAndClosingLoopIndices = extractMatchingOpeningAndClosingLoopIndices();
				int openingIndex = openingAndClosingLoopIndices[0];
				int closingIndex = openingAndClosingLoopIndices[1];

				// extract iterator number from loop
				int iteratorNumber = Integer.parseInt(list.get(openingIndex).split(" ")[1]);

				// remove closing bracket
				list.remove(closingIndex);

				// insert N (the iterator number) copies of the loop body at end loop position
				for (int j = 0; j < iteratorNumber - 1; j++) {

					list.add(closingIndex, list.subList(openingIndex + 1, closingIndex).toString());

				}

				list.remove(openingIndex);

			}

		} else {

			throw new Exception("Loop constructs do not match in file");

		}

	}

	public void printToConsole() {
		String formattedString = list.toString().replace(", ", "\n") // remove the commas
                .replace("[", "") // remove the right bracket
                .replace("]", "") // remove the left bracket
                .trim(); // remove trailing spaces from partially initialized arrays
		
		System.out.println(formattedString);
	}

	public void printToTextFile(String string) throws FileNotFoundException {

		String formattedString = list.toString().replace(", ", "\n") // remove the commas
                .replace("[", "") // remove the right bracket
                .replace("]", "") // remove the left bracket
                .trim(); // remove trailing spaces from partially initialized arrays
		
		PrintWriter out = new PrintWriter(string);
		out.println(formattedString);
		out.close();
		
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	private Integer[] extractMatchingOpeningAndClosingLoopIndices() {

		Integer[] openingAndClosingLoopIndices = new Integer[2];

		Map<Integer, Integer> matchingLoopMap = new HashMap<>();

		Stack<Integer> openingLoopStack = new Stack<Integer>();
		Stack<Integer> closingLoopStack = new Stack<Integer>();

		Stack<Integer> tempStack = new Stack<Integer>();

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).contains("loop") && !list.get(i).equals("endloop")) {
				openingLoopStack.push(i);
				tempStack.push(i);
			}

			if (list.get(i).equals("endloop")) {
				closingLoopStack.push(i);
				matchingLoopMap.put(openingLoopStack.pop(), closingLoopStack.pop());
			}

		}

		openingAndClosingLoopIndices[0] = tempStack.pop();
		openingAndClosingLoopIndices[1] = matchingLoopMap.get(openingAndClosingLoopIndices[0]);
		return openingAndClosingLoopIndices;

	}

	/**
	 * 
	 * @return the number of loops commands present in the list data structure
	 */
	private int getNumberOfLoopConstructs() {
		int numberOfLoops = 0;

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).contains("loop") && !list.get(i).equals("endloop")) {
				numberOfLoops++;
			}

		}

		return numberOfLoops;
	}

	/**
	 * 
	 * @return true if there is matching loop blocks, false otherwise
	 */
	private boolean isValidLoopFile() {

		int openingLoopCounter = 0;
		int closingLoopCounter = 0;
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).contains("loop") && !list.get(i).equals("endloop")) {
				openingLoopCounter++;
			}

			if (list.get(i).equals("endloop")) {
				closingLoopCounter++;

			}
		}

		return openingLoopCounter == closingLoopCounter;
	}

	/**
	 * stores file contents without comments and spaces into list data structure.
	 */
	private void storeCleanContentsInList(Scanner tempFile) {
		Scanner file = tempFile;
		list = new LinkedList<>();

		while (file.hasNext()) {
			String line = file.nextLine();
			if (!line.isEmpty() && !line.startsWith("#")) {
				list.add(line);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		CommandParser parser = new CommandParser();
		parser.setFile("C:\\Users\\Buvan\\Downloads\\NestedLoopExample.txt");
		parser.doProcessing();
		parser.printToConsole();
		parser.printToTextFile("output.txt");

		
	}

}
