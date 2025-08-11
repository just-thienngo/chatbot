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
include(":app")
include(":features:auth")
include(":features:chat")
include(":navigation")
include(":data:remote")
include(":data:repository-impl")
include(":domain:repository")
include(":domain:common-entity")
include(":features:home")
