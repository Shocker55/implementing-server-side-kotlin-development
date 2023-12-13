package com.example.implementingserversidekotlindevelopment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * ImplementingServerSideKotlinDevelopmentApplication
 *
 */
@SpringBootApplication
class ImplementingServerSideKotlinDevelopmentApplication

/**
 * main
 *
 * サンプルアプリケーションのメイン関数
 *
 * @param args
 */
fun main(args: Array<String>) {
    @Suppress("SpreadOperator") // 追加
    runApplication<ImplementingServerSideKotlinDevelopmentApplication>(*args)
}
