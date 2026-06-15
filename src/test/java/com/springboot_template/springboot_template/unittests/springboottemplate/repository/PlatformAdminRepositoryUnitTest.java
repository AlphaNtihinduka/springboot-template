package com.springboot_template.springboot_template.unittests.springboottemplate.repository;

import com.springboot_template.springboot_template.repository.PlatformAdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformAdminRepositoryUnitTest {

    @Test
    void shouldUseMongoRepository() {
        assertThat(MongoRepository.class.isAssignableFrom(PlatformAdminRepository.class)).isTrue();
    }
}
