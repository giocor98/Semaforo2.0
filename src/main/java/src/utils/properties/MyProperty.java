package src.utils.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * Class to represent a <code>Properties</code>.
 * </p>
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

    /**
     * <p>
     * <code>List</code> of all the <code>MyProperty</code> that
     * are referenced by this.
     * </p>
     */
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
            logger.trace("Built MyProperty: " + name);
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
        logger.trace("Loading: " + this.name);
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
        logger.trace("Loaded: " + this.name);
    }

    /**
     * <p>
     * Method to be called for starting the <code>MyProperty</code>,
     * to load the App <code>MyProperty</code> and load all the other
     * needed <code>MyProperty</code>.
     * </p>
     *
     * @return (the <code>MyProperty</code> of the App Property, or
     *         null if an error occurs).
     */
    public static MyProperty init(){
        MyProperty app;

        logger.trace("Initialisation");

        try {
            app = new MyProperty("App", "config/AppConfig.properties", null, null);
        } catch (IOException e) {
            logger.fatal("Cannot open the App property");
            return null;
        }

        try {
            app.load();
        } catch (PropertyLoadException e) {
            logger.fatal("Cannot load the App property");
            return null;
        }
        
        return app;
    }

    /**
     * <p>
     * Method to retrieve the <code>MyProperty</code> with given
     * name if reachable from this.
     * </p><p>
     * If the searched <code>MyProperty</code>'s not reachable or
     * doesn't exists, then it'll return null.
     * </p>
     *
     * @param myPropertyName (the searched <code>MyProperty</code>'s name).
     * @return (the searched <code>MyProperty</code> or null).
     */
    private MyProperty retrieveProperties(String myPropertyName){
        if(!this.ref.contains(myPropertyName)){
            logger.debug("MyProperty " + this.name + " doesn't have access to " + myPropertyName);
            return null;
        }
        synchronized (instancesMap){
            logger.debug("Retrieving " + myPropertyName + " from " + this.name);
            return instancesMap.get(myPropertyName);
        }
    }

    /**
     * <p>
     * Method to retrieve from this the <code>Property</code>
     * searched
     * </p>
     *
     * @param key (the <code>Property</code> to retrieve name).
     * @return (the <code>Property</code> searched).
     * @throws PropertyLoadException (if this cannot be loaded).
     */
    public String getProperty(String key) throws PropertyLoadException, NotSuchPropertyException {
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
                    logger.trace(this.name + "inoltring request to " + keyList.get(0));
                    logger.trace("asked: " + key);
                    logger.trace("asking " + String.join(".", keyList.subList(1, keyList.size())));
                    return this.retrieveProperties(keyList.get(0)).getProperty(String.join(".", keyList.subList(1, keyList.size())));
                }catch (NullPointerException e){
                    logger.debug(this.name + " has not found anything with " + key);
                    throw new NotSuchPropertyException();
                }
            }
        }
        return ret;
    }

    /**
     * <p>
     * Method to build the <code>ref</code> of this.
     * </p><p>
     * If load is true than it'll load the <code>ref</code>.
     * </p>
     *
     * @param load
     */
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

    /**
     * <p>
     * Method to load the <code>Properties</code> from the file
     * passed.
     * </p>
     *
     * @param fileName (the name of the file to be read).
     * @return (the newly read <code>Properties</code>).
     * @throws PropertyLoadException (if the file doesn't exists).
     */
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
