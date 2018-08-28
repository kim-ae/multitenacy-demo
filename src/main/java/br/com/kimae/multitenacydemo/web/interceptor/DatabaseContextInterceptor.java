package br.com.kimae.multitenacydemo.web.interceptor;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.kimae.multitenacydemo.config.database.DatabaseContext;
import br.com.kimae.multitenacydemo.config.security.AppPrincipal;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class DatabaseContextInterceptor {

    @Before("@annotation(br.com.kimae.multitenacydemo.web.interceptor.DatabaseContextSensitivy)")
    public void before(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppPrincipal principal = (AppPrincipal) auth.getPrincipal();
        log.debug("Setting tenant for this thread: {}",principal.getClientDatabase());
        DatabaseContext.setTenantId(principal.getClientDatabase());
    }
}
