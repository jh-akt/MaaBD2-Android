package com.maaframework.android.bd2

import com.maaframework.android.catalog.InterfaceCatalogLoader
import com.maaframework.android.host.MaaHostViewModel
import com.maaframework.android.project.MaaProjectManifest
import com.maaframework.android.runtime.ProjectLocalOverlay
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Test

class Bd2CatalogTest {
    private val assets = File(requireNotNull(System.getProperty("bd2.assets")))
    private val manifest = Json.decodeFromString<MaaProjectManifest>(File(assets, "maa_project_manifest.json").readText())
    private val catalog = InterfaceCatalogLoader(supportedControllers = manifest.supportedControllers)
        .loadFromDirectory(File(assets, "project"))

    @Test fun `upstream Android catalog keeps runnable tasks and both resource layers`() {
        assertTrue(catalog.tasks.size > 20)
        assertEquals(setOf("ADB"), catalog.resources.map { it.id }.toSet())
        assertEquals(listOf("./resource/base", "./resource/android_native"), catalog.resources.single().paths)
        assertTrue(catalog.tasks.all { it.controllerSupported })
        assertTrue(catalog.tasks.any { it.id == manifest.defaultTaskId })
        val checked = MaaHostViewModel.defaultCheckedTaskIds(catalog.tasks, manifest.defaultTaskId)
        assertTrue(checked.contains(manifest.defaultTaskId))
        assertTrue(checked.contains("[执行]快速狩猎扫荡"))
        assertTrue(File(assets, "project/resource/base/pipeline").isDirectory)
        assertTrue(File(assets, "project/resource/android_native/pipeline").isDirectory)
        assertEquals("com.neowizgames.game.browndust2", manifest.packageNameFor("ADB"))
    }

    @Test fun `bundled resources and online updates use the same local adapter`() {
        val overlay = ProjectLocalOverlay { File(assets, "${manifest.localOverlayPath}/$it").readBytes() }
        val marker = Json.parseToJsonElement(File(assets, "project/.maabd2-generated.json").readText()).jsonObject
        assertEquals(marker.getValue("overlay_sha256").jsonPrimitive.content, overlay.fingerprint())
        assertEquals(marker.getValue("tag").jsonPrimitive.content, manifest.githubRelease!!.bundledVersion)
        assertTrue(manifest.hasResourceUpdateSource())
        assertTrue(File(assets, "project/agent/android_entry.py").readText().contains("MFABD2_DATA_DIR"))
    }

    @Test fun `task options and presets survive shared host parsing`() {
        assertTrue(catalog.tasks.first { it.id == "[执行]快速狩猎扫荡" }.options.isNotEmpty())
        assertTrue(catalog.presets.isNotEmpty())
        val ids = catalog.tasks.map { it.id }.toSet()
        assertTrue(catalog.presets.all { preset -> preset.taskIds.all { it in ids } })
        assertTrue(catalog.tasks.none { it.id.contains("PC") || it.id == "多存档" })
    }
}
