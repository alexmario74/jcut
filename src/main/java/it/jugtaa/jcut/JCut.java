package it.jugtaa.jcut;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author alexmario74
 *
 * Questa è la classe principale dell'applicazione.
 *
 */
public class JCut {

    private static final Logger log = LogManager.getLogger(JCut.class);

    private Configurator conf = null;

    /**
     * Provvede alle operazioni di inizializzazione della classe.
     * Al momento crea l'oggetto di configurazione dell'applicazione
     * passandogli i parametri necessari.
     *
     * @param args è un array di stringhe che corrisponde ai parametri
     *             passati in input dall'utente.
     */
    private void init(String[] args) {

        conf = new Configurator(args);

        if (conf.getDebug()) {
            log.info("JCut initialized");
        }

    }


    /**
     * Esegue il comando con i paramentri impostati.
     *
     */
    public void execute() {

        if (conf.getDebug()) {
            log.info("Execute jcut ");
        }

        try {

            final CSVParser parse = CSVParser.parse(
                    new File(conf.getDataFileName()), Charset.defaultCharset(),
                    CSVFormat.DEFAULT.withDelimiter(conf.getFieldSeparator()));

            for (Iterator<CSVRecord> it = parse.iterator(); it.hasNext();) {

                CSVRecord r = it.next();

                Object third = r.get(2);
                Object fifth = r.get(4);

                if (third instanceof String && fifth instanceof String) {

                    String sthird = (String) third;
                    String sfifth = (String) fifth;

                    if (sthird.indexOf(conf.getFieldSeparator()) == -1 ||
                            sfifth.indexOf(conf.getFieldSeparator()) == -1) {
                        printRecord(new String[]{ sthird, sfifth});
                    } else {
                        printRecord(new String[]{ "\"" + sthird + "\"", "\"" + sfifth + "\""});
                    }

                }

            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * Stampa il record su standard output su una riga con un carattere di a capo.
     *
     * @param rec un array di stringhe dove ogni elemento corrisponde ad un campo
     *            da stampare su una riga
     */
    private void printRecord(String[] rec) {
        StringBuilder recStr = new StringBuilder();

        for (int i = 0; i < rec.length; i++) {
            if (i != 0) {
                recStr.append(conf.getFieldSeparator());
            }
            recStr.append(rec[i]);
        }

        System.out.println(recStr.toString());
    }

    /**
     * JCut.
     *
     * @param args
     */
    public static void main(String[] args) {

        JCut jcut = new JCut();

        jcut.init(args);

        jcut.execute();

    }

}
