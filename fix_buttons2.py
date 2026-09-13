import os
import re

files = [
    'app/src/main/kotlin/com/arturo254/opentune/ui/screens/library/LibraryAlbumsScreen.kt',
    'app/src/main/kotlin/com/arturo254/opentune/ui/screens/library/LibraryArtistsScreen.kt',
    'app/src/main/kotlin/com/arturo254/opentune/ui/screens/library/LibrarySongsScreen.kt',
    'app/src/main/kotlin/com/arturo254/opentune/ui/screens/library/LibraryPlaylistsScreen.kt'
]

for file_path in files:
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # The block starts right after Spacer(Modifier.width(12.dp)) and ends right before ChipsRow(
    pattern = re.compile(r'(Spacer\(Modifier\.width\(12\.dp\)\))\s*Box\(.*?contentAlignment = Alignment\.Center\s*\)\s*\{\s*Row\(.*?\)\s*\{\s*Icon\(.*?R\.drawable\.arrow_back.*?\).*?Text\(.*?\)\s*\}\s*\}\s*(ChipsRow\()', re.DOTALL)
    
    new_content = pattern.sub(r'\1\n            \2', content)
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
