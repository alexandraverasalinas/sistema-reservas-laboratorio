$files = Get-ChildItem -Path "src/app" -Recurse -Include *.html,*.ts,*.css

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8

    $content = $content `
      -replace "informaciÃ³n", "información" `
      -replace "InformaciÃ³n", "Información" `
      -replace "sesiÃ³n", "sesión" `
      -replace "SesiÃ³n", "Sesión" `
      -replace "contraseÃ±a", "contraseña" `
      -replace "ContraseÃ±a", "Contraseña" `
      -replace "electrÃ³nico", "electrónico" `
      -replace "ElectrÃ³nico", "Electrónico" `
      -replace "aquÃ­", "aquí" `
      -replace "AquÃ­", "Aquí" `
      -replace "dÃ­a", "día" `
      -replace "DÃ­a", "Día" `
      -replace "rÃ¡pida", "rápida" `
      -replace "RÃ¡pida", "Rápida" `
      -replace "prÃ¡ctica", "práctica" `
      -replace "PrÃ¡ctica", "Práctica" `
      -replace "acadÃ©mico", "académico" `
      -replace "AcadÃ©mico", "Académico" `
      -replace "acadÃ©micos", "académicos" `
      -replace "AcadÃ©micos", "Académicos" `
      -replace "Ãºltimas", "últimas" `
      -replace "Ãšltimas", "Últimas" `
      -replace "Ã©xito", "éxito" `
      -replace "Ã‰xito", "Éxito" `
      -replace "estÃ¡", "está" `
      -replace "EstÃ¡", "Está" `
      -replace "estÃ¡s", "estás" `
      -replace "EstÃ¡s", "Estás" `
      -replace "tÃº", "tú" `
      -replace "TÃº", "Tú"

    Set-Content -Path $file.FullName -Value $content -Encoding UTF8
}

Write-Host "Acentos corregidos correctamente." -ForegroundColor Green
