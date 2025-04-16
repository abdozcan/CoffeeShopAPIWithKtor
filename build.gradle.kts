plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.inspektor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

swagger {
    documentation {
        generateRequestSchemas = true
        info {
            title = "Ktor Server Title"
            description = "Ktor Server Description"
            version = "1.0"
            contact {
                name = "Inspektor"
                url = "https://github.com/tabilzad/ktor-docs-plugin"
            }
        }
    }

    pluginOptions {
        format = "yaml" // or json
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)
    implementation(libs.logback.classic)
    implementation(libs.sqlite.jdbc)
    implementation(libs.smiley4.ktor.swagger)
    implementation(libs.smiley4.ktor.openapi)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
