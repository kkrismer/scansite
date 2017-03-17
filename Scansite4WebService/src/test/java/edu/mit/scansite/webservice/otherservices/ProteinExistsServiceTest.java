package edu.mit.scansite.webservice.otherservices;

import edu.mit.scansite.shared.dispatch.BooleanResult;
import org.junit.Test;

import static edu.mit.scansite.webservice.ScansiteTestUtils.dsShortName;
import static edu.mit.scansite.webservice.ScansiteTestUtils.proteinIdentifier;

/**
 * Created by Thomas on 3/14/2017.
 *
 */
public class ProteinExistsServiceTest {

    @Test
    public void proteinExistsServiceTest() {
        edu.mit.scansite.webservice.transferobjects.BooleanResult result;
        result = ProteinExistsService.isProteinInDatabase(proteinIdentifier, dsShortName);

        assert (result.getIsSuccess());
    }
}
