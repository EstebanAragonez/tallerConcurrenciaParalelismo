# Respuestas Análisis - Concurrencia y Paralelismo

Respuestas basadas en los ejercicios del taller.

---

## 1. ¿Qué diferencia existe entre paralelismo y concurrencia?

**Concurrencia** es un concepto de diseño: múltiples tareas pueden *avanzar* durante el mismo período de tiempo, aunque no necesariamente se ejecuten al mismo instante. Un solo procesador puede simular concurrencia mediante planificación (time-slicing): alterna rápidamente entre tareas.

**Paralelismo** implica que varias tareas se ejecutan *a la vez*, en paralelo real, típicamente aprovechando múltiples núcleos de CPU. Cada tarea progresa simultáneamente en cores distintos.

- **Concurrencia**: "Hacer progresar varias cosas" (puede ser en un solo núcleo).
- **Paralelismo**: "Hacer varias cosas al mismo tiempo" (requiere múltiples núcleos).

En el taller:
- **Ejercicio 1** ilustra **paralelismo**: 4 hilos calculan sumas de rangos distintos a la vez.
- **Ejercicio 2** ilustra **concurrencia**: 3 hilos compiten por el mismo recurso (la cuenta) y deben coordinarse.

---

## 2. ¿Qué problema ocurre cuando varios hilos acceden al mismo recurso?

Cuando varios hilos acceden al mismo recurso sin sincronización pueden ocurrir:

1. **Condiciones de carrera (Race Conditions)**: El resultado depende del orden en que los hilos ejecutan sus operaciones.
2. **Datos inconsistentes**: Lecturas incorrectas o valores intermedios mientras otro hilo está modificando.
3. **Pérdida de actualizaciones**: Una modificación sobrescribe la de otro hilo.
4. **Estados inválidos**: El recurso queda en un estado incoherente (ej. saldo negativo en una cuenta).

En el Ejercicio 2, sin `synchronized`, dos clientes podrían leer el mismo saldo (1000), ambos decidir que pueden retirar 400, y ambos restar, dejando saldos incorrectos o negativos.

---

## 3. ¿Qué es una condición de carrera (Race Condition)?

Una **condición de carrera** ocurre cuando el resultado de un programa depende del orden o el timing en que los hilos ejecutan sus operaciones. Comportamiento observable cuando:

- Varios hilos leen y escriben una variable compartida.
- Al menos uno escribe.
- No hay sincronización adecuada.

Ejemplo típico con `saldo`:

```
Hilo A: lee saldo = 1000
Hilo B: lee saldo = 1000  (aún no actualizado por A)
Hilo A: escribe saldo = 600  (retiró 400)
Hilo B: escribe saldo = 600  (retiró 400, sobrescribiendo)
```

Resultado: se retiraron 800 pero el saldo solo disminuyó 400. La operación "leer-comprobar-escribir" debe ser **atómica** para evitar este tipo de errores.

---

## 4. ¿Por qué es importante sincronizar el acceso a recursos compartidos?

La sincronización es importante para:

1. **Corrección**: Asegurar que las operaciones compuestas (leer-modificar-escribir) se ejecuten de forma atómica.
2. **Consistencia**: Mantener invariantes del sistema (por ejemplo, saldo nunca negativo).
3. **Visibilidad**: Garantizar que los cambios de un hilo sean visibles para otros.
4. **Orden**: Establecer un orden de acceso cuando varios hilos compiten por el mismo recurso.

En Java, `synchronized` en métodos o bloques asegura que solo un hilo ejecute el código protegido a la vez, evitando condiciones de carrera y pérdida de actualizaciones en recursos compartidos como la cuenta bancaria.
