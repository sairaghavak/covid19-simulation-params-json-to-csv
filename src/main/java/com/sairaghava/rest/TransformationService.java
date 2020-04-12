package com.sairaghava.rest;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import com.sairaghava.config.ExternalConfiguration;
import com.sairaghava.schema.Covid19Params;
import com.sairaghava.utils.JsonToCsvConverter;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransformationService {
  private final JsonToCsvConverter jsonToCsvConverter;
  private final ExternalConfiguration externalConfig;

  String convertJsonToCsv(Covid19Params covid19Params) {
    try {
      return jsonToCsvConverter.runAsWebService(covid19Params);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | IntrospectionException e) {
      log.error(externalConfig.getErrorPrefix(), e);
      return externalConfig.getErrorPrefix() + "-" + e.getLocalizedMessage();
    }
  }

  boolean isItAJsonFile(String fileName) {
    if (Objects.isNull(fileName)) {
      return false;
    }
    int index = fileName.lastIndexOf(".");
    String fileExtension = "";
    if (index > 0) {
      fileExtension = fileName.substring(index + 1);
    }
    return "json".equalsIgnoreCase(fileExtension) ? true : false;
  }

}
