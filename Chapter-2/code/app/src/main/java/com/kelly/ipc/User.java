package com.kelly.ipc;

import java.io.Serializable;

public class User  implements Serializable {


    private static final long serialVersionUID = 5274214434246148601L;
    private String name;
    private String favorite;

    public User(String name, String favorite) {
        this.name = name;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
