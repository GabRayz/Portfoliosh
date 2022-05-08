package fr.gabray.portfoliosh.exception;

public class CommandRuntimeException extends RuntimeException {
    public CommandRuntimeException()
    {
    }

    public CommandRuntimeException(final String message)
    {
        super(message);
    }

    public CommandRuntimeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public CommandRuntimeException(final Throwable cause)
    {
        super(cause);
    }

    public CommandRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
