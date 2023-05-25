package com.example.cloud.vision.spring;

import com.google.cloud.spring.core.Credentials;
import com.google.cloud.spring.core.CredentialsSupplier;
import com.google.cloud.spring.core.Retry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("com.google.cloud.vision.v1.image-annotator")
public class ImageAnnotatorSpringProperties implements CredentialsSupplier {

  @NestedConfigurationProperty
  private final Credentials credentials =
          new Credentials(
                  "https://www.googleapis.com/auth/cloud-vision",
                  "https://www.googleapis.com/auth/cloud-platform");

  private String quotaProjectId;
  private Integer executorThreadCount;
  private boolean useRest = false;
  @NestedConfigurationProperty private Retry retry;
  @NestedConfigurationProperty private Retry batchAnnotateImagesRetry;
  @NestedConfigurationProperty private Retry batchAnnotateFilesRetry;

  @Override
  public Credentials getCredentials() {
    return this.credentials;
  }

  public String getQuotaProjectId() {
    return quotaProjectId;
  }

  public void setQuotaProjectId(String quotaProjectId) {
    this.quotaProjectId = quotaProjectId;
  }

  public Integer getExecutorThreadCount() {
    return executorThreadCount;
  }

  public void setExecutorThreadCount(int executorThreadCount) {
    this.executorThreadCount = executorThreadCount;
  }

  public boolean getUseRest() {
    return useRest;
  }

  public Retry getRetry() {
    return retry;
  }

  public void setRetry(Retry retry) {
    this.retry = retry;
  }

  public Retry getBatchAnnotateImagesRetry() {
    return batchAnnotateImagesRetry;
  }

  public void setBatchAnnotateImagesRetry(Retry batchAnnotateImagesRetry) {
    this.batchAnnotateImagesRetry = batchAnnotateImagesRetry;
  }

  public Retry getBatchAnnotateFilesRetry() {
    return batchAnnotateFilesRetry;
  }

  public void setBatchAnnotateFilesRetry(Retry batchAnnotateFilesRetry) {
    this.batchAnnotateFilesRetry = batchAnnotateFilesRetry;
  }

}
