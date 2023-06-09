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
}
