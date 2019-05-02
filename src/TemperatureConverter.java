import java.awt.List;
public class TemperatureConverter extends Calculator{
	private List choiceList = new List(2, false);
	TemperatureConverter() {
        super();
        setLblInput("C");
        setLblOutput("F");
		choiceList.add("C to F");
		choiceList.add("F to C");
		choiceList.addItemListener(e -> {
                if(choiceList.getSelectedIndex() == 0) {
                    clearText();
                    setLblInput("C");
                    setLblOutput("F");
                }else {
                    clearText();
                    setLblInput("F");
                    setLblOutput("C");
                }
            }
        );
		add(choiceList);
	}
	@Override
    protected String eval(String expr){
	    return expr.matches("[0-9]+([,.][0-9]{1,2})?")?choiceList.getSelectedIndex() == 0?String.valueOf((9.0/5.0)*Double.parseDouble(expr) + 32):String.valueOf((5.0/9.0) * (Double.parseDouble(expr) - 32)):"Invalid Expression";
    }
}