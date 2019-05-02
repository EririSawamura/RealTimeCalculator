class Save {
    private String[] save;
    private final int maxnum = 5;
    private int counter;
    Save(){
        save = new String[maxnum];
        counter=0;
    }
    void insert(String input){
        if(input.equals(" = "))
            return;
        if (counter >= 0)
            System.arraycopy(save, 0, save, 1, counter);
        save[0]=input;
        counter=counter++<maxnum-1?counter:maxnum-1;
    }
    String get(int i){
        return (0<=i && i<maxnum)? save[i]: null;
    }
    int len(){
        return maxnum;
    }
}