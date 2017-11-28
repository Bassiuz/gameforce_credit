package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.Customer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerJSONWriter {

    private static String JSON_LOCATION = "credit.json";

    public static void writeToJSON(List<Customer> customers)
    {
        //TO-DO: PERSIST TO JSON

        String json = new Gson().toJson(customers);

        try (FileWriter file = new FileWriter(JSON_LOCATION)) {

            file.write(json);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Customer> readFromJson()
    {
        try {
            Type listType = new TypeToken<List<Customer>>() {}.getType();
            List<Customer> customers = null;
            customers = new Gson().fromJson(new FileReader(JSON_LOCATION), listType);
            return customers;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
