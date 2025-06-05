package com.example.bookstore.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {
    
    private static final String REALM = "example-realm";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    
    @Inject
    DatabaseUserDetailsService userDetailsService;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHENTICATION_SCHEME + " ")) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        String base64Token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        String usernamePassword = new String(Base64.getDecoder().decode(base64Token), StandardCharsets.UTF_8);
        
        final String[] credentials = usernamePassword.split(":", 2);
        if (credentials.length != 2) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        SecurityIdentity identity = userDetailsService.authenticate(credentials[0], credentials[1]);
        if (identity == null) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public java.security.Principal getUserPrincipal() {
                return identity.getPrincipal();
            }
            
            @Override
            public boolean isUserInRole(String role) {
                return identity.hasRole(role);
            }
            
            @Override
            public boolean isSecure() {
                return requestContext.getSecurityContext().isSecure();
            }
            
            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }
    
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE, 
                    AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
            .build());
    }
}