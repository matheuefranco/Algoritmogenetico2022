/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmogeneticojava;
import java.util.Vector;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AlgoritmoGenetico {
    private int tamCromossomo=0,numGeracoes,tamPopulacao,
                 probMutacao,qtdeCruzamentos;
    private double capacidade = 0;
    private Vector<Vector> populacao;
    private Vector<Produto> produtos = new Vector();
    private Vector<Integer> roleta = new Vector();
    //--------------------------------
    private void carregaArquivo(){
       String csvFile = "dados.csv";
        String line = "";
        String[] produto = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                produto = line.split(",");
                Produto prod = new Produto();
                prod.setDescrição(produto[0]);
                prod.setPeso(Double.parseDouble(produto[1]));
                prod.setValor(Double.parseDouble(produto[2]));
                produtos.add(prod);
                prod.show();
                this.tamCromossomo++;
            }// fim percurso no arquivo
            
            System.out.println("Tamanho do cromossomo:"+this.tamCromossomo);
           // this.tamCromossomo = desc_items.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //-------------------------
    private Vector criaCromossomo(){
        
    }
  //---------------------------------- 
    private void criaPopulacao(){
      
    }
    //----------------------------------
     private double fitness(Vector cromossomo) {
        double peso=0, beneficio=0;
        for(int i=0; i< this.tamCromossomo;i++){
            int leva = (int)cromossomo.get(i);
            if(leva==1){
                Produto p = new Produto();
                p = produtos.get(i);
                peso+= p.getPeso();
                beneficio+= p.getValor();
            }// fim leva
        }// fim for
         if(peso<=this.capacidade)
             return beneficio;
         else
             return 0;
    }
     
      //------------------------------------------
     private int roleta(){
       
     }
   //-----------------------------------------------
    private int torneio(){ // Seleção dos pais
        int s1,s2,s3;
        double notaS1, notaS2, notaS3;
        Random r = new Random();
        s1 = r.nextInt(this.tamPopulacao);
        s2 = r.nextInt(this.tamPopulacao);
        s3 = r.nextInt(this.tamPopulacao);
        notaS1 = fitness(populacao.get(s1));
        notaS2 = fitness(populacao.get(s2));
        notaS3 = fitness(populacao.get(s3));
        if(notaS1 > notaS2 && notaS1 > notaS3)
            return s1;
        else if(notaS2 > notaS1 && notaS2 > notaS3)
            return s2;
        else
            return s3;   
     }
     //---------------
    private Vector cruzamento(){
        Vector filho1 = new Vector(); 
        Vector filho2 = new Vector();
        Vector<Vector>filhos = new Vector();
        Vector p1,p2 = new Vector();
        int ip1, ip2;
        //ip1 = torneio();
        //ip2 = torneio();
        ip1 = roleta();
        ip2 = roleta();
        p1 = populacao.get(ip1);
        p2 = populacao.get(ip2);
        Random r = new Random();
        int pos = r.nextInt(this.tamCromossomo); // ponto de corte
        for(int i=0;i<=pos;i++){
            filho1.add(p1.get(i));
            filho2.add(p2.get(i));
        }
        for(int i=pos+1;i<this.tamCromossomo;i++){
            filho1.add(p2.get(i));
            filho2.add(p1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }
    //----------------------------------------
     private void mutacao(Vector filho){
       Random r = new Random();
       int v = r.nextInt(100);
       if(v<this.probMutacao){
           int ponto = r.nextInt(this.tamCromossomo);
           if((int)filho.get(ponto)==1)
               filho.set(ponto,0);
           else
               filho.set(ponto,1);
         System.out.println("Ocorreu mutação!");
       }// fim if mutacao  
     }
 //--------------------------------------- 
    protected int obterPior(){
       int indicePior=0;
         double pior,nota=0;
        pior = fitness((Vector)populacao.get(0));
        for(int i=1;i<this.tamPopulacao;i++){
           nota = fitness((Vector)populacao.get(i));
           if(nota < pior){
               pior = nota;
               indicePior = i;
            }// fim if
        }// fim for
        return indicePior;
    }// fim funcao
    //---------------------------------
     private void novaPopulacao(){
       for(int i=0;i<this.qtdeCruzamentos;i++){
           populacao.remove(obterPior());
           populacao.remove(obterPior());
       }
     }
 //--------------------------------
     private void operadoresGeneticos(){
         Vector f1,f2,filhos = new Vector();
         for(int i=0;i<this.qtdeCruzamentos;i++){
            filhos = cruzamento();
            f1 = (Vector)filhos.get(0);
            f2 = (Vector)filhos.get(1);
            mutacao(f1);
            mutacao(f2);
            populacao.add(f1);
            populacao.add(f2);
         }
     }
   //------------------------------------------------
    protected int obterMelhor(){
         int indiceMelhor=0;
         double melhor,nota=0;
        melhor = fitness((Vector)populacao.get(0));
        for(int i=1;i<this.tamPopulacao;i++){
           nota = fitness((Vector)populacao.get(i));
           if(nota > melhor){
               melhor = nota;
               indiceMelhor = i;
            }// fim if
        }// fim for
        return indiceMelhor;
    }// fim funcao    
   
   //------------------------------------------  
    public void mostraPopulacao(){
        for(int i=0;i<this.tamPopulacao;i++){
            System.out.println("Cromossomo "+ i + 
                                populacao.get(i));
            System.out.println("Avaliação: "+ 
                          fitness((Vector)populacao.get(i)));
        }// fim for
    }
    //-------------------------
    public void executaAG(){ 
           criaPopulacao();
           // Executando todas gerações
           for(int i=0;i<this.numGeracoes;i++){
            System.out.println("Geração "+ i);
            mostraPopulacao();
            operadoresGeneticos(); // Seleção - Cruzamento - Mutacao - Adicionando na população
            novaPopulacao();
           }
           // Ao final do AG, mostrar o melhor
           
           int ind_melhor = obterMelhor();
           Vector melhor = new Vector();
           melhor = populacao.get(ind_melhor);
           System.out.println("Melhor Solução:"+melhor);
           System.out.println("Avaliação do Melhor:"+fitness(melhor));
           // percorrer a solução e mostrar os produtos
           System.out.println("Produtos levados na mochilha:");
           for(int i=0;i<this.tamCromossomo;i++){
                int leva = (int)melhor.get(i);
                if(leva==1){
                 Produto p = new Produto();
                 p= produtos.get(i);
                 System.out.println(p.getDescrição() + 
                         " Valor:"+p.getValor()+ " Peso:"+p.getPeso());
              }// fim if
           }// fim for
    }   // fim executa
    //--------------------------
    public AlgoritmoGenetico(int numGeracoes,
         int tamPopulacao,int probMutacao,int qtdeCruzamentos, double capacidade){
         this.numGeracoes = numGeracoes;
         this.tamPopulacao = tamPopulacao;
         this.probMutacao = probMutacao;
         this.qtdeCruzamentos = qtdeCruzamentos;
         this.capacidade = capacidade;
         this.carregaArquivo();
         
    }
    //----------------------------

}// fim classe
