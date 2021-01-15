
package socket;


/*
IMPORTAÇÕES
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author luiza
 */
public class ClienteMulti {
//conferir SO
    
    //Variável estática que irá conter o nome do SO em minúsculo
    private static String SO = System.getProperty("os.name").toLowerCase();
     public static boolean isWindows() {
        return (SO.indexOf("win") >= 0);
    }
     public static boolean isUnix() {
        return (SO.indexOf("nix") >= 0 || SO.indexOf("nux") >= 0 || SO.indexOf("aix") > 0);
    }
     
    
    /*------------------------------------------ COMANDOS PARA LINUX E WIN -----------------------------------------------*/
    
    public static void ComandoWin (Date data){
     //executando mudança
         Process executa;
         Process executa2;
        
         //convertendo formato da hora
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy"); 
        
        //imprimindo hora
        System.out.println("O HORÁRIO ATUAL É: " +sdf.format(data));
        System.out.println("A DATA ATUAL É: "+sdf2.format(data));
        
        try { 
       
            //chamando o comando no cmd
        executa = Runtime.getRuntime().exec("cmd /C time " + sdf.format(data)); //mudando hora
        executa2 = Runtime.getRuntime().exec("cmd /C date " + sdf2.format(data)); //mudando data
        
            
        //confere se foi executado com sucesso
        if ( executa.waitFor() == 0 && executa2.waitFor()==0 ) System.out.println("Executado."); 
        else System.out.println("Erro no CMD"); //mensagem de erro
        } 
        catch (IOException e) { e.printStackTrace(); 
        } catch (InterruptedException e) { e.printStackTrace(); }
         
    
    }

    
    public static void ComandoLinux(Date data){

    	//executando mudança
    	    Process executa, executa2;
    	   
    	    //convertendo formato da hora
    	   SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    	   SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy"); 
    	 
           SimpleDateFormat sdflinux = new SimpleDateFormat("MM/dd/yy");
	       SimpleDateFormat sdflinux2 = new SimpleDateFormat("hh:mm:ss");
    	   //imprimindo hora
    	   System.out.println("O HORÁRIO ATUAL É: " +sdf.format(data));
    	   System.out.println("A DATA ATUAL É: "+sdf2.format(data));
    	   
    	   try { 
    	  
    	       //chamando o comando no cmd
    	  executa = Runtime.getRuntime().exec("date -s" + sdflinux.format(data)); //mudando hora e data
    	   executa2 = Runtime.getRuntime().exec("date -s" + sdflinux2.format(data)); //mudando hora e data

    	   } 
    	  catch (IOException e) { e.printStackTrace(); }
    	    

    }


/*------------------------------------------ MENSAGEM EXIBIDA PARA O USUÁRIO -----------------------------------------------*/

    public static void MsgBoasVindas (){
            System.out.println("\n----------CLIENTE-----------");
            System.out.println("\nSeja bem vindo!\n");
            System.out.println("\nVocê foi conectado com sucesso!\n");
            System.out.println("\nSeu horário será atualizado.\n");
    }   
    
    public static void MsgSair(){
     		 System.out.println("\nSeu horário foi trocado com sucesso! O programa irá executar a cada 60 minutos para atualizá-lo.\n");
            System.out.println("Deseja continuar?\n>1< = SIM\n>-1< = NÃO");
    }


/*------------------------------------------ CALCULO DA DIFERENÇA  -----------------------------------------------*/    
    public static long CalcularDiferenca(long servidor, long cliente){ 
        //calculo da diferença
        long dif = servidor - cliente; 
        long dias = dif/(1000606024);
        long horas = ((dif - (dias*1000606024))/(10006060));
        long min = ((dif - (dias*1000606024))-(10006060*horas))/(1000*60);
        long seg = min*60;
        return seg;
         }
    /*------------------------------------------ MAIN -----------------------------------------------*/
    

    
    public static void main(String[] args) {
       
        try {

            int i = 0;
            Scanner lerSaida = new Scanner(System.in);
            
            Socket cliente = new Socket("192.168.0.130", 1234); //estabelencendo conexao com o IP do servidor
            
            //Mensagem para o Usuario
             MsgBoasVindas();
    
            //recebendo arquivo do servidor
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            Date data = (Date)in.readObject();  //data armazena a data do servidor 
            Date dataConf = new Date(); //data para armazenar a data do Cliente.
          
            //atulizando pela primeira vez...
            if(isWindows()) ComandoWin(data);
            else if (isUnix()) ComandoLinux(data);
            
            while(i != -1){
            cliente = new Socket("192.168.0.130", 1234); //estabelencendo conexao com o IP do servidor
              //recebendo arquivo do servidor
            in = new ObjectInputStream(cliente.getInputStream());
             data = (Date)in.readObject();  //data armazena a data do servidor
             dataConf = new Date();

          // armazena as datas d1 = servidor e d2 = cliente
             long d1 = data.getTime(); 
             long d2 = dataConf.getTime();

            //realiza a diferenças entre elas
             long dif = CalcularDiferenca(d1, d2);
/*------------------------------------------EXECUTANDO MUDANÇAS -----------------------------------------------*/

           //Confere o sistema operacional e o valor das datas para executar a mudança 
            if(isWindows()){
                if (dif > 60){
            System.out.println("\nExecutando em Windows\n");
            //executando comando para windowns
            ComandoWin(data);
                }
        }

        else if(isUnix()){
           if(dif > 60){
            System.out.println("\nExecutando em Linux ou Unix\n");

            //executando comando para linux
            ComandoLinux(data);
            }
        }
            Thread.sleep( 5 * 1000 ); //aguarda 60 minutos para que o laço se repita novamente
             MsgSair();     
              i = lerSaida.nextInt();
          }
    
}catch(Exception e){
            System.out.println("Erro de cliente"); //mensagem de erro caso ocorra
}
    
}

    }




