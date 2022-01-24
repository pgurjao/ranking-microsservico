package br.edu.infnet.rankingmicrosservico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RankingMicrosservicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankingMicrosservicoApplication.class, args);
	}

}
