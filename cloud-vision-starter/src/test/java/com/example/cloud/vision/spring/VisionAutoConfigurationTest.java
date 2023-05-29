package com.example.cloud.vision.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ProductSearchClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class VisionAutoConfigurationTest {

    private ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    GcpContextAutoConfiguration.class,
                                    ImageAnnotatorSpringAutoConfiguration.class,
                                    ProductSearchSpringAutoConfiguration.class));

    @Test
    void testServiceDefaultEnabled() {
        this.contextRunner
                .run(
                        ctx -> {
                            ImageAnnotatorClient imageAnnotatorClient = ctx.getBean(ImageAnnotatorClient.class);
                            assertThat(imageAnnotatorClient).isNotNull();
                            ProductSearchClient productSearchClient = ctx.getBean(ProductSearchClient.class);
                            assertThat(productSearchClient).isNotNull();
                        });
    }
    @Test
    void testServiceDisabledFromProperties() {
        this.contextRunner
                .withPropertyValues(
                        "com.google.cloud.vision.v1.image-annotator.enabled=false"
                )
                .run(
                        ctx -> {
                            for (String bean : ctx.getBeanDefinitionNames()) {
                                System.out.println(bean);
                            }
                            ProductSearchClient productSearchClient = ctx.getBean(ProductSearchClient.class);
                            assertThat(productSearchClient).isNotNull();
                            assertThatThrownBy(
                                    () -> ctx.getBean(ImageAnnotatorClient.class))
                                    .isInstanceOf(NoSuchBeanDefinitionException.class);
                        });
    }

    @Test
    void testTransportDefault() {
        this.contextRunner
                .run(
                        ctx -> {
                            ImageAnnotatorClient imageAnnotatorClient = ctx.getBean(ImageAnnotatorClient.class);
                            assertThat(imageAnnotatorClient).isNotNull();
                            assertThat(imageAnnotatorClient.getSettings().getTransportChannelProvider().getTransportName()).isEqualTo("grpc");

                            ProductSearchClient productSearchClient = ctx.getBean(ProductSearchClient.class);
                            assertThat(productSearchClient).isNotNull();
                            assertThat(productSearchClient.getSettings().getTransportChannelProvider().getTransportName()).isEqualTo("grpc");

                        });
    }
    @Test
    void testSetTransportFromProperties() {
        this.contextRunner
                .withPropertyValues(
                        "com.google.cloud.vision.v1.image-annotator.transport=REST",
                        "com.google.cloud.vision.v1.product-search.transport=rest"

                )
                .run(
                        ctx -> {
                            ImageAnnotatorClient imageAnnotatorClient = ctx.getBean(ImageAnnotatorClient.class);
                            assertThat(imageAnnotatorClient).isNotNull();
                            assertThat(imageAnnotatorClient.getSettings().getTransportChannelProvider().getTransportName()).isEqualTo("httpjson");

                            ProductSearchClient productSearchClient = ctx.getBean(ProductSearchClient.class);
                            assertThat(productSearchClient).isNotNull();
                            assertThat(productSearchClient.getSettings().getTransportChannelProvider().getTransportName()).isEqualTo("httpjson");

                        });
    }
}
