/**
 * test container 환경 구축 필요함
 */

package com.ddubok;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DdubokApplicationTests {

    @Test
    void contextLoads() {
    }

}
