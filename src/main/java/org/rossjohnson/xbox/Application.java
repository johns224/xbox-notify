package org.rossjohnson.xbox;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final int TOMCAT_PORT = 9900;
    private static final int AJP_PORT = 9901;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EmbeddedServletContainerCustomizer configureTomcat() {
        return c -> {
			TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) c;
			tomcat.setPort(TOMCAT_PORT);

			Connector ajpConnector = new Connector("AJP/1.3");
			ajpConnector.setPort(AJP_PORT);
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
		};
    }

}
