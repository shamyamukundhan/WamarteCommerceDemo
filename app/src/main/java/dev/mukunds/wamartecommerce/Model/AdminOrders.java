package dev.mukunds.wamartecommerce.Model;

public class AdminOrders
{
    private String name;
    private String phone;
    private String city;
    private String address;
    private String date;
    private String state;
    private String time;
    private String totalAmount;


    public AdminOrders(String name, String phone, String city, String address, String date, String state, String time, String totalAmount) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.date = date;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public AdminOrders()
    {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

  }
