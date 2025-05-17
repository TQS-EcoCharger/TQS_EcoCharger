package pt.ua.tqs.ecocharger.ecocharger.config;


import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .cleanDisabled(false) // <-- IMPORTANT
                    .load();
    }


    @Bean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        flyway.clean();
        return new FlywayMigrationInitializer(flyway);
    }
}
