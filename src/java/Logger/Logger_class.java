package Logger;

import java.io.File;
import java.util.logging.*;

public class Logger_class {
    
    private static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Logger_class() {
        try{
            FileHandler fh = new FileHandler(File.separator+"logging"+File.separator+"OnlineShopping.log", true);
            SimpleFormatter simple = new SimpleFormatter();
            fh.setFormatter(simple);
            fh.setLevel(Level.INFO);
            LOG.addHandler(fh);
            LOG.info("Started logging");
        }catch (Exception e){
            LOG.severe("Failed to setup logger FileHandler due to error: " + e);
        }
    }
}
