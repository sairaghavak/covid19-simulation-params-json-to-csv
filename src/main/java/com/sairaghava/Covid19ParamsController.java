package com.sairaghava;

import com.sairaghava.config.ExternalConfiguration;
import com.sairaghava.schema.Covid19Params;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Covid19ParamsController {
  private final TransformationService transFormationService;
  private final ExternalConfiguration externalConfig;

  @PostMapping(value = "/json-to-csv", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  ResponseEntity<String> convertJsonToCsv(@RequestBody Covid19Params covid19params) {
    String result = transFormationService.convertJsonToCsv(covid19params);
    return result.startsWith(externalConfig.getErrorPrefix()) ? ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR).body(externalConfig.getErrorMessage() + result)
        : ResponseEntity.ok().body(result);
  }
}
