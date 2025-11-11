package ru.itmo.tim;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class DatabaseFunctionsInitializier implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try{
        DataSource ds = (DataSource) new InitialContext().lookup("java:/PostgresDS");
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()){
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("sql/func.sql")) {
            if (is == null) {
                throw new FileNotFoundException("Resource 'sql/func.sql' not found");
            }
            stmt.execute(new String(is.readAllBytes(), StandardCharsets.UTF_8));
        }
        }
    }catch(Exception e){
            e.printStackTrace();
        }
    }
}
