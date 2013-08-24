package eu.nerdz.api;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import eu.nerdz.api.Application.Features;
import eu.nerdz.api.fullapp.FullBlownApp;
import eu.nerdz.api.messages.Messenger;

/**
 * Nerdz is an abstract class. It is crucial in the administration of various implementation of Nerdz API, being a wrapper around implementations.
 * Nerdz.getImplementation(String name) returns a Nerdz instance from the specified package with specified name, which can be used to get applications and data without knowing anything about the implementation.
 * A manager implementation MUST provide an empty constructor.
 */
public abstract class Nerdz implements Serializable {

    /**
     * Every implementation must be in a package beginning with this string.
     */
    public static final String IMPLEMENTATION_PACKAGE_ROOT = "eu.nerdz.api.impl";

    /**
     * Dynamically finds the Nerdz class under IMPLEMENTATION_PACKAGE_ROOT and returns an instance. The public default constructor of a manager
     * must be present and must throw no exception.
     *
     * @param implementationName the name of the implementation. Can also be in a subpackage, i.e. "implementation.Implementation".
     * @return a new instance of Nerdz belonging to the implementation.
     * @throws ClassNotFoundException  if implementationName is invalid.
     * @throws InvalidManagerException thrown if the implementation is not extending Nerdz correctly, i.e. there is no default constructor, the default constructor raises exceptions, or the class is not a subclass of Nerdz.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Nerdz getImplementation(String implementationName) throws ClassNotFoundException, InvalidManagerException, IllegalAccessException, InstantiationException {
        Class<?> manager = Class.forName(Nerdz.IMPLEMENTATION_PACKAGE_ROOT + '.' + implementationName);

        if (!(Nerdz.class.isAssignableFrom(manager))) {
            throw new InvalidManagerException(manager.getCanonicalName() + " is not a subclass of Nerdz.");
        }

        Constructor<?> constructor;
        try {
            constructor = manager.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new InvalidManagerException(manager.getCanonicalName() + " does not implement a public default constructor, can't be a valid Nerdz");
        }

        if (constructor.getExceptionTypes().length != 0) {
            throw new InvalidManagerException(manager.getCanonicalName() + " default constructor throws exceptions. This is invalid");
        }

        constructor.setAccessible(true); //GTFO and let me mess with you, little bitch

        try {
            return (Nerdz) constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw new InvalidManagerException(manager.getCanonicalName() + " default constructor has thrown a " + e.getClass().getCanonicalName() + " exception telling that " + e.getLocalizedMessage() + ". This is bad.");
        }
    }

    /**
     * Logs in and returns valid serializable data usable for a future login.
     *
     * @param userName A valid NERDZ userName
     * @param password a valid password for userName
     * @return a valid UserInfo instance
     * @throws IOException    indicates a failure in network access
     * @throws HttpException  indicates a wrong http status
     * @throws LoginException indicates that login data is wrong
     */
    public abstract UserInfo logAndGetInfo(String userName, String password) throws IOException, HttpException, LoginException;

    /**
     * Returns a new Application instance, initialized for userName with password.
     *
     * @param userName         A valid NERDZ userName
     * @param password         a valid password for userName
     * @param instanceFeatures the features wanted for this application. If null, the application can only do Login and fetch UserInfo
     * @return an Application initialized for userName with instanceFeatures features
     * @throws IOException    indicates a failure in network access
     * @throws HttpException  indicates a wrong http status
     * @throws LoginException indicates that login data is wrong
     */
    public final Application newApplication(String userName, String password, Features... instanceFeatures) throws IOException, HttpException, LoginException, WrongUserInfoTypeException {
        return this.restoreApplication(this.logAndGetInfo(userName, password), instanceFeatures);
    }

