package com.example.bookstore;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class BookstoreApplication {
    public static void main(String... args) {
        System.out.println("Starting Quarkus app with custom logic! This class is not needed otherwise.");
        Quarkus.run(args); // Launches Quarkus
    }
}