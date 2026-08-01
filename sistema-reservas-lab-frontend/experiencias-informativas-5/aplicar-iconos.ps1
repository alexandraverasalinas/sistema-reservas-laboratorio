chcp 65001 | Out-Null

Write-Host "Instalando Bootstrap Icons..." -ForegroundColor Cyan
npm install bootstrap-icons

Write-Host "Actualizando angular.json..." -ForegroundColor Cyan

$angularPath = "angular.json"

if (Test-Path $angularPath) {
    $angular = Get-Content $angularPath -Raw

    if ($angular -notmatch "bootstrap-icons") {
        $angular = $angular -replace '"styles":\s*\[\s*"src/styles.css"\s*\]', '"styles": [
              "node_modules/bootstrap-icons/font/bootstrap-icons.css",
              "src/styles.css"
            ]'

        Set-Content -Path $angularPath -Value $angular -Encoding UTF8
        Write-Host "angular.json actualizado." -ForegroundColor Green
    } else {
        Write-Host "angular.json ya tiene Bootstrap Icons." -ForegroundColor Yellow
    }
}

Write-Host "Agregando estilos globales..." -ForegroundColor Cyan

$stylesPath = "src/styles.css"

$globalCss = @"

/* ICONOS GLOBALES EI5 */
.bi {
  line-height: 1;
}

