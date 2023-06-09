/*
 * Copyright (c) 2023. caoccao.com Sam Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.internal.os.OperatingSystem

object Config {
    object Projects {
        // https://mvnrepository.com/artifact/commons-cli/commons-cli
        const val COMMONS_CLI = "commons-cli:commons-cli:${Versions.COMMONS_CLI}"
        // https://mvnrepository.com/artifact/org.apache.commons/commons-collections4
        const val COMMONS_COLLECTIONS4 = "org.apache.commons:commons-collections4:${Versions.COMMONS_COLLECTIONS4}"
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        const val COMMONS_LANG3 = "org.apache.commons:commons-lang3:${Versions.COMMONS_LANG3}"
        const val JAVET = "com.caoccao.javet:javet:${Versions.JAVET}"
        const val JAVET_MACOS = "com.caoccao.javet:javet-macos:${Versions.JAVET}"
        const val JUNIT_BOM = "org.junit:junit-bom:${Versions.JUNIT}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter"
        const val SLF4J_API = "org.slf4j:slf4j-api:${Versions.SLF4J}"
        const val SLF4J_LOG4J12 = "org.slf4j:slf4j-log4j12:${Versions.SLF4J}"
    }

    object Versions {
        const val COMMONS_CLI = "1.5.0"
        const val COMMONS_COLLECTIONS4 = "4.4"
        const val COMMONS_LANG3 = "3.12.0"
        const val JAVET = "2.2.0"
        const val JUNIT = "5.9.1"
        const val SLF4J = "2.0.7"
    }
}

plugins {
    id("java")
}

group = "com.caoccao.jaspiler"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Config.Projects.COMMONS_CLI)
    implementation(Config.Projects.COMMONS_COLLECTIONS4)
    implementation(Config.Projects.COMMONS_LANG3)
    if (OperatingSystem.current().isMacOsX) {
        implementation(Config.Projects.JAVET_MACOS)
    } else {
        implementation(Config.Projects.JAVET)
    }
    implementation(Config.Projects.SLF4J_API)
    implementation(Config.Projects.SLF4J_LOG4J12)
    testImplementation(platform(Config.Projects.JUNIT_BOM))
    testImplementation(Config.Projects.JUNIT_JUPITER)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.caoccao.jaspiler.JaspilerMain"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("manual")
    }
}

tasks.register<Test>("manualTest") {
    useJUnitPlatform {
        includeTags("manual")
    }
}
