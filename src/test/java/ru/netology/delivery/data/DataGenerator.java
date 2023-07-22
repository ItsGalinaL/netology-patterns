package ru.netology.delivery.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity(String locale) {
        return  Faker.instance(new Locale(locale)).address().cityName();
    }

    public static String generateName(String locale) {
        return Faker.instance(new Locale(locale)).name().fullName();
    }

    public static String generatePhone(String locale) {
        return Faker.instance(new Locale(locale)).numerify("+79#########");
    }

    public static UserInfo generateUser(String locale) {
        return new UserInfo(generateCity(locale), generateName(locale), generatePhone(locale));
    }
}
