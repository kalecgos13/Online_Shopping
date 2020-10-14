package Logger;

import java.util.logging.*;

public class Logger_class {
    
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Logger_class() {
        try{
            FileHandler fh = new FileHandler("~..\\..\\..\\Logging\\logger.log", true);
            SimpleFormatter simple = new SimpleFormatter();
            fh.setFormatter(simple);
            fh.setLevel(Level.INFO);
            LOG.addHandler(fh);
        }catch (Exception e){
            LOG.severe("Failed to setup logger FileHandler");
        }
    }
}
