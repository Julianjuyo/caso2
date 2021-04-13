package Principal;
import java.util.*;
import java.math.*;

public class MemoriaVirtual {

    // ATRIBUTOS

    //Este atributo indica si el Thread que se encarga de leer las páginas que se deben llamar, ya leyó todas las páginas.
    private boolean termineDeLeer;

    private final int numeroPaginasProceso;

    private final int numeroMarcosDePagina;

    private final int nivelDeLocalidad;

    private final Integer[] paginasPorLeer;

    private int cantFallosDePagina;

    private final long[] contadoresDeLlamadosACadaPagina;

    private final Boolean[] buffer;

    private final Integer[] tablaDePaginas;

    private int ocupacionTablaDePaginas;


    // CONSTRUCTOR

    public MemoriaVirtual(int numeroPaginasProceso, int numeroMarcosDePagina, Integer[] paginasPorLeer, int nivelDeLocalidad) {
        this.numeroPaginasProceso = numeroPaginasProceso;
        this.numeroMarcosDePagina = numeroMarcosDePagina;
        this.paginasPorLeer = paginasPorLeer;
        this.nivelDeLocalidad = nivelDeLocalidad;
        this.cantFallosDePagina = 0;
        this.contadoresDeLlamadosACadaPagina = new long[numeroPaginasProceso];
        this.buffer = new Boolean[numeroPaginasProceso];
        this.tablaDePaginas = new Integer[numeroMarcosDePagina];
        this.ocupacionTablaDePaginas = 0;
    }

    // METODOS

    public synchronized boolean darTermineDeLeer(){
        return this.termineDeLeer;
    }

    public synchronized void actualizarTermineDeLeer(){
        this.termineDeLeer= true;
    }

    public void actualizarOcupacionTablaDePaginas(){
        this.ocupacionTablaDePaginas++;
    }
    
    public int darOcupacionTablaDePaginas(){
        return this.ocupacionTablaDePaginas;
    }

    public int darCantidadFallosDePaginas(){
        return this.cantFallosDePagina;
    }

    public int darNivelDeLocalidad(){
        return this.nivelDeLocalidad;
    }

    public int darNumeroMarcosDePagina(){
        return this.numeroMarcosDePagina;
    }

    public int darNumeroPaginasProceso(){
        return this.numeroPaginasProceso;
    }

    public int darCantidadPaginasPorLeer(){
        return this.paginasPorLeer.length;
    }

    public int darPaginaConIndice(int indice){
        return paginasPorLeer[indice];
    }

    public void actualizarCantFallosDePagina(){
        this.cantFallosDePagina++;
    }

    /**
     * metodo para ver si una pagina esta en la tabla
     * @param paginaPorRevisar
     * @return
     */
    public boolean revisarSiLaPaginaEstaEnLaTablaDePaginas(int paginaPorRevisar) {
        for (Integer tablaDePagina : this.tablaDePaginas) {
            if (tablaDePagina != null && tablaDePagina == paginaPorRevisar) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo para saber si hay espacio libre en la tabla de paginas
     * @return
     */
    public boolean revisarSiHayEspacioEnLaTablaDePaginas() {
        return this.ocupacionTablaDePaginas < this.numeroMarcosDePagina;
    }

    /**
     * Metodo que actuliza el valor de buffer
     * @param paginaLlamada
     */
    public synchronized void actualizarBuffer(int paginaLlamada) {
        Arrays.fill(this.buffer, false);
        this.buffer[paginaLlamada] = true;
    }

    /**
     * Metodo que limpia el buffer y los deja en null
     */
    private void limpiarBuffer() {
        Arrays.fill(this.buffer, null);
    }

    /**
     * Método que entrega la página menos referenciada dentro de todas la páginas.
     * @return
     */
    public synchronized int darPaginaConMenosSumatoria() {
        long menorSumatoria = Long.MAX_VALUE;
        int paginaConMenorSumatoria = -1;
        for (int i = 0; i < this.contadoresDeLlamadosACadaPagina.length; i++) {
            if (this.contadoresDeLlamadosACadaPagina[i] < menorSumatoria && revisarSiLaPaginaEstaEnLaTablaDePaginas(i)) {
                menorSumatoria = this.contadoresDeLlamadosACadaPagina[i];
                paginaConMenorSumatoria = i;
            }
        }
        return paginaConMenorSumatoria;

    }

    /**
     * Metodo que remplaza una pagina por otra en la tabla de página
     * @param paginaNueva
     * @param paginaVieja
     */
    public void reemplazarPaginaEnTablaDePaginas(int paginaNueva, int paginaVieja){
        for(int i = 0; i < this.tablaDePaginas.length; i++){
            if(this.tablaDePaginas[i] == paginaVieja){
                this.tablaDePaginas[i] = paginaNueva;
                return;
            }
        }
    }

    /**
     * Metodo que agregar una página en la tabla de página esto sucede cuando hay espacio vacios
     * @param paginaNueva
     */
    public void agregarPaginaEnTablaDePaginas(int paginaNueva){
        this.tablaDePaginas[ocupacionTablaDePaginas]= paginaNueva;
    }


    /**
     * Este método realiza un corrimiento hacia la derecha a todas las páginas que no fueron llamadas, y adiciona un uno
     * a la izquierda en la página que fue llamada
     * @param paginaLlamada
     */
    private void actualizarContadores(int paginaLlamada){
        for(int i = 0; i < this.contadoresDeLlamadosACadaPagina.length; i++){
            this.contadoresDeLlamadosACadaPagina[i] = this.contadoresDeLlamadosACadaPagina[i] >> 1;
        }
        this.contadoresDeLlamadosACadaPagina[paginaLlamada] += Math.pow(2,30);
    }

    /**
     * Metodo que pregunta si el buffer ya tiene una referencia
     * @return
     */
    public synchronized boolean elBufferEstaLimpio(){
        return this.buffer[0] == null;
    }

    /**
     * Metodo que ejecuta el algoritmo de envejecimiento
     */
    public synchronized void algoritmoEnvejecimiento() {
        boolean encontreElUno = false;
        int paginaLlamada = -1;
        for (int i = 0; i < this.buffer.length && !encontreElUno; i++) {
            if (this.buffer[i]) {
                encontreElUno = true;
                paginaLlamada = i;
            }
        }
        actualizarContadores(paginaLlamada);
        limpiarBuffer();
    }
}