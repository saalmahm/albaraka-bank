package com.albaraka.digital;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Context loading test disabled for CI; configuration is validated manuellement")
class DigitalApplicationTests {

    @Test
    void contextLoads() {
    }
}