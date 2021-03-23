package com.example;

import io.micronaut.http.multipart.StreamingFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import javax.inject.Singleton;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

@Singleton
public class FileUploadService {

    Logger log = LoggerFactory.getLogger(FileUploadController.class);

    public Mono<UploadResult> uploadFile(String tenant, String incident, StreamingFileUpload data) {

        String file = "test.jpg";
        return Mono.create((MonoSink<FileChannel> sink) -> {
            try {
                RandomAccessFile writer = new RandomAccessFile(file, "rw");
                FileChannel channel = writer.getChannel();
                sink.success(channel);
                log.debug("WriteChannel Created");
            } catch (Exception e) {
                log.error("Unable to create WriteChannel in GCP storage due to error", e);
                sink.error(new StorageUploadError(e));
            }
        })
                .flatMap(writeChannel -> {
                    return Mono.create((MonoSink<UploadResult> sink) -> {
                        log.debug("Getting data from request");
                        Disposable disposable = Flux.from(data)
                                .subscribe(
                                        partData -> {
                                            log.debug("Received bytes from request");
                                            try {
                                                log.debug("size {} ", data.getSize());
                                                log.debug("position {} ", writeChannel.position());
                                                writeChannel.write(partData.getByteBuffer());
                                                log.debug("position {} ", writeChannel.position());
                                                log.debug("Bytes written to GCP WriteChannel");
                                            } catch (Exception e) {
                                                log.error("Failed to write bytes to GCP stream due to error", e);
                                                sink.error(new StorageUploadError(e));
                                            }
                                        },
                                        error -> {
                                            log.error("Failed to read bytes from request due to error", error);
                                            sink.error(new StorageUploadError(error));
                                        },
                                        () -> {
                                            log.debug("Finished receiving bytes.  Sending success");
                                            try {
                                                writeChannel.close();
                                                UploadResult result = new UploadResult();
                                                result.setTenantId(tenant);
                                                result.setIncidentId(incident);
                                                result.addIncidentFiles(file);
                                                sink.success(result);
                                            } catch (Exception e) {
                                                log.error("Encountered error closing GCP write stream", e);
                                                sink.error(new StorageUploadError(e));
                                            }
                                        }
                                );

//                    data.doOnComplete(() -> {
//                        log.debug("Request bytes subscription completed.  Disposing reference");
//                        disposable.dispose();
//                    });
                    });
                }).doOnSuccess((UploadResult result) -> {
                    log.debug("Preparing to publish to GCP");
//                uploadResultClient.send(result);
                    log.debug("Publish to GCP complete");
                });


    }
}
