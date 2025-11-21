import java.util.Properties
import java.io.FileInputStream

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }
}

// Helper function to read properties from local.properties or gradle.properties
fun getLocalProperty(key: String): String? {
    val localPropertiesFile = rootDir.resolve("local.properties")
    val gradlePropertiesFile = rootDir.resolve("gradle.properties")
    
    return when {
        localPropertiesFile.exists() -> {
            val props = Properties()
            FileInputStream(localPropertiesFile).use { props.load(it) }
            props.getProperty(key)
        }
        gradlePropertiesFile.exists() -> {
            val props = Properties()
            FileInputStream(gradlePropertiesFile).use { props.load(it) }
            props.getProperty(key)
        }
        else -> null
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Public repositories first (no authentication needed)
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.juspay.in/jp-build-packages/hyper-sdk/")
        
        // Local maven repo (for local builds if you have elevate installed locally)
        maven {
            url = uri("${System.getProperty("user.home")}/maven_local_repo")
        }
        
        // GitHub Packages for lazypay libraries (elevate, muses, watchman, influx)
        // Add credentials in local.properties or gradle.properties:
        // gpr.usr=your_github_username
        // gpr.key=your_github_personal_access_token
        // Or set environment variables: GITHUB_USERNAME and GITHUB_TOKEN
        // Using content filtering to ONLY serve com.lazypay.android packages
        // This prevents Gradle from trying to resolve other dependencies (like koin) from GitHub Packages
        val githubUser = getLocalProperty("gpr.usr") ?: System.getenv("GITHUB_USERNAME")
        val githubToken = getLocalProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        
        if (!githubUser.isNullOrBlank() && !githubToken.isNullOrBlank()) {
            maven {
                name = "GitHubPackagesElevate"
                url = uri("https://maven.pkg.github.com/paysense-india-services/elevate")
                credentials {
                    username = githubUser
                    password = githubToken
                }
                content {
                    includeGroup("com.lazypay.android")
                }
            }
            maven {
                name = "GitHubPackagesWatchman"
                url = uri("https://maven.pkg.github.com/paysense-india-services/watchman")
                credentials {
                    username = githubUser
                    password = githubToken
                }
                content {
                    includeGroup("com.lazypay.android")
                }
            }
            maven {
                name = "GitHubPackagesMuses"
                url = uri("https://maven.pkg.github.com/paysense-india-services/muses")
                credentials {
                    username = githubUser
                    password = githubToken
                }
                content {
                    includeGroup("com.lazypay.android")
                }
            }
            maven {
                name = "GitHubPackagesInflux"
                url = uri("https://maven.pkg.github.com/paysense-india-services/influx")
                credentials {
                    username = githubUser
                    password = githubToken
                }
                content {
                    includeGroup("com.lazypay.android")
                }
            }
        }
    }
}

rootProject.name = "PayUFinanceAndroid"
include(":app")

