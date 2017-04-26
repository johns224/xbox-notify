package org.rossjohnson.notification;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${tomcat.port}")
    private String tomcatPort;

    @Value("${ajp.port}")
    private String ajpPort;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EmbeddedServletContainerCustomizer configureTomcat() {
        return c -> {
			TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) c;
			tomcat.setPort(Integer.parseInt(tomcatPort));

			Connector ajpConnector = new Connector("AJP/1.3");
			ajpConnector.setPort(Integer.parseInt(ajpPort));
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
		};
    }

}
