package ethio.habesha.huluagerie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class DataController {

    Retrofit retrofit;

    private static DataController dataController = null;

    public static DataController getDataController() {

        if(dataController == null) {
            dataController = new DataController();
            return dataController;
        }
        return dataController;

    }

    // user model
    public static class User implements Serializable {
        public String email;
        public String password;
    }

    public static class Address implements Serializable {
        public String region = "default";
        public String zone = "default";
        public String city = "default";
        public String kebele = "default" ;
        public String street = "default";
    }

    public static class Service implements Serializable {
        public String name = "default";
        public ArrayList<String> category;
        public int service_level = 4;
    }

    public static class Artifact implements Serializable {
        public String name = "default";
        public String tag = "default";
    }

    public static class Site implements Serializable {
        public String name;
        public Address address;
        public ArrayList<Artifact> artifacts;
        public ArrayList<Service> site_services;
        public float avg_cost = 45.5f;
        public Date opening_hour;
        public Date closing_hour;
        public ArrayList<Date> work_days;
        public ArrayList<String> tags;
    }

    // general response model
    public static class GeneralResponse implements Serializable {
        public String message;
        public String error;
        public Boolean success;
        public Boolean is_admin;
    }

    public static class Preference implements Serializable {
        public ArrayList<Artifact> artifacts;
        public ArrayList<Service> services;
        public ArrayList<Address> locations;
        public ArrayList<String> tags;
        public float duration;
    }

    public static class Activity implements Serializable {
        public Site site;
        public Date startTime;
        public Date endTime;
    }

    public static class Roadmap implements Serializable {
        public String name;
        public String email;
        public ArrayList<Activity> activities;
        public Preference preference;
    }

    public static class SiteUpdate {
        public Site update;
        public String name;
    }

    public static class NameWrapper {
        public String name;
    }

    // encapsulate all the rest calls
    // in an interface
    interface RestAPI {

        /*
        ================================
            User API
        ================================
         */

        @GET("user/all")
        Call<ArrayList<User>> fetchUsers();

        @POST("user/login")
        Call<GeneralResponse> loginUser(@Body User user);

        @POST("user/register")
        Call<GeneralResponse> registerUser(@Body User user);

        /*
        ================================
            Site API
        ================================
         */

        @GET("site/all")
        Call<ArrayList<Site>> fetchSites();

        @GET("site/one")
        Call<Site> fetchSite(@Query("name") String name);

        @POST("site/save")
        Call<GeneralResponse> saveSite(@Body Site site);

        @POST("site/add-artifact")
        Call<GeneralResponse> addArtifact(@Body Artifact artifact, @Body String name);

        @POST("site/add-service")
        Call<GeneralResponse> addService(@Body Service service, @Body String name);

        @POST("site/add-working-day")
        Call<GeneralResponse> addWorkingDay(@Body Date day, @Body String name);

        @POST("site/update")
        Call<GeneralResponse> updateSite(@Body SiteUpdate updateInfo);

        @POST("site/remove")
        Call<GeneralResponse> removeSite(@Body NameWrapper name);

        /*
        ================================
            Roadmap API
        ================================
         */

        @GET("roadmap/all")
        Call<ArrayList<Roadmap>> fetchRoadmaps(@Query("email") String email);

        @POST("roadmap/save")
        Call<GeneralResponse> saveRoadmap(@Body Roadmap roadmap);

        @POST("roadmap/remove")
        Call<GeneralResponse> removeRoadmap(@Body NameWrapper wrapper);

        /*
        ================================
            Generate API
        ================================
         */

        @POST("generator/roadmap")
        Call<Roadmap> generateRoadmap(@Body Preference preference);

    }

//    static final String BASE_URL = "http://192.168.43.122:8000/";
//    static final String BASE_URL = "http://10.0.3.2:8000/"; // genymotion
    static final String BASE_URL = "http://10.0.2.2:8000/"; // android emulator

    RestAPI restAPI;

    public DataController() {

        // prepare a json builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // hook the json builder and the url into the
        // retrofit framework
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // register the rest api
        restAPI = retrofit.create(RestAPI.class);

    }

    /*
    ================================
        User request methods
    ================================
     */

    // method for registering callback listener to the get
    // users request
    public void fetchUsers(Callback<ArrayList<User>>  callback) {
        Call<ArrayList<User>> call = restAPI.fetchUsers();
        call.enqueue(callback);
    }

    // method for registering callback listener to the login
    // user request
    public void loginUser(Callback<GeneralResponse>  callback, User user) {
        Call<GeneralResponse> call = restAPI.loginUser(user);
        call.enqueue(callback);
    }

    // method for registering callback listener to the login
    // user request
    public void registerUser(Callback<GeneralResponse>  callback, User user) {
        Call<GeneralResponse> call = restAPI.registerUser(user);
        call.enqueue(callback);
    }

    /*
    ================================
        Site request methods
    ================================
     */

    // fetch all sites
    public void fetchSites(Callback<ArrayList<Site>>  callback) {
        Call<ArrayList<Site>> call = restAPI.fetchSites();
        call.enqueue(callback);
    }

    // fetch a single site
    public void fetchSite(Callback<Site>  callback, String name) {
        Call<Site> call = restAPI.fetchSite(name);
        call.enqueue(callback);
    }

    // save site
    public void saveSite(Callback<GeneralResponse>  callback, Site site) {
        Call<GeneralResponse> call = restAPI.saveSite(site);
        call.enqueue(callback);
    }

    // add artifact
    public void addArtifact(Callback<GeneralResponse>  callback, Artifact artifact, String name) {
        Call<GeneralResponse> call = restAPI.addArtifact(artifact, name);
        call.enqueue(callback);
    }

    // add service
    public void addService(Callback<GeneralResponse>  callback, Service service, String name) {
        Call<GeneralResponse> call = restAPI.addService(service, name);
        call.enqueue(callback);
    }

    // add work day
    public void addWorkDay(Callback<GeneralResponse>  callback, Date date, String name) {
        Call<GeneralResponse> call = restAPI.addWorkingDay(date, name);
        call.enqueue(callback);
    }

    // update site
    public void updateSite(Callback<GeneralResponse>  callback, SiteUpdate updateInfo) {
        Call<GeneralResponse> call = restAPI.updateSite(updateInfo);
        call.enqueue(callback);
    }

    // remove site
    public void removeSite(Callback<GeneralResponse>  callback, NameWrapper name) {
        Call<GeneralResponse> call = restAPI.removeSite(name);
        call.enqueue(callback);
    }

    /*
    ================================
        Roadmap request methods
    ================================
     */


    // save road map
    public void saveRoadmap(Callback<GeneralResponse>  callback, Roadmap roadmap) {
        Call<GeneralResponse> call = restAPI.saveRoadmap(roadmap);
        call.enqueue(callback);
    }

    // fetch all road maps
    public void fetchRoadmaps(Callback<ArrayList<Roadmap>>  callback, String email) {
        Call<ArrayList<Roadmap>> call = restAPI.fetchRoadmaps(email);
        call.enqueue(callback);
    }

    // remove road map
    public void removeRoadmap(Callback<GeneralResponse>  callback, NameWrapper name) {
        Call<GeneralResponse> call = restAPI.removeRoadmap(name);
        call.enqueue(callback);
    }

    /*
    ================================
        Generator request methods
    ================================
     */

    // fetch all road maps
    public void generateRoadmap(Callback<Roadmap>  callback, Preference preference) {
        Call<Roadmap> call = restAPI.generateRoadmap(preference);
        call.enqueue(callback);
    }

}