package com.procergs.quarkus.infra.security;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.mutiny.Uni;

/**
 * Busca as permissões do usuário no sistema e adiciona no contexto do usuário.
 * 
 * Desta forma é possível usar a anotação @RolesAllowed para verificar as permissões.
 * 
 * Faz um cache para evitar chamar o serviço de permissões do SOEWS a cada transação da aplicação. 
 * 
 * @author mauricio-wodarski
 *
 */
@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor {
	
	private static final Logger LOG = Logger.getLogger(RolesAugmentor.class);

	// TODO Trocar por um LRU Map para evitar OOM
	private Map<String, Set<String>> cache = new ConcurrentHashMap<>();
	
	@ConfigProperty(name = "procergs.sistema") 
	String sistema;
	
	@ConfigProperty(name = "procergs.soews.url") 
	String soewsURL;
	
    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
    	LOG.info("Principal "+ identity.getPrincipal());
    	return Uni.createFrom().item(build(identity));
    }

    private Supplier<SecurityIdentity> build(SecurityIdentity identity) {
        if(identity.isAnonymous()) {
            return () -> identity;
        } else {
            // create a new builder and copy principal, attributes, credentials and roles from the original identity
            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
            
            // add custom role source here
            builder.addRoles(getPermissoes(identity));
            return builder::build;
        }
    }

	private Set<String> getPermissoes(SecurityIdentity identity) {
		TokenCredential credential = identity.getCredential(TokenCredential.class);
		DefaultJWTCallerPrincipal principal = (DefaultJWTCallerPrincipal) identity.getPrincipal();
		String tokenID = principal.getTokenID();
		
		LOG.info("Token ID " + tokenID);

		if (cache.containsKey(tokenID)) {
			LOG.info("Permissões encontradas no cache. Não será feita consulta no SOEWS");
			return cache.get(tokenID);
		}
		
		Builder target = ClientBuilder.newClient()
				.target(this.soewsURL + "/permissoes/{codUsuario}/acoes/{sistema}")
				.resolveTemplate("codUsuario", principal.getName())
				.resolveTemplate("sistema", this.sistema)
				.request()
				.header("Authorization", "Bearer " + credential.getToken());
		
		try (Response response = target.get()) {
			Set<String> permissoes = response.readEntity(Set.class);
			LOG.info(String.format("Foram encontradas %d permissões para o usuário %s no sistema %s", permissoes.size(), principal.getName(), this.sistema));
			LOG.info("Adicionando permissões no cache");
			cache.put(tokenID, permissoes);
			return permissoes;
		}
		
	}
    
}