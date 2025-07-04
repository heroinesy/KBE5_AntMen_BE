package com.antmen.antwork.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "logging.level.root=OFF",
        "spring.main.banner-mode=off"
})
class AdminApplicationTests {
    @Test
    void contextLoads() {
    }
}