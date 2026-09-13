file_path = 'innertube/src/main/kotlin/com/arturo254/opentune/innertube/YouTube.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

target = '''    internal fun resolveGvsPoToken(): String? {
        if (!webClientPoTokenEnabled) return null
        return poTokenGvs?.takeIf { it.isNotBlank() }
            ?: poToken?.takeIf { it.isNotBlank() }
    }'''

replacement = '''    internal fun resolveGvsPoToken(): String? {
        if (!webClientPoTokenEnabled) return null
        val token = poTokenGvs?.takeIf { it.isNotBlank() } ?: poToken?.takeIf { it.isNotBlank() }
        if (token != null) return token
        
        val identifier = innerTube.visitorData ?: "visitorData"
        return PoTokenGenerator.generateSessionToken(identifier)
    }'''

content = content.replace(target, replacement)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
