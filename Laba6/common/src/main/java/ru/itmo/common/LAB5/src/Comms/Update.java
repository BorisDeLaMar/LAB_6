package ru.itmo.common.LAB5.src.Comms;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import ru.itmo.common.LAB5.src.Exceptions.*;
import ru.itmo.common.LAB5.src.GivenClasses.*;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;

import java.io.*;

public class Update implements Commands{
	/** 
	 *Updates element by id and given args
	 *@author BARIS 
	*/
	public String just_update(Worker w, DAO<Worker> dao){
		Worker e = dao.get(w.getId());
		if(e == null){
			return "There's no worker with such id";
		}
		else{
			try {
				e.setName(w.getName());
				e.setSalary(w.getSalary());
				e.setPosition(w.getPosition());
				e.setStatus(w.getStatus());
				e.setOrganization(w.getOrganization());
				e.setCoordinates(w.getCoordinates());
				return "Worker was successfully updated";
			}
			catch(NullException err){
				return err.getMessage() + " for server func update";
			}
			catch(LimitException err){
				return err.getMessage() + " for server func update";
			}
		}
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

		String update = upd.just_update(request.getArgumentAs(Worker.class), dao);
		Response response = new Response(
				Response.cmdStatus.OK,
				update
		);
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
