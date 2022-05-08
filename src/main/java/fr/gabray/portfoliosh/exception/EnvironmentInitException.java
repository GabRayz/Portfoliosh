package fr.gabray.portfoliosh.exception;

public class EnvironmentInitException extends RuntimeException {
    public EnvironmentInitException()
    {
    }

    public EnvironmentInitException(final String message)
    {
        super(message);
    }

    public EnvironmentInitException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public EnvironmentInitException(final Throwable cause)
    {
        super(cause);
    }

    public EnvironmentInitException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
