package com.easyway.business.framework.common.exception;

public class DaoException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 6229278986973902002L;

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

}
