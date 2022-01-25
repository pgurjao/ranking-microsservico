package br.edu.infnet.rankingmicrosservico.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

//import com.auth0.jwt.algorithms.Algorithm;

import br.edu.infnet.rankingmicrosservico.dtos.UsuarioAutenticado;
import br.edu.infnet.rankingmicrosservico.enums.AtacanteEnum;
import br.edu.infnet.rankingmicrosservico.model.Heroi;
import br.edu.infnet.rankingmicrosservico.model.ItemRanking;
import br.edu.infnet.rankingmicrosservico.model.Turno;
import br.edu.infnet.rankingmicrosservico.repository.RankingRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

@Service
@Getter
public class RankingService {

	@Value("${ranking.maximoDeTurnos}")
	private Integer maximoDeTurnos;
	
	@Autowired
	private RankingRepository rankingRepository;

	@Autowired
	private LogService logService;

	@Autowired
	private HeroiService heroiService;

	private String errorMessage;
	
	@Value("${jwtSigningKey}")
	private String jwtSigningKey;
	
	@CacheEvict(value="rankingItem", allEntries = true)
	public Boolean calculaRanking(Integer batalhaId, UsuarioAutenticado usuarioAutenticado) {

		final int id = usuarioAutenticado.getId();
		final String nomeUsuario = usuarioAutenticado.getUsername();
		
		final String token = Jwts.builder()
			.setSubject( Integer.toString( id ) )
			.claim("username", nomeUsuario)
			.signWith( SignatureAlgorithm.HS256 , this.jwtSigningKey)
			.compact();			
		
		
		final List<Turno> batalha = this.logService.getBatalha(token,batalhaId);
		
		if(batalha.isEmpty()) 
		{
			return false;
		}
		
		int pontucaoBatalha = this.maximoDeTurnos - batalha.size();
		
		Optional<ItemRanking> rankingJaCadastrado = this.obterPorUsuario( nomeUsuario );
		
		ItemRanking rankingAtual = rankingJaCadastrado.isPresent() ?
										rankingJaCadastrado.get() :
										ItemRanking	.builder()
													.usuario(nomeUsuario)
													.pontuacao(0)
													.build();
		
		rankingAtual.incrementaPontuacao(pontucaoBatalha);
		
		this.rankingRepository.save(rankingAtual);
		
		return true;
	}
	
	// MÉTODO NÃO DEVE SER USADO 
	// SOMENTE CASO VENHA A SER NECESSÁRIO VALIDAR SE O HEROI VENCEU A BATALHA
	private boolean verificaHeroiVenceu(final List<Turno> batalha) {
		final Turno primeiroTurno = batalha.get(0);
		final String nomeHeroi = primeiroTurno.getHeroi();

		// ADICIONAR ID DE HEROI NO TURNO?
//		Heroi heroi = this.heroiService.obterPorNome(nomeHeroi);
		Heroi heroi = null;
		final AtomicInteger pontosDeVidaHeroi = new AtomicInteger(heroi.getPontosDeVida());

		final String nomeMonstro = primeiroTurno.getMonstro();

		// ADICIONAR ID DE HEROI NO TURNO?
//		Heroi monstro = this.heroiService.obterPorNome(nomeMonstro);
		Heroi monstro = null;
		final AtomicInteger pontosDeVidaMonstro = new AtomicInteger(monstro.getPontosDeVida());
		
		AtomicInteger turnosAteTerminar = new AtomicInteger();
		
		final Optional<Turno> turnoFinal = batalha.stream().filter(turno -> {

			final AtacanteEnum atacanteAtual = turno.getAtacante();

			final Integer danoAtual = turno.getDano();
			
			int pontosDeVidaMonstroAtual;
			int pontosDeVidaHeroiAtual;
			
			if (AtacanteEnum.HEROI.equals(atacanteAtual)) {
				pontosDeVidaMonstroAtual = pontosDeVidaMonstro.addAndGet( - danoAtual);
				pontosDeVidaHeroiAtual = pontosDeVidaHeroi.get();
			} else {
				pontosDeVidaMonstroAtual = pontosDeVidaMonstro.get();
				pontosDeVidaHeroiAtual = pontosDeVidaHeroi.addAndGet( - danoAtual);
			}
			
			turnosAteTerminar.set( turnosAteTerminar.get() + 1 );

			return (pontosDeVidaMonstroAtual <= 0 || pontosDeVidaHeroiAtual <= 0);
		})
		.findFirst();
		
		if(turnoFinal.isEmpty() || turnoFinal.get().getAtacante() ==  AtacanteEnum.MONSTRO) 
		{
			return false;
		}
		
		return true;
	}

	private Optional<ItemRanking> obterPorUsuario(String nomeUsuario) {
		return this.rankingRepository.findByUsuario(nomeUsuario);
	}

	@Cacheable(cacheNames = "rankingItem", key = "#root.method.name")
	public List<ItemRanking> getRanking() {
		return rankingRepository.findAllSorted();
	}

	public ItemRanking getRanking(Integer batalhaId) {
		return this.rankingRepository.getById(batalhaId);
	}

}