package com.example.cloud.vision.spring;

import com.google.api.core.BetaApi;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import com.google.cloud.spring.core.DefaultCredentialsProvider;
import com.google.cloud.spring.core.Retry;
import com.google.cloud.spring.core.util.RetryUtil;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import java.io.IOException;
import javax.annotation.Generated;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Generated("by google-cloud-spring-generator")
@BetaApi("Autogenerated Spring autoconfiguration is not yet stable")
@AutoConfiguration
@AutoConfigureAfter(GcpContextAutoConfiguration.class)
@ConditionalOnClass(ImageAnnotatorClient.class)
// Check for explicit enabling on service level (treat as false if not set),
// Or blanket module-level enabling (treat as true if not set) without explicit disabling on service level
@ConditionalOnExpression(
        "{${com.google.cloud.vision.v1.image-annotator.enabled:false} == true} || {${com.google.cloud.vision.v1.all-services.enabled:true} != false && ${com.google.cloud.vision.v1.image-annotator.enabled:true} != false}")
@EnableConfigurationProperties(ImageAnnotatorSpringProperties.class)
public class ImageAnnotatorSpringAutoConfiguration {
  private final ImageAnnotatorSpringProperties clientProperties;
  private final CredentialsProvider credentialsProvider;
  private static final Log LOGGER = LogFactory.getLog(ImageAnnotatorSpringAutoConfiguration.class);


  protected ImageAnnotatorSpringAutoConfiguration(ImageAnnotatorSpringProperties clientProperties,
      CredentialsProvider credentialsProvider) throws IOException {
    this.clientProperties = clientProperties;
    if (this.clientProperties.getCredentials().hasKey()) {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Using credentials from ImageAnnotator-specific configuration");
      }
      this.credentialsProvider =
              ((CredentialsProvider) new DefaultCredentialsProvider(this.clientProperties));
    } else {
      this.credentialsProvider = credentialsProvider;
    }
  }

  @Bean
  @ConditionalOnMissingBean(name = "defaultImageAnnotatorTransportChannelProvider")
  public TransportChannelProvider defaultImageAnnotatorTransportChannelProvider() {
    if (this.clientProperties.getUseRest()) {
      return ImageAnnotatorSettings.defaultHttpJsonTransportProviderBuilder().build();
    }
    return ImageAnnotatorSettings.defaultTransportChannelProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public ImageAnnotatorSettings imageAnnotatorSettings(
          @Qualifier("defaultImageAnnotatorTransportChannelProvider")
          TransportChannelProvider defaultTransportChannelProvider) throws IOException {

    ImageAnnotatorSettings.Builder clientSettingsBuilder;
    if (this.clientProperties.getUseRest()) {
      clientSettingsBuilder = ImageAnnotatorSettings.newHttpJsonBuilder();
    } else {
      clientSettingsBuilder = ImageAnnotatorSettings.newBuilder();
    }

    clientSettingsBuilder
        .setCredentialsProvider(this.credentialsProvider)
        .setTransportChannelProvider(defaultTransportChannelProvider);

    if (this.clientProperties.getQuotaProjectId() != null) {
      clientSettingsBuilder.setQuotaProjectId(this.clientProperties.getQuotaProjectId());
    }
    if (this.clientProperties.getExecutorThreadCount() != null) {
      ExecutorProvider executorProvider =
              ImageAnnotatorSettings.defaultExecutorProviderBuilder()
                      .setExecutorThreadCount(this.clientProperties.getExecutorThreadCount())
                      .build();
      clientSettingsBuilder.setBackgroundExecutorProvider(executorProvider);
    }

    // Service-level retry logic - applying for only two methods here for simplicity
    Retry serviceRetry = clientProperties.getRetry();
    if (serviceRetry != null) {
      RetrySettings batchAnnotateImagesRetrySettings =
              RetryUtil.updateRetrySettings(
                      clientSettingsBuilder.batchAnnotateImagesSettings().getRetrySettings(), serviceRetry);
      clientSettingsBuilder
              .batchAnnotateImagesSettings()
              .setRetrySettings(batchAnnotateImagesRetrySettings);

      RetrySettings batchAnnotateFilesRetrySettings =
              RetryUtil.updateRetrySettings(
                      clientSettingsBuilder.batchAnnotateFilesSettings().getRetrySettings(), serviceRetry);
      clientSettingsBuilder
              .batchAnnotateFilesSettings()
              .setRetrySettings(batchAnnotateFilesRetrySettings);
    }
    // Method-level retry logic - applying for only two methods here for simplicity
    Retry batchAnnotateImagesRetry = clientProperties.getBatchAnnotateImagesRetry();
    if (batchAnnotateImagesRetry != null) {
      RetrySettings batchAnnotateImagesRetrySettings =
              RetryUtil.updateRetrySettings(
                      clientSettingsBuilder.batchAnnotateImagesSettings().getRetrySettings(),
                      batchAnnotateImagesRetry);
      clientSettingsBuilder
              .batchAnnotateImagesSettings()
              .setRetrySettings(batchAnnotateImagesRetrySettings);
    }

    Retry batchAnnotateFilesRetry = clientProperties.getBatchAnnotateFilesRetry();
    if (batchAnnotateFilesRetry != null) {
      RetrySettings batchAnnotateFilesRetrySettings =
              RetryUtil.updateRetrySettings(
                      clientSettingsBuilder.batchAnnotateFilesSettings().getRetrySettings(),
                      batchAnnotateFilesRetry);
      clientSettingsBuilder
              .batchAnnotateFilesSettings()
              .setRetrySettings(batchAnnotateFilesRetrySettings);
    }

    return clientSettingsBuilder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  public ImageAnnotatorClient imageAnnotatorClient(
          ImageAnnotatorSettings imageAnnotatorSettings) throws IOException {
    return ImageAnnotatorClient.create(imageAnnotatorSettings);
  }

}
