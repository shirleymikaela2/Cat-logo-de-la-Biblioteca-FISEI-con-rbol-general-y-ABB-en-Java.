package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

public class DatosPrueba {

    public static ArbolBiblioteca soloRaiz() {
        UnidadBiblioteca biblioteca = new UnidadBiblioteca(
                "BIB",
                "Biblioteca FISEI",
                TipoUnidad.BIBLIOTECA);

        return new ArbolBiblioteca(biblioteca);
    }

    public static Libro libro(int codigo) {
        return new Libro(
                codigo,
                "Libro " + codigo,
                "Autor de ejemplo",
                2026);
    }

    public static ArbolBiblioteca cargar() {
        ArbolBiblioteca arbol = soloRaiz();

        // Areas de la biblioteca.
        arbol.agregar("BIB", new UnidadBiblioteca(
                "SW",
                "Software",
                TipoUnidad.AREA));

        arbol.agregar("BIB", new UnidadBiblioteca(
                "TI",
                "Tecnologias de la Informacion",
                TipoUnidad.AREA));

        arbol.agregar("BIB", new UnidadBiblioteca(
                "TEL",
                "Telecomunicaciones",
                TipoUnidad.AREA));

        // Estantes de Software.
        arbol.agregar("SW", new UnidadBiblioteca(
                "SW-E1",
                "Estante 1 de Software",
                TipoUnidad.ESTANTE));

        arbol.agregar("SW", new UnidadBiblioteca(
                "SW-E2",
                "Estante 2 de Software",
                TipoUnidad.ESTANTE));

        // Estante de Tecnologias de la Informacion.
        arbol.agregar("TI", new UnidadBiblioteca(
                "TI-E1",
                "Estante 1 de TI",
                TipoUnidad.ESTANTE));

        // Libros en el orden exacto del enunciado.
        insertar(
                arbol.buscar("SW-E1").getCatalogo(),
                new int[]{60, 35, 80, 20, 45, 70, 90, 40, 75},
                0);

        insertar(
                arbol.buscar("SW-E2").getCatalogo(),
                new int[]{15, 10, 25},
                0);

        insertar(
                arbol.buscar("TI-E1").getCatalogo(),
                new int[]{50},
                0);

        return arbol;
    }

    private static void insertar(
            CatalogoABB catalogo,
            int[] codigos,
            int indice) {

        if (indice == codigos.length) {
            return;
        }

        catalogo.insertar(libro(codigos[indice]));

        insertar(catalogo, codigos, indice + 1);
    }
}