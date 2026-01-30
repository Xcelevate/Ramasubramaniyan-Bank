package com.training.mybank.util;

import javax.persistence.EntityManager;
import java.util.Random;

public class AccountNumberGenerator {

    private static final int MIN = 10000000;
    private static final int MAX = 99999999;
    private static final Random RANDOM = new Random();

    private AccountNumberGenerator() {
        // utility class
    }

    public static String generate(EntityManager em) {

        String accountNumber;

        while (true) {
            int number = MIN + RANDOM.nextInt(MAX - MIN + 1);
            accountNumber = String.valueOf(number);

            // check uniqueness using JPA
            Long count = em.createQuery(
                    "SELECT COUNT(a) FROM AccountEntity a WHERE a.accountNumber = :acc",
                    Long.class).setParameter("acc", accountNumber)
                    .getSingleResult();

            if (count == 0) {
                break; // unique number found
            }
        }

        return accountNumber;
    }
}
