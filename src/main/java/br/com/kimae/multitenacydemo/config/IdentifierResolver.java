package br.com.kimae.multitenacydemo.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import br.com.kimae.multitenacydemo.DatabaseContext;

public class IdentifierResolver implements CurrentTenantIdentifierResolver {

    private static String DEFAULT_TENANT_ID = "default";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenantId = DatabaseContext.getTenantId();
        return (currentTenantId != null) ? currentTenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
