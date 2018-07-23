/*
 *  Copyright (C) 2010-2018 Buvaneshwaran T
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

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

	/**
	 * This method returns the contents of a file in list form.
	 * 
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}

	/**
	 * Sets the file path of the input text file. Also, stores the clean contents of the file in a list data structure
	 * @param string the file path along with the file name
	 */
	public void setFile(String string) {
		try {
			file = new Scanner(new File(string));
			storeCleanContentsInList(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Prints the contents of the list data structure to the console.
	 * Removes commas, removes the right bracket, removes the left bracket, 
	 * and removes trailing spaces from partially initialized arrays.
	 */
	public void printToConsole() {
		String formattedString = list.toString().replace(", ", "\n") // remove the commas
				.replace("[", "") // remove the right bracket
				.replace("]", "") // remove the left bracket
				.trim(); // remove trailing spaces from partially initialized arrays

		System.out.println(formattedString);
	}

	/**
	 * This method takes in a string that contains the desired name for the output text file, 
	 * and it stores the contents of the list into the output file.
	 * @param string the name for the output text file
	 * @throws FileNotFoundException
	 */
	public void printToTextFile(String string) throws FileNotFoundException {

		String formattedString = list.toString().replace(", ", "\n") // remove the commas
				.replace("[", "") // remove the right bracket
				.replace("]", "") // remove the left bracket
				.trim(); // remove trailing spaces from partially initialized arrays

		PrintWriter out = new PrintWriter(string);
		out.println(formattedString);
		out.close();

	}

	/**
	 * This method contains the main logic of the program. As long as there are loops, 
	 * this method calculates the correct intervals for the opening and closing loops. 
	 * It inserts this sublist into the appropriate position in the list. It inserts
	 * the sublist the appropriate number of times in the appropriate location as 
	 * determined by the loop iterator value. This function also removes the opening loop
	 * and closing loop commands. As long as there are more loops to be considered, 
	 * this function repeats iteratively. 
	 * @throws Exception
	 */
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

	/**
	 * For each call, this function will return the appropriate opening loop and closing loop indexes.
	 * @return
	 */
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
	 * For each call, this function will return the number of loop bodies remaining.
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
	 * This method will check to see if each opening loop command has a matching end loop command
	 * @return true if there is matching loop blocks, false if the loops are not matching.
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

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		CommandParser parser = new CommandParser();
		parser.setFile("C:\\Users\\Buvan\\Downloads\\NestedLoopExample.txt");
		parser.doProcessing();
		parser.printToConsole();
		parser.printToTextFile("output.txt");

	}

}
