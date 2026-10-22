package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static ArbolBiblioteca biblioteca =
            DatosPrueba.soloRaiz();

    private static String texto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int numero(String mensaje) {
        return Integer.parseInt(texto(mensaje).trim());
    }

    private static CatalogoABB seleccionarEstante() {
        String codigo = texto("Codigo del estante: ");
        NodoUnidad unidad = biblioteca.buscar(codigo);

        if (unidad == null || unidad.getCatalogo() == null) {
            throw new IllegalArgumentException(
                    "El estante no existe.");
        }

        return unidad.getCatalogo();
    }

    private static Libro leerLibro() {
        int codigo = numero("Codigo: ");
        String titulo = texto("Titulo: ");
        String autor = texto("Autor: ");
        int anio = numero("Anio: ");

        return new Libro(codigo, titulo, autor, anio);
    }

    private static TipoRecorrido seleccionarRecorrido() {
        int opcion = numero(
                "1 Preorden, 2 Inorden, 3 Posorden: ");

        if (opcion < 1 || opcion > 3) {
            throw new IllegalArgumentException(
                    "El recorrido seleccionado no es valido.");
        }

        return TipoRecorrido.values()[opcion - 1];
    }

    private static void mostrarMenu() {
        System.out.println("\n===== BIBLIOTECA FISEI =====");
        System.out.println("1. Cargar estructura inicial");
        System.out.println("2. Mostrar jerarquia");
        System.out.println("3. Agregar unidad");
        System.out.println("4. Buscar unidad");
        System.out.println("5. Consultar estadisticas y nivel");
        System.out.println("6. Insertar libro");
        System.out.println("7. Buscar libro");
        System.out.println("8. Modificar libro");
        System.out.println("9. Eliminar libro");
        System.out.println("10. Mostrar recorrido y traza");
        System.out.println("11. Ejecutar retos del ABB");
        System.out.println("12. Ejecutar pruebas");
        System.out.println("13. Comprobar trazas personales");
        System.out.println("0. Salir");
    }

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();

            try {
                int opcion = numero("Opcion: ");

                switch (opcion) {
                    case 0:
                        continuar = false;
                        System.out.println("Programa finalizado.");
                        break;

                    case 1:
                        biblioteca = DatosPrueba.cargar();

                        System.out.println(
                                "Estructura inicial cargada."
                                + " Se reiniciaron los cambios anteriores.");
                        break;

                    case 2:
                        System.out.print(
                                biblioteca.mostrarJerarquia());
                        break;

                    case 3:
                        agregarUnidad();
                        break;

                    case 4:
                        buscarUnidad();
                        break;

                    case 5:
                        consultarEstadisticas();
                        break;

                    case 6: {
                        CatalogoABB catalogo = seleccionarEstante();
                        Libro libro = leerLibro();

                        System.out.println(
                                "Insertado: " + catalogo.insertar(libro));
                        break;
                    }

                    case 7: {
                        CatalogoABB catalogo = seleccionarEstante();
                        int codigo = numero("Codigo del libro: ");

                        Libro libro = catalogo.buscar(codigo);

                        System.out.println(
                                libro == null
                                        ? "Libro no encontrado."
                                        : libro);
                        break;
                    }

                    case 8:
                        modificarLibro();
                        break;

                    case 9: {
                        CatalogoABB catalogo = seleccionarEstante();
                        int codigo = numero("Codigo a eliminar: ");

                        System.out.println(
                                "Eliminado: " + catalogo.eliminar(codigo));
                        break;
                    }

                    case 10: {
                        CatalogoABB catalogo = seleccionarEstante();
                        TipoRecorrido tipo = seleccionarRecorrido();

                        System.out.println(
                                catalogo.trazaRecorrido(tipo));
                        break;
                    }

                    case 11:
                        ejecutarRetos();
                        break;

                    case 12:
                        PruebasBiblioteca.main(new String[0]);
                        break;

                    case 13: {
                        String digitos = texto(
                                "Ultimos cuatro digitos de cedula: ");

                        TrazasPersonales.main(
                                new String[]{digitos});
                        break;
                    }

                    default:
                        System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: debes ingresar un numero entero.");

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (java.util.NoSuchElementException e) {
                continuar = false;
            }
        }
    }

    private static void agregarUnidad() {
        String codigoPadre = texto("Codigo del padre: ");
        String codigo = texto("Codigo de la nueva unidad: ");
        String nombre = texto("Nombre: ");

        int opcion = numero("Tipo: 1 Area, 2 Estante: ");

        if (opcion != 1 && opcion != 2) {
            throw new IllegalArgumentException(
                    "El tipo seleccionado no es valido.");
        }

        TipoUnidad tipo = opcion == 1
                ? TipoUnidad.AREA
                : TipoUnidad.ESTANTE;

        UnidadBiblioteca nueva =
                new UnidadBiblioteca(codigo, nombre, tipo);

        System.out.println(
                "Agregada: "
                        + biblioteca.agregar(codigoPadre, nueva));
    }

    private static void buscarUnidad() {
        String codigo = texto("Codigo de la unidad: ");
        NodoUnidad unidad = biblioteca.buscar(codigo);

        if (unidad == null) {
            System.out.println("Unidad no encontrada.");
        } else {
            System.out.println(unidad.getDato());
            System.out.println(
                    "Tipo: " + unidad.getDato().getTipo());
        }
    }

    private static void consultarEstadisticas() {
        System.out.println(
                "Altura: " + biblioteca.altura());

        System.out.println(
                "Hojas: " + biblioteca.contarHojas());

        System.out.println(
                "Total de libros: "
                        + biblioteca.contarLibrosTotal());

        String codigo = texto(
                "Codigo de la unidad para consultar nivel: ");

        System.out.println(
                "Nivel: " + biblioteca.nivel(codigo));
    }

    private static void modificarLibro() {
        CatalogoABB catalogo = seleccionarEstante();
        int codigo = numero("Codigo del libro: ");

        if (catalogo.buscar(codigo) == null) {
            System.out.println("Libro no encontrado.");
            return;
        }

        String titulo = texto("Nuevo titulo: ");
        String autor = texto("Nuevo autor: ");
        int anio = numero("Nuevo anio: ");

        System.out.println(
                "Modificado: "
                        + catalogo.modificar(
                                codigo, titulo, autor, anio));
    }

    private static void ejecutarRetos() {
        CatalogoABB catalogo = seleccionarEstante();

        int codigo = numero("Codigo para camino de busqueda: ");

        System.out.println(
                "Camino: " + catalogo.caminoBusqueda(codigo));

        int desde = numero("Limite inferior del rango: ");
        int hasta = numero("Limite superior del rango: ");

        System.out.println(
                "Libros en el rango: "
                        + catalogo.contarEnRango(desde, hasta));

        System.out.println(
                "ABB valido: " + catalogo.esABBValido());

        String entrada = texto(
                "Preorden para reconstruir otro ABB "
                        + "(separado por espacios; Enter omite): ").trim();

        if (!entrada.isEmpty()) {
            String[] partes = entrada.split("\\s+");
            int[] preorden = new int[partes.length];

            convertirEnteros(partes, preorden, 0);

            CatalogoABB reconstruido =
                    CatalogoABB.desdePreorden(preorden);

            System.out.println(
                    reconstruido.trazaRecorrido(
                            TipoRecorrido.PREORDEN));
        }
    }

    private static void convertirEnteros(
            String[] partes,
            int[] resultado,
            int indice) {

        if (indice == partes.length) {
            return;
        }

        resultado[indice] = Integer.parseInt(partes[indice]);

        convertirEnteros(partes, resultado, indice + 1);
    }
}