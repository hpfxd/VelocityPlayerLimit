enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            alias("velocity-api").to("com.velocitypowered", "velocity-api")
                .version("3.0.1")
        }
    }
}

rootProject.name = "velocity-player-limit"