    /**
     * Returns a new Application instance, initialized with userInfo. Validity of userInfo will be checked.
     *
     * @param userInfo         valid UserInfo for this Nerdz implementation, filled with valid login data
     * @param instanceFeatures the features wanted for this application. If null, the application can only do Login and fetch UserInfo
     * @return an Application initialized with userInfo with instanceFeatures features
     * @throws IOException                indicates a failure in network access
     * @throws HttpException              indicates a wrong http status
     * @throws LoginException             indicates that login data is wrong
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public abstract Application newApplication(UserInfo userInfo, Features... instanceFeatures) throws IOException, HttpException, LoginException, WrongUserInfoTypeException;

    /**
     * Returns a new Application instance, initialized with userInfo belonging to an old instance. Validity of userInfo will not be checked.
     *
     * @param userInfo         valid UserInfo for this Nerdz implementation, filled with valid login data
     * @param instanceFeatures the features wanted for this application. If null, the application can only do Login and fetch UserInfo
     * @return an Application initialized with userInfo with instanceFeatures features
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public abstract Application restoreApplication(UserInfo userInfo, Features... instanceFeatures) throws WrongUserInfoTypeException;

    /**
     * Returns a new Messenger instance, initialized for userName with password.
     *
     * @param userName A valid NERDZ userName
     * @param password a valid password for userName
     * @return a Messenger initialized for userName
     * @throws IOException    indicates a failure in network access
     * @throws HttpException  indicates a wrong http status
     * @throws LoginException indicates that login data is wrong
     */
    public final Messenger newMessenger(String userName, String password) throws IOException, HttpException, LoginException, InvalidManagerException, WrongUserInfoTypeException {
        try {
            return (Messenger) this.newApplication(userName, password, Features.MESSENGER);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }
    }

    /**
     * Returns a new Messenger instance, initialized with userInfo. Validity of userInfo will be checked.
     *
     * @param userInfo valid UserInfo for this Nerdz implementation, filled with valid login data
     * @return a Messenger initialized with userInfo
     * @throws IOException                indicates a failure in network access
     * @throws HttpException              indicates a wrong http status
     * @throws LoginException             indicates that login data is wrong
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public final Messenger newMessenger(UserInfo userInfo) throws IOException, HttpException, LoginException, InvalidManagerException, WrongUserInfoTypeException {
        try {
            return (Messenger) this.newApplication(userInfo, Features.MESSENGER);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }
    }

    /**
     * Returns a new Application instance, initialized with userInfo belonging to an old instance. Validity of userInfo will not be checked.
     *
     * @param userInfo valid UserInfo for this Nerdz implementation, filled with valid login data
     * @return an Application initialized with userInfo
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public final Messenger restoreMessenger(UserInfo userInfo) throws WrongUserInfoTypeException, InvalidManagerException {
        try {
            return (Messenger) this.restoreApplication(userInfo, Features.MESSENGER);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }

    }

    /**
     * Returns a new FullBlownApp instance, initialized for userName with password.
     *
     * @param userName A valid NERDZ userName
     * @param password a valid password for userName
     * @return a FullBlownApp initialized for userName
     * @throws IOException    indicates a failure in network access
     * @throws HttpException  indicates a wrong http status
     * @throws LoginException indicates that login data is wrong
     */
    public final FullBlownApp newFullBlownApp(String userName, String password) throws IOException, HttpException, LoginException, InvalidManagerException, WrongUserInfoTypeException {
        try {
            return (FullBlownApp) this.newApplication(userName, password, Features.FULLBLOWNAPP);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }
    }

    /**
     * Returns a new FullBlownApp instance, initialized with userInfo. Validity of userInfo will be checked.
     *
     * @param userInfo valid UserInfo for this Nerdz implementation, filled with valid login data
     * @return a FullBlownApp initialized with userInfo
     * @throws IOException                indicates a failure in network access
     * @throws HttpException              indicates a wrong http status
     * @throws LoginException             indicates that login data is wrong
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public final FullBlownApp newFullBlownApp(UserInfo userInfo) throws IOException, HttpException, LoginException, InvalidManagerException, WrongUserInfoTypeException {
        try {
            return (FullBlownApp) this.newApplication(userInfo, Features.FULLBLOWNAPP);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }
    }

    /**
     * Returns a new Application instance, initialized with userInfo belonging to an old instance. Validity of userInfo will not be checked.
     *
     * @param userInfo valid UserInfo for this Nerdz implementation, filled with valid login data
     * @return an Application initialized with userInfo
     * @throws WrongUserInfoTypeException indicates that UserInfo does not belong to this Nerdz implementation
     */
    public final FullBlownApp restoreFullBlownApp(UserInfo userInfo) throws InvalidManagerException, WrongUserInfoTypeException {
        try {
            return (FullBlownApp) this.restoreApplication(userInfo, Features.FULLBLOWNAPP);
        } catch (ClassCastException e) {
            throw new InvalidManagerException(this.getClass().getCanonicalName() + "is not a valid Nerdz and does not respect instanceFeatures");
        }

    }

}
