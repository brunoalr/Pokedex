plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.sqlDelight)
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    jvm("desktop")

    androidTarget {
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Pokedex the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0.0"
        ios.deploymentTarget = "18.3"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            export(libs.decompose)
            export(libs.essenty.lifecycle)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose
                with(compose) {
                    api(runtime)
                    api(foundation)
                    api(material)
                    api(material3)
                    api(materialIconsExtended)
                }

                // Ktor
                api(libs.ktor.clientCore)
                api(libs.ktor.serializationKotlinxJson)
                api(libs.ktor.clientContentNegotiation)
                api(libs.ktor.clientLogging)

                // Logback for ktor logging
                implementation(libs.logbackClassic)

                // SqlDelight
                api(libs.sqlDelight.coroutinesExtensions)
                api(libs.sqlDelight.primitiveAdapters)

                // Koin
                api(libs.koin.core)
                api(libs.koin.test)

                // KotlinX Serialization Json
                implementation(libs.kotlinxSerializationJson)

                // Coroutines
                implementation(libs.coroutinesCore)

                // MVIKotlin
                api(libs.mvikotlin)
                api(libs.mvikotlinMain)
                api(libs.mvikotlinExtensionsCoroutines)


                // Decompose
                api(libs.decompose)
                api(libs.decompose.extensionsCompose)

                // Image Loading
                api(libs.imageLoader)
                implementation(libs.essenty.lifecycle)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                // Ktor
                implementation(libs.ktor.clientAndroid)

                // SqlDelight
                implementation(libs.sqlDelight.androidDriver)

                // Koin
                implementation(libs.koin.android)
            }
        }
        val androidUnitTest by getting

        val desktopMain by getting {
            dependsOn(commonMain)

            dependencies {
                // Ktor
                implementation(libs.ktor.clientJava)

                // SqlDelight
                implementation(libs.sqlDelight.sqliteDriver)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                // Ktor
                implementation(libs.ktor.clientDarwin)

                // SqlDelight
                implementation(libs.sqlDelight.nativeDriver)

                // TouchLab
                implementation(libs.statelyCommon)
            }
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

android {
    namespace = "com.mocoding.pokedex"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("PokemonDatabase") {
            packageName.set("com.mocoding.pokedex.core.database")
        }
    }
}