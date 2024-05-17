import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

public class contadorController {

    private int ms,s,min,hora,total,voltaMarcar,trueTimes; //setando todos inteiros como zero
    private boolean contadorAtivado = true; //melhor controle do contador
    private int lastID = 0; //recupar ultimo ID

    @FXML
    private Button allRestults; //btn para mostrar todos resultados recuperando pelo BD

    @FXML
    private Button btnMarcarVolta;

    @FXML
    private Label cronometro;

    @FXML
    private Label resultado;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    //função para formatar string
    public static String formatarTempo(int hora, int min, int s, int ms){
        String formatarTempo = String.format("%02d:%02d:%02d:%03d",hora,min,s,ms);
        return formatarTempo;
    }

    //recuperando ultimo ID e passando para variavel lastID.
    public contadorController(){
        this.lastID = mysql.takeId();
    }


    @FXML
    public void initialize(){ //metodo chamada assim que a janela do javaFX aparece
        stop.setDisable(true);
        btnMarcarVolta.setDisable(true);
        
        new Thread(){
            public void run(){
 
                Arduino arduino = new Arduino();
                Lock lock = new ReentrantLock();

                boolean controle = false;
                while(true){
                    lock.lock();

                    try{
                        boolean resposta = arduino.getResposta();
                        if(controle != resposta){
                            if(resposta){
                                //retorna TRUE do sensor

                                trueTimes++;
                                if(trueTimes == 1){
                                    Platform.runLater(()->{
                                        inicioContador(null);
                                    });
                                }else if(trueTimes == 2){
                                    Platform.runLater(()->{
                                        marcarVolta(null);
                                    });
                                    
                                }else if(trueTimes == 3){
                                    Platform.runLater(()->{
                                        pararContador(null);
                                    });                                    
                                }
                            }else{
                                //retorna FALSE do sensor
                                
                            }
                            controle = resposta;
                        }
                        
                    }finally{
                        lock.unlock();
                    }

                }

            }
            
        }.start();
    }

    @FXML
    void inicioContador(ActionEvent event) {

        if(!contadorAtivado){
            contadorAtivado = true;
        }

        if(trueTimes != 1){
            trueTimes = 1;
        }

        resultado.setText("");

        btnMarcarVolta.setDisable(false);
        start.setDisable(true); //desativar o botao para não duplicar eventos.

        new Thread(){ //cria Thread para manipular o Delay da execução.

            public void run(){

                while (contadorAtivado) {
                    try {
                        Thread.sleep(1); //1ms para cada execução.
                    } catch (Exception e) {
                        
                    }

                    //comando para Executar na Interface do Usuário.
                    Platform.runLater(() -> {
                        ms++;
                        total++; //tempo em ms do INCIO -> FIM.

                        String formatarTempo = formatarTempo(hora, min, s, ms);
                        cronometro.setText(formatarTempo);

                        if(ms == 1000){
                            s++;
                            ms = 0;
                        }

                        if(s == 60){
                            ms = 0;
                            s = 0;
                            min++;
                        }

                        if(min == 60){
                            ms = 0;
                            s = 0;
                            min = 0;
                            hora++;
                        }

                    });
                }
            }
        }.start();

    }

    @FXML
    void marcarVolta(ActionEvent event) {
        if(trueTimes != 2){
            trueTimes = 2;
        }

        voltaMarcar++;
        stop.setDisable(false);
        btnMarcarVolta.setDisable(true);

        //marcando a volta.
        String formatarTempo = formatarTempo(hora, min, s, ms);

        resultado.setText(voltaMarcar+"° volta: "+formatarTempo);

        mysql.volta(lastID, formatarTempo);

        ms = 0;
        s = 0;
        min = 0;
        hora = 0;
    }

    @FXML
    void pararContador(ActionEvent event) {
        if(contadorAtivado){
            contadorAtivado = false;
        }

        //Calculando o tempo toltal somente com MS.
        String tempoTotal = formatarTempo(total/3600000, (total%3600000)/60000, (total%60000)/1000, total%1000);

        //Adicionando o tempo total no Banco de Dados.
        mysql sql = new mysql("cronometro","VALUES(null,?,?)");
        sql.insertInto(lastID,tempoTotal);

        //Adicionando a ultima volta no Banco de Dados.
        String ultimaVolta = formatarTempo(hora, min, s, ms);
        mysql.volta(lastID,ultimaVolta);

        //Recuperando todas as voltas da Sessão Atual.
        String voltas = mysql.resultsVoltas("volta", lastID);

        resultado.setText(voltas+"Total: "+tempoTotal);

        start.setDisable(false);

        Platform.runLater(() -> {
            cronometro.setText("00:00:00:000");
        });

        ms = 0;
        s = 0;
        min = 0;
        hora = 0;
        total = 0;
        voltaMarcar = 0;
        trueTimes = 0;

        lastID++;

        stop.setDisable(true);
        btnMarcarVolta.setDisable(true);
    }


    @FXML
    void showAllResults(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("allResults.fxml"));
            Parent root1 = fxmlLoader.load();
    
            Stage stage = new Stage();
            stage.setTitle("Todos os resultados");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível abrir a janela");
        }
    }

}
