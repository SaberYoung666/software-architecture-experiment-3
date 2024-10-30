package org.young.bsarchitecture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class BsArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsArchitectureApplication.class, args);
	}

}
