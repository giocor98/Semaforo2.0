package src.utils.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.utils.exception.NotSuchPropertiesException;
import src.utils.exception.PropertyLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Class SINGLETON managing the configuration files.
 */
public class AppProperty {

    private static final Logger logger = LogManager.getLogger("properties");

    /**
     * The singleton instance.
     */
    private static AppProperty actual;

    /**
     * The main properties to be loaded.
     */
    private Properties appProperties;

    private Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    private List<String> propertiesList;

    /**
     * <p>
     * Factory method to build the instance.
     * </p><p>
     * Returns true iif the instance has been built in a successful way.
     * </p>
     *
     * @return (false if any problem occurs).
     */
    public static boolean build(){
        if (actual != null){
            logger.info("actual is already set.");
            return true;
        }

        try {
            new AppProperty();
        } catch (IOException e) {
            logger.error("Cannot load the AppProperty.");
            e.printStackTrace();
            return false;
        }
        logger.trace("AppProperty properly read.");

        if(!buildPropertiesMap()){
            logger.fatal("Wrongly configured Properties into the JAR.");
            actual = null;
            return false;
        }

        return true;
    }

    public static String getProperty(String propertyName) throws NotSuchPropertiesException, PropertyLoadException {
        System.out.println(propertyName);
        System.out.println(propertyName.replace('.', ' '));
        System.out.println(Arrays.toString(propertyName.replace('.', ' ').split(" ")));
        String propertiesName = propertyName.replace('.', ' ').split(" ")[0];
        Properties properties;

        if(!actual.propertiesList.contains(propertiesName))
            throw new NotSuchPropertiesException();

        properties = actual.propertiesMap.get(propertiesName);
        if(properties == null){
            if (actual.appProperties.getProperty("app.Properties." + propertiesName + "override")!=null)
                throw new PropertyLoadException();

            try {
                properties = actual.loadResourceProperties(actual.appProperties.getProperty("app.Properties." + propertiesName + ".from"));
            } catch (IOException e) {
                throw new PropertyLoadException();
            }

            actual.propertiesMap.put(propertiesName, properties);
        }
        return properties.getProperty(propertyName);
    }

    private static boolean buildPropertiesMap(){

        if(actual==null)
            return false;

        actual.propertiesList = new ArrayList<>(Arrays.asList(actual.appProperties.getProperty("app.Properties.List").split(" ")));
        actual.propertiesMap.put("app", actual.appProperties);

        for(String property: actual.propertiesList){
            try (InputStream inputStream = AppProperty.class.getClassLoader().getResourceAsStream(actual.appProperties.getProperty("app.Properties." + property + ".from"))){
                actual.propertiesMap.put(property, null);
                logger.trace(property + ":\tWell configured.");
            } catch (IOException e) {
                logger.error(property + ":\tWrongly configured.");
                e.printStackTrace();
                return false;
            }
        }
        actual.propertiesList.add("app");
        return true;
    }

    private AppProperty() throws IOException {

        appProperties = loadResourceProperties("config/appConfig.properties");

        actual = this;
    }

    private Properties loadResourceProperties(String fileName) throws IOException {
        Properties ret = new Properties();
        try(InputStream input = AppProperty.class.getClassLoader().getResourceAsStream(fileName)){
            if(input==null) {
                System.out.println("Unable to find the file: " + fileName);
                return null;
            }
            ret.load(input);
        }catch (IOException e){
            e.printStackTrace();
        }

        return ret;
    }

}
