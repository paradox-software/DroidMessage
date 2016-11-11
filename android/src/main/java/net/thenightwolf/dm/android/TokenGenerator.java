package net.thenightwolf.dm.android;

import android.util.Log;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.security.SecureRandom;

public class TokenGenerator {

        private static Key key = MacProvider.generateKey();

        public static String generateToken() {
            return Jwts.builder()
                    .setIssuer("DroidMessage")
                    .signWith(SignatureAlgorithm.HS512, key)
                    .compact();
        }

        public static boolean validateToken(String jwt) {
            try{
                return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getIssuer().equals("DroidMessage");
            } catch (Exception e){
                return false;
            }
        }


}