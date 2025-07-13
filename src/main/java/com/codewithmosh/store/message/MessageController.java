package com.codewithmosh.store.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
  @RequestMapping(path = "/hello", method = RequestMethod.GET)
  public Message sayHello() {
    return new Message("Hello World!");
  }
}
