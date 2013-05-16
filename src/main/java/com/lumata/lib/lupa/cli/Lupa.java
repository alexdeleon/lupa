/*
 * 2013 copyright Lumata
 */
package com.lumata.lib.lupa.cli;

import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lumata.lib.lupa.Scraper;

/**
 * Entry point for command line interface for Lupa.
 * 
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Lupa {

	private static final Logger LOG = LoggerFactory.getLogger(Lupa.class);

	private final Options options;

	public static void main(String[] args) {
		Lupa lupa = new Lupa();
		lupa.run(args);
	}

	private Lupa() {
		options = createOptions();
	}

	private Options createOptions() {
		Options options = new Options();
		options.addOption("h", false, "Print help");
		return options;
	}

	public final void run(String[] args) {
		try {
			CommandLine line = parseCommandLine(args);
			int result = execute(line);
			System.exit(result);
		} catch (ParseException exp) {
			printUsage();
		} catch (Throwable exp) {
			LOG.error("Unexpected error", exp);
		}
	}

	private int execute(CommandLine line) {
		if (line.hasOption("h") || line.getArgList().isEmpty()) {
			printUsage();
		} else {
			Scraper scraper = loadStringContextAndGetScraper();
			String url = (String) line.getArgList().get(0);
			LOG.debug("Executing scraper for URL {}", url);
			try {
				// TODO: serialize content to JSON or other specified formats
				PrintStream out = new PrintStream(System.out, true, "UTF-8");
				out.println(scraper.scrapContent(url));
			} catch (Exception e) {
				LOG.error("Scraper failed to extract content for {}", url, e);
				return 1;
			}
		}
		return 0;
	}

	private Scraper loadStringContextAndGetScraper() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"com/lumata/lib/lupa/webscraper-config.xml");
		return context.getBean(Scraper.class);
	}

	private CommandLine parseCommandLine(String[] args) throws ParseException {
		CommandLineParser parser = new GnuParser();
		return parser.parse(options, args);
	}

	public void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("lupa URL", options, true);
	}

}
