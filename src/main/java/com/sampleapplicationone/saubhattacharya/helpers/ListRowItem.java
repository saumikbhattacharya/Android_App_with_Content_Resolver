package com.sampleapplicationone.saubhattacharya.helpers;

import java.io.Serializable;

public class ListRowItem implements Serializable{

    String name,email,phone_number;

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String emailaddr)
    {
        this.email = emailaddr;
    }

    public void setPhone_number(String phone)
    {
        this.phone_number = phone;
    }
}
