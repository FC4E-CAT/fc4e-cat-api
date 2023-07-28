package org.grnet.cat.enums;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.grnet.cat.exceptions.EntityNotFoundException;
import org.grnet.cat.exceptions.InternalServerErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration of Integration Sources
 */
public enum Source {

    ROR("ror", "ROR", "https://api.ror.org/organizations") {

        public RorSearchInfo execute(String name, int page) {
            Response resp = connectHttpClient(url + "?query.advanced=name:" + name +"+OR+"+"acronyms:"+name+ "&page=" + page, name);
            try {
              return  buildOrgsInfo(resp.body().string());
            } catch (IOException ex) {
                Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "null response body", ex);
            }
            return null;
        }

        public String[] execute(String id) {
            try {
                Response resp = connectHttpClient(url + "/" + id, id);
                return buildOrgInfo(resp.body().string());
            } catch (IOException ex) {
                Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "null response body", ex);
            }
            return null;
        }

    },
    EOSC("eosc", "EOSC", "http://api.eosc-portal.eu/provider") {
        public String[] execute(String id) {
            try {
                Response resp = connectHttpClient(url + "/" + id, id);
                return buildOrgInfo(resp.body().string());
            } catch (IOException ex) {
                Logger.getLogger(Source.class.getName()).log(Level.SEVERE, "null response body", ex);
            }
            return null;

        }

        @Override
        public RorSearchInfo execute(String id, int page) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    },
    RE3DATA("re3data", "RE3DATA", "") {
        public String[] execute(String id) {
            throw new InternalServerErrorException("Source re3data is not supported.", 501);
        }

        @Override
        public RorSearchInfo execute(String id, int page) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    };

    public abstract String[] execute(String id);

    public abstract RorSearchInfo execute(String id, int page);

    public final String label;
    public final String organisationSource;
    public final String url;

    Source(String label, String organisationSource, String url) {
        this.label = label;
        this.organisationSource = organisationSource;
        this.url = url;
    }

    public String getLabel() {
        return label;

    }

    public String getOrganisationSource() {
        return organisationSource;
    }

    public String getUrl() {
        return url;
    }

    public Source getEnumNameForValue(String value) {
        List<Source> values = Arrays.asList(Source.values());
        for (Source op : values) {

            if (op.getLabel().equalsIgnoreCase(value)) {
                return op;
            }
        }
        return null;
    }

    @SneakyThrows
    Response connectHttpClient(String url, String identifier) {
        var client = new OkHttpClient().newBuilder()
                .build();

        var request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response resp = null;
        try {
            resp = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (resp.code() == 404) {
            throw new EntityNotFoundException("Organisation " + identifier + ", not found in " + organisationSource);
        } else if (resp.code() != 200) {
            throw new InternalServerErrorException("Cannot communicate with " + organisationSource, 500);
        }
        return resp;
    }

   String[] buildOrgInfo(String content) {

        JsonParser jsonParser = new JsonParser();
        // Grab the first - and only line of json from ops data
        JsonElement jElement = jsonParser.parse(content);

        JsonObject jRoot = jElement.getAsJsonObject();
        return returnOrgInfo(jRoot);
    }

    RorSearchInfo buildOrgsInfo(String content) {

        JsonParser jsonParser = new JsonParser();
        JsonElement jElement = jsonParser.parse(content);
         JsonObject jRoot = jElement.getAsJsonObject();
        
      
        int total = jRoot.get("number_of_results").getAsInt();
        JsonArray items=jRoot.get("items").getAsJsonArray();
        
        List<String[]> list=new ArrayList<>();
        
        Iterator iter=items.iterator();
        while(iter.hasNext()){
        JsonObject item=(JsonObject)iter.next();
            list.add(returnOrgInfo(item));
        }
        return  new RorSearchInfo(total, list);
                
                }

    private String[] returnOrgInfo(JsonObject jRoot) {

        String id = jRoot.get("id").getAsString();
        id = id.replaceAll("https://ror.org/", "");

        String name = jRoot.get("name").getAsString();
        String website = null;
        if (jRoot.has("links")) {
            website = jRoot.get("links").getAsJsonArray().get(0).getAsString();

        } else if (jRoot.has("website")) {
            website = jRoot.get("website").getAsString();

        }
        String acronym=null;
        if (jRoot.has("acronyms")) {
            JsonArray acronyms=jRoot.get("acronyms").getAsJsonArray();
          if(!acronyms.isEmpty()) {
              acronym = acronyms.get(0).getAsString();
          }
        }else if(jRoot.has("abbreviation")){
            acronym=jRoot.get("abbreviation").getAsString();
        }
        return new String[]{id,name,website,acronym};

    }
    
    public class RorSearchInfo{
    
        int total;
        List<String[]> orgElements;

        public RorSearchInfo(int total, List<String[]> orgElements) {
            this.total = total;
            this.orgElements = orgElements;
        }

        public int getTotal() {
            return total;
        }

        public List<String[]> getOrgElements() {
            return orgElements;
        }
        
        
        
    
    }
    
    

}
