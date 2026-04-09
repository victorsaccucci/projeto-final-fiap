package com.sussmartassistant.shared.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CIDTest {

    @Test
    void deveCriarCidValido() {
        CID cid = new CID("J06.9");
        assertEquals("J06.9", cid.codigo());
    }

    @Test
    void deveRejeitarCidNulo() {
        assertThrows(DomainException.class, () -> new CID(null));
    }

    @Test
    void deveRejeitarCidVazio() {
        assertThrows(DomainException.class, () -> new CID(""));
    }

    @Test
    void deveRejeitarCidEmBranco() {
        assertThrows(DomainException.class, () -> new CID("   "));
    }
}
