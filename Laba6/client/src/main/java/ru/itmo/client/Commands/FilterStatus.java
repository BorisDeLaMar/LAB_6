package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.LAB5.src.Comms.GistStaff;
import ru.itmo.common.LAB5.src.GivenClasses.Status;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class FilterStatus implements Command {
    public Status filter_less_than_status(BufferedReader bf) throws IOException{
        Status state = null;
        while(true) {
            System.out.print("Enter status: ");
            try {
                state = Status.valueOf(bf.readLine());
                break;
            }
            catch(IllegalArgumentException e) {
                System.out.println("Available status values:" + Status.strConvert());
            }
        }
        return state;
    }

    @Override
    public String executeCommand(BufferedReader bf) throws IOException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Status state = filter_less_than_status(bf);
        Response response = serverAPI.filter_less_than_status(state);

        if(response == null){
            return "Filter_less_than_status command returned null";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Filter_less_than_status command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "filter_less_than_status";
    }
}
