package org.springframework.context;

import org.springframework.beans.BeanException;

public class ApplicationContextException extends BeanException {
    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
