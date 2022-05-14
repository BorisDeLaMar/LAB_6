package ru.itmo.client;

import ru.itmo.client.Commands.Add;
import ru.itmo.client.Commands.Exit;
import ru.itmo.client.Commands.Clear;
import ru.itmo.client.Commands.AddIfMin;
import ru.itmo.client.Commands.History;
import ru.itmo.client.Commands.Info;
import ru.itmo.client.Commands.Show;
import ru.itmo.client.Commands.ExecuteScript;
import ru.itmo.client.Commands.FilterStatus;
import ru.itmo.client.Commands.PrintDescending;
import ru.itmo.client.Commands.Help;
import ru.itmo.client.Commands.PrintUniqueStatus;
import ru.itmo.client.Commands.Remove;
import ru.itmo.client.Commands.RemoveLower;
import ru.itmo.client.Commands.Update;
import ru.itmo.common.LAB5.src.Comms.*;
import ru.itmo.common.connection.*;
import ru.itmo.common.LAB5.src.GivenClasses.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        ServerAPI serverAPI = new ServerAPIImpl();

        Long c = (long) 0;
        Worker.bannedID.add(0, c);//как этот массив и на серве и здесь работать будет одновременно?

        try {
            read();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void read() throws IOException{
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);

        ArrayList<Command> cmd = fillLst();

        while(Exit.getExit()) {

            GistStaff.setFlag(false);
            GistStaff.setReply("");
            ru.itmo.common.LAB5.src.Comms.ExecuteScript.file_bdCleaner();

            String[] line = bf.readLine().split(" ");
            String command = line[0];
            int flag = 0;

            for(int i = 0; i < cmd.size(); i++) {
                Command cm = cmd.get(i);
                if(cm.getName().equals(command)) {
                    flag += 1;
                    try {
                        System.out.println(cm.executeCommand(bf));
                    }
                    catch(IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }
                }
            }

            if(flag == 0) {
                System.out.println("Unknown command. Type \"help\" for the list of available commands");
            }
        }
        bf.close();
    }

    private static ArrayList<Command> fillLst(){
        ArrayList<Command> cmd = new ArrayList<Command>();

        Command add = new Add();
        Command add_if_min = new AddIfMin();
        Command info = new Info();
        Command show = new Show();
        Command clear = new Clear();
        Command exit = new Exit();
        Command history = new History();
        Command execute_script = new ExecuteScript();
        Command filter_less_than_status = new FilterStatus();
        Command help = new Help();
        Command print_descending = new PrintDescending();
        Command print_unique_status = new PrintUniqueStatus();
        Command remove = new Remove();
        Command remove_lower = new RemoveLower();
        Command update = new Update();

        cmd.add(add);
        cmd.add(add_if_min);
        cmd.add(info);
        cmd.add(show);
        cmd.add(clear);
        cmd.add(exit);
        cmd.add(history);
        cmd.add(execute_script);
        cmd.add(filter_less_than_status);
        cmd.add(help);
        cmd.add(print_descending);
        cmd.add(print_unique_status);
        cmd.add(remove);
        cmd.add(remove_lower);
        cmd.add(update);

        return cmd;
    }
}