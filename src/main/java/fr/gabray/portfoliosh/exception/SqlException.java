package fr.gabray.portfoliosh.exception;

public class SqlException extends RuntimeException {

    public SqlException()
    {
    }

    public SqlException(final String message)
    {
        super(message);
    }

    public SqlException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public SqlException(final Throwable cause)
    {
        super(cause);
    }

    public SqlException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
