package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.LAB5.src.GivenClasses.Worker;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class Update implements Command{
    public Response update(BufferedReader on) throws IOException{
        System.out.print("Enter id: ");
        Worker w = new Worker();
        try {
            long id = Long.parseLong(on.readLine().split(" ")[0]);
            w.setTemporaryID(id);
            Add add = new Add();
            add.add_read(w, on);

            ServerAPI serverAPI = new ServerAPIImpl();
            return serverAPI.update(w);
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String executeCommand(BufferedReader bf) throws IOException{
        Response response = update(bf);

        if(response == null){
            return "Update command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Update command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "update";
    }
}
