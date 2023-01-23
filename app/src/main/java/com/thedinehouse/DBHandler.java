package com.thedinehouse;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thedinehouse.model.Item;
import com.thedinehouse.model.ItemCategory;
import com.thedinehouse.model.OrderLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {

	// creating a constant variables for our database.
	// below variable is for our database name.
	private static final String DB_NAME = "dinehouse_db";
	
	// below int is our database version
	private static final int DB_VERSION = 1;

	private static final String ID_COL = "id";
	private static final String NAME_COL = "name";

	// below variable is for our table name.
	public static final String TB_CATEGORY = "CATEGORY";

	// below variable is for our table name.
	public static final String TB_PAYMENT_METHOD = "PAYMENT_METHOD";

	// below variable is for our table name.
	public static final String TB_WAITER = "WAITER";

	// below variable is for our table name.
	public static final String TB_TRANS_GROUP = "TRANS_GROUP";


	// below variable is for our table name.
	public static final String TB_LOCATION = "LOCATION";
	private static final String TYPE_COL = "type";


	// below variable is for our table name.
	public static final String TB_ITEM = "ITEM";

	private static final String STATUS_COL = "status";
	private static final String CATEGORY_ID_COL = "category_id";
	private static final String PRICE_COL = "price";
	private static final String USER_ID_COL = "user_id";
	private static final String CREATED_ON_COL = "created_on";
	private static final String VEGAN_COL = "vegan";

	// =========================================================


	// creating a constructor for our database handler.
	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	// below method is for creating a database by running a sqlite query
	@Override
	public void onCreate(SQLiteDatabase db) {
		// on below line we are creating
		// an sqlite query and we are
		// setting our column names
		// along with their data types.

		String TB_CATEGORY_QUERY = "CREATE TABLE " + TB_CATEGORY + " ("
				+ ID_COL + " INTEGER , "
				+ NAME_COL + " TEXT)";

		String TB_LOCATION_QUERY = "CREATE TABLE " + TB_LOCATION + " ("
				+ ID_COL + " INTEGER , "
				+ NAME_COL + " TEXT, "
				+ STATUS_COL + " TEXT, "
				+ TYPE_COL + " TEXT, "
				+ CREATED_ON_COL + " TEXT)";

		String TB_ITEM_QUERY = "CREATE TABLE " + TB_ITEM + " ("
				+ ID_COL + " INTEGER , "
				+ NAME_COL + " TEXT, "
				+ STATUS_COL + " TEXT, "
				+ CATEGORY_ID_COL + " TEXT, "
				+ PRICE_COL + " INTEGER, "
				+ USER_ID_COL + " TEXT, "
				+ VEGAN_COL + " TEXT, "
				+ CREATED_ON_COL + " TEXT)";


		String TB_PAYMENT_METHOD_QUERY = "CREATE TABLE " + TB_PAYMENT_METHOD + " ("+ NAME_COL + " TEXT)";

		String TB_SERVERS_QUERY = "CREATE TABLE " + TB_WAITER + " ("+ NAME_COL + " TEXT)";

		String TB_TRANS_GROUP_QUERY = "CREATE TABLE " + TB_TRANS_GROUP + " ("+ NAME_COL + " TEXT)";

		db.execSQL(TB_CATEGORY_QUERY);
		db.execSQL(TB_LOCATION_QUERY);
		db.execSQL(TB_ITEM_QUERY);
		db.execSQL(TB_PAYMENT_METHOD_QUERY);
		db.execSQL(TB_SERVERS_QUERY);
		db.execSQL(TB_TRANS_GROUP_QUERY);
	}

	public void deleteTableData(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+tableName);
	}

	public void insertCategoryBulk(List<ItemCategory> categoryList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (ItemCategory itemCategory : categoryList) {
				values.put(ID_COL, itemCategory.getId());
				values.put(NAME_COL, itemCategory.getName());
				db.insert(TB_CATEGORY, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public void insertLocationBulk(List<OrderLocation> orderLocationList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (OrderLocation orderLocation : orderLocationList) {
				values.put(ID_COL, orderLocation.getId());
				values.put(NAME_COL, orderLocation.getName());
				values.put(STATUS_COL, orderLocation.getStatus());
				values.put(TYPE_COL, orderLocation.getType());
				values.put(CREATED_ON_COL, orderLocation.getCreatedOn());
				db.insert(TB_LOCATION, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public void insertItemBulk(List<Item> categoryList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (Item itemCategory : categoryList) {
				values.put(ID_COL, itemCategory.getId());
				values.put(NAME_COL, itemCategory.getName());
				values.put(STATUS_COL, itemCategory.getStatus());
				values.put(CATEGORY_ID_COL, itemCategory.getCategoryId());
				values.put(PRICE_COL, itemCategory.getPrice());
				values.put(USER_ID_COL, itemCategory.getUserId());
				values.put(VEGAN_COL, itemCategory.isVeg());
				values.put(CREATED_ON_COL, itemCategory.getCreatedOn());
				db.insert(TB_ITEM, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public void insertPaymentMethods(List<String> paymentMethods){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String name : paymentMethods) {
				values.put(NAME_COL, name);
				db.insert(TB_PAYMENT_METHOD, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public void insertServers(List<String> serversList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String name : serversList) {
				values.put(NAME_COL, name);
				db.insert(TB_WAITER, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public void insertTransType(List<String> typeList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			for (String name : typeList) {
				values.put(NAME_COL, name);
				db.insert(TB_TRANS_GROUP, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	@SuppressLint("Range")
	public Map<Integer,String> getCategoriesList(){
		SQLiteDatabase db = this.getWritableDatabase();
		Map<Integer,String> categoryMap = new HashMap<>();
		String query = "SELECT id,name FROM "+ TB_CATEGORY;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			categoryMap.put(cursor.getInt(cursor.getColumnIndex(ID_COL)),cursor.getString(cursor.getColumnIndex(NAME_COL)));
		}
		return  categoryMap;
	}

	@SuppressLint("Range")
	public List<String> getOrderLocationList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> orderLocationList = new ArrayList<>();
		String query = "SELECT id,name FROM "+ TB_LOCATION;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			orderLocationList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
		}
		return  orderLocationList;
	}

	@SuppressLint("Range")
	public List<Item> getItemsList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<Item> orderLocationList = new ArrayList<>();
		String query = "SELECT id,name,status,category_id,price,vegan FROM "+ TB_ITEM;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			Item item = new Item();
			item.setItemId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
			item.setId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
			item.setName(cursor.getString(cursor.getColumnIndex(NAME_COL)));
			item.setStatus(cursor.getString(cursor.getColumnIndex(STATUS_COL)));
			item.setQuantity(0);
			item.setCategoryId(cursor.getInt(cursor.getColumnIndex(CATEGORY_ID_COL)));
			item.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE_COL)));
			item.setVeg(cursor.getString(cursor.getColumnIndex(VEGAN_COL)).equalsIgnoreCase(TheDineHouseConstants.VEGAN_FLAG));
			orderLocationList.add(item);
		}
		return  orderLocationList;
	}

	@SuppressLint("Range")
	public List<String> getPaymentList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> paymentList = new ArrayList<>();
		String query = "SELECT name FROM "+ TB_PAYMENT_METHOD;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			paymentList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
		}
		return  paymentList;
	}

	@SuppressLint("Range")
	public List<String> getServersList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> paymentList = new ArrayList<>();
		String query = "SELECT name FROM "+ TB_WAITER;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			paymentList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
		}
		return  paymentList;
	}

	@SuppressLint("Range")
	public List<String> getTranGroupList(){
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> paymentList = new ArrayList<>();
		String query = "SELECT name FROM "+ TB_TRANS_GROUP;
		Cursor cursor = db.rawQuery(query,null);
		while (cursor.moveToNext()){
			paymentList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
		}
		return  paymentList;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// this method is called to check if the table exists already.
		db.execSQL("DROP TABLE IF EXISTS " + TB_LOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TB_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + TB_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TB_PAYMENT_METHOD);
		db.execSQL("DROP TABLE IF EXISTS " + TB_WAITER);
		db.execSQL("DROP TABLE IF EXISTS " + TB_TRANS_GROUP);
		onCreate(db);
	}
}
