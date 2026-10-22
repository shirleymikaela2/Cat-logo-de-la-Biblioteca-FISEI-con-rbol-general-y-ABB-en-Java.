package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {

    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca biblioteca) {
        if (biblioteca == null
                || biblioteca.getTipo() != TipoUnidad.BIBLIOTECA) {

            throw new IllegalArgumentException(
                    "La raiz debe ser de tipo BIBLIOTECA.");
        }

        raiz = new NodoUnidad(biblioteca);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    // AGREGAR UNA UNIDAD

    public boolean agregar(
            String codigoPadre,
            UnidadBiblioteca nueva) {

        if (nueva == null) {
            return false;
        }

        if (buscar(nueva.getCodigo()) != null) {
            return false;
        }

        NodoUnidad padre = buscar(codigoPadre);

        if (padre == null) {
            return false;
        }

        if (!padre.getDato().getTipo()
                .puedeTenerHijo(nueva.getTipo())) {

            return false;
        }

        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    // BUSCAR UNA UNIDAD

    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }

        return buscar(raiz, codigo.trim());
    }

    private NodoUnidad buscar(
            NodoUnidad nodo,
            String codigo) {

        if (nodo.getDato().getCodigo()
                .equalsIgnoreCase(codigo)) {

            return nodo;
        }

        return buscarEnHijos(nodo, codigo, 0);
    }

    private NodoUnidad buscarEnHijos(
            NodoUnidad padre,
            String codigo,
            int indice) {

        if (indice == padre.getHijos().size()) {
            return null;
        }

        NodoUnidad resultado = buscar(
                padre.getHijos().get(indice),
                codigo);

        if (resultado != null) {
            return resultado;
        }

        return buscarEnHijos(
                padre, codigo, indice + 1);
    }

    // ALTURA EN ARISTAS

    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        return alturaHijos(nodo, 0, 0);
    }

    private int alturaHijos(
            NodoUnidad padre,
            int indice,
            int maxima) {

        if (indice == padre.getHijos().size()) {
            return maxima;
        }

        NodoUnidad hijo = padre.getHijos().get(indice);

        int alturaDelHijo = 1 + altura(hijo);

        return alturaHijos(
                padre,
                indice + 1,
                Math.max(maxima, alturaDelHijo));
    }

    // CONTAR HOJAS

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo.getHijos().isEmpty()) {
            return 1;
        }

        return contarHojasHijos(nodo, 0);
    }

    private int contarHojasHijos(
            NodoUnidad padre,
            int indice) {

        if (indice == padre.getHijos().size()) {
            return 0;
        }

        return contarHojas(padre.getHijos().get(indice))
                + contarHojasHijos(padre, indice + 1);
    }

    // NIVEL DE UNA UNIDAD

    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }

        return nivel(raiz, codigo.trim(), 0);
    }

    private int nivel(
            NodoUnidad nodo,
            String codigo,
            int nivelActual) {

        if (nodo.getDato().getCodigo()
                .equalsIgnoreCase(codigo)) {

            return nivelActual;
        }

        return nivelEnHijos(
                nodo, codigo, nivelActual, 0);
    }

    private int nivelEnHijos(
            NodoUnidad padre,
            String codigo,
            int nivelActual,
            int indice) {

        if (indice == padre.getHijos().size()) {
            return -1;
        }

        int resultado = nivel(
                padre.getHijos().get(indice),
                codigo,
                nivelActual + 1);

        if (resultado >= 0) {
            return resultado;
        }

        return nivelEnHijos(
                padre,
                codigo,
                nivelActual,
                indice + 1);
    }

    // MOSTRAR JERARQUIA EN PREORDEN

    public String mostrarJerarquia() {
        StringBuilder resultado = new StringBuilder();

        mostrar(raiz, 0, resultado);

        return resultado.toString();
    }

    private void mostrar(
            NodoUnidad nodo,
            int nivel,
            StringBuilder resultado) {

        resultado.append("  ".repeat(nivel))
                .append(nodo.getDato())
                .append('\n');

        mostrarHijos(nodo, nivel, resultado, 0);
    }

    private void mostrarHijos(
            NodoUnidad padre,
            int nivel,
            StringBuilder resultado,
            int indice) {

        if (indice == padre.getHijos().size()) {
            return;
        }

        mostrar(
                padre.getHijos().get(indice),
                nivel + 1,
                resultado);

        mostrarHijos(
                padre,
                nivel,
                resultado,
                indice + 1);
    }

    // TOTAL DE LIBROS DE TODOS LOS ESTANTES

    public int contarLibrosTotal() {
        return contarLibros(raiz);
    }

    private int contarLibros(NodoUnidad nodo) {
        int propios = 0;

        if (nodo.getCatalogo() != null) {
            propios = nodo.getCatalogo().contar();
        }

        return propios + contarLibrosHijos(nodo, 0);
    }

    private int contarLibrosHijos(
            NodoUnidad padre,
            int indice) {

        if (indice == padre.getHijos().size()) {
            return 0;
        }

        return contarLibros(padre.getHijos().get(indice))
                + contarLibrosHijos(padre, indice + 1);
    }
}