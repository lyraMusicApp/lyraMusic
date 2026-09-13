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

    # We want to remove the Box containing the arrow_back Icon
    pattern = re.compile(r'Box\(\s*modifier\s*=\s*Modifier\s*\.clip\(CircleShape\)\s*\.background[^{]+\{\s*Row[^{]+\{\s*Icon\(\s*painter\s*=\s*painterResource\(R\.drawable\.arrow_back\)[^}]+Text\([^}]+}[^}]+}\s*\}\s*', re.DOTALL)
    
    new_content = pattern.sub('', content)
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
