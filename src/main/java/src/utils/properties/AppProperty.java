//package src.utils.properties;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import src.utils.exception.NotSuchPropertiesException;
//import src.utils.exception.PropertyLoadException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
///**
// * Class managing the properties for the whole program,
// * having a reference to the "root" app properties.
// */
//public class AppProperty {
//
//    /**
//     * <p>
//     * <code>Logger</code> for the Class.
//     * </p><p>
//     * It's manually set to write all the information because
//     * this class is called before the configurations are loaded
//     * and parsed, so we can retrieve a complete log even if
//     * something went wrong.
//     * </p>
//     */
//    private static final Logger logger = LogManager.getLogger("properties");
//
//    /**
//     * The properties to be loaded.
//     */
//    private Properties properties;
//
//    /**
//     * Map mapping the <code>AppProperty</code> to its' <code>String</code>.
//     */
//    private Map<String, AppProperty> propertiesMap = new HashMap<String, AppProperty>();
//
//    /**
//     * <code>List</code> of all the AppProperties in this.
//     */
//    private List<String> propertiesList;
//
//    /**
//     * <code>String</code> representing the name of this.
//     */
//    private final String name;
//
//    // TODO: 15/07/20 recomment it
//    /**
//     * <p>
//     * Factory method to build the root.
//     * </p><p>
//     * Returns true iif the instance has been built in a successful way.
//     * </p>
//     *
//     * @return (false if any problem occurs).
//     */
//    public static AppProperty build(){
//        AppProperty appProperty;
//        try {
//            appProperty = new AppProperty();
//        } catch (IOException e) {
//            logger.error(e);
//            logger.error("Cannot load the AppProperty.");
//            return null;
//        }
//        logger.trace("AppProperty properly read.");
//
//        if(!buildPropertiesMap()){
//            logger.fatal("Wrongly configured Properties into the JAR.");
//            return null;
//        }
//
//        return appProperty;
//    }
//
//    public String getProperty(String propertyName) throws NotSuchPropertiesException, PropertyLoadException {
//
//        String propertiesName = propertyName.replace('.', ' ').split(" ")[0];
//        AppProperty properties;
//
//        if(!this.propertiesList.contains(propertiesName))
//            throw new NotSuchPropertiesException();
//
//        properties = this.propertiesMap.get(propertiesName);
//        if(properties == null){
//            if (this.properties.getProperty(this.name + ".Properties." + propertiesName + "override")!=null)
//                throw new PropertyLoadException();
//
//            try {
//                properties = this.loadResourceProperties(this.properties.getProperty(name + ".Properties." + propertiesName + ".from"));
//            } catch (IOException e) {
//                throw new PropertyLoadException();
//            }
//
//            this.propertiesMap.put(propertiesName, properties);
//        }
//        return properties.getProperty(propertyName);
//    }
//
//    private boolean buildPropertiesMap(){
//
//        this.propertiesList = new ArrayList<String>(Arrays.asList(this.properties.getProperty(this.name + ".Properties.List").split(" ")));
//        this.propertiesMap.put(this.name, this);
//
//        for(String property: this.propertiesList){
//            try (InputStream inputStream = AppProperty.class.getClassLoader().getResourceAsStream(this.properties.getProperty(this.name + ".Properties." + property + ".from"))){
//                root.propertiesMap.put(property, null);
//                logger.trace(property + ":\tWell configured.");
//            } catch (IOException e) {
//                logger.error(property + ":\tWrongly configured.");
//                e.printStackTrace();
//                return false;
//            }
//        }
//        root.propertiesList.add("app");
//        return true;
//    }
//
//    private AppProperty() throws IOException {
//
//        appProperties = loadResourceProperties("config/AppConfig.properties");
//
//        root = this;
//        name = null;
//    }
//
//    private AppProperties loadResourceProperties(String fileName) throws IOException {
//        Properties ret = new Properties();
//            try(InputStream input = AppProperty.class.getClassLoader().getResourceAsStream(fileName)){
//            if(input==null) {
//                System.out.println("Unable to find the file: " + fileName);
//                return null;
//            }
//            ret.load(input);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return ret;
//    }
//
//}
