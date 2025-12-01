package cl.samuel.barzarena.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoggingConfig {

    public LoggingConfig() {
        log.info("Sistema iniciado — Backend BarzArena funcionando correctamente");
    }
}

