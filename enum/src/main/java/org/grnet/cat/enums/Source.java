package org.grnet.cat.enums;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Enumeration of  Integration Sources
 */
public enum Source {
    ROR("ror","ROR","https://api.ror.org/organizations"),    
    EOSC("eosc","EOSC","http://api.eosc-portal.eu/provider"),
    RE3DATA("re3data","RE3DATA","");

    public final String label;
    public final String organisationSource;
    public final String url;

    private Source(String label, String organisationSource, String url) {
        this.label = label;
        this.organisationSource = organisationSource;
        this.url = url;
    }


    public String getLabel(){
        return label;

    }

    public String getOrganisationSource() {
        return organisationSource;
    }

    public String getUrl() {
        return url;
    }
    

    public static Source getEnumNameForValue(String value){
        List<Source> values = Arrays.asList(Source.values());
        for(Source op:values){

            if (op.getLabel().equalsIgnoreCase(value)) {
                return op;
            }
        }
        return  null;}


    
}
