import java.sql.*;

import javax.swing.JOptionPane;

public class mysql {


    static String database = "javadb";
    private static String table;
    private static String condition;

    @SuppressWarnings("static-access")
    public mysql(String table, String condition){
        //metodo construct
        this.table = table;
        this.condition = condition;
    }
    
    static Connection conectar() {
        Connection con;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database, "root", null);
            
        }catch(Exception e){
            con = null;
            JOptionPane.showMessageDialog(null, "Não foi possível conectar com o banco de dados.");
        }

        return con;
    }

    //Recuperando o ultimo id da tabela.
    public static int takeId(){
        int i = 1;
        try {
            Connection conectar = conectar();
            PreparedStatement stmt = conectar.prepareStatement("SELECT * FROM cronometro ORDER BY ID DESC LIMIT 1");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                //conseguiu executar o comando.

                i = rs.getInt(1) + 1;
            } else {
                //não encontrou dados na tabela.

                i = 1;
            }
            conectar.close();
            stmt.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error ao pegar ID: "+e);
        }

        return i;
    }


    //Inserindo dados na tabela.
    public void insertInto(int lastID, String execute){
        try {
            Connection conectar = conectar();

            //Exmplo da query passada:
            //"INSERT INTO cronometro VALUES(null,?,?)";
            String sql = "INSERT INTO "+table+" "+condition;
            PreparedStatement stmt = conectar.prepareStatement(sql);
            stmt.setInt(1, lastID);
            stmt.setString(2,execute);
            stmt.executeUpdate();

            conectar.close();
            stmt.close();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error ao inserir dados: "+e);
        }

    }


    //Selecionando todos os dados da tabela.
    public static String selectAll(String query){
        String result = "";
        try {
            Connection conectar = conectar();

            PreparedStatement stmt = conectar.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            int i = 1;
            while(rs.next()){
                result += i+"° lugar carro: "+rs.getString(1)+"\n       TEMPO TOTAL: "+rs.getString(3)+"\n";
                try {
                    //Realizando outra conexão usando os parametros da conexão anterior.

                    PreparedStatement voltas = conectar.prepareStatement("call buscarcomid("+rs.getString(2)+")");
                    ResultSet resultado = voltas.executeQuery();
                    int y = 1;
                    while(resultado.next()){
                        result += "             "+y+"° volta:"+resultado.getString(2)+"\n";
                        y++;
                    }

                    voltas.close();
                } catch (Exception e) {
                    
                }
                result += "\n\n";
                i++;
            }

            conectar.close();
            stmt.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error ao realizar conexão: "+e);
        }

        return result;
    }


    public static String resultsVoltas(String table, int relativeId) {
        String result = "";
        
        try {
            Connection conectar = conectar();

            String query = "SELECT * FROM " + table + " WHERE relative_id = ?";
            
            PreparedStatement stmt = conectar.prepareStatement(query);
            stmt.setInt(1, relativeId);
            ResultSet rs = stmt.executeQuery();
            int i = 1;
            while (rs.next()) {
                // Imprima todos os valores das colunas para cada linha
                result += i+"° volta: "+rs.getString(3)+"\n";
                i++;
            }

            conectar.close();
            stmt.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error ao armazenar volta: "+e);
        }

        return result;
    }


    public static void volta(int lastID, String execute){
        try {
            //inserindo os valores;
            Connection conectar = conectar();

            //Exmplo da query passada:
            String sql = "INSERT INTO volta VALUES(null,?,?)";
            PreparedStatement stmt = conectar.prepareStatement(sql);
            stmt.setInt(1, lastID);
            stmt.setString(2, execute);
            stmt.executeUpdate();

            conectar.close();
            stmt.close();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error ao inserir dados: "+e);
        }
    }



}


