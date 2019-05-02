import java.util.Stack;
class History {
    private Stack<String> save;
    History(){
        save = new Stack<String>();
    }
    void push(String input){
        save.push(input);
    }
    String pop(){
        return save.empty()?"":save.pop();
    }
}