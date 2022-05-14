package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.LAB5.src.Comms.*;
import ru.itmo.common.LAB5.src.Exceptions.LimitException;
import ru.itmo.common.LAB5.src.GivenClasses.*;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class AddIfMin implements Command {
    public Worker add_if_min(BufferedReader bf) throws IOException {
        Worker w = new Worker();
        Add add = new Add();
        add.add_read(w, bf);
        w.setCreationDate();
        try {
            w.setID(Worker.findPossibleID());
            return w;
        }
        catch(LimitException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String executeCommand(BufferedReader bf) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Worker w = add_if_min(bf);
        Response response = serverAPI.add_if_min(w);

        if(response == null){
            return "Response for add_if_min is null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Add_if_min command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "add_if_min";
    }
}
