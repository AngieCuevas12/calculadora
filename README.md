# Calculadora

Calculadora para Android hecha en **Kotlin** con **Android Studio**.

- Diseño llamativo: fondo degradado azul → morado → rojo.
- Botones redondeados con degradados que cambian de color al tocarlos.
- Operaciones: +, −, ×, ÷, %, cambio de signo (±), borrar (⌫) y limpiar (C).

## Requisitos

- JDK 17
- Android Studio (recomendado)
- SDK Android API 34

## Cómo compilar

En Android Studio:

```
File → Open → selecciona la carpeta del proyecto
```

O por terminal (con JDK 17 configurado):

```
gradlew assembleDebug
```

El APK queda en:

```
app/build/outputs/apk/debug/app-debug.apk
```

## Estructura

```
app/src/main/java/com/example/calculadora2/MainActivity.kt   ← lógica
app/src/main/res/layout/activity_main.xml                   ← diseño
app/src/main/res/drawable/bg_*.xml                          ← fondos y botones
```
