package com.example.ep1cliente;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Cliente {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

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
