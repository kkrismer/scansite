package edu.mit.scansite.server.domains;

import java.util.ArrayList;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * Class for locating domains.
 * 
 * @author tobieh
 */
public class MockDomainLocator extends DomainLocator {

	public MockDomainLocator(ConfigReader reader) {
		super(reader);
	}

	public ArrayList<DomainPosition> getDomainPositions(String realPath, String sequence) {
		ArrayList<DomainPosition> positions = new ArrayList<DomainPosition>();

		positions.add(new DomainPosition(19, 104, "CAMSAP_CH", "PFAM", "PF00621", "IPR001715", "some name"));
		positions
				.add(new DomainPosition(199, 373, "RhoGEF", "PFAM", "PF00130", "IPR001715", "some name"));
		positions.add(new DomainPosition(404, 505, "PH", "PFAM", "PF00017", "IPR001715", "some name"));
		positions.add(new DomainPosition(517, 569, "C1_1", "PFAM", "PF00621", "IPR001715", "some name"));
		positions.add(new DomainPosition(616, 653, "SH3_1", "PFAM", "PF07653", "IPR001715", "some name"));
		positions.add(new DomainPosition(672, 746, "SH2", "PFAM", "PF00169", "IPR001715", "some name"));
		positions.add(new DomainPosition(789, 835, "SH3_1", "PFAM", "PF00130", "IPR001715", "some name"));

		ArrayList<DomainPosition> positions2 = new ArrayList<DomainPosition>();
		for (DomainPosition p : positions) {
			if (p.getFrom() < sequence.length()
					&& p.getTo() < sequence.length()) {
				positions2.add(p);
			}
		}
		return positions2;
	}

	@Override
	public void init() {
	}
}
