package com.clearlydecoded.commander.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
//@RequestMapping("/executeCommand")
public class RestCommandExecutor {

  @Value("${my.path}")
  private String path;

  @Autowired
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @PostConstruct
  private void postConstruct() throws Exception {
    System.out.println("Path from properties is: " + path);

    RequestMappingInfo requestMappingInfo = RequestMappingInfo
        .paths(path)
        .methods(RequestMethod.POST)
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .build();

    requestMappingHandlerMapping.registerMapping(requestMappingInfo, this,
        RestCommandExecutor.class.getDeclaredMethod("executeCommand", String.class));
  }

  //  @PostMapping
  public String executeCommand(@RequestBody String commandString) throws Exception {
    System.out.println(commandString);

    ObjectMapper mapper = new ObjectMapper();
    Command command = mapper.readValue(commandString, Command.class);
    System.out.println("Command is: " + command);

    Response response = new Response("Hello text", 123);

    if (response.getNumber() == 123) {
      throw new IllegalArgumentException(
          "Command type of " + command.getType() + " is not supported");
    }

    return mapper.writeValueAsString(response);
  }

}
