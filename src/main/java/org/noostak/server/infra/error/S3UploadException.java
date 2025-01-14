package org.noostak.server.infra.error;

import org.noostak.server.global.error.core.BaseException;

public class S3UploadException extends BaseException {
    public S3UploadException(S3UploadErrorCode errorCode) {
        super(errorCode);
    }
}