package negocio;

import enums.TipoRecorrido;
import modelo.Libro;

import java.util.ArrayList;
import java.util.List;

public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    // INSERTAR

    public boolean insertar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro es obligatorio.");
        }

        if (buscar(libro.getCodigo()) != null) {
            return false;
        }

        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }

        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(
                    insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(
                    insertar(nodo.getDerecho(), libro));
        }

        return nodo;
    }

    // BUSCAR

    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }

        int actual = nodo.getDato().getCodigo();

        if (codigo == actual) {
            return nodo.getDato();
        }

        if (codigo < actual) {
            return buscar(nodo.getIzquierdo(), codigo);
        }

        return buscar(nodo.getDerecho(), codigo);
    }

    // CONTAR

    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }

        return 1
                + contar(nodo.getIzquierdo())
                + contar(nodo.getDerecho());
    }

    // RECORRIDOS

    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de recorrido es obligatorio.");
        }

        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(
            NodoLibro nodo,
            TipoRecorrido tipo,
            List<Libro> resultado) {

        if (nodo == null) {
            return;
        }

        if (tipo == TipoRecorrido.PREORDEN) {
            resultado.add(nodo.getDato());
        }

        recorrer(nodo.getIzquierdo(), tipo, resultado);

        if (tipo == TipoRecorrido.INORDEN) {
            resultado.add(nodo.getDato());
        }

        recorrer(nodo.getDerecho(), tipo, resultado);

        if (tipo == TipoRecorrido.POSORDEN) {
            resultado.add(nodo.getDato());
        }
    }

    // TRAZA DEL RECORRIDO

    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de recorrido es obligatorio.");
        }

        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();

        trazar(raiz, tipo, 0, traza, secuencia);

        traza.append("Secuencia ")
                .append(tipo)
                .append(": ")
                .append(secuencia);

        return traza.toString();
    }

    private void trazar(
            NodoLibro nodo,
            TipoRecorrido tipo,
            int nivel,
            StringBuilder traza,
            StringBuilder secuencia) {

        String margen = "  ".repeat(nivel);

        if (nodo == null) {
            traza.append(margen)
                    .append("null -> regresa\n");
            return;
        }

        traza.append(margen)
                .append("entra ")
                .append(nodo.getDato().getCodigo())
                .append('\n');

        if (tipo == TipoRecorrido.PREORDEN) {
            registrarVisita(nodo, margen, traza, secuencia);
        }

        trazar(
                nodo.getIzquierdo(),
                tipo,
                nivel + 1,
                traza,
                secuencia);

        if (tipo == TipoRecorrido.INORDEN) {
            registrarVisita(nodo, margen, traza, secuencia);
        }

        trazar(
                nodo.getDerecho(),
                tipo,
                nivel + 1,
                traza,
                secuencia);

        if (tipo == TipoRecorrido.POSORDEN) {
            registrarVisita(nodo, margen, traza, secuencia);
        }
    }

    private void registrarVisita(
            NodoLibro nodo,
            String margen,
            StringBuilder traza,
            StringBuilder secuencia) {

        int codigo = nodo.getDato().getCodigo();

        traza.append(margen)
                .append("VISITA ")
                .append(codigo)
                .append('\n');

        if (secuencia.length() > 0) {
            secuencia.append(' ');
        }

        secuencia.append(codigo);
    }

    // MODIFICAR

    public boolean modificar(
            int codigo,
            String titulo,
            String autor,
            int anio) {

        Libro libro = buscar(codigo);

        if (libro == null) {
            return false;
        }

        libro.actualizar(titulo, autor, anio);
        return true;
    }

    // ELIMINAR

    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }

        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }

        int actual = nodo.getDato().getCodigo();

        if (codigo < actual) {
            nodo.setIzquierdo(
                    eliminar(nodo.getIzquierdo(), codigo));

        } else if (codigo > actual) {
            nodo.setDerecho(
                    eliminar(nodo.getDerecho(), codigo));

        } else {
            // Hoja o nodo que solo tiene hijo derecho.
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }

            // Nodo que solo tiene hijo izquierdo.
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            // Nodo con dos hijos: usar sucesor inorden.
            NodoLibro sucesor = minimo(nodo.getDerecho());

            nodo.setDato(sucesor.getDato());

            nodo.setDerecho(
                    eliminar(
                            nodo.getDerecho(),
                            sucesor.getDato().getCodigo()));
        }

        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }

        return minimo(nodo.getIzquierdo());
    }

    // RETO 1: CONTAR EN RANGO CON PODA

    public int contarEnRango(int a, int b) {
        if (a > b) {
            return 0;
        }

        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }

        int codigo = nodo.getDato().getCodigo();

        if (codigo < a) {
            return contarEnRango(nodo.getDerecho(), a, b);
        }

        if (codigo > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }

        return 1
                + contarEnRango(nodo.getIzquierdo(), a, b)
                + contarEnRango(nodo.getDerecho(), a, b);
    }

    // RETO 2: CAMINO DE BUSQUEDA

    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(
            NodoLibro nodo,
            int codigo,
            List<Integer> camino) {

        if (nodo == null) {
            return;
        }

        int actual = nodo.getDato().getCodigo();
        camino.add(actual);

        if (codigo < actual) {
            caminoBusqueda(
                    nodo.getIzquierdo(), codigo, camino);

        } else if (codigo > actual) {
            caminoBusqueda(
                    nodo.getDerecho(), codigo, camino);
        }
    }

    // RETO 3: VALIDAR TODO EL ABB

    public boolean esABBValido() {
        return validar(
                raiz,
                0L,
                (long) Integer.MAX_VALUE + 1);
    }

    private boolean validar(
            NodoLibro nodo,
            long minimo,
            long maximo) {

        if (nodo == null) {
            return true;
        }

        int codigo = nodo.getDato().getCodigo();

        return codigo > minimo
                && codigo < maximo
                && validar(
                        nodo.getIzquierdo(), minimo, codigo)
                && validar(
                        nodo.getDerecho(), codigo, maximo);
    }

    // EXTRA: RECONSTRUIR DESDE PREORDEN SIN INSERTAR

    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException(
                    "El preorden es obligatorio.");
        }

        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};

        catalogo.raiz = construirDesdePreorden(
                preorden,
                indice,
                0L,
                (long) Integer.MAX_VALUE + 1);

        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException(
                    "Preorden invalido, repetido o con codigos no positivos.");
        }

        return catalogo;
    }

    private static NodoLibro construirDesdePreorden(
            int[] preorden,
            int[] indice,
            long minimo,
            long maximo) {

        if (indice[0] == preorden.length) {
            return null;
        }

        int codigo = preorden[indice[0]];

        if (codigo <= minimo || codigo >= maximo) {
            return null;
        }

        indice[0]++;

        Libro libro = new Libro(
                codigo,
                "Libro " + codigo,
                "Autor de ejemplo",
                2026);

        NodoLibro nodo = new NodoLibro(libro);

        nodo.setIzquierdo(
                construirDesdePreorden(
                        preorden, indice, minimo, codigo));

        nodo.setDerecho(
                construirDesdePreorden(
                        preorden, indice, codigo, maximo));

        return nodo;
    }
}