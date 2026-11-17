#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")/.."

acao="${1:-run}"
case "$acao" in
    build|run|verify) ;;
    *) echo "Uso: bash scripts/projeto.sh [build|run|verify]" >&2; exit 1 ;;
esac

if [[ "$acao" == "verify" ]]; then
    if ! command -v mvn >/dev/null 2>&1; then
        echo "Para executar os testes, instale o Maven e o JDK 17 ou superior." >&2
        exit 1
    fi
    exec mvn --batch-mode --no-transfer-progress clean verify
fi

for ferramenta in java javac jar; do
    if ! command -v "$ferramenta" >/dev/null 2>&1; then
        echo "Instale o JDK 17 ou superior e adicione java, javac e jar ao PATH." >&2
        exit 1
    fi
done

mkdir -p target
rm -rf target/classes
mkdir -p target/classes
find src/main/java -name '*.java' -print | sort > target/sources.txt
javac --release 17 -encoding UTF-8 -d target/classes @target/sources.txt
cp -R src/main/resources/. target/classes/
jar --create --file target/dino-adventure.jar \
    --main-class br.com.dinoadventure.app.Main -C target/classes .
echo "Compilado: target/dino-adventure.jar"

if [[ "$acao" == "run" ]]; then
    java -jar target/dino-adventure.jar
fi
