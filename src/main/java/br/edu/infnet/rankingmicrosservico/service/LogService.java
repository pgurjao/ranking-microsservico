package br.edu.infnet.rankingmicrosservico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import br.edu.infnet.rankingmicrosservico.model.ItemRanking;
import br.edu.infnet.rankingmicrosservico.model.Turno;
//import br.edu.infnet.rankingmicrosservico.repository.LogRepository;
import lombok.Getter;

//@Service
//@Getter
@FeignClient("HEROI")
public interface LogService {
	
//	public List<ItemRanking> obterRanking() {
//		
//		return null;
//	}

	@GetMapping("/log/batalhas/{idBatalha}")
	List<Turno> getBatalha( @RequestHeader("Authorization") String authHeader
								,  @PathVariable("idBatalha") Integer batalhaId);
	
}
