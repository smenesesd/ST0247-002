/**
 *
 * @author Samuel Meneses, Juan Felipe Ortiz, ljpalaciom
 */

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import java.util.Random;

public class Ruteos {
    ArrayList<ArrayList<Integer>> rutas;
    int noRutas;
    public static int n, m, u, breaks;
    public static double r, speed, Tmax, Smax, st_customer, Q;
    Digraph mapa;
    public static short tipoEstacion[];
    public static float pendienteFuncionCarga[];
    String filename;
    ArrayList<Pair<Float, Float>> coordenadas;
    double tiempoSolucion;
    public Ruteo(String filename) {
        this.filename = filename;
        BufferedReader lector;
        String linea;
        String lineaPartida[];
        try {
            lector = new BufferedReader(new FileReader(filename));
            double[] valores = new double[10];
            for (int i = 0; i < 10; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                valores[i] = Float.parseFloat(lineaPartida[2]);
            }
            n = (int) valores[0];
            m = (int) valores[1];
            u = (int) valores[2];
            breaks = (int) valores[3];
            r = valores[4];
            speed = valores[5];
            Tmax = valores[6];
            Smax = valores[7];
            st_customer = valores[8];
            Q = valores[9];
            lector.readLine();
            lector.readLine();
            lector.readLine();
            coordenadas = new ArrayList<Pair<Float, Float>>();
            mapa = new DigraphAM(n);
            for (int i = 0; i <= m; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas.add(new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3])));
            }
            tipoEstacion = new short[u];
            for (int i = 0; i < u; i++) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                coordenadas.add(new Pair(Float.parseFloat(lineaPartida[2]), Float.parseFloat(lineaPartida[3])));
                tipoEstacion[i] = Short.parseShort(lineaPartida[5]);
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double tiempo = (Math.sqrt(Math.pow(coordenadas.get(i).first - coordenadas.get(j).first,2)+ 
                                Math.pow(coordenadas.get(i).second - coordenadas.get(j).second, 2)))/speed;
                    mapa.addArc(i, j, tiempo);
                }
            }
            pendienteFuncionCarga = new float[3];
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]);
            }
            lector.readLine();
            lector.readLine();
            lector.readLine();
            for (int i = 0; i < 3; ++i) {
                linea = lector.readLine();
                lineaPartida = linea.split(" ");
                pendienteFuncionCarga[i] = Float.parseFloat(lineaPartida[3]) / pendienteFuncionCarga[i];
            }
            tiempoSolucion = Double.MAX_VALUE;
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public String toString() {
        return "Ruteo{" + "r=" + r + ", speed=" + speed + ", Tmax=" + Tmax + ", Smax=" + Smax + ", st_customer=" + st_customer + ", Q=" + Q + ", tiempoSolucion=" + tiempoSolucion + '}';
    }

    public void exportarPuntosCSV() {
        try {
            PrintStream escribirCoordenadas = new PrintStream(new File("ArchivosGenerados\\Coordenadas.csv"));
            escribirCoordenadas.println("X,Y");
            for (Pair<Float, Float> coordenada : coordenadas) {
                escribirCoordenadas.println(coordenada.first + "," + coordenada.second);
            }
            escribirCoordenadas.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

   

    public void solucionar(int metodo, long seed) {
        calculos solucion;
        if (metodo == 2) {
            solucion = new calculos(seed);
        } else {
            solucion = new calculos(metodo);
        }

        solucion.ahorros(this.mapa, m+1);
        solucion.calculadorRuta(this.mapa);
        this.rutas = solucion.rutas;
        this.rutas.trimToSize();
        this.noRutas = this.rutas.size();
        System.out.println();
    }

    /**
     * Este metodo es un test para verificar que la solucion es correcta. 
     * @param rutas Es un contenedor de rutas representadas por un arraylist de parejas donde el primer elemento indica el nodo
     * y el segundo elemento el tiempo que se quedo en ese nodo
     * @return Verdadero si el tiempo de solucion expresado concuerda y si la bateria nunca esta por debajo de 0.
     */
    public boolean comprobarSolucion( ArrayList<ArrayList<Pair<Integer, Integer>>> rutas){
        return false;
    }

    public static String[][] test() throws IOException{
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        Ficheros resultados = new Ficheros(names.size());
        File fileResults = new File("Resultados.txt");
        if(!fileResults.createNewFile()) { //Si Resultados.txt ya existia
            resultados.read();
        }
        String[][] analisis = new String[names.size()][7];
        int cont = 0;
        for(String file: names) {
            System.gc();
            Runtime runtime = Runtime.getRuntime();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            long mejor, peor, prom;
            mejor = Long.MAX_VALUE;
            peor = 0;
            prom = 0;
            Random generadorSemillas = new Random(System.currentTimeMillis());
            long seed = Math.abs(generadorSemillas.nextLong());
            for (int i = 0; i < 100; i++) {
                long ti = System.currentTimeMillis();
                Ruteo problema1 = new Ruteo("../DataSets/"+file);
                analisis[cont][2] = ""+problema1.m;
                problema1.solucionar(2, seed);   
                long tf = System.currentTimeMillis();
                long total = tf - ti;
                mejor = total < mejor ? total : mejor;
                peor = total > peor ? total : peor;
            }
            Ruteo problema1 = new Ruteos("../DataSets/"+file);
            problema1.solucionar(2, seed);
            analisis[cont][3] = "" + problema1.noRutas;
            double tiempoTotalRutas = problema1.tiempoTotalRutas();
            analisis[cont][4] = "" + tiempoTotalRutas;
            analisis[cont][5] = "" + mejor;
            prom = (peor + mejor) /2;
            analisis[cont][6] = "" + prom;
            System.gc();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = ((memoryAfter - memoryBefore)/1024); //Memoria en KB
            analisis[cont][0] = "" + memoryUsed;
            analisis[cont][1] = "" + peor;
            cont++;
            if (resultados.contains(file) == -1) {
                resultados.addNew(file, problema1.tiempoTotalRutas(), seed);
            } else if (tiempoTotalRutas < resultados.getTime(file)) {
                resultados.update(file, tiempoTotalRutas, seed);
            }
        }
        resultados.write();
        return analisis;
    }

    public double tiempoTotalRutas() {
        double tiempo = 0;
        for (ArrayList<Integer> ruta: this.rutas) {
            tiempo += this.mapa.getWeight(0, ruta.get(0));
            for (int i = 0; i < ruta.size()-1; i++) {
                tiempo += this.mapa.getWeight(ruta.get(i), ruta.get(i+1));
            }
            tiempo += this.mapa.getWeight(0, ruta.get(ruta.size()-1));
        }
        tiempo = Math.round(tiempo * 100.0) / 100.0;
        return tiempo;
    }

    public static void clearScreen() {  
        System.out.printf("\033[H\033[2J");  
        System.out.flush();  
        System.out.println();
    } 

    private static void printResults(String[][] analisis) {
        clearScreen();
        for(int i = 0; i < analisis.length; i++ ) {
            System.out.printf("%s KB, %s ms (peor), %s ms (mejor), %s ms (prom), %s clientes, %s camiones, %s tiempo total de todas las rutas\n",
                analisis[i][0], analisis[i][1], analisis[i][5], analisis[i][6], analisis[i][2], analisis[i][3], analisis[i][4]);
        }
    }

    public static void main(String[] args) throws IOException {
        long ti = System.currentTimeMillis();
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        Ficheros resultados = new Ficheros(names.size());                
        System.gc();
        String[][] analisis = test();
        //String[][] analisis = best();
        printResults(analisis);
        System.out.println("Bien hecho");
        long tf = System.currentTimeMillis();
        long total = tf - ti;
        System.out.println("Tiempo del algoritmo (best): " + total + " ms");
        //DibujarRuta bueno = new DibujarRuta(
        //problema1.exportarPuntosCSV();
    }

    public static void trainning() throws IOException{
        long ti = System.currentTimeMillis();
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        Ficheros resultados = new Ficheros(names.size());
        File fileResults = new File("Resultados.txt");
        if(!fileResults.createNewFile()) { //Si Resultados.txt ya existia
            resultados.read();
        } else {
            return;
        }
        for(String file: names) {
            for (int i = 0; i < 1000; i++) {
                //long ti = System.currentTimeMillis();
                Random generadorSemillas = new Random(System.currentTimeMillis());
                long seed = Math.abs(generadorSemillas.nextLong());
                Ruteo problema1 = new Ruteo("../DataSets/"+file);
                problema1.solucionar(2, seed);   
                // long tf = System.currentTimeMillis();
                // long total = tf - ti;
                // mejor = total < mejor ? total : mejor;
                // peor = total > peor ? total : peor;
                double tiempoTotalRutas = problema1.tiempoTotalRutas();
                if (resultados.contains(file) == -1) {
                    resultados.addNew(file, problema1.tiempoTotalRutas(), seed);
                } else if (tiempoTotalRutas < resultados.getTime(file)) {
                    resultados.update(file, tiempoTotalRutas, seed);
                }
            }
        }
        long tf = System.currentTimeMillis();
        long total = tf - ti;
        System.out.println("Tiempo del algoritmo (entrenamiento): " + total + " ms");
    }
    
    public static String[][] best() throws IOException{
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        Ficheros resultados = new Ficheros(names.size());
        File fileResults = new File("Resultados.txt");
        if(!fileResults.createNewFile()) { //Si Resultados.txt ya existia
            resultados.read();
        } else {
            return null; 
        }
        String[][] analisis = new String[names.size()][7];
        int cont = 0;
        for(String file: names) {
            System.gc();
            Runtime runtime = Runtime.getRuntime();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            long mejor, peor, prom;
            mejor = Long.MAX_VALUE;
            peor = 0;
            prom = 0;
            long seed = resultados.getSeed(file);
            for (int i = 0; i < 100; i++) {
                long ti = System.currentTimeMillis();
                Ruteo problema1 = new Ruteo("../DataSets/"+file);
                analisis[cont][2] = ""+problema1.m;
                problema1.solucionar(2, seed);   
                long tf = System.currentTimeMillis();
                long total = tf - ti;
                mejor = total < mejor ? total : mejor;
                peor = total > peor ? total : peor;
            }
            Ruteo problema1 = new Ruteo("../DataSets/"+file);
            problema1.solucionar(2, seed);
            analisis[cont][3] = "" + problema1.noRutas;
            double tiempoTotalRutas = problema1.tiempoTotalRutas();
            analisis[cont][4] = "" + tiempoTotalRutas;
            analisis[cont][5] = "" + mejor;
            prom = (peor + mejor) /2;
            analisis[cont][6] = "" + prom;
            System.gc();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = ((memoryAfter - memoryBefore)/1024); //Memoria en KB
            analisis[cont][0] = "" + memoryUsed;
            analisis[cont][1] = "" + peor;
            cont++;
            if (resultados.contains(file) == -1) {
                resultados.addNew(file, problema1.tiempoTotalRutas(), seed);
            } else if (tiempoTotalRutas < resultados.getTime(file)) {
                resultados.update(file, tiempoTotalRutas, seed);
            }
        }
        resultados.write();
        return analisis;
    }
    
    public static void probando() {
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        for(String file: names) {
            System.gc();
            Ruteo problema1 = new Ruteo("../DataSets/"+file);
            System.out.print("");
        }
        System.out.println("Good");
    }
}
