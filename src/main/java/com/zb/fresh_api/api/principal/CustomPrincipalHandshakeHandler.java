package com.zb.fresh_api.api.principal;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

public class CustomPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(final ServerHttpRequest request, final WebSocketHandler wsHandler, final Map<String, Object> attributes) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return super.determineUser(request, wsHandler, attributes);
        }

        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new UsernamePasswordAuthenticationToken(authentication.getName(), "", userDetails.getAuthorities());
    }
}
