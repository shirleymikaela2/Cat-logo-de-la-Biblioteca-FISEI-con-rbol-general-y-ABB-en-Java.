package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodoUnidad {

    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "La unidad es obligatoria.");
        }

        this.dato = dato;
        this.hijos = new ArrayList<>();

        if (dato.getTipo() == TipoUnidad.ESTANTE) {
            this.catalogo = new CatalogoABB();
        } else {
            this.catalogo = null;
        }
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public List<NodoUnidad> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    public CatalogoABB getCatalogo() {
        return catalogo;
    }

    void agregarHijo(NodoUnidad hijo) {
        hijos.add(hijo);
    }
}