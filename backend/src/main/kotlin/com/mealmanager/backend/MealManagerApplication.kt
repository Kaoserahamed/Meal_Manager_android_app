package com.mealmanager.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MealManagerApplication

fun main(args: Array<String>) {
    SpringApplication.run(MealManagerApplication::class.java, *args)
}
