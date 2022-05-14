package ru.itmo.client.Commands;

import ru.itmo.client.Command;
import ru.itmo.client.ServerAPI;
import ru.itmo.client.ServerAPIImpl;
import ru.itmo.common.LAB5.src.Exceptions.LimitException;
import ru.itmo.common.LAB5.src.Exceptions.NullException;
import ru.itmo.common.LAB5.src.GivenClasses.*;
import ru.itmo.common.connection.Response;

import java.io.BufferedReader;
import java.io.IOException;

public class Add implements Command {

    public Worker add(BufferedReader on) throws IOException {
        Worker w = new Worker();
        add_read(w, on);
        w.setCreationDate();
        try {
            w.setID(Worker.findPossibleID());
            return w;
        }
        catch(LimitException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void add_read(Worker w, BufferedReader on) throws IOException{
        int i = 0;
        while(i < 8) {
            if (i == 0) {
                try {
                    System.out.print("Enter name: ");
                    String name = on.readLine().split(" ")[0];
                    i = 1;
                    w.setName(name);
                }
                catch(NullException e) {
                    i = 0;
                    System.out.println(e.getMessage());
                }
            }
            if(i == 1) {
                try {
                    System.out.print("Enter salary: ");
                    String salo = on.readLine().split(" ")[0];
                    long salary = Long.parseLong(salo);
                    i = 2;
                    w.setSalary(salary);
                }
                catch(NumberFormatException e) {
                    i = 1;
                    System.out.println("Salary should be just a number");
                }
                catch(LimitException e) {
                    i = 1;
                    System.out.println(e.getMessage());
                }
            }
            if(i == 2) {
                try {
                    System.out.print("Enter position: ");
                    String posit = on.readLine().split(" ")[0];
                    i = 3;
                    Position pos =  Position.valueOf(posit);
                    w.setPosition(pos);
                }
                catch(IllegalArgumentException e) {
                    i = 2;
                    System.out.println("Available values for position are: " + Position.strConvert());
                }
            }
            if(i == 3) {
                try {
                    System.out.print("Enter status: ");
                    String stata = on.readLine().split(" ")[0];
                    //scam.nextLine();
                    i = 4;
                    Status state = Status.valueOf(stata);
                    w.setStatus(state);
                }
                catch(IllegalArgumentException e) {
                    i = 3;
                    System.out.println("Available values for status are: " + Status.strConvert());
                }
                catch(NullException e) {
                    i = 3;
                    System.out.println(e.getMessage());
                }
            }
            if(i == 4) {
                try {
                    System.out.println("Enter organization: ");
                    String[] arg = on.readLine().split(" ");
                    i = 6;
                    Organization org = new Organization(arg[0], arg[1]);
                    if(!Organization.getFlag()) {
                        i = 4;
                    }
                    Organization.setFlag(true);
                    w.setOrganization(org);
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    i = 4;
                    System.out.println("There should be two args in organization field: name and type\nAvailbale organizationtypes: " + OrganizationType.strConvert());
                }
            }
            if(i == 6) {
                try {
                    System.out.print("Enter coordinates: ");
                    String[] arg = on.readLine().split(" ");
                    i = 8;
                    Coordinates cords = new Coordinates(arg[0], arg[1]);
                    if(!Coordinates.getFlag()) {
                        i = 6;
                    }
                    Coordinates.setFlag();
                    try {
                        w.setCoordinates(cords);
                    }
                    catch(LimitException e) {
                        i = 6;
                        System.out.println(e.getMessage());
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    i = 6;
                    System.out.println("There should be two args in coordinates field: x and y");
                }
            }
        }
    }

    @Override
    public String executeCommand(BufferedReader bf) throws IOException, NullPointerException{
        ServerAPI serverAPI = new ServerAPIImpl();
        Worker w = add(bf);
        Response response = serverAPI.add(w);
        if(response == null){
            return "So that worker wasn't added";
        }
        else {
            if(response.status == Response.cmdStatus.OK){
                return response.getArgumentAs(String.class);
            }
            else{
                return "Add command returned status 'ERROR': " + response.getArgumentAs(String.class);
            }
        }
    }
    @Override
    public String getName(){
        return "add";
    }
}
