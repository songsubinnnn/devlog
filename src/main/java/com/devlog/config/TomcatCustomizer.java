package com.devlog.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers((Context context) -> {
            context.setAllowCasualMultipartParsing(true); // multipart/form-data 허용
        });
        factory.addConnectorCustomizers((Connector connector) -> {
            connector.setMaxPostSize(52428800); // 전체 요청 바이트 (50MB)
            connector.setProperty("fileCountMax", "10"); // 파일 개수 최대 10개로 설정
        });
    }
}
