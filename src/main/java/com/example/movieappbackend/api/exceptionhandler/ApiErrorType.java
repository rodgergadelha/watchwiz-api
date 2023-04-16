package com.example.movieappbackend.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ApiErrorType {
	
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_DE_NGOCIO("/erro-de-negocio", "Erro de negócio"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sitema");
	
	private String title;
	private String uri;
	
	ApiErrorType(String path, String title) {
		this.uri = "https://localhost:8080" + path;
        this.title = title;
	}
}
