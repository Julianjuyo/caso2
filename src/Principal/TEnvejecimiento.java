package Principal;

public class TEnvejecimiento extends Thread  {

    public MemoriaVirtual memoriaVirtual;

    /**
     * Metodo construtoe
     * @param memoriaVirtual
     */
    public TEnvejecimiento(MemoriaVirtual memoriaVirtual) {
        this.memoriaVirtual = memoriaVirtual;
    }

    /**
     * Metodo run del thread
     */
    public void run(){
        try {
            while(!this.memoriaVirtual.darTermineDeLeer()){
                if(!this.memoriaVirtual.elBufferEstaLimpio()){
                    this.memoriaVirtual.algoritmoEnvejecimiento();
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}