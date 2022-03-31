package sbp.su.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import sbp.su.springbootdemo.services.Accounts;

/**
 * Класс SpringConfig содержит конфигурацию Spring с использованием аннотации
 * @ComponentScan для пакета sbp.su.springbootdemo.dao и аннотацию @Bean для класса Accounts
 *
 * @version 1.0
 * @autor Sergey Proshchaev
 */
@Configuration
@ComponentScan("sbp.su.springbootdemo.dao")
//@PropertySource("db.properties")
@PropertySource(value = "classpath:db.properties", encoding = "UTF-8")
public class SpringConfig {

    @Bean
    public Accounts accounts() {
        return new Accounts();
    }

}
