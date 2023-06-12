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
        // https://mvnrepository.com/artifact/org.apache.commons/commons-collections4
        const val COMMONS_COLLECTIONS4 = "org.apache.commons:commons-collections4:${Versions.COMMONS_COLLECTIONS4}"

        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        const val COMMONS_LANG3 = "org.apache.commons:commons-lang3:${Versions.COMMONS_LANG3}"

        // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
        const val JACKSON_DATABIND = "com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}"

        // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
        const val JACKSON_DATATYPE_JSR310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.JACKSON}"

        const val JAVET = "com.caoccao.javet:javet:${Versions.JAVET}"
        const val JAVET_MACOS = "com.caoccao.javet:javet-macos:${Versions.JAVET}"
        const val JUNIT_BOM = "org.junit:junit-bom:${Versions.JUNIT}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter"

        // https://mvnrepository.com/artifact/info.picocli/picocli
        const val PICOCLI = "info.picocli:picocli:${Versions.PICOCLI}"

        const val SLF4J_API = "org.slf4j:slf4j-api:${Versions.SLF4J}"
        const val SLF4J_LOG4J12 = "org.slf4j:slf4j-log4j12:${Versions.SLF4J}"
    }

    object Versions {
        const val COMMONS_COLLECTIONS4 = "4.4"
        const val COMMONS_LANG3 = "3.12.0"
        const val JACKSON = "2.15.2"
        const val JAVET = "2.2.0"
        const val JUNIT = "5.9.1"
        const val PICOCLI = "4.7.4"
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
    implementation(Config.Projects.COMMONS_COLLECTIONS4)
    implementation(Config.Projects.COMMONS_LANG3)
    implementation(Config.Projects.JACKSON_DATABIND)
    implementation(Config.Projects.JACKSON_DATATYPE_JSR310)
    if (OperatingSystem.current().isMacOsX) {
        implementation(Config.Projects.JAVET_MACOS)
    } else {
        implementation(Config.Projects.JAVET)
    }
    implementation(Config.Projects.PICOCLI)
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
