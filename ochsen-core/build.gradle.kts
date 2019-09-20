plugins {
    kotlin("jvm")
    java
}

dependencies {
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.+")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.+")
}