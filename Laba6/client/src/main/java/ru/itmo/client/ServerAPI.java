package ru.itmo.client;

import ru.itmo.common.connection.*;
import ru.itmo.common.LAB5.src.GivenClasses.*;

public interface ServerAPI {
    Response add(Worker w);
    Response add_if_min(Worker w);
    Response info();
    Response show();
    Response clear();
    Response exit();
    Response history();
    Response execute_script(String filename);
    Response filter_less_than_status(Status state);
    Response help();
    Response print_descending();
    Response print_unique_status();
    Response remove(long id);
    Response remove_lower(long id);
    Response update(Worker w);
}
