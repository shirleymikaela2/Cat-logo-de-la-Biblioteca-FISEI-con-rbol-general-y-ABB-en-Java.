package modelo;

public class Libro {

    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException(
                    "El codigo debe ser mayor que cero.");
        }

        validar(titulo, autor, anio);

        this.codigo = codigo;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    public static void validar(String titulo, String autor, int anio) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException(
                    "El titulo es obligatorio.");
        }

        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException(
                    "El autor es obligatorio.");
        }

        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException(
                    "El anio debe estar entre 1900 y 2026.");
        }
    }

    public void actualizar(String titulo, String autor, int anio) {
        validar(titulo, autor, anio);

        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setTitulo(String titulo) {
        actualizar(titulo, autor, anio);
    }

    public void setAutor(String autor) {
        actualizar(titulo, autor, anio);
    }

    public void setAnio(int anio) {
        actualizar(titulo, autor, anio);
    }

    @Override
    public String toString() {
        return codigo + " | " + titulo + " | " + autor + " | " + anio;
    }
}