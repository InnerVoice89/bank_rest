package com.example.bankcards.utils;


import com.example.bankcards.util.EncryptionUtils;
import org.testng.annotations.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.testng.Assert.assertEquals;

public class EncryptionTest {


    @Test
    public void encryptionTest() throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {
        EncryptionUtils encryptionUtils = new EncryptionUtils("my-super-secret-encryption-key-1");
        String original = "1234";

        String encrypt = encryptionUtils.encrypt(original);
        String decrypt = encryptionUtils.decrypt(encrypt);
        assertEquals(original, decrypt);


    }


}
