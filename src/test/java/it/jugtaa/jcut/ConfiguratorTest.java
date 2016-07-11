package it.jugtaa.jcut;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Configuration Test Suite.
 *
 */
public class ConfiguratorTest {

    @Test
    public void changeSeparatorTest() {

        String[] opts = new String[]{"-fs", ";", "data/data01.csv"};

        Configurator c = new Configurator(opts);

        assertTrue("Field separator should be ;", ";".equals("" + c.getFieldSeparator()));

    }

    @Test
    public void checkDataFileNameTest() {

        String[] opts = new String[]{"data/data01.csv"};

        Configurator c = new Configurator(opts);

        assertTrue("Should set data file name: data/data01.csv",
                (opts[0]).equals(c.getDataFileName()));

    }

}
