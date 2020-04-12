package com.sairaghava.rest;

import static com.sairaghava.utils.Constants.NEW_LINE;
import java.io.IOException;
import java.text.MessageFormat;
import javax.json.bind.JsonbException;
import javax.json.stream.JsonParsingException;
import com.sairaghava.config.ExternalConfiguration;
import com.sairaghava.schema.Covid19Params;
import com.sairaghava.utils.JsonBConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class Covid19ParamsController {
  private final TransformationService transFormationService;
  private final ExternalConfiguration externalConfig;

  @PostMapping(value = "/jsontocsv", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  ResponseEntity<String> convertJsonToCsv(@RequestBody Covid19Params covid19params) {
    String result = transFormationService.convertJsonToCsv(covid19params);
    return result.startsWith(externalConfig.getErrorPrefix())
        ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(externalConfig.getErrorMessage() + NEW_LINE + result)
        : ResponseEntity.ok().body(result);
  }

  @PostMapping(value = "/uploadjson", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> uploadFile(@RequestParam("inputFile") MultipartFile inputFile) {
    String fileName = inputFile.getOriginalFilename();
    ResponseEntity<?> result = null;
    if (!transFormationService.isItAJsonFile(fileName)) {
      result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(externalConfig.getWrongFileUploadErrorMessage());
    } else {
      try {
        Covid19Params covid19Params =
            JsonBConfig.getJsonb().fromJson(inputFile.getInputStream(), Covid19Params.class);
        log.info("FileContent = " + JsonBConfig.getJsonb().toJson(covid19Params));
        result = ResponseEntity.ok()
            .body(MessageFormat.format(externalConfig.getFileUploadSuccess(), fileName));
      } catch (JsonParsingException | JsonbException | IOException e) {
        log.error(externalConfig.getCantReadJsonFile(), e);
        result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(externalConfig.getCantReadJsonFile());
      }
    }
    return result;
  }
}
