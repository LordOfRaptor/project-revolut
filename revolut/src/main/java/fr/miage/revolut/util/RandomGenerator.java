package fr.miage.revolut.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@NoArgsConstructor
public class RandomGenerator {

    private SecureRandom rand = new SecureRandom();



    private String generateRandomChars(String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(rand.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }

    public String generateNumericString(int length){
        return generateRandomChars("0123456789",length);
    }
}
