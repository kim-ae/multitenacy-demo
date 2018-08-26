package br.com.kimae.multitenacydemo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseContext {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static String getTenantId() {
        return CONTEXT.get();
    }

    public static void setTenantId(String tenantId) {
        log.debug("Setting database {}", tenantId);
        CONTEXT.set(tenantId);
    }

}
