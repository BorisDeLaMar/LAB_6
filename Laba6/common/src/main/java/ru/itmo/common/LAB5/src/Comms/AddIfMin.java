package ru.itmo.common.LAB5.src.Comms;
import com.google.gson.JsonSyntaxException;
import ru.itmo.common.LAB5.src.Exceptions.*;
import java.io.*;
//import Exceptions.NullException;
import ru.itmo.common.LAB5.src.GivenClasses.*;
import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;

public class AddIfMin implements Commands{
	/** 
	 *Adds element if it's minimum of all the elements
	 *@author BARIS  
	*/
	public Response just_add_if_min(DAO<Worker> dao, Worker w){
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean flag = true;
		for(Worker e : bd) {
			if(e.hashCode() <= w.hashCode()) {
				flag = false;
			}
		}
		if(flag) {
			dao.appendToList(w);
			return new Response(
					Response.cmdStatus.OK,
					"Worker successfully added"
			);
		}
		else{
			return new Response(
					Response.cmdStatus.OK,
					"Worker is not min, so that he was not added"
			);
		}
	}
	public void add_if_min(DAO<Worker> dao, BufferedReader on) throws LimitException, IOException{
		Worker w = new Worker();
		Add dd = new Add();
		dd.add_read(w, on);
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean f = true;
		for(Worker e : bd) {
			if(e.hashCode() < w.hashCode()) {
				f = false;
			}
		}
		if(f) {
			w.setCreationDate();
			w.setID(Worker.findPossibleID());
			dao.appendToList(w);
			System.out.println("Worker successfully added");
		}
		else {
			System.out.println("Worker is not min, so he wasn't added");
		}
	}
	public void add_if_min_exec(DAO<Worker> dao, BufferedReader on) throws LimitException, IOException{
		Worker w = new Worker();
		Add dd = new Add();
		dd.add_read_exec(w, on);
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		boolean f = true;
		for(Worker e : bd) {
			if(e.hashCode() < w.hashCode()) {
				f = false;
			}
		}
		if(f) {
			w.setCreationDate();
			String reply = GistStaff.getReply();
			reply += "\nWorker from file was successfully added\n";
			GistStaff.setReply(reply);
			w.setID(Worker.findPossibleID());
			dao.appendToList(w);
		}
		else{
			String reply = GistStaff.getReply();
			reply += "\nWorker from file wasn't min, so he wasn't added\n";
			GistStaff.setReply(reply);
		}
	}
	@Override
	public String getGist() {
		return "adds element to collection, if it's the lowest one";
	}
	@Override
	public String getName() {
		return "add_if_min";
	}
	@Override
	public ArrayDeque<Commands> executeCommand(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on) throws IOException{
		AddIfMin aim = new AddIfMin();
		q = History.cut(q);
		q.addLast(aim);
		try {
			if(GistStaff.getFlag()) {
				aim.add_if_min_exec(dao, on);
			}
			else {
				aim.add_if_min(dao, on);
			}
		}
		catch(LimitException e) {
			System.out.println(e.getMessage());
		}
		return q; //Проверить history. Если не робит, попробуй в try возвращать q, а здесь null
	}
	@Override
	public ArrayDeque<Commands> requestExecute(DAO<Worker> dao, ArrayDeque<Commands> q, BufferedReader on, Request request, SocketChannel client) throws IOException {
		q = History.cut(q);
		q.addLast(this);
		Response response;
		try{
			this.just_add_if_min(dao, request.getArgumentAs(Worker.class));
			response = new Response(
					Response.cmdStatus.OK,
					"Worker successfully added"
			);
		}
		catch(JsonSyntaxException e){
			response = new Response(
					Response.cmdStatus.ERROR,
					"Not valid arguments for function 'add'"
			);
		}
		client.write(ByteBuffer.wrap(response.toJson().getBytes(StandardCharsets.UTF_8)));

		return q;
	}
}
