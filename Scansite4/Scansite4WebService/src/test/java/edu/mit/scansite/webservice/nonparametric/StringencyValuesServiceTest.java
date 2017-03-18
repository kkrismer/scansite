package edu.mit.scansite.webservice.nonparametric;

import edu.mit.scansite.webservice.otherservices.OrganismClassesService;
import edu.mit.scansite.webservice.otherservices.StringencyValuesService;
import edu.mit.scansite.webservice.transferobjects.OrganismClasses;
import edu.mit.scansite.webservice.transferobjects.StringencyValues;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 3/14/2017.
 */
public class StringencyValuesServiceTest {

    @Test
    public void organismClassesServiceTest() {
        StringencyValues stringencyValues = StringencyValuesService.getStringencyValues();

        List<String> stringencyValueNames = new ArrayList<>();
        stringencyValueNames.add("High");
        stringencyValueNames.add("Medium");
        stringencyValueNames.add("Low");
        stringencyValueNames.add("Minimum");


        int matches = 0;
        for (int i=0; i < stringencyValues.getStringencyValue().length; i++) {
            for (String stringencyValueName : stringencyValueNames) {
                if(stringencyValues.getStringencyValue()[i].equals(stringencyValueName)) {
                    matches++;
                }
            }
        }
        assert (matches == stringencyValueNames.size());
    }
}
