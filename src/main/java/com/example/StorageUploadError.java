package com.example;

public class StorageUploadError extends RuntimeException {

    private static final long serialVersionUID = 546241200811473178L;
    public static final String DEFAULT_ERROR_MSG = "Unable to perform file upload";

    public StorageUploadError(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageUploadError(Throwable cause) {
        super(DEFAULT_ERROR_MSG, cause);
    }

    public StorageUploadError(String message) {
        super(message);
    }

    public StorageUploadError() {
        super(DEFAULT_ERROR_MSG);
    }

}
