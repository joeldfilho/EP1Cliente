package com.example.ep1cliente;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Cliente {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    /*
    * Aqui é gerado o objeto cliente. Cada vez que gerar um novo será criado um novo socket, permitindo a utilizaç~~ao de vários usuários ao mesmo tempo
    * */
    public Cliente(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e ){
            System.out.println("erro criando cliente");
            e.printStackTrace();
        }

    }

    /*
    * A mensagem só é enviada quando o bufferedWriter faz o flush()
    * */
    public void enviarMensagemServidor(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            System.out.println("Erro enviando mensagem ao cliente");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    * A funcao de receber mensagens do servidor roda em uma thread própria pois ela atualiza dinamicamente a tela com a mensagem. Para que essa atualização seja feita o JavaFX precisa de uma nova thread
    * */
    public void receberMensagemServidor(VBox vboxMessages) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try{
                        String mensagemDoCliente = bufferedReader.readLine();
                        Controller.addLabel(mensagemDoCliente, vboxMessages);
                    } catch (IOException e){
                        System.out.println("Erro recebendo mensagem do cliente");
                        e.printStackTrace();
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }
}
