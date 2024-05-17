import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.*;

public class Arduino{
    private boolean resposta;

    public Arduino(){

        new Thread(() -> {
        
        SerialPort comPort = SerialPort.getCommPort("COM5");  //abrindo porta COM

        try {
            comPort.openPort();
            comPort.setBaudRate(9600);  //frequencia da porta serial
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0); //tempo para o java carregar primeiro

            StringBuilder message = new StringBuilder(); //criar uma String manipulavel

            while (true) {
                if (comPort.getInputStream().available() > 0) { //verificar se tem dados do arduino para serem passados

                    int data = comPort.getInputStream().read(); //lê o byte passado pela porta serial
                    char ch = (char) data;  //converter de bytes para caracter

                    message.append(ch);  //adiciona cada caracter para um unica String


                    if (ch == '\n') { //Verifica se o caracter é uma quebra de linha ou seja a mensagem esta completa.
                        String completeMessage = message.toString().trim();  //transforma a StrinBuilder em uma String normal e tira a quebra de linha ("\n");
                        
                        this.resposta = processLine(completeMessage);  //chamando a função para resposta em console
                        message.setLength(0);  //resetando a mensagem
                    }
                }
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Não foi possível encontrar o Arduino");
        } finally {
            if (comPort.isOpen()) {
                comPort.closePort();
            }
            
        }
        }).start();
    }

    public boolean getResposta(){
        return this.resposta;
    }

    static boolean processLine(String line) {
        boolean respostaInterna = false;

        if (line.equals("Ativado")) {
            //Alguma coisa detectado (TRUE)
            respostaInterna = true;
            
        } else if (line.equals("Desativado")) {
            //nada detectado (FALSE)
            respostaInterna = false;
        }

        return respostaInterna;
    }
}