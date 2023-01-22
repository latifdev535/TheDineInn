package com.thedinehouse;

import com.thedinehouse.model.Item;
import com.thedinehouse.model.ItemCategory;
import com.thedinehouse.model.OrderLocation;
import com.thedinehouse.model.OrderResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheDineHouseConstants {

    public static final String TAB_KEY = "TAB_ITEM";
    public static final String SUCCESS = "SUCCESS";
    public static final String EMPTY = "";
    public static final String CART = "CART";
    public static final String VEGAN_FLAG = "1";
    public static final String OK = "OK";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String GREEN_COLOR_CODE = "#00FF00";
    public static final String PLEASE_SELECT = "Please select";
    public static final String TABLE_SERVER = "TABLE_SERVER";
    public static final boolean TEST = false;

    private static Map<Integer, Item> cartItems;


    public static Map<Integer,Item> getCartItems() {
        if(cartItems == null) cartItems = new HashMap<>(2);
        return cartItems;
    }

    public static void emptyCart() {
        cartItems = new HashMap<>(2);
    }


    public static final String SharedPrefVersion = "THE_DINE_HOUSE_V1";

    public static List<ItemCategory> getCategories() {
        List<ItemCategory> categoryList = new ArrayList<>();
        categoryList.add(new ItemCategory(8,"Starters"));
        categoryList.add(new ItemCategory(20,"MainCourse"));
        categoryList.add(new ItemCategory(15,"Fastfood"));
        categoryList.add(new ItemCategory(41,"Deserts"));
        return categoryList;
    }

    public static List<OrderLocation> getOrderLocation() {
        List<OrderLocation> orderLocationList = new ArrayList<>();
        orderLocationList.add(new OrderLocation(1,"TAB001","true","dinein","2023-01-02T23:43:38.000+00:00"));
        orderLocationList.add(new OrderLocation(2,"TAB002","true","dinein","2023-01-02T23:43:38.000+00:00"));
        orderLocationList.add(new OrderLocation(3,"TAB003","true","dinein","2023-01-02T23:43:38.000+00:00"));
        return orderLocationList;
    }

    public static List<Item> getItems() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(355,"Corn Soup","Active",8,"admin",50,0,true));
        itemList.add(new Item(23,"Mushroom Soup","Active",8,"admin",65,0,true));
        itemList.add(new Item(45,"Veg Hot and sour Soup EF","Active",8,"admin",65,0,true));
        itemList.add(new Item(67,"చికెన్ Soup","Active",8,"admin",65,0,false));
        itemList.add(new Item(89,"Chicken Hot and sour Soup AB","Active",8,"admin",65,0,false));
        itemList.add(new Item(78,"Chicken Afgani Soup CD","Active",8,"admin",65,0,false));
        itemList.add(new Item(743,"Chicken Mushroom Soup","Active",8,"admin",65,0,false));
        itemList.add(new Item(85,"Veg Cutlets AB","Active",8,"admin",65,0,true));
        itemList.add(new Item(93,"Chicken Cutlet","Active",8,"admin",65,0,false));
        itemList.add(new Item(130,"Chicken Afgani","Active",8,"admin",65,0,false));
        itemList.add(new Item(813,"Gobi Manchuria EF","Active",8,"admin",65,0,true));
        itemList.add(new Item(512,"Veg Birnay","Active",20,"admin",180,0,true));
        itemList.add(new Item(153,"Mushroom Birnay GH","Active",20,"admin",200,0,true));
        itemList.add(new Item(154,"Paneer Birnay","Active",20,"admin",200,0,true));
        itemList.add(new Item(165,"Ulava Charu Birnay AB","Active",20,"admin",200,0,true));
        itemList.add(new Item(167,"Chicken 65 Birnay","Active",20,"admin",200,0,false));
        itemList.add(new Item(17,"Chicken Afgani Birnay CD","Active",20,"admin",200,0,false));
        itemList.add(new Item(188,"Chicken Dum Birnay","Active",20,"admin",200,0,false));
        itemList.add(new Item(199,"Chicken Mugali Birnay IJK","Active",20,"admin",200,0,false));
        itemList.add(new Item(230,"Chilli Chicken Birnay GH","Active",20,"admin",200,0,false));
        itemList.add(new Item(213,"Chicken Fry Birnay","Active",20,"admin",200,0,false));
        itemList.add(new Item(223,"Chicken Double Joint Birnay","Active",20,"admin",200,0,false));
        itemList.add(new Item(2663,"Veg Noodels","Active",15,"admin",110,0,true));
        itemList.add(new Item(243,"Chicken Noodels IJK","Active",15,"admin",120,0,false));
        itemList.add(new Item(275,"Veg Fried Rice","Active",15,"admin",120,0,true));
        itemList.add(new Item(286,"Chicken Fried Rice","Active",15,"admin",140,0,false));
        itemList.add(new Item(297,"Ice Cream CD","Active",41,"admin",30,0,true));
        itemList.add(new Item(2,"Gulab Jam EF","Active",41,"admin",20,0,true));
        return itemList;
    }

    public static List<OrderResponse> getOrderResponse() {

        List<OrderResponse> list = new ArrayList<>();

        List<Item> orderItems1 = Arrays.asList(
                new Item(355,"1","Corn Soup1",3,"75",100,1,true),
                new Item(275,"1","Corn Soup1",4,"75",200,2,true),
                new Item(2,"1","Corn Soup1",54,"75",300,9,true));

        List<Item> orderItems2 = Arrays.asList(
                new Item(2,"1","Corn Soup2",3,"75",210,2,true),
                new Item(243,"1","Corn Soup3",4,"75",300,7,true),
                new Item(297,"1","Corn Soup3",54,"75",80,27,true));

        List<Item> orderItems3 = Arrays.asList(
                new Item(17,"1","Corn Soup3",3,"75",200,6,true),
                new Item(188,"1","Corn Soup4",4,"75",200,16,true),
                new Item(199,"1","Corn Soup5",54,"75",200,26,true));


        list.add(new OrderResponse("2000","Pending","1","TAB001","Raju",orderItems1));
        list.add(new OrderResponse("387","Completed","2","TAB002","Ravi",orderItems2));
        list.add(new OrderResponse("876","Pending","","TAB003","Ramu",orderItems3));


        return list;
    }

    public static List<String> getPayments() {
        return Arrays.asList("Phone Pe","Card","Other");
    }

    public static List<String> getServers() {
        return Arrays.asList("Raju","Ravi","Ramu");
    }

    public static List<String> getTransGroup() {
        return Arrays.asList("Wages","Vegitables","Others");
    }

    public enum ServerSetting {
        USER_ID, TOKEN, ADMIN, IP_ADDRESS
    }

    public enum PaymentMethod {
        PhonePe,
        ZomatoPay,
        Cash,
        Card,
        Paytm,
        Pending
    }
}
