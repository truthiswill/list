/* The List powered by Creative Commons

   Copyright (C) 2014 Creative Commons

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package org.creativecommons.thelist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesMethods {
    public static final String TAG = SharedPreferencesMethods.class.getSimpleName();

    protected Context mContext;

    public SharedPreferencesMethods(Context context) {
        mContext = context;
    }

    //SharedPreferences Constants
    public static final String CATEGORY_PREFERENCE_KEY = "category";
    public static final String LIST_ITEM_PREFERENCE_KEY = "item";
    public static final String USER_ID_PREFERENCE_KEY = "id";
    public static final String USER_TOKEN_PREFERENCE_KEY = "skey";
    public static final String USER_KEY = "ekey.#j1ldkf9dj3jf9";

    public static final String APP_PREFERENCES_KEY = "org.creativecommons.thelist.43493255t43";


    //Save Any Preference
    public void SaveSharedPreference (String key, String value){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.v("ADDED AND SAVED ITEM ID: ", value);
    }

    public void SaveKey(String key){
        SaveSharedPreference(USER_KEY, key);
    }

    //----------------------------------------------------------
    //GET PREFERENCES
    //----------------------------------------------------------

    public String getKey(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if(sharedPref.contains(SharedPreferencesMethods.USER_KEY)){
            return sharedPref.getString(USER_KEY, null);
        } else {
            return null;
        }
    } //getKey

    //TODO: get rid of this: getAuthToken to replace
    public String getUserToken(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String token = sharedPref.getString(USER_TOKEN_PREFERENCE_KEY, null);
        return token;
    } //getUserToken

    //Get User ID from SharedPreferences
    public String getUserId(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if(sharedPref.contains(SharedPreferencesMethods.USER_ID_PREFERENCE_KEY)){
            String userID = sharedPref.getString(USER_ID_PREFERENCE_KEY, null);
            return userID;
        } else {
            return null;
        }
    } //getUserId


    //Non-logged in user
    public int getUserItemCount(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);

        if(sharedPref.contains(SharedPreferencesMethods.LIST_ITEM_PREFERENCE_KEY)){
            String listOfValues = sharedPref.getString(LIST_ITEM_PREFERENCE_KEY, null);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(listOfValues);
            JsonArray array = element.getAsJsonArray();
            int size = array.size();
            return size;
        } else{
            return 0;
        }
    } //getUserItemCount


    //----------------------------------------------------------
    //RETRIEVE LIST PREFERENCES
    //----------------------------------------------------------

    //RetrieveSharedPreferenceList (generic)
    public JSONArray RetrieveSharedPreferenceList(String key){
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, null);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(value);
        JsonArray array = element.getAsJsonArray();

        //Make usable as JSONArray
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            list.add(array.get(i).getAsString());
        }
        return new JSONArray(list);
    } //RetrieveSharedPreferenceList

    public JSONArray RetrieveCategorySharedPreference (){
        return RetrieveSharedPreferenceList(CATEGORY_PREFERENCE_KEY);
    } //RetrieveCategorySharedPreference

    public JSONArray RetrieveUserItemPreference() {
        return RetrieveSharedPreferenceList(LIST_ITEM_PREFERENCE_KEY);
    } //RetrieveUserItemPreference

    //----------------------------------------------------------
    //CLEAR PREFERENCES
    //----------------------------------------------------------

    //Remove single key in Preferences
    public void ClearSharedPreference(String key) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

    //Clear Temporary Preferences (CAT + ITEMS)
    public void ClearTempPreferences(){
        ClearSharedPreference(CATEGORY_PREFERENCE_KEY);
        ClearSharedPreference(LIST_ITEM_PREFERENCE_KEY);
    }

    //Clear all sharedPreferences
    //TODO: add other keys like session token
    public void ClearAllSharedPreferences() {
        ClearSharedPreference(CATEGORY_PREFERENCE_KEY);
        ClearSharedPreference(LIST_ITEM_PREFERENCE_KEY);
        ClearSharedPreference(USER_KEY);
        //TODO: remove once getAuthToken replaces this
        ClearSharedPreference(USER_TOKEN_PREFERENCE_KEY);
        ClearSharedPreference(USER_ID_PREFERENCE_KEY);
    }

    //Remove single value in Preferences
    public void RemoveUserItemPreference(String itemID) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String listOfValues = sharedPref.getString(LIST_ITEM_PREFERENCE_KEY, null);
        Log.v("REMOVE ITEM ID: ", itemID);
        //Convert from String to Array
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(listOfValues);
        JsonArray array = element.getAsJsonArray();
        Log.v("ARRAY FROM SHARED PREF: ", array.toString());

        for (int i = 0; i < array.size(); i++) {
            String singleItem = array.get(i).getAsString();
            if (singleItem.equals(itemID)) {
                Log.v("ITEM TO REMOVE IS: ", singleItem);
                array.remove(i);
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LIST_ITEM_PREFERENCE_KEY, array.toString());
        editor.apply();
    } //RemoveUserItemPreference

} //SharedPreferenceMethods
