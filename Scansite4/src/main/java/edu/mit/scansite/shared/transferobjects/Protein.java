package edu.mit.scansite.shared.transferobjects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class Protein extends LightWeightProtein implements IsSerializable {
	private Taxon species;
	private OrganismClass organismClass = OrganismClass.OTHER;
	private double molecularWeight = 0;
	private double pI = 0;
	private double pIPhos1 = 0;
	private double pIPhos2 = 0;
	private double pIPhos3 = 0;
	private HashMap<String, Set<String>> annotations = new HashMap<String, Set<String>>();

	public Protein() {
		super();
	}

	public Protein(String identifier, DataSource dataSource, String sequence,
			Taxon species, double molecularWeight, double pI) {
		super(identifier, dataSource, sequence);
		this.species = species;
		this.molecularWeight = molecularWeight;
		this.pI = pI;
	}

	public Protein(String identifier, DataSource dataSource, String sequence,
			Taxon species, double molecularWeight, double pI,
			HashMap<String, Set<String>> annotations) {
		super(identifier, dataSource, sequence);
		this.species = species;
		this.molecularWeight = molecularWeight;
		this.pI = pI;
		this.annotations = annotations;
	}

	public Taxon getSpecies() {
		return species;
	}

	public void setSpecies(Taxon species) {
		this.species = species;
	}

	public double getMolecularWeight() {
		if (molecularWeight == 0) {
			ScansiteAlgorithms algs = new ScansiteAlgorithms();
			molecularWeight = algs.calculateMolecularWeight(sequence, 0);
		}
		return molecularWeight;
	}

	public void setMolecularWeight(double molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public double getpI() {
		if (pI == 0) {
			ScansiteAlgorithms algs = new ScansiteAlgorithms();
			pI = algs.calculateIsoelectricPoint(sequence, 0);
		}
		return pI;
	}

	public void setpI(double pI) {
		this.pI = pI;
	}

	public void addAnnotation(String category, String annotation) {
		Set<String> entries = annotations.get(category);
		if (entries == null) {
			entries = new HashSet<String>();
		}
		entries.add(annotation);
		annotations.put(category, entries);
	}

	public Set<String> getAnnotation(String category) {
		return annotations.get(category);
	}

	public HashMap<String, Set<String>> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(HashMap<String, Set<String>> annotations) {
		this.annotations = annotations;
	}

	public double getpIPhos1() {
		if (pIPhos1 <= 0) {
			ScansiteAlgorithms algs = new ScansiteAlgorithms();
			pIPhos1 = algs.calculateIsoelectricPoint(sequence, 1);
		}
		return pIPhos1;
	}

	public double getpIPhos2() {
		if (pIPhos2 <= 0) {
			ScansiteAlgorithms algs = new ScansiteAlgorithms();
			pIPhos2 = algs.calculateIsoelectricPoint(sequence, 2);
		}
		return pIPhos2;
	}

	public double getpIPhos3() {
		if (pIPhos3 <= 0) {
			ScansiteAlgorithms algs = new ScansiteAlgorithms();
			pIPhos3 = algs.calculateIsoelectricPoint(sequence, 3);
		}
		return pIPhos3;
	}

	public void setpIPhos1(double pIPhos1) {
		this.pIPhos1 = pIPhos1;
	}

	public void setpIPhos2(double pIPhos2) {
		this.pIPhos2 = pIPhos2;
	}

	public void setpIPhos3(double pIPhos3) {
		this.pIPhos3 = pIPhos3;
	}

	public void setOrganismClass(OrganismClass organismClass) {
		this.organismClass = organismClass;
	}

	public OrganismClass getOrganismClass() {
		return organismClass;
	}
}
