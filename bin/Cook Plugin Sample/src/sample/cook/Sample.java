package sample.cook;

import cook.core.FreemarkerWrapper;
import cook.core.IFCook;
import cook.core.ResultProcess;
import cook.util.FileUtil;
import cook.util.PrintUtil;
import java.util.Scanner;

public class Sample implements IFCook {

    //Path out file
    private String PATH_OUT;
    //In param
    private String[] param;

    //Return the version of plugin
    @Override
    public String getVersion() {
        return "0.1";
    }

    //Print header message of plugin start
    @Override
    public void printHeader() {
        PrintUtil.outn("Sample Plugin. Version "+getVersion());
    }

    //Print help invoke
    @Override
    public void printHelp() {
        PrintUtil.outn("Use: cook sample [name]");
    }

    //Start cook plugin. Use thi method for valid in param
    @Override
    public boolean start(String[] param) {
        
        //Valid in param
        if(param.length==1 || param[1].equals("")){
            PrintUtil.outn("Please enter your name");
            printHelp(); //show help
            PrintUtil.outn("");
            return false;
        }
        
        this.param = param;
        
        return true;
        
    }

    //Valid directory for execute the plugin
    @Override
    public boolean validDirectory() {

        boolean saida;
        
        //get the path of user execute script
        String pwd = FileUtil.getPromptPath();

        //Teste if out dir is created
        if (FileUtil.fileExist(pwd + "/out")) {
            PATH_OUT = pwd + "/out";
            saida = true;
            PrintUtil.outn("Plugin generate file in " + PATH_OUT);
        } else {
            //If out dir not created, try create dir
            if (FileUtil.createDir(pwd + "/out")){
                PATH_OUT = pwd + "/out";
                saida = true;
                PrintUtil.outn("Plugin generate file in " + PATH_OUT);
            }else{
                saida = false;
                PrintUtil.outn("Don't create a folder for generate.");
            }

        }

        return saida;
    }

    //Execute plugin
    @Override
    public ResultProcess cook() {
        
        ResultProcess out = new ResultProcess();
        
        try {
            
            
            //Prompt last name
            PrintUtil.outn("");
            String last_name = PrintUtil.inString("Now info last name: ");
            PrintUtil.outn("");
            
            //Add variable for param define in template
            FreemarkerWrapper.getInstance().addVar("name", param[1]+" "+last_name);    
            
            //Call parse for template
            String arq = FreemarkerWrapper.getInstance().parseTemplate("test.ftl");
            
            //Save template out
            PrintUtil.outn("Save file "+PATH_OUT + "/" + param[1] + ".txt");            
            FileUtil.saveToPath(PATH_OUT + "/" + param[1] + ".txt", arq);
            
            //Define out of process
            out.setResultProcess(ResultProcess.SUCESS, "Successfully generated");
            
        } catch (Exception ex) {
            
            PrintUtil.outn(""); 
            PrintUtil.outn("Erro generated!!"); 
            //Define out of process exception
            out.setResultProcess(ResultProcess.ERROR, ex.getMessage());
            
        } finally {
            
            return out;
            
        }
    }

    //End of file cicle
    @Override
    public void end() {
        PrintUtil.outn("");
        PrintUtil.outn("Open file to see the result.");
    }

    
}
