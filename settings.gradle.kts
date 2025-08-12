pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChatBot"

include(
    ":app",
    ":features:auth",
    ":features:chat",
    ":navigation",
    ":data:remote",
    ":data:repository-impl",
    ":domain:repository",
    ":domain:common-entity",
    ":features:home"

)
