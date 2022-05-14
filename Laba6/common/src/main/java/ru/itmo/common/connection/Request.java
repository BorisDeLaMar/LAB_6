package ru.itmo.common.connection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

public class Request implements Serializable {
    public final String commandName;
    public final Object argument;

    public Request(String commandName, Object argument){
        this.commandName = commandName;
        this.argument = new Gson().toJson(argument);
    }

    public static Request fromJson(String json){
        return new Gson().fromJson(json, Request.class);
    }
    public <T> T getArgumentAs(Class<T> clazz){
        return new Gson().fromJson((String) argument, clazz);
    }
    public <T> Object getArgumentAs(TypeToken<T> typeToken){
        return new Gson().fromJson((String) argument, typeToken.getType());
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}
