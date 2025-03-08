package View.Command;

import Controller.Controller;

public class RunExCommand extends Command{
    private Controller ctrl;
    private boolean used;

    public RunExCommand(String key, String description, Controller ctrl) {
        super(key, description);
        this.ctrl = ctrl;
        this.used = false;
    }

    @Override
    public void execute(){
        if(this.used){
            System.out.println("Program already used!");
            return;
        }
        try{
            ctrl.allStep();
            this.used = true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



}
