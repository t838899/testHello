package com.telus.starter.springboot.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.telus.p3ms.product.warranty.template.service.dvo.PingResponse;
import com.telus.starter.springboot.model.Greeting;
import com.telus.starter.springboot.service.ConfigService;
import com.telus.starter.springboot.service.GreetingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@RequestMapping(value = "/test")
//@RestController
//
//@RequestMapping(value = "/product/warrantyTemplate")
@RestController

public class HelloWorldController {


  private final AtomicLong counter = new AtomicLong();

  @Autowired
  private GreetingService greetingSvc;

  @Autowired
  private ConfigService configService;


  public AtomicLong getCounter() {
  	return counter;
  }

  private final Log log = LogFactory
          .getLog( HelloWorldController.class );


  @RequestMapping(value={"", "/", "/greeting","/greeting2","/greeting3" })
  public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    log.debug("HelloWorld:DEBUG");
    log.info("HelloWorld:INFO");
    log.warn("HelloWorld:WARN");
    log.error("HelloWorld:ERROR");
    return new Greeting(counter.incrementAndGet(), greetingSvc.getGreetingMessage(name));
  }

  @RequestMapping (path = "/ping")
  public Greeting ping() {
	  log.debug("ping()");
      return new Greeting(counter.incrementAndGet(), greetingSvc.getGreetingMessage("WORLD"));
  }

  @RequestMapping(value={"/config"})
  public String config(@RequestParam(value="html", defaultValue="false") boolean htmlFormat) {
        return configService.outputConfig(htmlFormat);
  }



}