.menu a {
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu a i {
  width: 22px;
  font-size: 18px;
  text-align: center;
}

.logout {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
}

button i,
a i {
  pointer-events: none;
}

.btn-primary,
.btn-login,
.btn-refresh,
.btn-clear,
.btn-cancel,
.btn-reservar,
.btn-edit,
.btn-delete,
.logout {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.icon-box {
  width: 48px;
  height: 48px;
  border-radius: 16px;
  background: #eaf4ff;
  color: #0057b8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 23px;
  flex-shrink: 0;
}

.icon-success {
  color: #16a34a;
}

.icon-danger {
  color: #dc2626;
}

.icon-primary {
  color: #0057b8;
}

.icon-purple {
  color: #6366f1;
}

.icon-warning {
  color: #f59e0b;
}

.feature-card span {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.18);
  display: flex;
  align-items: center;
  justify-content: center;
}

.feature-card span i {
  font-size: 22px;
}

.legend {
  gap: 16px;
}

.legend span {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.block-icon i {
  font-size: 24px;
}

.empty-icon i {
  font-size: 42px;
}

"@

if (Test-Path $stylesPath) {
    $styles = Get-Content $stylesPath -Raw

    if ($styles -notmatch "ICONOS GLOBALES EI5") {
        Add-Content -Path $stylesPath -Value $globalCss
        Write-Host "Estilos globales agregados." -ForegroundColor Green
    } else {
        Write-Host "Los estilos globales ya existen." -ForegroundColor Yellow
    }
}

function Replace-InFile {
    param (
        [string]$Path
    )

    if (!(Test-Path $Path)) {
        return
    }

    $content = Get-Content $Path -Raw

    # MENUS ADMIN
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/dashboard"[^>]*>)[\s\S]*?Dashboard\s*</a>', '$1<i class="bi bi-speedometer2"></i> Dashboard</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/laboratorios"[^>]*>)[\s\S]*?Laboratorios\s*</a>', '$1<i class="bi bi-pc-display-horizontal"></i> Laboratorios</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/horarios"[^>]*>)[\s\S]*?Horarios\s*</a>', '$1<i class="bi bi-clock-history"></i> Horarios</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/profesores"[^>]*>)[\s\S]*?Profesores\s*</a>', '$1<i class="bi bi-person-video3"></i> Profesores</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/alumnos"[^>]*>)[\s\S]*?Alumnos\s*</a>', '$1<i class="bi bi-mortarboard"></i> Alumnos</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/admin/reservas"[^>]*>)[\s\S]*?Reservas\s*</a>', '$1<i class="bi bi-calendar-check"></i> Reservas</a>')

    # MENUS ALUMNO
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/alumno/calendario"[^>]*>)[\s\S]*?Calendario\s*</a>', '$1<i class="bi bi-calendar-week"></i> Calendario</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/alumno/mis-reservas"[^>]*>)[\s\S]*?Mis reservas\s*</a>', '$1<i class="bi bi-journal-check"></i> Mis reservas</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/alumno/perfil"[^>]*>)[\s\S]*?Perfil\s*</a>', '$1<i class="bi bi-person-circle"></i> Perfil</a>')

    # MENUS PROFESOR
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/profesor/calendario"[^>]*>)[\s\S]*?Calendario\s*</a>', '$1<i class="bi bi-calendar-week"></i> Calendario</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/profesor/mis-reservas"[^>]*>)[\s\S]*?Mis reservas\s*</a>', '$1<i class="bi bi-journal-check"></i> Mis reservas</a>')
    $content = [regex]::Replace($content, '(<a[^>]*routerLink="/profesor/perfil"[^>]*>)[\s\S]*?Perfil\s*</a>', '$1<i class="bi bi-person-circle"></i> Perfil</a>')

    # CERRAR SESION
    $content = [regex]::Replace($content, '<button class="logout" \(click\)="cerrarSesion\(\)">[\s\S]*?Cerrar sesión[\s\S]*?</button>', '<button class="logout" (click)="cerrarSesion()"><i class="bi bi-box-arrow-right"></i> Cerrar sesión</button>')

    # LOGIN FEATURE CARDS
    $content = [regex]::Replace($content, '(<div class="feature-card">\s*<span>)[\s\S]*?(</span>\s*<div>\s*<strong>Calendario</strong>)', '$1<i class="bi bi-calendar-week"></i>$2')
    $content = [regex]::Replace($content, '(<div class="feature-card">\s*<span>)[\s\S]*?(</span>\s*<div>\s*<strong>Acceso seguro</strong>)', '$1<i class="bi bi-shield-lock"></i>$2')
    $content = [regex]::Replace($content, '(<div class="feature-card">\s*<span>)[\s\S]*?(</span>\s*<div>\s*<strong>Laboratorios</strong>)', '$1<i class="bi bi-pc-display-horizontal"></i>$2')

    # LOGIN INPUTS
    $content = [regex]::Replace($content, '<span>[\s\S]*?</span>\s*(<input\s+type="email")', '<span><i class="bi bi-envelope"></i></span>$1')
    $content = [regex]::Replace($content, '<span>[\s\S]*?</span>\s*(<input\s+type="password")', '<span><i class="bi bi-lock"></i></span>$1')

    # BOTON LOGIN
    $content = [regex]::Replace($content, '<button type="submit" class="btn-login" \[disabled\]="cargando">[\s\S]*?</button>', '<button type="submit" class="btn-login" [disabled]="cargando"><i class="bi bi-box-arrow-in-right"></i> {{ cargando ? ''Validando acceso...'' : ''Iniciar sesión'' }}</button>')

    # TARJETAS RESUMEN POR CLASE
    $content = [regex]::Replace($content, '(<div class="summary-card total">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-journal-check"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card active">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-check-circle"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card canceled">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-x-circle"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card finished">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-flag"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card lab">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-pc-display-horizontal"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card teacher">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-person-video3"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card student">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-mortarboard"></i></span>')
    $content = [regex]::Replace($content, '(<div class="summary-card today">\s*)<span[\s\S]*?</span>', '$1<span class="icon-box"><i class="bi bi-calendar-day"></i></span>')

    # BOTONES FILTROS / ACTUALIZAR / CANCELAR
    $content = [regex]::Replace($content, '<button class="btn-clear" type="button" \(click\)="limpiarFiltros\(\)">[\s\S]*?Limpiar filtros[\s\S]*?</button>', '<button class="btn-clear" type="button" (click)="limpiarFiltros()"><i class="bi bi-eraser"></i> Limpiar filtros</button>')

    $content = [regex]::Replace($content, '<button class="btn-refresh" type="button" \(click\)="listarMisReservas\(\)">[\s\S]*?Actualizar[\s\S]*?</button>', '<button class="btn-refresh" type="button" (click)="listarMisReservas()"><i class="bi bi-arrow-clockwise"></i> Actualizar</button>')

    $content = [regex]::Replace($content, '<button class="btn-refresh" type="button" \(click\)="listarReservas\(\)">[\s\S]*?Actualizar[\s\S]*?</button>', '<button class="btn-refresh" type="button" (click)="listarReservas()"><i class="bi bi-arrow-clockwise"></i> Actualizar</button>')

    $content = [regex]::Replace($content, '<button\s+class="btn-cancel"[\s\S]*?\(click\)="cancelarReserva\(reserva\.idReserva\)"[\s\S]*?>[\s\S]*?Cancelar[\s\S]*?</button>', '<button class="btn-cancel" *ngIf="reserva.estado === ''RESERVADO''" (click)="cancelarReserva(reserva.idReserva)"><i class="bi bi-x-lg"></i> Cancelar</button>')

    # CALENDARIO LEYENDA
    $content = [regex]::Replace($content, '<div class="legend">[\s\S]*?</div>', '<div class="legend"><span><i class="bi bi-check-circle-fill icon-success"></i> Disponible</span><span><i class="bi bi-x-circle-fill icon-danger"></i> Ocupado</span></div>')

    # CALENDARIO BLOQUE ICONO
    $content = [regex]::Replace($content, '<div class="block-icon">[\s\S]*?</div>', '<div class="block-icon"><i class="bi" [class.bi-check-circle-fill]="bloque.estado === ''DISPONIBLE''" [class.bi-x-circle-fill]="bloque.estado === ''OCUPADO''" [class.icon-success]="bloque.estado === ''DISPONIBLE''" [class.icon-danger]="bloque.estado === ''OCUPADO''"></i></div>')

    # BOTON RESERVAR
    $content = [regex]::Replace($content, '<button\s+\*ngIf="bloque\.estado === ''DISPONIBLE''"[\s\S]*?class="btn-reservar"[\s\S]*?\(click\)="reservar\(bloque\)"[\s\S]*?>[\s\S]*?Reservar[\s\S]*?</button>', '<button *ngIf="bloque.estado === ''DISPONIBLE''" class="btn-reservar" (click)="reservar(bloque)"><i class="bi bi-calendar-plus"></i> Reservar</button>')

    # EMPTY ICON
    $content = [regex]::Replace($content, '<div class="empty-icon">[\s\S]*?</div>', '<div class="empty-icon"><i class="bi bi-calendar-week"></i></div>')

    # DASHBOARD ULTIMAS RESERVAS ICON
    $content = [regex]::Replace($content, '<div class="reservation-icon">[\s\S]*?</div>', '<div class="reservation-icon"><i class="bi bi-calendar-check"></i></div>')

    Set-Content -Path $Path -Value $content -Encoding UTF8
    Write-Host "Actualizado: $Path" -ForegroundColor Green
}

Write-Host "Reemplazando emojis por iconos..." -ForegroundColor Cyan

$files = @(
    "src/app/auth/login/login.html",

    "src/app/admin/dashboard/dashboard.html",
    "src/app/admin/laboratorios/laboratorios.html",
    "src/app/admin/horarios/horarios.html",
    "src/app/admin/profesores/profesores.html",
    "src/app/admin/alumnos/alumnos.html",
    "src/app/admin/reservas/reservas.html",

    "src/app/alumno/calendario/calendario.html",
    "src/app/alumno/mis-reservas/mis-reservas.html",
    "src/app/alumno/perfil/perfil.html",

    "src/app/profesor/calendario/calendario.html",
    "src/app/profesor/mis-reservas/mis-reservas.html",
    "src/app/profesor/perfil/perfil.html"
)

foreach ($file in $files) {
    Replace-InFile -Path $file
}

Write-Host ""
Write-Host "Proceso terminado correctamente." -ForegroundColor Green
Write-Host "Ahora ejecuta: ng serve" -ForegroundColor Cyan
