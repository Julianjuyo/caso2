package Principal;

public class TActualizarReferencias extends Thread {

    // ATRIBUTOS

    public MemoriaVirtual memoriaVirtual;

    /**
     * Inicializar
     * @param memoriaVirtual
     */
    public TActualizarReferencias(MemoriaVirtual memoriaVirtual){
        this.memoriaVirtual = memoriaVirtual;
    }


    /**
     *
     */
    public void run(){

        try {

            for (int i = 0; i < memoriaVirtual.darCantidadPaginasPorLeer(); i++) {

                int paginaLeida = memoriaVirtual.darPaginaConIndice(i);

                //Revisa si la pagina ya esta en la tabla de páginas
                if(memoriaVirtual.revisarSiLaPaginaEstaEnLaTablaDePaginas(paginaLeida)){

                    memoriaVirtual.actualizarBuffer(paginaLeida);
                }
                else {
                    //Ocurre un fallo de pagina

                    memoriaVirtual.actualizarCantFallosDePagina();

                    //Hay espacio libre en la tabla de paginas
                    if (memoriaVirtual.revisarSiHayEspacioEnLaTablaDePaginas()){

                        memoriaVirtual.agregarPaginaEnTablaDePaginas(paginaLeida);
                        memoriaVirtual.actualizarOcupacionTablaDePaginas();
                        memoriaVirtual.actualizarBuffer(paginaLeida);
                    }
                    else {
                        //No hay espacio en la tabla de páginas
                        memoriaVirtual.reemplazarPaginaEnTablaDePaginas(paginaLeida,memoriaVirtual.darPaginaConMenosSumatoria());
                        memoriaVirtual.actualizarBuffer(paginaLeida);
                    }
                }
                Thread.sleep(5);
            }

            memoriaVirtual.actualizarTermineDeLeer();

            System.out.println(memoriaVirtual.darCantidadFallosDePaginas());



            System.out.println();
        } catch (InterruptedException e) {
        e.printStackTrace();
    }

    }

}
