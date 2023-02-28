package commons;


import java.util.ArrayList;
import java.util.List;


public class Board {
    private List<BoardList> lists;
    private String name;

    public Board(){
        this.name = "";
        this.lists = new ArrayList<BoardList>();
    }

    public Board(String name, List<BoardList> lists){
        this.name = name;
        this.lists = lists;
    }

    public List<BoardList> getLists(){
        return this.lists;
    }

    public void setLists(List<BoardList> lists){
        this.lists = lists;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        return other instanceof Board &&
               ((Board) other).lists.equals(this.lists) &&
               ((Board) other).name.equals(this.name);
    }

    //TODO addList

    //TODO deleteList




}
