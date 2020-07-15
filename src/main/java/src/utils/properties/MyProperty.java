package src.utils.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.utils.exception.PropertyLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Class to represent a <code>Properties</code>.
 */
public class MyProperty {

    /**
     * <p>
     * <code>Logger</code> for the Class.
     * </p><p>
     * It's manually set to write all the information because
     * this class is called before the configurations are loaded
     * and parsed, so we can retrieve a complete log even if
     * something went wrong.
     * </p>
     */
    private static final Logger logger = LogManager.getLogger("properties");

    //##FOR GREEDY ALGORITHM -and avoiding infinite loops
    /**
     * <code>Map</code> connecting each <code>MyProperty</code>'s name
     * to it's instance. Used for Greedy reasons.
     */
    private final static Map<String, MyProperty> instancesMap = new HashMap<>();

    //##INSTANCE VARIABLES
    /**
     * The name of this.
     * Each <code>MyProperty</code> has a unique id.
     */
    private final String name;

    /**
     * The filename of the default file from
     * which load this (inside the JAR).
     */
    private final String defaultFileName;

    /**
     * The filename of the file from which
     * load this (outside the JAR).
     */
    private final String otherFileName;

    /**
     * true if otherFileName refers to a read only file.
     */
    private final boolean isOtherFileNameReadOnly;

    /**
     * This' <code>Properties</code>.
     */
    private Properties properties;

    private List<String> ref;

    /**
     * Method to build the desired <code>MyProperty</code> (or retrieve
     * it if already exists).
     *
     * @param name             (the name of the <code>MyProperty</code>).
     * @param defaultFileName  (the default file name of the <code>MyProperty</code>).
     * @param userFileName     (the user file name of the <code>MyProperty</code>).
     * @param readOnlyFileName (the read only file name of the <code>MyProperty</code>).
     * @return                 (the searched <code>MyProperty</code>).
     */
    public static MyProperty build(String name, String defaultFileName, String userFileName, String readOnlyFileName){
        //Tries to retrieve the <code>MyProperty</code>
        MyProperty ret;
        synchronized (instancesMap) {
            ret = instancesMap.get(name);
        }
        //if doesn't exists constructs it.
        if(ret == null){
            try {
                ret = new MyProperty(name, defaultFileName, userFileName, readOnlyFileName);
            } catch (IOException | NullPointerException e) {
                logger.error(e);
                logger.error("File doesn't exists (or unable to access it) " + defaultFileName );
                logger.error("Cannot create " + name + " MyProperty.");
                return null;
            }
            synchronized (instancesMap) {
                instancesMap.put(name, ret);
            }
        }
        return ret;
    }

    /**
     * Constructor
     *
     * @param name             (the name of the <code>MyProperty</code>).
     * @param defaultFileName  (the default file name of the <code>MyProperty</code>).
     * @param userFileName     (the user file name of the <code>MyProperty</code>).
     * @param readOnlyFileName (the read only file name of the <code>MyProperty</code>).
     * @throws IOException     (if userFileName is not valid).
     */
    private MyProperty(String name, String defaultFileName, String userFileName, String readOnlyFileName) throws IOException, NullPointerException {
        this.name = name;
        if(readOnlyFileName != null){
            this.otherFileName = readOnlyFileName;
            this.isOtherFileNameReadOnly = true;
        }else{
            this.otherFileName = userFileName;
            this.isOtherFileNameReadOnly = false;
        }
        MyProperty.class.getClassLoader().getResourceAsStream(defaultFileName).close();
        this.defaultFileName = defaultFileName;
        this.properties = null;
    }

    /**
     * Method to load this' properties.
     *
     * @throws PropertyLoadException
     */
    private void load() throws PropertyLoadException {
        //Loading properties

        // TODO: 15/07/20 check also other fileName 
        properties = loadFromFileProperties(defaultFileName);

        //loading the greedy load
        String retString = this.properties.getProperty(this.name + ".ref.greedyLoad");
        if(retString == null)
            retString = "";
        ref = new ArrayList<>(Arrays.asList(retString.split(" ")));
        this.buildReferencies(true);

        //loading all the references
        retString = this.properties.getProperty(this.name + ".ref.list");
        if(retString == null)
            retString = "";
        ref.addAll(Arrays.asList(retString.split(" ")));
        this.buildReferencies(false);
    }

    // TODO: 15/07/20 sistemalo 
    public static MyProperty init(){
        MyProperty app;
        try {
            app = new MyProperty("App", "config/AppConfig.properties", null, null);
        } catch (IOException e) {
            return null;
        }

        try {
            app.load();
        } catch (PropertyLoadException e) {
            return null;
        }
        
        return app;
    }

    private MyProperty retrieveProperties(String myPropertyName){
        if(!this.ref.contains(myPropertyName))
            return null;
        synchronized (instancesMap){
            return instancesMap.get(myPropertyName);
        }
    }

    public String getProperty(String key) throws PropertyLoadException {
        String ret;
        if(this.properties == null)
            this.load();
        if(key.startsWith(this.name)){
            ret = this.properties.getProperty(key);
        }else {
            ret = this.properties.getProperty(this.name + "." + key);
        }
        if(ret == null){
            ArrayList<String> keyList = new ArrayList<>(Arrays.asList(key.replace(".", " ").split(" ")));
            if(this.name.equals(keyList.get(0))){
                return this.getProperty(String.join(".", keyList.subList(1, keyList.size())));
            }else{
                try{
                    return this.retrieveProperties(keyList.get(0)).getProperty(String.join(".", keyList.subList(1, keyList.size())));
                }catch (NullPointerException e){
                    return null;
                }
            }
        }
        return ret;
    }

    private void buildReferencies(boolean load){

        for(String loadee: this.ref){
            MyProperty loaded;
            if(loadee.equals(""))
                continue;

            String loadeeDefaultFileName = this.properties.getProperty(this.name + ".ref." + loadee + ".defaultFileName");
            String loadeeUserFileName = this.properties.getProperty(this.name + ".ref." + loadee + ".userFileName");
            String loadeeReadOnlyFileName = this.properties.getProperty(this.name + ".ref." + loadee + ".readOnlyFileName");

            if(loadeeDefaultFileName == null){
                loaded = retrieveProperties(loadee);
            }else {
                loaded = build(loadee, loadeeDefaultFileName, loadeeUserFileName, loadeeReadOnlyFileName);
            }

            if(load){
                if(loaded==null){
                    logger.error("Cannot load " + loadee + " as MyProperty.");
                    logger.error(loadee + " required by " + this.name);
                    logger.error(loadee + " default file name: " + loadeeDefaultFileName);
                }else{
                    try {
                        loaded.load();
                    } catch (PropertyLoadException e) {
                        logger.error(e);
                        logger.error("Cannot load " + loadee + ", error in the loader.");
                        logger.error(loadee + " required by " + this.name);
                    }
                }
            }else{
                if(loaded==null){
                    logger.warn(this.name + "References to: " + loadee + " but hasn't been built.");
                }
            }
        }
    }
    
    private static Properties loadFromFileProperties(String fileName) throws PropertyLoadException {
        Properties ret = new Properties();
        InputStream input = MyProperty.class.getClassLoader().getResourceAsStream(fileName);
        if(input==null) {
            logger.error("Doesn't exists the file");
            throw new PropertyLoadException();
        }
        try{
            ret.load(input);
        }catch (IOException e){
            throw new PropertyLoadException();
        }
        return ret;
    }
}
