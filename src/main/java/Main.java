import modelo.TicketSoporte;
import servicio.GestorMesaAyuda;

import java.util.Scanner;

/**
 * Main — Clase principal del Sistema de Mesa de Ayuda TI.
 *
 * Presenta un menú de consola con las 15 opciones obligatorias
 * y gestiona la interacción del usuario con el sistema.
 */
public class Main {

    private static final GestorMesaAyuda gestor = new GestorMesaAyuda();
    private static final Scanner sc = new Scanner(System.in);

    private static final String RESET  = "\u001B[0m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";

    public static void main(String[] args) {
        mostrarBienvenida();
        cargarDatosDePrueba();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            procesarOpcion(opcion);
        } while (opcion != 15);

        System.out.println("\n👋 Gracias por usar el Sistema de Mesa de Ayuda TI. ¡Hasta pronto!\n");
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n" + CYAN + "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        SISTEMA DE MESA DE AYUDA TI — MENÚ PRINCIPAL       ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣" + RESET);
        System.out.println("  " + GREEN + "1."  + RESET + "  Registrar nuevo ticket");
        System.out.println("  " + GREEN + "2."  + RESET + "  Ver todos los tickets registrados");
        System.out.println("  " + GREEN + "3."  + RESET + "  Ver tickets pendientes");
        System.out.println("  " + GREEN + "4."  + RESET + "  Procesar siguiente ticket");
        System.out.println("  " + GREEN + "5."  + RESET + "  Ver historial de tickets procesados");
        System.out.println("  " + GREEN + "6."  + RESET + "  Buscar ticket por código (Map)");
        System.out.println("  " + GREEN + "7."  + RESET + "  Buscar ticket por usuario (Stream)");
        System.out.println("  " + GREEN + "8."  + RESET + "  Filtrar tickets (Stream)");
        System.out.println("  " + GREEN + "9."  + RESET + "  Ordenar tickets (Stream)");
        System.out.println("  " + GREEN + "10." + RESET + " Ver estadísticas (Stream + Map)");
        System.out.println("  " + GREEN + "11." + RESET + " Ver agrupamientos (Stream + Map)");
        System.out.println("  " + GREEN + "12." + RESET + " Cancelar ticket pendiente");
        System.out.println("  " + GREEN + "13." + RESET + " Deshacer último procesamiento");
        System.out.println("  " + GREEN + "14." + RESET + " Ver cantidad de elementos");
        System.out.println("  " + RED   + "15." + RESET + " Salir");
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════╝" + RESET);
    }

    private static void procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1  -> opcionRegistrar();
                case 2  -> gestor.verTodosLosTickets();
                case 3  -> gestor.verPendientes();
                case 4  -> gestor.procesarSiguiente();
                case 5  -> gestor.verHistorial();
                case 6  -> opcionBuscarPorCodigo();
                case 7  -> opcionBuscarPorUsuario();
                case 8  -> opcionFiltrar();
                case 9  -> opcionOrdenar();
                case 10 -> gestor.verEstadisticas();
                case 11 -> opcionAgrupar();
                case 12 -> opcionCancelar();
                case 13 -> gestor.deshacerUltimoProcesamiento();
                case 14 -> gestor.verCantidades();
                case 15 -> {}
                default -> System.out.println(YELLOW + "⚠️  Opción inválida. Ingrese un número del 1 al 15." + RESET);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(RED + "\n❌ Error: " + e.getMessage() + RESET);
        }
    }

    private static void opcionRegistrar() {
        System.out.println("\n" + GREEN + "── REGISTRAR NUEVO TICKET ──" + RESET);
        String codigo = leerTexto("Código del ticket (ej. TKT-001): ");

        if (gestor.existeCodigo(codigo)) {
            System.out.println(RED + "❌ Ya existe un ticket con ese código." + RESET);
            return;
        }

        String usuario      = leerTexto("Nombre del usuario: ");
        String descripcion  = leerTexto("Descripción del problema: ");

        System.out.println("Categorías: HARDWARE | SOFTWARE | RED | ACCESOS | OTRO");
        String categoria    = leerTexto("Categoría: ");

        System.out.println("Prioridades: ALTA | MEDIA | BAJA");
        String prioridad    = leerTexto("Prioridad: ");

        String tecnico      = leerTexto("Técnico asignado: ");

        TicketSoporte ticket = new TicketSoporte(
                codigo, usuario, descripcion, categoria, prioridad, tecnico);
        gestor.registrarTicket(ticket);
    }

    private static void opcionBuscarPorCodigo() {
        System.out.println("\n" + GREEN + "── BUSCAR POR CÓDIGO (Map) ──" + RESET);
        String codigo = leerTexto("Ingrese el código del ticket: ");
        gestor.buscarPorCodigo(codigo);
    }

    private static void opcionBuscarPorUsuario() {
        System.out.println("\n" + GREEN + "── BUSCAR POR USUARIO (Stream) ──" + RESET);
        String usuario = leerTexto("Ingrese el nombre del usuario: ");
        gestor.buscarPorUsuario(usuario);
    }

    private static void opcionFiltrar() {
        System.out.println("\n" + GREEN + "── FILTRAR TICKETS (Stream) ──" + RESET);
        System.out.println("  1. Por estado (PENDIENTE / PROCESADO / CANCELADO)");
        System.out.println("  2. Por categoría (HARDWARE / SOFTWARE / RED / ACCESOS / OTRO)");
        System.out.println("  3. Por prioridad (ALTA / MEDIA / BAJA)");
        int sub = leerEntero("Seleccione criterio de filtro: ");
        switch (sub) {
            case 1 -> {
                String estado = leerTexto("Estado a filtrar: ");
                gestor.filtrarPorEstado(estado);
            }
            case 2 -> {
                String cat = leerTexto("Categoría a filtrar: ");
                gestor.filtrarPorCategoria(cat);
            }
            case 3 -> {
                String prior = leerTexto("Prioridad a filtrar: ");
                gestor.filtrarPorPrioridad(prior);
            }
            default -> System.out.println(YELLOW + "⚠️  Opción de filtro inválida." + RESET);
        }
    }

    private static void opcionOrdenar() {
        System.out.println("\n" + GREEN + "── ORDENAR TICKETS (Stream) ──" + RESET);
        System.out.println("  1. Por código (ascendente)");
        System.out.println("  2. Por nombre de usuario (descendente)");
        System.out.println("  3. Por prioridad (ALTA → MEDIA → BAJA)");
        int sub = leerEntero("Seleccione criterio de orden: ");
        switch (sub) {
            case 1 -> gestor.ordenarPorCodigo();
            case 2 -> gestor.ordenarPorUsuarioDesc();
            case 3 -> gestor.ordenarPorPrioridad();
            default -> System.out.println(YELLOW + "⚠️  Opción de orden inválida." + RESET);
        }
    }

    private static void opcionAgrupar() {
        System.out.println("\n" + GREEN + "── AGRUPAMIENTOS (Stream + Map) ──" + RESET);
        System.out.println("  1. Agrupar por categoría");
        System.out.println("  2. Agrupar por técnico asignado");
        int sub = leerEntero("Seleccione agrupamiento: ");
        switch (sub) {
            case 1 -> gestor.agruparPorCategoria();
            case 2 -> gestor.agruparPorTecnico();
            default -> System.out.println(YELLOW + "⚠️  Opción de agrupamiento inválida." + RESET);
        }
    }

    private static void opcionCancelar() {
        System.out.println("\n" + GREEN + "── CANCELAR TICKET PENDIENTE ──" + RESET);
        String codigo = leerTexto("Ingrese el código del ticket a cancelar: ");
        gestor.cancelarTicket(codigo);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            int valor = Integer.parseInt(sc.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void mostrarBienvenida() {
        System.out.println(CYAN);
        System.out.println("  ╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║         SISTEMA DE MESA DE AYUDA TI                   ║");
        System.out.println("  ║   Estructuras de Datos — Unidad III                   ║");
        System.out.println("  ║   Colecciones SDK Java + Stream                        ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    /**
     * Carga tickets de ejemplo para facilitar las pruebas del sistema.
     * Estos datos son ficticios y representan casos típicos de soporte TI.
     */
    private static void cargarDatosDePrueba() {
        System.out.println(YELLOW + "  ℹ️  Cargando datos de prueba..." + RESET);

        gestor.registrarTicket(new TicketSoporte(
                "TKT-001", "Carlos Pérez",
                "El computador no enciende al presionar el botón de encendido",
                "HARDWARE", "ALTA", "Andrés García"));

        gestor.registrarTicket(new TicketSoporte(
                "TKT-002", "María López",
                "No puedo acceder al correo corporativo desde esta mañana",
                "ACCESOS", "ALTA", "Laura Martínez"));

        gestor.registrarTicket(new TicketSoporte(
                "TKT-003", "Juan Torres",
                "El sistema de facturación presenta error al generar reportes",
                "SOFTWARE", "MEDIA", "Andrés García"));

        gestor.registrarTicket(new TicketSoporte(
                "TKT-004", "Ana Ramírez",
                "La impresora del área contable no imprime documentos PDF",
                "HARDWARE", "BAJA", "Pedro Ruiz"));

        gestor.registrarTicket(new TicketSoporte(
                "TKT-005", "Luis Hernández",
                "Sin acceso a internet desde el área de ventas",
                "RED", "ALTA", "Laura Martínez"));

        gestor.registrarTicket(new TicketSoporte(
                "TKT-006", "Sandra Castro",
                "El antivirus corporativo requiere actualización urgente",
                "SOFTWARE", "MEDIA", "Pedro Ruiz"));

        System.out.println(GREEN + "  ✅ 6 tickets de prueba cargados correctamente.\n" + RESET);
    }
}
