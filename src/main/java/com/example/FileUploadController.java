package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

@Controller("/incident/{tenantName}")
public class FileUploadController {

    private FileUploadService fileUploadService;
    public FileUploadController (FileUploadService fileUploadService)  {
        this.fileUploadService = fileUploadService;
    }

    Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Post(value = "/{incidentId}/files", consumes = MULTIPART_FORM_DATA, produces = TEXT_PLAIN)
    @ExecuteOn(TaskExecutors.IO)
    public Mono<HttpResponse<UploadResult>> uploadIncidentFiles(String tenantName, String incidentId, StreamingFileUpload file) {
        log.debug("In upload method");
        return fileUploadService.uploadFile(tenantName, incidentId,  file).map(uploadResult -> {
            return HttpResponse.created(uploadResult);
        });
    }

}
