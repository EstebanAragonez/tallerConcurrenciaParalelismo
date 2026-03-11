package com.taller.concurrencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Taller: Concurrencia y Paralelismo.
 * Ejercicio 1: Suma paralela con 4 hilos.
 * Ejercicio 2: Cuenta bancaria compartida con 3 clientes.
 */
@Component
public class TallerRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TallerRunner.class);
    private static final long LIMITE = 1_000_000;
    private static final int NUM_HILOS = 4;
    private static final double SALDO_INICIAL = 1000;
    private static final double RETIRO = 400;
    private static final int NUM_CLIENTES = 3;

    @Override
    public void run(String... args) {
        log.info("\n##################################################");
        log.info("#  TALLER: Concurrencia y Paralelismo en Java     #");
        log.info("##################################################");

        ejecutarEjercicio1();
        ejecutarEjercicio2();

        log.info("Taller completado.");
    }

    /** Ejercicio 1 - Paralelismo: suma 1 a 1.000.000 con 4 hilos */
    private void ejecutarEjercicio1() {
        log.info("\n========== EJERCICIO 1 - PARALELISMO ==========");
        log.info("Suma de 1 a {} usando {} hilos", LIMITE, NUM_HILOS);

        long[] resultados = new long[NUM_HILOS];
        List<Thread> hilos = new ArrayList<>();

        for (int i = 0; i < NUM_HILOS; i++) {
            long inicio = 1 + i * (LIMITE / NUM_HILOS);
            long fin = (i + 1) * (LIMITE / NUM_HILOS);
            int indice = i;

            Thread t = new Thread(() -> {
                long suma = 0;
                for (long n = inicio; n <= fin; n++) suma += n;
                resultados[indice] = suma;
                log.info("  Hilo [{}]: suma {} - {} = {}", indice + 1, inicio, fin, suma);
            }, "Hilo-" + (i + 1));

            hilos.add(t);
            t.start();
        }

        for (Thread t : hilos) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        long sumaTotal = 0;
        for (long r : resultados) sumaTotal += r;

        log.info("--- Resultado ---");
        log.info("SUMA TOTAL: {}", sumaTotal);
        log.info("(Verificación n(n+1)/2: {})", LIMITE * (LIMITE + 1) / 2);
        log.info("==========================================");
    }

    /** Ejercicio 2 - Concurrencia: cuenta compartida, 3 clientes retiran */
    private void ejecutarEjercicio2() {
        log.info("\n========== EJERCICIO 2 - CONCURRENCIA ==========");
        log.info("Saldo inicial: {}. {} clientes retiran {} cada uno", SALDO_INICIAL, NUM_CLIENTES, RETIRO);

        CuentaBancaria cuenta = new CuentaBancaria(SALDO_INICIAL);
        List<Thread> clientes = new ArrayList<>();

        for (int i = 0; i < NUM_CLIENTES; i++) {
            String nombre = "Cliente-" + (i + 1);
            Thread t = new Thread(() -> cuenta.retirar(RETIRO, nombre));
            clientes.add(t);
            t.start();
        }

        for (Thread t : clientes) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        log.info("--- Resultado final: saldo = {} ---", cuenta.getSaldo());
        log.info("==========================================");
    }

    /** Cuenta bancaria con sincronización para evitar saldo negativo */
    private static class CuentaBancaria {
        private double saldo;

        CuentaBancaria(double saldoInicial) { this.saldo = saldoInicial; }

        synchronized boolean retirar(double monto, String nombreCliente) {
            if (saldo >= monto) {
                saldo -= monto;
                log.info("  [{}] Retiró {}. Saldo: {} -> {}", nombreCliente, monto, saldo + monto, saldo);
                return true;
            }
            log.info("  [{}] RECHAZADO: saldo {} insuficiente para retirar {}", nombreCliente, saldo, monto);
            return false;
        }

        synchronized double getSaldo() { return saldo; }
    }
}
