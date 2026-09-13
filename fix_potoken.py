file_path = 'innertube/src/main/kotlin/com/arturo254/opentune/innertube/YouTube.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

target = '''        val webFallback = poToken?.takeIf { it.isNotBlank() }
        if (webFallback != null) return webFallback

        return null
    }'''

replacement = '''        val webFallback = poToken?.takeIf { it.isNotBlank() }
        if (webFallback != null) return webFallback

        val identifier = innerTube.visitorData ?: "visitorData"
        return PoTokenGenerator.generateContentToken(identifier, videoId)
    }'''

content = content.replace(target, replacement)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
