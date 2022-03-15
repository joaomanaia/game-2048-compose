pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Game2048"
include(":app")
include(":core")
include(":home-presentation")
include(":model")
include(":data")
include(":domain")
include(":settings-presentation")
