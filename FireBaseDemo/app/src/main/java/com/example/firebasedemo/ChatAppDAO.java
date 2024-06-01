package com.example.firebasedemo;
import java.util.ArrayList;
import java.util.Hashtable;

public interface ChatAppDAO {
    public boolean save(Hashtable<String,String> row);
    public ArrayList<Hashtable<String,String>> load();
    public ArrayList<Hashtable<String,String>> load(String constraint);
}
