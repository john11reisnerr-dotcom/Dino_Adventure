# Localiza um Java compativel sem modificar PATH ou as configuracoes do computador.
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot

function Testar-Java([string]$Caminho) {
    if (-not (Test-Path -LiteralPath $Caminho -PathType Leaf)) { return $false }
    $processo = New-Object System.Diagnostics.Process
    $processo.StartInfo.FileName = $Caminho
    $processo.StartInfo.Arguments = '--version'
    $processo.StartInfo.UseShellExecute = $false
    $processo.StartInfo.CreateNoWindow = $true
    $processo.StartInfo.RedirectStandardOutput = $true
    $processo.StartInfo.RedirectStandardError = $true
    try {
        [void]$processo.Start()
        if (-not $processo.WaitForExit(5000)) {
            $processo.Kill()
            return $false
        }
        $versao = $processo.StandardOutput.ReadToEnd()
        $correspondencia = [regex]::Match($versao, '(?:openjdk|java)\s+(?:version\s+)?"?(\d+)')
        return $processo.ExitCode -eq 0 -and $correspondencia.Success -and [int]$correspondencia.Groups[1].Value -ge 17
    } catch {
        return $false
    } finally {
        $processo.Dispose()
    }
}

try {
    $candidatos = New-Object 'System.Collections.Generic.List[string]'
    if ($env:JAVA_HOME) { $candidatos.Add((Join-Path $env:JAVA_HOME 'bin\java.exe')) }
    $comando = Get-Command java.exe -ErrorAction SilentlyContinue
    if ($comando) { $candidatos.Add($comando.Source) }

    $pastas = @((Join-Path $env:USERPROFILE '.jdks'),
                (Join-Path $env:ProgramFiles 'Java'),
                (Join-Path $env:ProgramFiles 'Eclipse Adoptium'),
                (Join-Path $env:ProgramFiles 'Microsoft'))
    foreach ($pasta in $pastas) {
        if (Test-Path -LiteralPath $pasta) {
            Get-ChildItem -LiteralPath $pasta -Directory -ErrorAction SilentlyContinue |
                ForEach-Object { $candidatos.Add((Join-Path $_.FullName 'bin\java.exe')) }
        }
    }

    $java = $null
    foreach ($candidato in ($candidatos | Select-Object -Unique)) {
        if (Testar-Java $candidato) { $java = $candidato; break }
    }
    if (-not $java) {
        Write-Host 'Nao foi encontrado Java 17 ou superior.' -ForegroundColor Yellow
        Write-Host 'No IntelliJ, use Download JDK e escolha a versao 17 ou 21.'
        Write-Host 'Depois execute JOGAR.bat novamente.'
        exit 1
    }
    $jar = Join-Path $PSScriptRoot 'dist\dino-adventure.jar'
    if (-not (Test-Path -LiteralPath $jar)) {
        throw 'O JAR nao foi encontrado. Extraia todo o ZIP antes de abrir JOGAR.bat.'
    }
    Write-Host "Java encontrado: $java"
    Write-Host 'Iniciando Dino Adventure...'
    & $java -jar $jar
    exit $LASTEXITCODE
} catch {
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}
