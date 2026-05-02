package es.kitti.adoption.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdNumberEncryptionServiceTest {

    private static final String TEST_KEY_BASE64 = "ZGV2LWlkLWVuY3J5cHRpb24ta2V5LTMyYnl0ZXMhISE=";

    private IdNumberEncryptionService service;

    @BeforeEach
    void setUp() {
        service = new IdNumberEncryptionService();
        service.encryptionKeyBase64 = TEST_KEY_BASE64;
        service.init();
    }

    @Test
    void encryptDecrypt_roundtrip() {
        String plain = "12345678A";
        assertEquals(plain, service.decrypt(service.encrypt(plain)));
    }

    @Test
    void encrypt_producesDifferentCiphertextEachTime() {
        String plain = "12345678A";
        assertNotEquals(service.encrypt(plain), service.encrypt(plain));
    }

    @Test
    void init_invalidKeyLength_throwsIllegalState() {
        IdNumberEncryptionService bad = new IdNumberEncryptionService();
        bad.encryptionKeyBase64 = "dG9vc2hvcnQ="; // "tooshort"
        assertThrows(IllegalStateException.class, bad::init);
    }
}
