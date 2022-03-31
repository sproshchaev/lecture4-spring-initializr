package sbp.su.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestAppConfiguration.class)
class SpringBootDemoApplicationTests {

    @Test
    void contextLoads() {
    }

}
