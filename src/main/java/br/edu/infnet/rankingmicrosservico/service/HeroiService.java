package br.edu.infnet.rankingmicrosservico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.infnet.rankingmicrosservico.model.Heroi;
import lombok.Getter;

@Getter
@Service
public class HeroiService {
	
	private String errorMessage;
	
//	@Autowired
//	private HeroiRepository heroiRepository;
	
//	public Optional<Heroi> getById(Integer idHeroi) {
//		return heroiRepository.findById(idHeroi);
//	}
	
//	public List<Heroi> findAll(){ 
//		return heroiRepository.findAll();
//	}

//	public Heroi obterPorNome(String nomeHeroi) {
//		// TODO Auto-generated method stub
//		return this.heroiRepository.findByHeroi(nomeHeroi);
//	}

}
