package Principal;

public class TEnvejecimiento extends Thread  {

    public MemoriaVirtual memoriaVirtual;

    public TEnvejecimiento(MemoriaVirtual memoriaVirtual) {
        this.memoriaVirtual = memoriaVirtual;
    }

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