package br.com.kimae.multitenacydemo.config.database;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentifierResolver implements CurrentTenantIdentifierResolver {

    private static String DEFAULT_TENANT_ID = "default";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenantId = DatabaseContext.getTenantId();
        log.debug("Retrieving current tenant for this thread: {}", currentTenantId);
        return (currentTenantId != null) ? currentTenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
