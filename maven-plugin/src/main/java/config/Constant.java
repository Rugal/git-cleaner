package config;

/**
 * Class that contains constant value.
 *
 * @author Rugal Bernstein
 */
public interface Constant {

  String ENCODING = "UTF-8";

  String PREFIX = "gitcleaner";

  String FAIL_ON_ERROR = PREFIX + ".failOnError";

  String SKIP = PREFIX + ".skip";

  String IS_COMPRESSIVE = PREFIX + ".isCompressive";

  String SIZE_TO_FILTER = PREFIX + ".sizeToFilter";

  String GIT_FOLDER = PREFIX + ".gitFolder";
}
