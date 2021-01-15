
package socket;
/*


IMPORTAÇÕES


*/
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author luiza
 */


public class ServidorMulti extends Thread {
        private static ServerSocket servidor;
		private static Socket cliente;

    public ServidorMulti(Socket cliente) throws IOException {
            this.cliente = cliente;
        }

        public void run(){

            //enviando dados para o cliente
            
            try {
            	//captura o objeto
           ObjectOutputStream out = new ObjectOutputStream(this.cliente.getOutputStream());
           		//enviando para o cliente 
                out.flush();
                Date data = new Date();
                out.writeObject(data);
                out.close();
                cliente.close(); //fechando cliente
            
            } catch (IOException e) {
		              System.out.println("erro em run");	e.printStackTrace(); //tratamento de erro
		}


        }

    public static void main(String args[]){

        try{
            //ouvindo porta 1234

            servidor = new ServerSocket(1234);
             }catch (Exception e){
            System.out.println("Erro de servidor!\n" +e.getMessage());
        } 
                System.out.println("Esperando conexao... ");

		try{
           
                while(true){
         //accept bloqueia a conexao ate que o servidor receba um pedido de conexao
                cliente = servidor.accept();

                //cria uma thread para tratar a conexão
                ServidorMulti ss = new ServidorMulti(cliente);
                 // Inicia a thread para o cliente conectado
                new Thread(ss).start();
              
            	
                }
               
            
            }catch (Exception e){
            System.out.println("Erro de servidor!\n" +e.getMessage()); //tratamento de erro
        } 
    }
}
