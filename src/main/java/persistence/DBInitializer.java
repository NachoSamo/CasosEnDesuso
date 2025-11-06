package persistence;

import org.flywaydb.core.Flyway;

public class DBInitializer {
    public static void init() {
        // Flyway configurado para usar el mismo archivo H2 en ./data/red_sismica
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:./data/red_sismica;AUTO_SERVER=TRUE", "sa", "")
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
}
