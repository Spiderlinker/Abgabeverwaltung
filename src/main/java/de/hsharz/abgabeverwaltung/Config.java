package de.hsharz.abgabeverwaltung;

import java.io.File;

public class Config {

    public static final String APPLICATION_NAME                = "Abgabenverwaltung";

    public static final File   APPLICATION_FOLDER              = new File(System.getProperty("user.home"), APPLICATION_NAME);

    public static final File   ADDRESS_BOOK_FILE               = new File(APPLICATION_FOLDER.getAbsolutePath(), "addressbook.db");
    public static final File   MODULES_FILE                    = new File(APPLICATION_FOLDER.getAbsolutePath(), "modules.db");

    public static final File   EMAIL_CONFIGURATION_FILE        = new File(APPLICATION_FOLDER.getAbsolutePath(), "email.configuration");
    public static final File   EMAIL_SERVER_CONFIGURATION_FILE = new File(APPLICATION_FOLDER.getAbsolutePath(), "email_server.configuration");

    public static final File   EMAIL_TEMPLATE_FILE             = new File(APPLICATION_FOLDER.getAbsolutePath(), "email_template.txt");

}
