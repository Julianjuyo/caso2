package Principal;

import java.util.ArrayList;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    // ATRIBUTOS
    static String ruta= "";

    // METODO MAIN
    public static void main(String[] args) {

        //CREACIÓN DE PANEL PARA CARGAR ARCHIVO

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JComboBox Test");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Select File");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    System.out.println(selectedFile.getPath());
                    ruta= selectedFile.getPath();

                    int numeroMarcosDePagina=0;
                    int numeroPaginasProceso=0;
                    int nivelDeLocalidad=0;

                    ArrayList<Integer> paginasPorLeer = new ArrayList<Integer>();

                    try {


                        // Lectura del documento
                        String cadena;
                        FileReader f = new FileReader(ruta);
                        BufferedReader b = new BufferedReader(f);

                        cadena = b.readLine();
                        numeroMarcosDePagina = Integer.parseInt(cadena);

                        cadena = b.readLine();
                        numeroPaginasProceso = Integer.parseInt(cadena);

                        cadena = b.readLine();
                        nivelDeLocalidad = (int)Double.parseDouble(cadena);

                        while((cadena = b.readLine())!=null) {
                            paginasPorLeer.add(Integer.parseInt(cadena));
                        }
                        b.close();

                        System.out.println("numeroMarcosDePagina: "+numeroMarcosDePagina);
                        System.out.println("numeroPaginasProceso: "+numeroPaginasProceso);
                        System.out.println("nivelDeLocalidad: "+nivelDeLocalidad);

                        // Inicialización del programa

                        Integer[] paginasPorLeerArray= new Integer[paginasPorLeer.size()];
                        paginasPorLeerArray = paginasPorLeer.toArray(paginasPorLeerArray);

                        MemoriaVirtual memoriaVirtual = new MemoriaVirtual(numeroPaginasProceso, numeroMarcosDePagina, paginasPorLeerArray, nivelDeLocalidad);

						TActualizarReferencias tActualizarReferencias = new TActualizarReferencias(memoriaVirtual);
						TEnvejecimiento tEnvejecimiento = new TEnvejecimiento(memoriaVirtual);

                        tActualizarReferencias.start();
                        tEnvejecimiento.start();






                    } catch (FileNotFoundException e) {

                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });
        frame.add(button);
        frame.pack();
        frame.setVisible(true);



    }

}
