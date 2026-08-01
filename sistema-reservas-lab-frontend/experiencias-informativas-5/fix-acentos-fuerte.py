from pathlib import Path

def c(*codes):
    return "".join(chr(x) for x in codes)

replacements = {
    c(0x00C3, 0x00A1): "á",
    c(0x00C3, 0x00A9): "é",
    c(0x00C3, 0x00AD): "í",
    c(0x00C3, 0x00B3): "ó",
    c(0x00C3, 0x00BA): "ú",
    c(0x00C3, 0x00B1): "ñ",

    c(0x00C3, 0x0081): "Á",
    c(0x00C3, 0x0089): "É",
    c(0x00C3, 0x008D): "Í",
    c(0x00C3, 0x0093): "Ó",
    c(0x00C3, 0x009A): "Ú",
    c(0x00C3, 0x0091): "Ñ",

    c(0x00C2, 0x00BF): "¿",
    c(0x00C2, 0x00A1): "¡",
    c(0x00C2, 0x00B0): "°",
    c(0x00C2): "",

    # Casos dobles frecuentes
    c(0x00C3, 0x0192, 0x00C2, 0x00A1): "á",
    c(0x00C3, 0x0192, 0x00C2, 0x00A9): "é",
    c(0x00C3, 0x0192, 0x00C2, 0x00AD): "í",
    c(0x00C3, 0x0192, 0x00C2, 0x00B3): "ó",
    c(0x00C3, 0x0192, 0x00C2, 0x00BA): "ú",
    c(0x00C3, 0x0192, 0x00C2, 0x00B1): "ñ",
}

extensiones = ["*.html", "*.ts", "*.css"]

archivos = []
for ext in extensiones:
    archivos.extend(Path("src").rglob(ext))

corregidos = 0

for archivo in archivos:
    texto = archivo.read_text(encoding="utf-8", errors="ignore")
    original = texto

    cambio = True
    while cambio:
        antes = texto
        for malo, bueno in replacements.items():
            texto = texto.replace(malo, bueno)
        cambio = texto != antes

    if texto != original:
        archivo.write_text(texto, encoding="utf-8")
        print(f"Corregido: {archivo}")
        corregidos += 1

print(f"\nArchivos corregidos: {corregidos}")

print("\nBuscando textos que todavía tengan caracteres raros...")
pendientes = []

for archivo in archivos:
    texto = archivo.read_text(encoding="utf-8", errors="ignore")
    if "Ã" in texto or "Â" in texto:
        pendientes.append(str(archivo))

if pendientes:
    print("\nAún quedan posibles archivos con mojibake:")
    for p in pendientes:
        print(" -", p)
else:
    print("No quedan archivos con Ã o Â.")
