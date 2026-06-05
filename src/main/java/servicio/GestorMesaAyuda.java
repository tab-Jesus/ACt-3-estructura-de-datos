package servicio;

import modelo.TicketSoporte;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GestorMesaAyuda — Clase de servicio del sistema de Mesa de Ayuda TI.
 *
 * Colecciones usadas:
 *   List<TicketSoporte>   → registro general de todos los tickets
 *   Queue<TicketSoporte>  → cola de tickets pendientes por atender (FIFO)
 *   Deque<TicketSoporte>  → historial de tickets procesados (pila LIFO)
 *   Map<String, TicketSoporte> → índice para búsqueda rápida por código
 */
public class GestorMesaAyuda {

    private final List<TicketSoporte> tickets = new ArrayList<>();

    private final Queue<TicketSoporte> pendientes = new LinkedList<>();

    private final Deque<TicketSoporte> historial = new ArrayDeque<>();

    private final Map<String, TicketSoporte> indicePorCodigo = new HashMap<>();

    /**
     * Registra un nuevo ticket en el sistema.
     * Valida que el código no exista antes de insertar.
     * Usa MAP para la validación de duplicados (O(1)).
     */
    public void registrarTicket(TicketSoporte ticket) {
        if (indicePorCodigo.containsKey(ticket.getCodigoTicket())) {
            throw new IllegalArgumentException(
                "Ya existe un ticket con el código: " + ticket.getCodigoTicket());
        }
        tickets.add(ticket);
        pendientes.offer(ticket);
        indicePorCodigo.put(ticket.getCodigoTicket(), ticket);

        System.out.println("\n✅ Ticket registrado exitosamente.");
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 2. VER TODOS LOS TICKETS
    public void verTodosLosTickets() {
        if (tickets.isEmpty()) {
            System.out.println("\n⚠️  No hay tickets registrados.");
            return;
        }
        System.out.println("\n📋 TODOS LOS TICKETS REGISTRADOS (" + tickets.size() + "):");
        System.out.println("─".repeat(80));
        tickets.forEach(t -> System.out.println(t.toStringResumen()));
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 3. VER TICKETS PENDIENTES
    public void verPendientes() {
        if (pendientes.isEmpty()) {
            System.out.println("\n⚠️  No hay tickets pendientes en la cola.");
            return;
        }
        System.out.println("\n⏳ TICKETS PENDIENTES EN COLA (" + pendientes.size() + "):");
        System.out.println("─".repeat(80));
        System.out.println("   → Próximo a procesar: " + pendientes.peek().getCodigoTicket()
                + " | " + pendientes.peek().getNombreUsuario());
        System.out.println();
        pendientes.forEach(t -> System.out.println(t.toStringResumen()));
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 4. PROCESAR SIGUIENTE TICKET
    /**
     * Procesa el siguiente ticket en la cola (principio FIFO).
     * Lo retira de la QUEUE y lo apila en el DEQUE (LIFO).
     */
    public void procesarSiguiente() {
        // QUEUE.poll() — saca el primero (FIFO)
        TicketSoporte procesado = pendientes.poll();
        if (procesado == null) {
            throw new IllegalStateException("No hay tickets pendientes para procesar.");
        }
        procesado.setEstado("PROCESADO");
        historial.push(procesado);

        System.out.println("\n✅ Ticket procesado exitosamente:");
        System.out.println(procesado.toStringResumen());
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 5. VER HISTORIAL DE PROCESADOS
    public void verHistorial() {
        if (historial.isEmpty()) {
            System.out.println("\n⚠️  El historial de procesados está vacío.");
            return;
        }
        System.out.println("\n📂 HISTORIAL DE TICKETS PROCESADOS (" + historial.size() + "):");
        System.out.println("─".repeat(80));
        System.out.println("   → Último procesado: " + historial.peek().getCodigoTicket());
        System.out.println();
        historial.forEach(t -> System.out.println(t.toStringResumen()));
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 6. BUSCAR POR CÓDIGO (Map)
    public void buscarPorCodigo(String codigo) {
        if (!indicePorCodigo.containsKey(codigo)) {
            System.out.println("\n❌ No existe un ticket con el código: " + codigo);
            return;
        }
        TicketSoporte encontrado = indicePorCodigo.get(codigo);
        System.out.println("\n🔍 Ticket encontrado:");
        System.out.println(encontrado);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 7. BUSCAR POR NOMBRE DE USUARIO (Stream)
    public void buscarPorUsuario(String nombreUsuario) {
        Optional<TicketSoporte> resultado = tickets.stream()
                .filter(t -> t.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst();

        if (resultado.isPresent()) {
            System.out.println("\n🔍 Ticket encontrado por usuario:");
            System.out.println(resultado.get());
        } else {
            System.out.println("\n❌ No se encontró ningún ticket del usuario: " + nombreUsuario);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 8. FILTRAR TICKETS (Stream)
    public void filtrarPorEstado(String estado) {
        List<TicketSoporte> filtrados = tickets.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());

        System.out.println("\n🔎 Tickets con estado [" + estado.toUpperCase() + "] (" + filtrados.size() + "):");
        System.out.println("─".repeat(80));
        if (filtrados.isEmpty()) {
            System.out.println("   Sin resultados.");
        } else {
            filtrados.forEach(t -> System.out.println(t.toStringResumen()));
        }
    }

    public void filtrarPorCategoria(String categoria) {
        List<TicketSoporte> filtrados = tickets.stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        System.out.println("\n🔎 Tickets de categoría [" + categoria.toUpperCase() + "] (" + filtrados.size() + "):");
        System.out.println("─".repeat(80));
        if (filtrados.isEmpty()) {
            System.out.println("   Sin resultados.");
        } else {
            filtrados.forEach(t -> System.out.println(t.toStringResumen()));
        }
    }

    public void filtrarPorPrioridad(String prioridad) {
        List<TicketSoporte> filtrados = tickets.stream()
                .filter(t -> t.getPrioridad().equalsIgnoreCase(prioridad))
                .collect(Collectors.toList());

        System.out.println("\n🔎 Tickets con prioridad [" + prioridad.toUpperCase() + "] (" + filtrados.size() + "):");
        System.out.println("─".repeat(80));
        if (filtrados.isEmpty()) {
            System.out.println("   Sin resultados.");
        } else {
            filtrados.forEach(t -> System.out.println(t.toStringResumen()));
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 9. ORDENAR TICKETS (Stream)
    public void ordenarPorCodigo() {
        List<TicketSoporte> ordenados = tickets.stream()
                .sorted(Comparator.comparing(TicketSoporte::getCodigoTicket))
                .collect(Collectors.toList());

        System.out.println("\n📑 TICKETS ORDENADOS POR CÓDIGO (ASC):");
        System.out.println("─".repeat(80));
        ordenados.forEach(t -> System.out.println(t.toStringResumen()));
    }

    public void ordenarPorUsuarioDesc() {
        List<TicketSoporte> ordenados = tickets.stream()
                .sorted(Comparator.comparing(TicketSoporte::getNombreUsuario).reversed())
                .collect(Collectors.toList());

        System.out.println("\n📑 TICKETS ORDENADOS POR USUARIO (DESC):");
        System.out.println("─".repeat(80));
        ordenados.forEach(t -> System.out.println(t.toStringResumen()));
    }

    public void ordenarPorPrioridad() {
        List<String> ordenPrioridad = Arrays.asList("ALTA", "MEDIA", "BAJA");

        List<TicketSoporte> ordenados = tickets.stream()
                .sorted(Comparator.comparingInt(
                        t -> ordenPrioridad.indexOf(t.getPrioridad())))
                .collect(Collectors.toList());

        System.out.println("\n📑 TICKETS ORDENADOS POR PRIORIDAD (ALTA → BAJA):");
        System.out.println("─".repeat(80));
        ordenados.forEach(t -> System.out.println(t.toStringResumen()));
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 10. ESTADÍSTICAS (Stream + Map)
    public void verEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DEL SISTEMA DE MESA DE AYUDA TI");
        System.out.println("═".repeat(60));

        System.out.println("  Total de tickets registrados : " + tickets.size());
        System.out.println("  En cola (pendientes)         : " + pendientes.size());
        System.out.println("  En historial (procesados)    : " + historial.size());
        System.out.println("  Indexados en mapa            : " + indicePorCodigo.size());

        System.out.println("\n  📌 Conteo por ESTADO:");
        Map<String, Long> porEstado = tickets.stream()
                .collect(Collectors.groupingBy(TicketSoporte::getEstado, Collectors.counting()));
        porEstado.forEach((estado, cantidad) ->
                System.out.printf("     %-12s → %d tickets%n", estado, cantidad));

        System.out.println("\n  📌 Conteo por PRIORIDAD:");
        Map<String, Long> porPrioridad = tickets.stream()
                .collect(Collectors.groupingBy(TicketSoporte::getPrioridad, Collectors.counting()));
        porPrioridad.forEach((prioridad, cantidad) ->
                System.out.printf("     %-8s → %d tickets%n", prioridad, cantidad));

        System.out.println("\n  📌 Conteo por CATEGORÍA:");
        Map<String, Long> porCategoria = tickets.stream()
                .collect(Collectors.groupingBy(TicketSoporte::getCategoria, Collectors.counting()));
        porCategoria.forEach((categoria, cantidad) ->
                System.out.printf("     %-12s → %d tickets%n", categoria, cantidad));

        boolean hayUrgentes = tickets.stream()
                .anyMatch(t -> t.getPrioridad().equalsIgnoreCase("ALTA")
                               && t.getEstado().equalsIgnoreCase("PENDIENTE"));
        System.out.println("\n  ⚠️  ¿Hay tickets ALTA prioridad pendientes? " + (hayUrgentes ? "SÍ" : "NO"));

        boolean todosTienenTecnico = tickets.stream()
                .allMatch(t -> t.getTecnicoAsignado() != null && !t.getTecnicoAsignado().isBlank());
        System.out.println("  ✅ ¿Todos los tickets tienen técnico asignado? " + (todosTienenTecnico ? "SÍ" : "NO"));

        long totalProcesados = tickets.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase("PROCESADO"))
                .count();
        System.out.println("  ✔️  Total procesados           : " + totalProcesados);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 11. AGRUPAMIENTOS (Stream + Map)
    public void agruparPorCategoria() {
        System.out.println("\n📂 AGRUPAMIENTO DE TICKETS POR CATEGORÍA:");
        System.out.println("═".repeat(60));

        Map<String, List<TicketSoporte>> porCategoria = tickets.stream()
                .collect(Collectors.groupingBy(TicketSoporte::getCategoria));

        porCategoria.forEach((categoria, lista) -> {
            System.out.println("\n  📁 Categoría: " + categoria + " (" + lista.size() + " tickets)");
            lista.forEach(t -> System.out.println("     " + t.toStringResumen()));
        });
    }

    public void agruparPorTecnico() {
        System.out.println("\n📂 AGRUPAMIENTO DE TICKETS POR TÉCNICO ASIGNADO:");
        System.out.println("═".repeat(60));

        Map<String, List<TicketSoporte>> porTecnico = tickets.stream()
                .collect(Collectors.groupingBy(TicketSoporte::getTecnicoAsignado));

        porTecnico.forEach((tecnico, lista) -> {
            System.out.println("\n  👤 Técnico: " + tecnico + " (" + lista.size() + " tickets)");
            lista.forEach(t -> System.out.println("     " + t.toStringResumen()));
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 12. CANCELAR TICKET PENDIENTE
    /**
     * Cancela un ticket que esté en estado PENDIENTE.
     * Se actualiza su estado en la LIST y se elimina de la QUEUE.
     * Permanece en LIST y MAP para dejar evidencia del registro.
     */
    public void cancelarTicket(String codigo) {
        TicketSoporte ticket = indicePorCodigo.get(codigo);
        if (ticket == null) {
            throw new IllegalArgumentException("No existe un ticket con el código: " + codigo);
        }
        if (!ticket.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new IllegalStateException(
                "Solo se pueden cancelar tickets PENDIENTES. Estado actual: " + ticket.getEstado());
        }
        ticket.setEstado("CANCELADO");
        pendientes.removeIf(t -> t.getCodigoTicket().equalsIgnoreCase(codigo));

        System.out.println("\n🚫 Ticket cancelado exitosamente: " + codigo);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 13. DESHACER ÚLTIMO PROCESAMIENTO
    /**
     * Deshace el último procesamiento.
     * Saca el tope de la pila DEQUE y lo devuelve a la QUEUE de pendientes.
     */
    public void deshacerUltimoProcesamiento() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("No hay procesados en el historial para deshacer.");
        }
        TicketSoporte ultimo = historial.pop();
        ultimo.setEstado("PENDIENTE");
        pendientes.offer(ultimo);

        System.out.println("\n↩️  Procesamiento deshecho. Ticket regresado a pendientes:");
        System.out.println(ultimo.toStringResumen());
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // 14. VER CANTIDAD DE ELEMENTOS
    public void verCantidades() {
        System.out.println("\n🔢 CANTIDAD DE ELEMENTOS EN EL SISTEMA:");
        System.out.println("─".repeat(50));
        System.out.println("  List  - Todos los tickets     : " + tickets.size());
        System.out.println("  Queue - Tickets pendientes    : " + pendientes.size());
        System.out.println("  Deque - Tickets en historial  : " + historial.size());
        System.out.println("  Map   - Tickets indexados     : " + indicePorCodigo.size());

        System.out.println("\n  Conteo por estado (Stream):");
        long pendientesCount = tickets.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase("PENDIENTE")).count();
        long procesadosCount = tickets.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase("PROCESADO")).count();
        long canceladosCount = tickets.stream()
                .filter(t -> t.getEstado().equalsIgnoreCase("CANCELADO")).count();

        System.out.println("     PENDIENTE : " + pendientesCount);
        System.out.println("     PROCESADO : " + procesadosCount);
        System.out.println("     CANCELADO : " + canceladosCount);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    public boolean hayTickets() {
        return !tickets.isEmpty();
    }

    public boolean hayPendientes() {
        return !pendientes.isEmpty();
    }

    public boolean hayHistorial() {
        return !historial.isEmpty();
    }

    /** Verifica existencia de un código usando MAP */
    public boolean existeCodigo(String codigo) {
        return indicePorCodigo.containsKey(codigo);
    }

    /**
     * STREAM: obtiene lista de nombres de usuario (transformación map)
     * Demuestra la operación map() de Stream para transformar datos.
     */
    public void listarNombresUsuarios() {
        System.out.println("\n👥 USUARIOS CON TICKETS REGISTRADOS:");
        List<String> nombres = tickets.stream()
                .map(TicketSoporte::getNombreUsuario)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        nombres.forEach(n -> System.out.println("   - " + n));
    }

    /**
     * STREAM: Collectors.toMap — reconstruye índice a partir de la lista.
     * Útil para demostrar la operación toMap con manejo de colisiones.
     */
    public Map<String, TicketSoporte> reconstruirIndiceDesdeLista() {
        return tickets.stream()
                .collect(Collectors.toMap(
                        TicketSoporte::getCodigoTicket,
                        t -> t,
                        (existente, repetido) -> existente
                ));
    }
}
