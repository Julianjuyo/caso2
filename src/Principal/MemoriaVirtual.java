package Principal;
import java.util.*;
import java.math.*;

public class MemoriaVirtual {

    // ATRIBUTOS

    //Este atributo indica si el Thread que se encarga de leer las páginas que se deben llamar, ya leyó todas las páginas.
    private static boolean termineDeLeer;

    private static int numeroPaginasProceso;

    private static int numeroMarcosDePagina;

    private static int nivelDeLocalidad;

    private static Integer[] paginasPorLeer;

    private int cantFallosDePagina;

    private static long[] contadoresDeLlamadosACadaPagina;

    private static Boolean[] buffer;

    private static Integer[] tablaDePaginas;

    private static int ocupacionTablaDePaginas;

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

    public synchronized boolean revisarSiLaPaginaEstaEnLaTablaDePaginas(int paginaPorRevisar) {
        for (int i = 0; i < this.tablaDePaginas.length; i++) {
            if (this.tablaDePaginas[i] != null && this.tablaDePaginas[i] == paginaPorRevisar) {
                return true;
            }
        }
        return false;
    }


    public synchronized boolean revisarSiHayEspacioEnLaTablaDePaginas() {
        return this.ocupacionTablaDePaginas < this.numeroMarcosDePagina;
    }

    public synchronized void actualizarBuffer(int paginaLlamada) {
        Arrays.fill(this.buffer, false);
        this.buffer[paginaLlamada] = true;
    }

    private void limpiarBuffer() {
        Arrays.fill(this.buffer, null);
    }

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

    //¿Deberíamos reiniciar el contador de la página que sale?
    public synchronized void reemplazarPaginaEnTablaDePaginas(int paginaNueva, int paginaVieja){
        for(int i = 0; i < this.tablaDePaginas.length; i++){
            if(this.tablaDePaginas[i] == paginaVieja){
                this.tablaDePaginas[i] = paginaNueva;
                return;
            }
        }
    }

    public synchronized void agregarPaginaEnTablaDePaginas(int paginaNueva){
        this.tablaDePaginas[ocupacionTablaDePaginas]= paginaNueva;
    }

    //Este método realiza un corrimiento hacia la derecha a todas las páginas que no fueron llamadas, y adiciona un uno
    //a la izquierda en la página que fue llamada
    private void actualizarContadores(int paginaLlamada){
        for(int i = 0; i < this.contadoresDeLlamadosACadaPagina.length; i++){
            this.contadoresDeLlamadosACadaPagina[i] = this.contadoresDeLlamadosACadaPagina[i] >> 1;
        }
        this.contadoresDeLlamadosACadaPagina[paginaLlamada] += Math.pow(2,30);
    }

    public synchronized boolean elBufferEstaLimpio(){
        return this.buffer[0] == null;
    }

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