package src.Comms;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;

import com.google.gson.reflect.TypeToken;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;
import src.Exceptions.LimitException;
import src.Exceptions.NullException;
import src.GivenClasses.*;

import java.io.*;
import java.util.ArrayList;

public class Update implements Commands{
	/** 
	 *Updates element by id and given args
	 *@author BARIS 
	*/
	public String just_update(ArrayList<String> args, DAO<Worker> dao){
		long id = -1;
		try {
			id = Long.parseLong(args.get(8));
		}
		catch(NumberFormatException e){
			return "Id should be type long for " + getName() + " func";
		}
		Worker w = dao.get(id);
		if(w == null){
			return "There's no worker with such id";
		}
		try {
			try {
				String name = args.get(0);
				w.setName(name);
			} catch (NullException e) {
				return "Name field for " + getName() + " func cannot be null";
			}
			try {
				long salo = Long.parseLong(args.get(1));
				w.setSalary(salo);
			} catch (LimitException | IllegalArgumentException e) {
				return "Salary field for " + getName() + " func should be type long and cannot be negative or zero";
			}
			try {
				Position pos = Position.valueOf(args.get(2));
				w.setPosition(pos);
			} catch (IllegalArgumentException e) {
				return "Available arguments for position filed for " + getName() + " function are:\n" + Position.strConvert();
			}
			try {
				Status state = Status.valueOf(args.get(3));
				w.setStatus(state);
			} catch (IllegalArgumentException | NullException e) {
				return "Available arguments for status filed for " + getName() + " function are:\n" + Status.strConvert();
			}
			try {
				OrganizationType orgType = OrganizationType.valueOf(args.get(5));
				Organization org = new Organization(args.get(4), orgType);
				w.setOrganization(org);
			} catch (IllegalArgumentException e) {
				return "Available arguments for organizationType filed for " + getName() + " function are:\n" + OrganizationType.strConvert();
			}
			try {
				long x = Long.parseLong(args.get(6));
				double y = Double.parseDouble(args.get(7));
				Coordinates cords = new Coordinates(x, y);
				w.setCoordinates(cords);
			} catch (IllegalArgumentException e) {
				return "'x' should be type long, 'y' should be type double for coordinates field in function " + getName();
			} catch (LimitException e) {
				return e.getMessage() + " for function " + getName();
			}
		}
		catch(IndexOutOfBoundsException e){
			return "There's lack of arguments for function " + getName();
		}

		return "Worker " + w.getName() + " was successfully added with all fields";
	}
	public void update_by_id(DAO<Worker> dao, BufferedReader on) throws IOException{
		Add dd = new Add();
		try {
			long id = Long.valueOf(on.readLine().split(" ")[0]);
			Worker w = dao.get(id);
			if(w == null) {
				String reply = GistStaff.getReply();
				reply += "\nThere's no guy with such id\n";
				GistStaff.setReply(reply);
			}
			else {
				dd.add_read_exec(w, on);
				String reply = GistStaff.getReply();
				reply += "\nWorker with id " + w.getId() + " was successfully updated\n";
				GistStaff.setReply(reply);
			}
		}
		catch(IllegalArgumentException e) {
			String reply = GistStaff.getReply();
			reply += "\nId should be type long for update func\n";
			GistStaff.setReply(reply);
		}
	}
	
	@Override 
	public String getGist() {
		return "updates element with new arguments";
	}
	@Override
	public String getName() {
		return "update";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		Update upd = new Update();
		q = History.cut(q);
		q.addLast(upd);

		upd.update_by_id(dao, on);

		return q;
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException{
		Update upd = new Update();
		q = History.cut(q);
		q.addLast(upd);

		TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
		String update = upd.just_update((ArrayList<String>) request.getArgumentAs(typeToken), dao);
		Response response = new Response(
				Response.cmdStatus.OK,
				update
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
