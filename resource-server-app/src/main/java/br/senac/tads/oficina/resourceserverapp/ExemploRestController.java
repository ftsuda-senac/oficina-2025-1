package br.senac.tads.oficina.resourceserverapp;

import java.text.MessageFormat;

import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExemploRestController {

    private String montarMensagem(Jwt token, String permissao) {
        String username = token.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
        String sub = token.getClaimAsString(StandardClaimNames.SUB);
        String mensagem = MessageFormat.format("Usuário {0} (ID {1}) com permissão \"{2}\"",
        username, sub, permissao);
        return mensagem;
    }

	@GetMapping("/peao")
	public Resposta obterMensagemPeao(JwtAuthenticationToken auth) {
        return new Resposta(montarMensagem(auth.getToken(), "PEAO"));
	}

	@GetMapping("/gerente")
	public Resposta obterMensagemGerente(JwtAuthenticationToken auth) {
        return new Resposta(montarMensagem(auth.getToken(), "GERENTE"));
	}

	@GetMapping("/diretor")
	public Resposta obterMensagemDiretor(JwtAuthenticationToken auth) {
        return new Resposta(montarMensagem(auth.getToken(), "DIRETOR"));
	}

	public record Resposta(String mensagem) {

	}

}
