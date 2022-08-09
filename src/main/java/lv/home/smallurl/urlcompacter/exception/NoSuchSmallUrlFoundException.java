package lv.home.smallurl.urlcompacter.exception;

public class NoSuchSmallUrlFoundException extends Exception {

    public static final String NO_SMALL_URL_FOUND_ERROR_MSG = "No such small url found";

    public NoSuchSmallUrlFoundException() {
        super(NO_SMALL_URL_FOUND_ERROR_MSG);
    }
}
