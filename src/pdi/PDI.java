/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author Luiz
 */

public class PDI {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int tam[];
        int matriz[][];
        int matriz2[][];
        int matrizTrans[][];
        int matrizResul[][];
        int histograma[];
        
        String AbrirArq1 = "E:\\lena256.pgm";
//        String AbrirArq2 = "E:\\caveira2.pgm";
        String SalvarArq = "E:\\modificado.pgm";
        String SalvarHistograma = "E:\\histograma.txt";
        
        tam = tamMatriz(AbrirArq1);
        
        matriz = lerArquivo(AbrirArq1, tam[0], tam[1]);
        
//        matrizTrans = matTrans(256, 256, 25);
//        
//        matrizResul = somarMatriz(matriz, matrizTrans, tam[0], tam[1]);
//        matrizResul = flipMatriz(matriz, tam[0], tam[1], 1);;
//        matriz2 = destacarPonto(matriz, tam[0], tam[1], 90, 120, 100);;
//        matrizResul = subImagem(matriz, matriz2, tam[0], tam[1]);
//        matrizResul = zoomIn(matriz, tam[0], tam[1]);
        
        histograma = histograma(matriz,tam[0],tam[1]);
        matrizResul = eqReta(matriz, tam[0], tam[1], 100, 200);

//        salvarMatriz(matriz2,tam[0],tam[1], AbrirArq2);
        salvarMatriz(matrizResul,tam[0],tam[1], SalvarArq);
//        salvarHistograma(histograma,SalvarHistograma);
    }
    
    private static int [][] iniciarMatriz(int i, int j){
        int m[][] = new int[i][j];
        Scanner s = new Scanner(System.in);
        
        for(int aL = 0; aL < i; aL++)
            for(int aC = 0; aC < j; aC++){
                System.out.print("Valor de entrada ["+(aL+1)+"]["+(aC+1)+"] > ");
                m[aL][aC] = s.nextInt();
            }
        
        return m;
    }
    
    private static void imprimirMatriz(int m[][], int i, int j){
        for(int aL = 0; aL < i; aL++){
            //System.out.print("|  ");
            for(int aC = 0; aC < j; aC++)
                System.out.print(m[aL][aC]+"\n");
            //System.out.println("  |");
        }
    }
    
    private static int[][] somarPerMatriz(int m1[][], int i, int j, float p){
        int mR[][] = new int[i][j];
        
        for(int aL = 0; aL < i; aL++)
            for(int aC = 0; aC < j; aC++)
                if((m1[aL][aC] * p) > 255) mR[aL][aC] = 255;
                    else if ((m1[aL][aC] * p) < 0) mR[aL][aC] = 0;
                    else mR[aL][aC] = (int)(m1[aL][aC] * p);
        return mR;
    }
    
    private static int[][] somarMatriz(int m1[][], int m2[][],int i,int j){
        int mR[][] = new int[i][j];
        
        for(int aL = 0; aL < i; aL++)
            for(int aC = 0; aC < j; aC++)
                if(m1[aL][aC] + m2[aL][aC] > 255) mR[aL][aC] = 255;
                    else if (m1[aL][aC] + m2[aL][aC] < 0) mR[aL][aC] = 0;
                    else mR[aL][aC] = m1[aL][aC] + m2[aL][aC];
        return mR;
    }
    
    private static int[] tamMatriz(String localArq) throws FileNotFoundException, IOException{
        int tam[] = new int[2];
        
        BufferedReader br;
        
        br = new BufferedReader(new FileReader(localArq));
                                                                // leitura cabecalho
        String valoresLinha = br.readLine();                    // . tipo do arquivo
        valoresLinha = br.readLine();                           // . comentario
        valoresLinha = br.readLine();                           // . tamanho da imagem
        String valoresQuebrado[] = valoresLinha.split(" ");     // obtendo tamanho da imagem
        
        tam[1] = Integer.valueOf(valoresQuebrado[0]);           // coluna
        tam[0] = Integer.valueOf(valoresQuebrado[1]);           // linha

        br.close();

        return tam;
    }
    
    private static int[][] lerArquivo(String localArq, int l, int c) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(localArq));
        int matriz[][] = new int[l][c];
        String valoresLinha = null;
        
        for(int aux = 0; aux < 4; aux++)
            valoresLinha = br.readLine();
        
        for(int aL = 0; aL < l; aL++)
            for(int aC = 0; aC < c; aC++)
                matriz[aL][aC] = Integer.valueOf(br.readLine());
        
        return matriz;
    }
    
    private static int[][] matTrans(int i, int j, int valor){   //inicia a matriz para operacao
        int matriz[][] = new int[i][j];
        
        for(int aL = 0; aL < i; aL++)
            for(int aC = 0; aC < j; aC++){
                matriz[aL][aC] = valor;
            }
        
        return matriz;
    }
    
    private static boolean salvarMatriz(int[][] m,int i, int j, String SalvarArq) throws FileNotFoundException, IOException{
        OutputStream salvar = new FileOutputStream(SalvarArq);
        
        String cabecalho = "P2\n#CREATOR: LUIZ G THOMAZ\n"+j+" "+i+"\n255\n";
        byte[] salvando = cabecalho.getBytes();
        salvar.write(salvando); //escrevendo cabecalho do arq

        for(int aL = 0; aL < i; aL++)
            for(int aC = 0; aC < j; aC++){
                salvando =  String.valueOf(m[aL][aC]).getBytes();
                salvar.write(salvando);
                salvando =  "\n".getBytes();
                salvar.write(salvando);
            }
        
        salvar.close();
        
        return true;
    }
    
    private static boolean salvarHistograma(int[] histograma, String SalvarHistograma) throws FileNotFoundException, IOException{
        OutputStream salvar = new FileOutputStream(SalvarHistograma);
        
        byte[] salvando;
        for(int aux = 0; aux < histograma.length; aux++){
            salvando =  String.valueOf(histograma[aux]+" \n").getBytes();            
            salvar.write(salvando);
        }
        
        salvar.close();
        return true;
    }
    
    private static int[][] girarNovGraus(int matriz[][], int i, int j){
        int giro[][] = new int[j][i];
        
        int linha = i-1;
        
        for(int auxL = 0; auxL < i ; auxL++, linha--)
            for(int auxC = 0; auxC < j; auxC++)
                giro[auxC][linha] = matriz[auxL][auxC];
        
        return giro;
    }
    
    private static int[][] binarizarMatriz(int matriz[][], int i, int j, int k){;
        int imagem[][] = new int[i][j];
        
        for(int auxL = 0; auxL < i; auxL++)
            for(int auxC = 0; auxC < j; auxC++)
                if(matriz[auxL][auxC] >= k)
                    imagem[auxL][auxC] = 255;
                else imagem[auxL][auxC] = 0;
        
        return imagem;
    }
    
    private static int[][] flipMatriz(int matriz[][], int i, int j, int mod){
        int imagem[][] = new int[i][j];
        
        if(mod == 0)
            for(int auxL = 0; auxL < i; auxL++)
                for(int auxC = 0; auxC < j; auxC++)
                    imagem[i-1-auxL][auxC] = matriz[auxL][auxC];
        else
            for(int auxC = 0; auxC < j; auxC++)
                for(int auxL = 0; auxL < i; auxL++)
                    imagem[auxL][j-1-auxC] = matriz[auxL][auxC];
        
        return imagem;
    }
    
    private static int[][] binarizacaoSeletiva(int matriz[][], int i, int j, int A, int B, int tomClaro, int tomEscuro){
        int imagem[][] = new int[i][j];
        
        for(int auxL = 0; auxL < i; auxL++)
                for(int auxC = 0; auxC < j; auxC++)
                    if((matriz[auxL][auxC] < A)||(matriz[auxL][auxC] > B))
                        imagem[auxL][auxC] = tomEscuro;
                    else imagem[auxL][auxC] = tomClaro;
        
        return imagem;
    }
    
    private static int[][] destacarPonto(int matriz[][], int i, int j, int A, int B, int tomClaro){
        int imagem[][] = new int[i][j];
        
        for(int auxL = 0; auxL < i; auxL++)
                for(int auxC = 0; auxC < j; auxC++)
                    if((matriz[auxL][auxC] < A)||(matriz[auxL][auxC] > B))
                        imagem[auxL][auxC] = matriz[auxL][auxC];
                    else imagem[auxL][auxC] = tomClaro;
        
        return imagem;
    }

