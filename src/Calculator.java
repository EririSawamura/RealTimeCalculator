import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.math.*;
import java.util.*;
public class Calculator extends Panel implements ActionListener{ //AWT GUI program
    private Label lblInput, lblOutput;     // Declare input,output Label
    private TextField tfInput, tfOutput;  // Declare input,output TextField
    private TextArea save;   //Declare save area
    private Save save_store;
    private History his_store;
    Calculator() { // Constructor to setup the GUI components and event handlers
        setLayout(new FlowLayout());        
        this.lblInput = new Label("Enter an Expression");
        add(this.lblInput);
        tfInput = new TextField(50);
        add(tfInput);
        tfInput.addActionListener(this);
        this.lblOutput = new Label("Output");
        add(this.lblOutput);
        tfOutput = new TextField(50);
        add(tfOutput);
        Button reset = new Button("Reset");  // Construct the Button
        add(reset);
        reset.addActionListener(this);
        Button enter = new Button("Enter");
        add(enter);
        enter.addActionListener(this);
        save = new TextArea("",5,50, TextArea.SCROLLBARS_BOTH);
        save.setEditable(false);
        add(save);
        KeyListener key_Listener = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    tfInput.setText(his_store.pop());
                else
                    his_store.push(tfInput.getText());
                String expr = tfInput.getText();
                if (validate(expr))
                    tfOutput.setText(eval(expr));
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    addSave("Input:" + expr + "; Output:" + tfOutput.getText());
            }
            public void keyPressed(KeyEvent e) {
            }
        };
        tfInput.addKeyListener(key_Listener);
        save_store = new Save();
        his_store = new History();
    }
    private void addSave(String input){
        tfInput.selectAll();
        save_store.insert(input);
        save.setText("");
        for(int i=0;i<save_store.len();i++)
            save.append(save_store.get(i)==null?"":save_store.get(i)+"\n");
    }
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Reset")){
            clearText();
        }else {
            String expr = tfInput.getText();
            if(validate(expr)){
                tfOutput.setText(eval(expr));
            }
            else {
                tfOutput.setText("Invalid expression");
            }
        }
    }
    void clearText() {
        tfInput.setText("");
        tfOutput.setText("");
    }
    void setLblInput(String newLbl) {
    	lblInput.setText(newLbl);
    }
    void setLblOutput(String newLbl) {
    	lblOutput.setText(newLbl);
    }
    private boolean validate(String expr){
        expr = expr.replaceAll("\\s+", "");
        int checkBrackets = 0;
        for(int i=0; i<expr.length(); i++){
            if (((expr.charAt(i)<'0' || expr.charAt(i)>'9') && "+-*/()%.".indexOf(expr.charAt(i)) == -1) || checkBrackets < 0)
                return false;
            checkBrackets += expr.charAt(i) != '('? expr.charAt(i) != ')' ? 0: -1: 1;
        }
        return true;
    }
    protected String eval(String expr){
        expr = expr.replaceAll("\\s+", "");
        Stack<String> operatorStack = new Stack<>();
        Stack<String> operandStack = new Stack<>();
        StringTokenizer tokens = new StringTokenizer(expr, "{}()*/+-%", true);
        StringBuilder tempExpr = new StringBuilder();
        boolean negative = false;
        while(tokens.hasMoreTokens()){
            String tkn = tokens.nextToken();
            if(tkn.matches("[0-9]\\d*\\.?\\d*")){
                if (negative){
                    if (tkn.charAt(0) == '0'){
                        operatorStack.add("-");
                        operandStack.add(tkn);
                    }else{
                        operandStack.push("-"+tkn);
                    }
                    negative = false;
                }else {
                    operandStack.push(tkn);
                }
            }
            else if (tkn.equals("(")){
                operatorStack.push(tkn);
            }
            else if (tkn.equals("*") || tkn.equals("/") || tkn.equals("+") || tkn.equals("-") || tkn.equals("%")){
                if (tkn.equals("-") && (tempExpr.length()==0 || !(tempExpr.charAt(tempExpr.length()-1)>='0' && tempExpr.charAt(tempExpr.length()-1)<='9'))){
                    negative = true;
                } else {
                    while (!operatorStack.isEmpty() && operandStack.size() >= 2 && prec(operatorStack.peek()) >= prec(tkn) ){
                        String opResult = operate(operatorStack, operandStack);
                        if(!opResult.equals("ok"))
                            return opResult;
                    }
                    operatorStack.push(tkn);
                }
            } else if(tkn.equals(")")){
                while (!operatorStack.isEmpty() && operandStack.size() >= 2 && !operatorStack.peek().equals("(") ){
                    String opResult = operate(operatorStack, operandStack);
                    if(!opResult.equals("ok"))
                        return opResult;
                }
                operatorStack.pop();
            }
            tempExpr.append(tkn);
        }
        while (!operatorStack.isEmpty() && operandStack.size() >= 2){
            String opResult = operate(operatorStack, operandStack);
            if(!opResult.equals("ok"))
                return opResult;
        }
        if (!operatorStack.isEmpty())
            return "Invalid expression: Too many operations";
        if (operandStack.isEmpty())
            return "";
        return operandStack.peek();
    }
    private String operate(Stack<String> operatorStack, Stack<String> operandStack){
        String tempOperator = operatorStack.pop();
        BigDecimal operand2 = new BigDecimal(operandStack.pop());
        BigDecimal operand1 = new BigDecimal(operandStack.pop());
        if (tempOperator.equals("+"))
            operandStack.push(String.valueOf(operand1.add(operand2)));
        if (tempOperator.equals("-"))
            operandStack.push(String.valueOf(operand1.subtract(operand2)));
        if (tempOperator.equals("*"))
            operandStack.push(String.valueOf(operand1.multiply(operand2)));
        if (tempOperator.equals("/")) {
            if (operand2.compareTo(BigDecimal.ZERO) == 0){
                return "Invalid expression: Divide by zero";
            } else {
                operandStack.push(String.valueOf(operand1.divide(operand2 ,2, RoundingMode.HALF_EVEN)));
            }
        }
        if (tempOperator.equals("%")){
            if (operand2.compareTo(BigDecimal.ZERO) == 0){
                return "Invalid expression: Mod by zero";
            }
            else {
                operandStack.push(String.valueOf(operand1.remainder(operand2)));
            }
        }
        return "ok";
    }
    private int prec(String op){
        if (op.equals("*") || op.equals("/") || op.equals("%"))
            return 2;
        else  if (op.equals("+") || op.equals("-"))
            return 1;
        else  if (op.equals("(") || op.equals(")"))
            return 0;
        return 3;
    }
}