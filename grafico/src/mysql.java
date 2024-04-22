import java.sql.*;

import javax.swing.JOptionPane;

public class mysql {


    static String database = "javadb";
    private static String table;
    private static String condition;
    private static String execute;

    @SuppressWarnings("static-access")
    public mysql(String table, String condition , String execute){
        //metodo construct
        this.table = table;
        this.condition = condition;
        this.execute = execute;
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

    public int takeId(){
        int i = 1;
        try {
            Connection conectar = conectar();
            PreparedStatement stmt = conectar.prepareStatement("SELECT * FROM cronometro ORDER BY ID DESC LIMIT 1");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                i = rs.getInt(1) + 1;
                //System.out.println("Último ID inserido: " + i);
            } else {
                //tem que ter algum dado(ID) primario na tabela
                i = 1;
            }
            conectar.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Error ao pegar ID: "+e);
        }

        return i;
    }


    public void insertInto(int lastID){
        try {
            //inserindo os valores;
            Connection conectar = conectar();

            //condition = 
            //"INSERT INTO cronometro (relative_id, time) VALUES(?,?)";
            String sql = "INSERT INTO "+table+" "+condition;
            PreparedStatement stmt = conectar.prepareStatement(sql);
            stmt.setInt(1, lastID);
            stmt.setString(2,execute);
            stmt.executeUpdate();

            conectar.close();
            stmt.close();
            
        }catch(Exception e){
            System.out.println("Error ao inserir dados: "+e);
        }

    }


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
            System.out.println(e);
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
            //System.out.println(e);
        }

        return result;
    }


    public static void volta(int lastID, String timeFormatted){
        try {
            //inserindo os valores;
            Connection conectar = conectar();

            //condition = 
            //"INSERT INTO cronometro (relative_id, time) VALUES(?,?)";
            String sql = "INSERT INTO volta (relative_id, time) VALUES(?,?)";
            PreparedStatement stmt = conectar.prepareStatement(sql);
            stmt.setInt(1, lastID);
            stmt.setString(2, timeFormatted);
            stmt.executeUpdate();

            conectar.close();
            stmt.close();
            
        }catch(Exception e){
            System.out.println("Error ao inserir dados: "+e);
        }
    }



}


