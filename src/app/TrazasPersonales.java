package app;

import enums.TipoRecorrido;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public class TrazasPersonales {

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].matches("[0-9]{4}")) {
            throw new IllegalArgumentException(
                    "Debes ingresar exactamente cuatro digitos.");
        }

        // Utiliza una biblioteca independiente del menu.
        CatalogoABB catalogo = DatosPrueba.cargar()
                .buscar("SW-E1")
                .getCatalogo();

        System.out.println("\n===== TRAZAS PERSONALES =====");

        insertarPersonales(catalogo, args[0], 0);

        System.out.println("\nABB despues de insertar:");
        mostrarDiagrama(catalogo);

        mostrarRecorridos(catalogo, 0);

        System.out.println("\nTraza del preorden:");
        System.out.println(
                catalogo.trazaRecorrido(TipoRecorrido.PREORDEN));

        eliminarCaso(catalogo, 0, "hoja");
        eliminarCaso(catalogo, 1, "un hijo");
        eliminarCaso(catalogo, 2, "dos hijos");
    }

    private static void insertarPersonales(
            CatalogoABB catalogo,
            String digitos,
            int indice) {

        if (indice == 3) {
            return;
        }

        String par = digitos.substring(indice, indice + 2);
        int original = Integer.parseInt(par);

        int codigo = encontrarLibre(catalogo, original);

        catalogo.insertar(DatosPrueba.libro(codigo));

        System.out.println(
                "Par " + par
                        + " -> codigo personal "
                        + (indice + 1)
                        + ": " + codigo);

        insertarPersonales(catalogo, digitos, indice + 1);
    }

    private static int encontrarLibre(
            CatalogoABB catalogo,
            int codigo) {

        if (codigo <= 0 || catalogo.buscar(codigo) != null) {
            return encontrarLibre(catalogo, codigo + 1);
        }

        return codigo;
    }

    private static void mostrarRecorridos(
            CatalogoABB catalogo,
            int indice) {

        if (indice == TipoRecorrido.values().length) {
            return;
        }

        TipoRecorrido tipo = TipoRecorrido.values()[indice];

        System.out.print(tipo + ": ");
        mostrarCodigos(catalogo.recorrer(tipo), 0);
        System.out.println();

        mostrarRecorridos(catalogo, indice + 1);
    }

    private static void mostrarCodigos(
            java.util.List<modelo.Libro> libros,
            int indice) {

        if (indice == libros.size()) {
            return;
        }

        if (indice > 0) {
            System.out.print(" ");
        }

        System.out.print(libros.get(indice).getCodigo());

        mostrarCodigos(libros, indice + 1);
    }

    private static void mostrarDiagrama(CatalogoABB catalogo) {
        System.out.println("```mermaid");
        System.out.println("flowchart TD");

        dibujar(catalogo.getRaiz());

        System.out.println("```");
    }

    private static void dibujar(NodoLibro nodo) {
        if (nodo == null) {
            return;
        }

        int codigo = nodo.getDato().getCodigo();

        System.out.println(
                "  n" + codigo + "[\"" + codigo + "\"]");

        if (nodo.getIzquierdo() != null) {
            System.out.println(
                    "  n" + codigo
                            + " -->|I| n"
                            + nodo.getIzquierdo()
                                    .getDato().getCodigo());
        }

        if (nodo.getDerecho() != null) {
            System.out.println(
                    "  n" + codigo
                            + " -->|D| n"
                            + nodo.getDerecho()
                                    .getDato().getCodigo());
        }

        dibujar(nodo.getIzquierdo());
        dibujar(nodo.getDerecho());
    }

    private static NodoLibro buscarCandidato(
            NodoLibro nodo,
            int hijosBuscados) {

        if (nodo == null) {
            return null;
        }

        int hijos = 0;

        if (nodo.getIzquierdo() != null) {
            hijos++;
        }

        if (nodo.getDerecho() != null) {
            hijos++;
        }

        if (hijos == hijosBuscados) {
            return nodo;
        }

        NodoLibro resultado = buscarCandidato(
                nodo.getIzquierdo(), hijosBuscados);

        if (resultado != null) {
            return resultado;
        }

        return buscarCandidato(
                nodo.getDerecho(), hijosBuscados);
    }

    private static void eliminarCaso(
            CatalogoABB catalogo,
            int cantidadHijos,
            String descripcion) {

        NodoLibro candidato = buscarCandidato(
                catalogo.getRaiz(), cantidadHijos);

        if (candidato == null) {
            System.out.println(
                    "No existe un candidato para: " + descripcion);
            return;
        }

        int codigo = candidato.getDato().getCodigo();

        System.out.println(
                "\nEliminar " + codigo
                        + " (" + descripcion + ")");

        System.out.println(
                "Resultado: " + catalogo.eliminar(codigo));

        mostrarDiagrama(catalogo);

        System.out.println(
                "ABB valido: " + catalogo.esABBValido());

        System.out.print("Preorden resultante: ");

        mostrarCodigos(
                catalogo.recorrer(TipoRecorrido.PREORDEN), 0);

        System.out.println();
    }
}
