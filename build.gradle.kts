import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"
	/**
	 * detekt
	 *
	 * URL
	 * - https://github.com/detekt/detekt
	 * GradlePlugins(plugins.gradle.org)
	 * - https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt
	 * Main用途
	 * - Linter/Formatter
	 * Sub用途
	 * - 無し
	 * 概要
	 * KotlinのLinter/Formatter
	 */
	id("io.gitlab.arturbosch.detekt") version "1.23.3"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	/**
	 * detektの拡張: detekt-formatting
	 *
	 * 概要
	 * - formattingのルール
	 * - 基本はktlintと同じ
	 * - format自動適用オプションの autoCorrect が使えるようになる
	 */
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


/**
 * detektの設定
 *
 * 基本的に全て `detekt-override.yml` で設定する
 */
detekt {
	/**
	 * ./gradlew detektGenerateConfig でdetekt.ymlが生成される(バージョンが上がる度に再生成する)
	 */
	config = files(
			"$projectDir/config/detekt/detekt.yml",
			"$projectDir/config/detekt/detekt-override.yml",
	)
}
