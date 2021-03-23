package com.example;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micronaut.core.annotation.Introspected;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Introspected
@JsonNaming(value = SnakeCaseStrategy.class)
public class UploadResult {

    private String tenantId;
    private String incidentId;
    private List<String> incidentFiles;

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getIncidentId() {
        return this.incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public List<String> getIncidentFiles() {
        return this.incidentFiles;
    }

    public void setIncidentFiles(List<String> incidentFiles) {
        this.incidentFiles = incidentFiles;
    }

    public void addIncidentFiles(String ... addedFiles) {
        if (incidentFiles == null) {
            incidentFiles = new ArrayList<String>();
        }

        for(String file : addedFiles) {
            incidentFiles.add(file);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UploadResult)) {
            return false;
        }
        UploadResult uploadResult = (UploadResult) o;
        return Objects.equals(tenantId, uploadResult.tenantId) && Objects.equals(incidentId, uploadResult.incidentId) && Objects.equals(incidentFiles, uploadResult.incidentFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, incidentId, incidentFiles);
    }

}
