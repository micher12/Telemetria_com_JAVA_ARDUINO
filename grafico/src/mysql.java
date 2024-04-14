import java.sql.*;

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
    
    public int takeId(){
        int i = 1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database, "root", null);
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

        } catch (Exception e) {
            System.out.println("Error ao pegar ID: "+e);
        }

        return i;
    }


    public void insertInto(int lastID){
        try {
            //inserindo os valores;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database , "root", null);

            //condition = 
            //"INSERT INTO cronometro (relative_id, time) VALUES(?,?)";
            String sql = "INSERT INTO "+table+" "+condition;
            PreparedStatement pstmt = conectar.prepareStatement(sql);
            pstmt.setInt(1, lastID);
            pstmt.setString(2,execute);
            pstmt.executeUpdate();
            conectar.close();
            
        }catch(Exception e){
            System.out.println("Error ao inserir dados: "+e);
        }

    }


    public static String selectAll(String query){
        String result = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database, "root", null);
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
                    
                } catch (Exception e) {
                    
                }
                result += "\n\n";
                i++;
            }
            conectar.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }


    public static String resultsVoltas(String table, int relativeId) {
        String result = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", null);

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
            

        } catch (Exception e) {
            //System.out.println(e);
        }

        return result;
    }


    public static void volta(int lastID, String timeFormatted){
        try {
            //inserindo os valores;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database , "root", null);

            //condition = 
            //"INSERT INTO cronometro (relative_id, time) VALUES(?,?)";
            String sql = "INSERT INTO volta (relative_id, time) VALUES(?,?)";
            PreparedStatement pstmt = conectar.prepareStatement(sql);
            pstmt.setInt(1, lastID);
            pstmt.setString(2, timeFormatted);
            pstmt.executeUpdate();
            conectar.close();
            
        }catch(Exception e){
            System.out.println("Error ao inserir dados: "+e);
        }
    }



}


