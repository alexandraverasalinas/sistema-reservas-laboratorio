from pathlib import Path

replacements = {
    "Ã¡": "á",
    "Ã©": "é",
    "Ã­": "í",
    "Ã³": "ó",
    "Ãº": "ú",
    "Ã±": "ñ",
    "Ã": "Á",
    "Ã‰": "É",
    "Ã": "Í",
    "Ã“": "Ó",
    "Ãš": "Ú",
    "Ã‘": "Ñ",
    "Â¿": "¿",
    "Â¡": "¡",
    "Â°": "°",
    "Â": "",
    "â€“": "–",
    "â€”": "—",
    "â€˜": "‘",
    "â€™": "’",
    "â€œ": "“",
    "â€": "”",
    "â€¦": "…"
}

extensions = ["*.html", "*.ts", "*.css"]

files = []
for ext in extensions:
    files.extend(Path("src").rglob(ext))

changed = 0

for file in files:
    text = file.read_text(encoding="utf-8", errors="ignore")
    original = text

    for bad, good in replacements.items():
        text = text.replace(bad, good)

    if text != original:
        file.write_text(text, encoding="utf-8")
        print(f"Corregido: {file}")
        changed += 1

print(f"\nArchivos corregidos: {changed}")
