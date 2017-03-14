package it.caldesi.commandlineserverdb;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.h2.jdbc.JdbcSQLException;

import it.caldesi.commandlineserverdb.database.ServerServiceH2;

public class Main {

	// Long options
	private static final String OPTION_CLEAR_LONG = "clear";
	private static final String OPTION_LIST_LONG = "list";
	private static final String OPTION_COUNT_LONG = "count";
	private static final String OPTION_DELETE_LONG = "delete";
	private static final String OPTION_EDIT_LONG = "edit";
	private static final String OPTION_ADD_LONG = "add";
	private static final String OPTION_HELP_LONG = "help";

	// short options
	private static final String OPTION_HELP = "h";
	private static final String OPTION_CLEAR = "x";
	private static final String OPTION_LIST = "l";
	private static final String OPTION_COUNT = "c";
	private static final String OPTION_DELETE = "d";
	private static final String OPTION_EDIT = "e";
	private static final String OPTION_ADD = "a";

	public static void main(String[] args) {
		int exitStatus = 0;
		Options options = initOptions();

		// create the parser
		CommandLineParser parser = new BasicParser();
		ServerServiceH2 serverServiceH2 = new ServerServiceH2();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (hasNoKnownOptions(line)) {
				System.err.println("Unknown option");
				printHelp(options);
				exitStatus = -1;
			} else {
				// Lazy table creation
				serverServiceH2.createServerTableIfNotExists();
				handleOptions(options, serverServiceH2, line);
			}

		} catch (JdbcSQLException jdbcSQLException) {
			if (jdbcSQLException.getMessage().contains("Unique ")) {
				System.err.println("Id already exists!");
				exitStatus = -2;
			}
		} catch (ParseException pe) {
			System.err.println("Parsing failed.  Reason: " + pe.getMessage());
			exitStatus = -2;
		} catch (Exception e) {
			e.printStackTrace();
			exitStatus = -1;
		} finally {
			serverServiceH2.connectionClose();
			System.exit(exitStatus);
		}

	}

	private static void handleOptions(Options options, ServerServiceH2 serverServiceH2, CommandLine line)
			throws Exception {
		// permits multiple (different) options, but with fixed
		// execution order (it does not depends on the order of
		// arguments provided)
		if (line.hasOption(OPTION_CLEAR)) {
			serverServiceH2.clearTable();
			System.out.println("Server records deleted");
		}
		if (line.hasOption(OPTION_DELETE)) {
			String[] deleteArgs = line.getOptionValues(OPTION_DELETE);
			serverServiceH2.delete(Integer.parseInt(deleteArgs[0]));
			System.out.println("Server deleted");
		}

		if (line.hasOption(OPTION_ADD)) {
			String[] addArgs = line.getOptionValues(OPTION_ADD);
			serverServiceH2.insert(Integer.parseInt(addArgs[0]), addArgs[1], addArgs[2]);
			System.out.println("Server added");
		}
		if (line.hasOption(OPTION_EDIT)) {
			String[] editArgs = line.getOptionValues(OPTION_EDIT);
			serverServiceH2.update(Integer.parseInt(editArgs[0]), editArgs[1], editArgs[2]);
			System.out.println("Server edited");
		}

		if (line.hasOption(OPTION_COUNT)) {
			serverServiceH2.count();
		}
		if (line.hasOption(OPTION_LIST)) {
			serverServiceH2.list();
		}

		if (line.hasOption(OPTION_HELP)) {
			printHelp(options);
		}
	}

	private static boolean hasNoKnownOptions(CommandLine line) {
		return !line.hasOption(OPTION_HELP) && !line.hasOption(OPTION_ADD) && !line.hasOption(OPTION_CLEAR)
				&& !line.hasOption(OPTION_LIST) && !line.hasOption(OPTION_COUNT) && !line.hasOption(OPTION_DELETE)
				&& !line.hasOption(OPTION_EDIT);
	}

	/**
	 * Automatically generate the help statement
	 * 
	 * @param options
	 *            Options
	 * 
	 */
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(" ", options);
	}

	/**
	 * Initialize command line options
	 */
	@SuppressWarnings("static-access")
	private static Options initOptions() {
		Options options = new Options();

		// 1) Provide help / usage info
		Option help = new Option(OPTION_HELP, OPTION_HELP_LONG, false, "Print this help message");
		options.addOption(help);

		// 2) Add Server (id, name, description)
		Option add = OptionBuilder //
				.withArgName("id> <name> <description") //
				.withValueSeparator(' ') //
				.hasArgs(3) //
				.withDescription("Add a new server") //
				.withLongOpt(OPTION_ADD_LONG) //
				.create(OPTION_ADD);
		options.addOption(add);

		// 3) Edit Server (name, description)
		Option edit = OptionBuilder //
				.withArgName("id> <name> <description") //
				.withValueSeparator(' ') //
				.hasArgs(3) //
				.withDescription("Edit a server given the id") //
				.withLongOpt(OPTION_EDIT_LONG) //
				.create(OPTION_EDIT);
		options.addOption(edit);

		// 4) Delete Server (id)
		Option delete = OptionBuilder //
				.withArgName("id") //
				.withValueSeparator(' ') //
				.hasArgs(1) //
				.withDescription("Delete a server given the id") //
				.withLongOpt(OPTION_DELETE_LONG) //
				.create(OPTION_DELETE);
		options.addOption(delete);

		// 5) Count Number of Servers
		Option count = new Option(OPTION_COUNT, OPTION_COUNT_LONG, false, "Return the number of servers");
		options.addOption(count);

		// 6) List Servers
		Option list = new Option(OPTION_LIST, OPTION_LIST_LONG, false, "List all servers");
		options.addOption(list);

		// clear table
		Option clear = new Option(OPTION_CLEAR, OPTION_CLEAR_LONG, false, "Removes all records");
		options.addOption(clear);

		return options;
	}

}
