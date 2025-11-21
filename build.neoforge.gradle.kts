plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")}]"
		}
		required("neoforge") {
			forgeVersionRange = "[1,)"
		}
		required("fzzy_config") {
			forgeVersionRange = "[1,)"
		}
	}
}

stonecutter {
	replacements.string {
		direction = eval(current.version, ">1.21.10")
		replace("ResourceLocation", "Identifier")
	}
}

neoForge {
	version = property("deps.neoforge") as String
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = (property("deps.parchment") as String).split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.active?.version})"
		}
	}

	mods {
		register(property("mod.id") as String) {
			sourceSet(sourceSets["main"])
		}
	}
}

repositories {
	maven("https://maven.fzzyhmstrs.me/") { name = "Fzzy Config" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "KotlinForForge" }
	maven("https://jitpack.io") { name = "Jitpack" }
}

dependencies {
	implementation( "me.fzzyhmstrs:fzzy_config:${prop("deps.fzzy_config")}+neoforge")
	implementation("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	jarJar("com.moulberry:mixinconstraints:${prop("deps.mixinconstraints")}")
	implementation("com.github.ramixin:mixson-neoforge:${prop("deps.mixson")}")
	jarJar("com.github.ramixin:mixson-neoforge:${prop("deps.mixson")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
