import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class allResultsController {

    @FXML
    private Label showAllResults;

    @FXML
    public void initialize(){
        String res = mysql.selectAll("SELECT * FROM `cronometro` ORDER BY `time` LIMIT 5");
        showAllResults.setText(res);
    }

}
