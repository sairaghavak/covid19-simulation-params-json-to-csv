package com.sairaghava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

  @GetMapping("/")
  public String home() {
    System.out.println("In get mapping");
    return "index";
  }
}
