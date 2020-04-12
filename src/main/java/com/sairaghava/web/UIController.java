package com.sairaghava.web;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import javax.json.bind.JsonbException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import com.sairaghava.config.ExternalConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UIController {
  private final RestTemplate restTemplate;
  private final ExternalConfiguration externalConfig;

  @GetMapping("/")
  public String home() {
    System.out.println("In get mapping");
    return "index";
  }

  @PostMapping("/uploadjson")
  public String uploadJson(@RequestParam("inputFile") MultipartFile inputFile,
      RedirectAttributes redirectAttributes, HttpServletRequest request)
      throws JsonbException, IOException {
    String serverUpdate = "";
    boolean isError = true;
    if (Objects.isNull(inputFile) || inputFile.isEmpty()) {
      serverUpdate = "Please select the file";
    } else {
      HttpEntity<MultiValueMap<String, Object>> httpEntity =
          getRequestEntity("inputFile", inputFile.getResource());
      String url = generateAbsoluteRestUrl(request.getRequestURL(), "/api/uploadjson");
      ResponseEntity<?> response = null;
      try {
        response = restTemplate.postForEntity(url, httpEntity, String.class);
        redirectAttributes.addFlashAttribute("csvFile", response.getBody().toString());
        serverUpdate = MessageFormat.format(externalConfig.getFileUploadSuccess(),
            inputFile.getOriginalFilename());
        isError = false;
      } catch (HttpStatusCodeException e) {
        response = ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        serverUpdate = response.getBody().toString();
      }
    }
    redirectAttributes.addFlashAttribute("isError", isError);
    redirectAttributes.addFlashAttribute("serverUpdate", serverUpdate);
    return "redirect:/";
  }

  @PostMapping("/downloadcsv")
  public ResponseEntity<?> downloadCsv(@RequestParam("csv") String csv) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=covid19-params.csv")
        .contentType(MediaType.TEXT_PLAIN).contentLength(csv.getBytes().length).body(csv);
  }

  private HttpEntity<MultiValueMap<String, Object>> getRequestEntity(final String key,
      final Resource resource) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add(key, resource);
    return new HttpEntity<>(body, headers);
  }

  private String generateAbsoluteRestUrl(final StringBuffer sb, final String apiSuffix) {
    return sb.substring(0, sb.lastIndexOf("/")) + apiSuffix;
  }
}
