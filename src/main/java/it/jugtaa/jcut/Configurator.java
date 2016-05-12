package it.jugtaa.jcut;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author alexmario74
 *
 * Questa classe ha il compito di gestire la configurazione per l'applicazione.
 *
 */
public class Configurator {

    private static final Logger log = LogManager.getLogger(Configurator.class);

    // Riga di output dell'help del comando
    private static final String SCRIPT_NAME = "jcut [OPTIONS] <file name>";

    // Valore di default del carattere di separatore dei campi del file csv
    private static final String DEFAULT_FIELD_SEPARATOR = ",";

    // Carattere di separatore dei campi del file csv
    private char fieldSeparator;

    // Nome del file da elaborare
    private String dataFileName = null;

    // Flag che indica se il debug deve essere attivo.
    // Qualora assumesse il valore true il programma produce anche un log dell'esecuzione.
    private boolean debug = false;

    // Oggetto che contiene le opzioni impostate da riga di comando
    private static Options opts = null;

    // Opzione di help
    private static final String OPT_NAME_HELP      = "h";
    private static final String OPT_NAME_HELP_LONG = "help";
    private static final String HELP_DESC          = "Print this help and exit";

    // Opzione di debug
    private static final String OPT_NAME_DEBUG     = "debug";
    private static final String DEBUG_DESC         = "Print out the debug infos";

    // Opzione di out che indica il nome di un file dove salvare l'output
    private static final String OPT_NAME_OUTPUT_FILE      = "o";
    private static final String OPT_NAME_OUTPUT_FILE_LONG = "out";
    private static final String OUTPUT_FILE_DESC          = "The name of the file where the output will be saved";

    private static final String OPT_NAME_FIELD_SEPARATOR      = "fs";
    private static final String OPT_NAME_FIELD_SEPARATOR_LONG = "field-separator";
    private static final String FIELD_SEPARATOR_DESC          = "The character used as the field separator, default value is ,";

    /**
     * Costruttore, si occupa di definire le opzioni e di fare il parsing della riga di comando
     *
     * @param options
     */
    public Configurator(String[] options) {

        opts = new Options();

        opts.addOption(OPT_NAME_HELP, OPT_NAME_HELP_LONG, false, HELP_DESC);

        opts.addOption(Option.builder(OPT_NAME_FIELD_SEPARATOR)
                .argName(OPT_NAME_FIELD_SEPARATOR)
                .longOpt(OPT_NAME_FIELD_SEPARATOR_LONG)
                .hasArg(true)
                .optionalArg(true)
                .valueSeparator()
                .desc(FIELD_SEPARATOR_DESC).build());

        opts.addOption(Option.builder(OPT_NAME_DEBUG)
                .argName(OPT_NAME_DEBUG)
                .longOpt(OPT_NAME_DEBUG)
                .hasArg(false)
                .optionalArg(true)
                .valueSeparator()
                .desc(DEBUG_DESC).build());

        if (!"".equals(options)) {
            parse(options);
        }

    }

    /**
     * Si occupa di effettuare il parsing delle opzioni a partire dalla riga di comando.
     * In caso di errore stampa l'help ed esce.
     *
     * @param args array di stringhe che corrisponde alla riga di comando
     */
    public void parse(String[] args) {

        CommandLineParser parser =  new DefaultParser();

        try {

            CommandLine line = parser.parse(opts, args, true);

            if (line.hasOption(OPT_NAME_DEBUG)) {
                debug = true;
                if (log.isInfoEnabled()) {
                    log.info("enable debug");
                }
            }

            if (line.hasOption(OPT_NAME_FIELD_SEPARATOR)) {
                fieldSeparator = line.getOptionValue(OPT_NAME_FIELD_SEPARATOR, DEFAULT_FIELD_SEPARATOR).charAt(0);
                if (log.isInfoEnabled()) {
                    log.info("field separator {} value {}", OPT_NAME_FIELD_SEPARATOR, fieldSeparator);
                }
            } else if(line.hasOption(OPT_NAME_FIELD_SEPARATOR_LONG)) {
                fieldSeparator = line.getOptionValue(OPT_NAME_FIELD_SEPARATOR_LONG, DEFAULT_FIELD_SEPARATOR).charAt(0);
                if (log.isInfoEnabled()){
                    log.info("field separator {} value {} ", OPT_NAME_FIELD_SEPARATOR_LONG, fieldSeparator);
                }
            } else {
                fieldSeparator = DEFAULT_FIELD_SEPARATOR.charAt(0);
            }

            if (line.hasOption(OPT_NAME_HELP)) {
                help();
                log.warn("Exit after help");
                System.exit(0);
            }

            if (line.hasOption(OPT_NAME_HELP_LONG)) {
                help();
                log.warn("Exit after help");
                System.exit(0);
            }

            String[] files = line.getArgs();

            if (files.length > 0) {

                if (log.isInfoEnabled()) {
                    log.info("Extra args {}", files[0]);
                }

                dataFileName = files[0];
                File f = new File(dataFileName);

                if (!f.exists()) {

                    throw new Exception(String.format("File %s does not exists", dataFileName));

                } else {
                    if (log.isInfoEnabled()) {
                        log.info(" File exists!! {}", dataFileName);
                    }
                }

            } else {
                throw new Exception("No data file provided");
            }

        } catch(Exception e) {

            log.error(e.getMessage());

            help();

            System.exit(-1);

        }

    }

    /**
     * Stampa l'help
     *
     * @return
     */
    public static String help() {

        HelpFormatter frmt = new HelpFormatter();

        frmt.printHelp(SCRIPT_NAME, opts);

        return "";

    }

    public char getFieldSeparator() {
        return fieldSeparator;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public boolean getDebug() { return debug; }

}
