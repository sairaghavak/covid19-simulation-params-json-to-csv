package com.sairaghava.config;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "messages")
public class ExternalConfiguration {
  private String errorMessage;
  private String errorPrefix;
  private List<String> ageBands;
  private List<String> csvHeaders;
  private Map<String, String> ageBandMap;
}
