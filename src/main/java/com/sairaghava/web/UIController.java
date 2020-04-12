package com.sairaghava.web;

import java.io.IOException;
import java.util.Objects;
import javax.json.bind.JsonbException;
import javax.servlet.http.HttpServletRequest;
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

  @GetMapping("/")
  public String home() {
    System.out.println("In get mapping");
    return "index";
  }

  @PostMapping("/uploadjson")
  public String uploadJson(@RequestParam("inputFile") MultipartFile inputFile,
      RedirectAttributes redirectAttributes, HttpServletRequest request)
      throws JsonbException, IOException {
    redirectAttributes.addFlashAttribute("isError", false);
    if (Objects.isNull(inputFile) || inputFile.isEmpty()) {
      redirectAttributes.addFlashAttribute("isError", true);
      redirectAttributes.addFlashAttribute("serverUpdate", "Please select the file");
    } else {
      // Set headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      // Set body
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("inputFile", inputFile.getResource());
      // Wrap headers, body to HttpEntity
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      String url = request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/"))
          + "/api/uploadjson";
      ResponseEntity<?> response = null;
      try {
        response = restTemplate.postForEntity(url, requestEntity, String.class);
      } catch (HttpStatusCodeException e) {
        response = ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        redirectAttributes.addFlashAttribute("isError", true);
      }
      redirectAttributes.addFlashAttribute("serverUpdate", response.getBody().toString());
    }
    return "redirect:/";
  }
}
