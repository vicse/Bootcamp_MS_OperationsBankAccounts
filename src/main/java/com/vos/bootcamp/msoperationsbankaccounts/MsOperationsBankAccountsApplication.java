package com.vos.bootcamp.msoperationsbankaccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsOperationsBankAccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsOperationsBankAccountsApplication.class, args);
  }

}
