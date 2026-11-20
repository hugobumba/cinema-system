package com.hugobumba.cinemasystem.config;

import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"test", "dev"})
public class H2ConsoleConfig {

    @Bean
    @ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
    public ServletRegistrationBean<Servlet> h2ConsoleServletRegistration(
            @Value("${spring.h2.console.path:/h2-console}") String path
    ) {
        try {
            Servlet servlet = (Servlet) Class.forName("org.h2.server.web.JakartaWebServlet")
                    .getDeclaredConstructor()
                    .newInstance();

            String mapping = path.endsWith("/") ? path + "*" : path + "/*";
            return new ServletRegistrationBean<>(servlet, mapping);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to initialize H2 console servlet", e);
        }
    }
}
