package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

import java.util.List;

public class PruebasBiblioteca {

    private static int total;
    private static int correctas;

    private static void prueba(String nombre, boolean resultado) {
        total++;

        if (resultado) {
            correctas++;
        }

        System.out.println(
                (resultado ? "OK    " : "FALLO ")
                        + nombre);
    }

    private static String codigos(List<Libro> libros) {
        return codigos(libros, 0);
    }

    private static String codigos(
            List<Libro> libros,
            int indice) {

        if (indice == libros.size()) {
            return "";
        }

        return (indice == 0 ? "" : " ")
                + libros.get(indice).getCodigo()
                + codigos(libros, indice + 1);
    }

    private static void invalido(
            String nombre,
            Runnable accion) {

        try {
            accion.run();
            prueba(nombre, false);

        } catch (IllegalArgumentException e) {
            prueba(nombre, true);
        }
    }

    public static void main(String[] args) {
        total = 0;
        correctas = 0;

        ArbolBiblioteca soloRaiz = DatosPrueba.soloRaiz();
        ArbolBiblioteca biblioteca = DatosPrueba.cargar();

        // ARBOL GENERAL

        prueba(
                "Solo raiz: altura 0 y una hoja",
                soloRaiz.altura() == 0
                        && soloRaiz.contarHojas() == 1);

        prueba(
                "Biblioteca inicial: altura 2",
                biblioteca.altura() == 2);

        prueba(
                "Biblioteca inicial: cuatro hojas",
                biblioteca.contarHojas() == 4);

        prueba(
                "Biblioteca inicial: trece libros",
                biblioteca.contarLibrosTotal() == 13);

        prueba(
                "Niveles de BIB, sw, SW-E2 y XYZ",
                biblioteca.nivel("BIB") == 0
                        && biblioteca.nivel("sw") == 1
                        && biblioteca.nivel("SW-E2") == 2
                        && biblioteca.nivel("XYZ") == -1);

        prueba(
                "Rechazar estante directamente bajo biblioteca",
                !biblioteca.agregar(
                        "BIB",
                        new UnidadBiblioteca(
                                "MAL",
                                "Estante incorrecto",
                                TipoUnidad.ESTANTE)));

        prueba(
                "Rechazar codigo de unidad repetido",
                !biblioteca.agregar(
                        "BIB",
                        new UnidadBiblioteca(
                                "sw",
                                "Unidad duplicada",
                                TipoUnidad.AREA)));

        prueba(
                "Buscar sin distinguir mayusculas",
                biblioteca.buscar("sw-e1")
                        == biblioteca.buscar("SW-E1"));

        CatalogoABB catalogo =
                biblioteca.buscar("SW-E1").getCatalogo();

        // RECORRIDOS

        prueba(
                "Preorden inicial",
                codigos(catalogo.recorrer(
                        TipoRecorrido.PREORDEN))
                        .equals(
                                "60 35 20 45 40 80 70 75 90"));

        prueba(
                "Inorden inicial",
                codigos(catalogo.recorrer(
                        TipoRecorrido.INORDEN))
                        .equals(
                                "20 35 40 45 60 70 75 80 90"));

        prueba(
                "Posorden inicial",
                codigos(catalogo.recorrer(
                        TipoRecorrido.POSORDEN))
                        .equals(
                                "20 40 45 35 75 70 90 80 60"));

        // INSERCION Y RETOS

        prueba(
                "Rechazar libro repetido",
                !catalogo.insertar(DatosPrueba.libro(60))
                        && catalogo.contar() == 9);

        prueba(
                "Camino del codigo inexistente 42",
                catalogo.caminoBusqueda(42)
                        .equals(List.of(60, 35, 45, 40)));

        prueba(
                "Rango inclusivo de 40 a 75",
                catalogo.contarEnRango(40, 75) == 5);

        prueba(
                "Rango invertido devuelve cero",
                catalogo.contarEnRango(75, 40) == 0);

        prueba(
                "Insertar libro 42",
                catalogo.insertar(DatosPrueba.libro(42)));

        // TRES CASOS DE ELIMINACION

        prueba(
                "Eliminar hoja 20",
                catalogo.eliminar(20)
                        && catalogo.esABBValido());

        prueba(
                "Eliminar nodo 70 con un hijo",
                catalogo.eliminar(70)
                        && catalogo.buscar(75) != null
                        && catalogo.esABBValido());

        prueba(
                "Eliminar nodo 60 con dos hijos",
                catalogo.eliminar(60)
                        && catalogo.getRaiz()
                                .getDato().getCodigo() == 75
                        && catalogo.esABBValido());

        prueba(
                "Preorden final",
                codigos(catalogo.recorrer(
                        TipoRecorrido.PREORDEN))
                        .equals("75 35 45 40 42 80 90"));

        // BUSQUEDA Y MODIFICACION

        prueba(
                "Buscar modificar y eliminar codigo inexistente",
                catalogo.buscar(999) == null
                        && !catalogo.modificar(
                                999, "Ejemplo", "Autor", 2026)
                        && !catalogo.eliminar(999));

        prueba(
                "Modificar libro existente",
                catalogo.modificar(
                        42, "Java", "Autora", 2025)
                        && catalogo.buscar(42)
                                .getTitulo().equals("Java"));

        // VALIDACIONES DEL MODELO

        invalido(
                "Rechazar codigo cero",
                () -> new Libro(0, "Titulo", "Autor", 2026));

        invalido(
                "Rechazar titulo vacio",
                () -> new Libro(1, " ", "Autor", 2026));

        invalido(
                "Rechazar autor nulo",
                () -> new Libro(1, "Titulo", null, 2026));

        invalido(
                "Rechazar anio 1899",
                () -> new Libro(1, "Titulo", "Autor", 1899));

        invalido(
                "Rechazar anio 2027",
                () -> new Libro(1, "Titulo", "Autor", 2027));

        invalido(
                "Rechazar modificacion invalida",
                () -> catalogo.modificar(
                        42, "Otro titulo", "Otro autor", 1800));

        prueba(
                "Modificacion invalida conserva datos anteriores",
                catalogo.buscar(42).getTitulo().equals("Java"));

        // NIETO MAL UBICADO
        // 65 es mayor que 35, pero esta en el subarbol
        // izquierdo de 60: por eso el ABB es invalido.

        CatalogoABB incorrecto = new CatalogoABB();

        NodoLibro nodo60 =
                new NodoLibro(DatosPrueba.libro(60));

        NodoLibro nodo35 =
                new NodoLibro(DatosPrueba.libro(35));

        nodo60.setIzquierdo(nodo35);

        nodo35.setDerecho(
                new NodoLibro(DatosPrueba.libro(65)));

        incorrecto.setRaiz(nodo60);

        prueba(
                "Detectar nieto mal ubicado",
                !incorrecto.esABBValido());

        // CATALOGO VACIO

        CatalogoABB vacio = new CatalogoABB();

        prueba(
                "Operaciones sobre ABB vacio",
                vacio.contar() == 0
                        && vacio.esABBValido()
                        && !vacio.eliminar(1)
                        && vacio.caminoBusqueda(1).isEmpty());

        // EXTRA DESDE PREORDEN

        CatalogoABB reconstruido =
                CatalogoABB.desdePreorden(
                        new int[]{
                            60, 35, 20, 45, 40,
                            80, 70, 75, 90
                        });

        prueba(
                "Reconstruir ABB desde preorden",
                reconstruido.esABBValido()
                        && codigos(reconstruido.recorrer(
                                TipoRecorrido.PREORDEN))
                                .equals(
                                        "60 35 20 45 40 80 70 75 90"));

        invalido(
                "Rechazar preorden invalido",
                () -> CatalogoABB.desdePreorden(
                        new int[]{60, 80, 35}));

        invalido(
                "Rechazar preorden repetido",
                () -> CatalogoABB.desdePreorden(
                        new int[]{60, 60}));

        // TRAZA Y ELIMINACION DE RAIZ UNICA

        prueba(
                "Traza del preorden",
                reconstruido.trazaRecorrido(
                        TipoRecorrido.PREORDEN)
                        .endsWith(
                                "Secuencia PREORDEN: "
                                        + "60 35 20 45 40 80 70 75 90"));

        CatalogoABB catalogoTI =
                biblioteca.buscar("TI-E1").getCatalogo();

        prueba(
                "Eliminar unica raiz de un catalogo",
                catalogoTI.eliminar(50)
                        && catalogoTI.getRaiz() == null);

        System.out.println(
                "\n" + correctas + "/" + total + " pruebas OK");

        if (correctas != total) {
            throw new AssertionError(
                    "Existen pruebas fallidas.");
        }
    }
}