//    private static int[][] subImagem(int matriz1[][], int matriz2[][], int i, int j){
//        int imagem[][] = new int[i][j];
//        
//        
//        for(int auxL = 0; auxL < i; auxL++)
//                for(int auxC = 0; auxC < j; auxC++){
//                    int aux = matriz1[auxL][auxC];
//                    int aux2 = matriz2[auxL][auxC];
//                    if(aux && aux2)
//                        imagem[auxL][auxC] = matriz1[auxL][auxC];
//                    else imagem[auxL][auxC] = 0;
//                }
//        return imagem;
//    }
    
    private static int[][] gama(int matriz[][], int i, int j, float c, float g){
        int imagem[][] = new int[i][j];
        
        for(int auxL = 0; auxL < i; auxL++)
                for(int auxC = 0; auxC < j; auxC++){
                    imagem[auxL][auxC] = (int)((c*Math.pow(matriz[auxL][auxC]/255.0,g))*255);
                }
        return imagem;
    }
    
    private static int[][] log(int matriz[][], int i, int j, int r, int c){
        int imagem[][] = new int[i][j];
        
        for(int auxL = 0; auxL < i; auxL++)
                for(int auxC = 0; auxC < j; auxC++)
                    imagem[auxL][auxC] = (int)(c*Math.log(1+(float)matriz[auxL][auxC]/255)*255);
        
        return imagem;
    }
    
    private static int[][] zoomIn(int matriz[][], int i, int j){
        int imagem[][] = new int[i*2][j*2];
        
        for(int auxL = 0, aL = 0; auxL < i; auxL++, aL = aL+2)
                for(int auxC = 0, aC = 0; auxC < j; auxC++, aC = aC+2){
                    imagem[aL][aC] = matriz[auxL][auxC];
                    imagem[aL+1][aC] = matriz[auxL][auxC];
                    imagem[aL][aC+1] = matriz[auxL][auxC];
                    imagem[aL+1][aC+1] = matriz[auxL][auxC];
                }
        
        return imagem;
    }
    
    private static int[][] zoomOut(int matriz[][], int i, int j){
        int imagem[][] = new int[i/2][j/2];
        
        for(int auxL = 0, aL = 0; auxL < i; auxL=auxL+2, aL++)
                for(int auxC = 0, aC = 0; auxC < j; auxC=auxC+2, aC++)
                    imagem[aL][aC] = (matriz[auxL][auxC]+matriz[auxL+1][auxC]+matriz[auxL][auxC+1]+matriz[auxL+1][auxC+1])/4;
        
        return imagem;
    }
    
    private static int[] histograma(int matriz[][], int i, int j){
        int histograma[] = new int[255];
        
        for(int auxL = 0, aL = 0; auxL < i; auxL=auxL+2, aL++)
                for(int auxC = 0, aC = 0; auxC < j; auxC=auxC+2, aC++)
                    histograma[matriz[auxL][auxC]]++;
        
        return histograma;
    }
    
    private static int[][] eqReta(int matriz[][], int i, int j, int min, int max){
        int imagem[][] = new int [i][j];
        
        float a = (float) (max-min)/255;
        float b = (float) (max - 255.0*a);
        
        for(int auxL = 0; auxL < i; auxL++)
            for(int auxC = 0; auxC < j; auxC++){
                if(matriz[auxL][auxC] < min) matriz[auxL][auxC] = 0;
                else if(matriz[auxL][auxC] > max) matriz[auxL][auxC] = 255;
                    else imagem[auxL][auxC] = (int) (a*matriz[auxL][auxC]+b);
            }
        return imagem;
    }

}