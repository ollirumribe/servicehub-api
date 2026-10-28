package com.servicehub.servicehub_api.Util;

import org.apache.commons.text.StringEscapeUtils;

public final class Sanitizer {

    private Sanitizer() {
    }

    // Remove espaços das pontas e transforma <, >, ", & em entidades HTML (evita XSS)
    public static String limpar(String texto) {
        if (texto == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(texto.trim());
    }

    // Padroniza o e-mail: sem espaços e em minúsculas
    public static String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
