plugins {
	id("mod-platform")
	id("fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = prop("deps.minecraft").replace("pre", "beta.")
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${libs.fabric.loader.get().version}"
		}
		required("fzzy_config") {
			versionRange = "*"
		}
		optional("modmenu") {}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/${prop("mod.id")}.accesswidener")
}

stonecutter {
	replacements.string {
		direction = eval(current.version, ">1.21.10")
		replace("ResourceLocation", "Identifier")
	}
}

repositories {
	maven("https://maven.fzzyhmstrs.me/") { name = "Fzzy Config" }
	maven("https://maven.terraformersmc.com/" ) { name = "TerraformersMC" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "KotlinForForge" }
	maven("https://jitpack.io") { name = "Jitpack" }
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation(libs.fabric.loader)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("me.fzzyhmstrs:fzzy_config:${prop("deps.fzzy_config")}")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	implementation("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	include("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	modImplementation("com.github.ramixin:mixson-fabric:${prop("deps.mixson")}") {
		exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
	}
	include("com.github.ramixin:mixson-fabric:${prop("deps.mixson")}") {
		exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
	}
}
