# Taller: Concurrencia y Paralelismo en Java con Spring Boot

## Objetivo

Desarrollar programas en Java que permitan comprender el uso de hilos (threads) para ejecutar tareas en paralelo y manejar recursos compartidos.

## Requisitos

- Java 17+
- Maven 3.6+

## Ejecución

```bash
mvn spring-boot:run
```

O desde IntelliJ: ejecutar la clase `TallerConcurrenciaApplication`.

## Estructura del Proyecto

```
src/main/java/com/taller/concurrencia/
├── TallerConcurrenciaApplication.java   # Punto de entrada
└── TallerRunner.java                    # Ejercicio 1 + Ejercicio 2 (incluye CuentaBancaria como clase interna)
```

## Ejercicios

### Ejercicio 1 – Paralelismo

- Suma de números del 1 al 1.000.000 usando 4 hilos.
- División del trabajo: Hilo 1 (1–250.000), Hilo 2 (250.001–500.000), Hilo 3 (500.001–750.000), Hilo 4 (750.001–1.000.000).
- Se usan `Thread` y `Runnable` (lambdas). Se espera a todos los hilos con `join()` antes de mostrar el resultado.
- La suma total se verifica con la fórmula n(n+1)/2.

### Ejercicio 2 – Concurrencia

- Cuenta bancaria compartida con saldo inicial de 1.000.
- 3 clientes (hilos) intentan retirar 400 cada uno.
- `synchronized` en los métodos de la cuenta evita saldo negativo y condiciones de carrera.
- Resultado esperado: 2 retiros exitosos, 1 rechazado; saldo final 200.

## Preguntas de Análisis

Ver [PREGUNTAS_ANALISIS.md](PREGUNTAS_ANALISIS.md) para las respuestas detalladas.
