package br.edu.infnet.rankingmicrosservico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UsuarioDiferenteDoAutenticadoException extends Exception {

}
