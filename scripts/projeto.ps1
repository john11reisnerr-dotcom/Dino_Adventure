param(
    [ValidateSet('build', 'run', 'verify')]
    [string]$Acao = 'run'
)

$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)

if ($Acao -eq 'verify') {
    if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
        throw 'Para executar os testes, instale o Maven e o JDK 17 ou superior.'
    }
    & mvn --batch-mode --no-transfer-progress clean verify
    exit $LASTEXITCODE
}

foreach ($ferramenta in @('java', 'javac', 'jar')) {
    if (-not (Get-Command $ferramenta -ErrorAction SilentlyContinue)) {
        throw 'Instale o JDK 17 ou superior e adicione java, javac e jar ao PATH.'
    }
}

if (Test-Path 'target/classes') { Remove-Item 'target/classes' -Recurse -Force }
New-Item -ItemType Directory -Path 'target/classes' -Force | Out-Null
$fontes = @(Get-ChildItem 'src/main/java' -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
& javac --release 17 -encoding UTF-8 -d target/classes @fontes
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Copy-Item 'src/main/resources/*' 'target/classes' -Recurse -Force
& jar --create --file target/dino-adventure.jar --main-class br.com.dinoadventure.app.Main -C target/classes .
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Host 'Compilado: target/dino-adventure.jar'

if ($Acao -eq 'run') {
    & java -jar target/dino-adventure.jar
    exit $LASTEXITCODE
}
