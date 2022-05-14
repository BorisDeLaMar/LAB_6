package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class PrintDescending implements Command {

    @Override
    public String executeCommand(BufferedReader bf) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.print_descending();

        if(response == null){
            return "Print_descending command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Print_descending command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "print_descending";
    }
}
