package br.edu.infnet.rankingmicrosservico.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioAutenticado {

	private int id;
	
	private String username;
	
}
