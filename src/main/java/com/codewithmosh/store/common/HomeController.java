package com.codewithmosh.store.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {
  @RequestMapping("/")
  public String index(Model model) {
    // Sample user object
    Map<String, Object> user = new HashMap<>();
    user.put("id", 101);
    user.put("name", "Sachin");
    user.put("loggedIn", true);
    user.put("active", true);
    model.addAttribute("user", user);

    // Sample item list
    List<String> items = List.of("Item A", "Item B", "Item C");
    model.addAttribute("items", items);

    // Form backing object
    FormData formData = new FormData();
    formData.setName("Sachin");
    formData.setEmail("sachin@example.com");
    model.addAttribute("formData", formData);

    return "index";
  }
}