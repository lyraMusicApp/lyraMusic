file_path = 'innertube/src/main/kotlin/com/arturo254/opentune/innertube/YouTube.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('if (!webClientPoTokenEnabled) return null', '')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
