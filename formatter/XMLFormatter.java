package com.testing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLFormatter {

	static void format(File file, boolean verbose) throws IOException {

		File outfile = File.createTempFile("output_", ".xml", file.getParentFile());
		PrintWriter pw = new PrintWriter(outfile);
		Scanner scan = new Scanner(file);
		scan.useDelimiter("\n");
		String x = "";
		String firstPassOutput = "";
		while (scan.hasNext()) {
			if ("".equals(x))
				x = scan.nextLine();
			else
				x = x + scan.nextLine();
			if (!x.endsWith(">") || !x.endsWith("/>")) {
				if (verbose)
					System.out.println("XX was " + x);
				continue;
			}
			if (verbose)
				System.out.println("But x was " + x);
			firstPassOutput += makeLinesFirstPass(x);
			x = "";
		}
		if (!"".equals(x))
			firstPassOutput += makeLinesFirstPass(x);

		pw.write(makeLinesSecondPass(firstPassOutput, verbose));
		pw.flush();
		pw.close();
		scan.close();
	}

	private static String makeLinesSecondPass(String next, boolean verbose) {
		Scanner scan = new Scanner(next);
		String result = "";
		while (scan.hasNextLine()) {
			Pattern p = Pattern.compile("<.*?>|(?!<>)[0-9a-zA-z _\\.\\-:\\+]*");
			String nextLine = scan.nextLine();
			Matcher thisMatcher = p.matcher(nextLine);
			boolean alreadyFound = false;
			while (alreadyFound || thisMatcher.find()) {
				if (thisMatcher.group().length() >= 1) {
					String thisToken = thisMatcher.group();
					alreadyFound = false;
					if (thisToken.startsWith("<") && thisToken.endsWith("/>")) {
						result += thisToken + "\n";
						continue;
					} else if (thisToken.matches("<.*>")) {
						// Get the first word
						String firstWord = getFirstWord(thisToken);
						// Get the next line
						if (thisMatcher.find()) {
							String nextNextLine = thisMatcher.group();
							if (nextNextLine.matches("(?!<>)[0-9a-zA-z _\\.\\-:\\+]*")) {
								// For text content only
								result += thisToken;
								result += nextNextLine;

								// close XML
								if (thisMatcher.find()) {
									result += thisMatcher.group();
								}
								if (verbose)
									System.out.println("1 token is " + thisToken + nextNextLine + thisMatcher.group());
								result += "\n";
							} else if (nextNextLine.contains("/" + firstWord)) {
								// For <Message></Message>
								result += thisToken;
								result += nextNextLine;
								if (verbose)
									System.out.println("2 token is " + thisToken + nextNextLine);
								result += "\n";
							} else {
								// Completely different, don't search
								result += thisToken;
								if (verbose)
									System.out.println("3 token is " + thisToken);
								result += "\n";
								alreadyFound = true;
								continue;
							}
						}
					}
				}
			}
		}
		scan.close();
		return result;
	}

	private static String getFirstWord(String thisToken) {
		if (thisToken.contains(" "))
			return thisToken.substring(1, thisToken.indexOf(" "));
		else
			return thisToken.substring(1, thisToken.length() - 1);
	}

	private static String makeLinesFirstPass(String next) {
		String x = next;
		x = "";
		Pattern p = Pattern.compile(".*?/>");
		Matcher m = p.matcher(next);
		if (!m.find()) {
			x = x + next;
		} else {
			m.reset();
			while (m.find()) {
				x += m.group();
				x += "\n";
			}
			if (next.contains("/>") && (next.lastIndexOf("/>") + 1 != next.length() - 1)) {
				x += next.substring(next.lastIndexOf("/>") + 2, next.length());
			}
		}
		return x;
	}

	public static void main(String[] args) throws IOException {

		format(new File(args[0]), false);
	}

}
