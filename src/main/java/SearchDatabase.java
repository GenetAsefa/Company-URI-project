/**
 * Created by GeniAse on 7/4/16.
 */


import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.MappingLoader;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConfiguration;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class SearchDatabase {

    final String owlfile = "src/main/resources/example/CompanyURI0107.owl";
    final String obdafile = "src/main/resources/example/CompanyURI0107.obda";

    public String[] runQuery(String QueryName) throws Exception {

		/*
         * Load the ontology from an external .owl file.
		 */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
		 * Load the OBDA model from an external .obda file
		 */
        final OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
        QuestPreferences preference = new QuestPreferences();
        preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
        QuestOWLConfiguration config = QuestOWLConfiguration.builder()
                .preferences(preference).obdaModel(obdaModel).build();

        QuestOWLFactory factory = new QuestOWLFactory();

        QuestOWL reasoner = factory.createReasoner(ontology, config);

		/*
		 * Prepare the data connection for querying.
		 */
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

		/*
		 * Get the book information that is stored in the database
		 */
        String Company =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?companyName  ?incorporationDate ?companyURI \n" +
                        "WHERE { ?x a :Company; :companyName ?companyName; \n" +
                        ":companyURI ?companyURI.\n"+
                        "OPTIONAL { ?x :incorporationDate ?incorporationDate } . }";

        String Continent =
                "PREFIX : <http://ex.org/Company#>\n" +
                        "SELECT *\n" +
                        "WHERE { \n" +
                        "?x a :Continent.\n" +
                        "?x :continentName ?y\n" +
                        "}";

        /* Query for account */
        String Account =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?accountRefDay ?accountRefMonth ?accountNextDueDate ?accountLastMadeUpDate ?accountCategory \n" +
                        "WHERE { ?x a :Account; :accountRefDay ?accountRefDay; :accountRefMonth ?accountRefMonth; \n" +
                        ":accountCategory ?accountCategory. \n"+
                        "OPTIONAL { ?x :accountNextDueDate ?accountNextDueDate; :accountLastMadeUpDate ?accountLastMadeUpDate } .  }";


        /* Query for country */
        String Country =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?countryName ?population ?euMember \n" +
                        "WHERE { ?x a :Country; :countryName ?countryName; :population ?population. \n" +
                        "OPTIONAL { ?x :euMember ?euMember } .  }";
        /* Query for CEO */
        String CEO =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?ceoName ?ceoGender ?ceoNationality ?ceoDOB \n" +
                        "WHERE { ?x a :CEO; :ceoName ?ceoName; :ceoGender ?ceoGender; :ceoNationality ?ceoNationality. \n" +
                        "OPTIONAL { ?x :ceoDOB ?ceoDOB }.  }";

        /* Query for DepartmentCategory */
        String DepartmentCategory =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?departmentName \n" +
                        "WHERE { ?x a :DepartmentCategory; :departmentName ?departmentName. }";

        /*Query for Shareholders */
        String Shareholders =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?shareHolderName ?shareHolderDOB ?shareHolderGender ?shareHolderNationality\n" +
                        "WHERE { ?x a :ShareHolder; :shareHolderName ?shareHolderName; :shareHolderDOB ?shareHolderDOB; \n"+
                        ":shareHolderGender ?shareHolderGender; :shareHolderNationality ?shareHolderNationality. }";

        /* Query for SICCODE */
        String SICCODE =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?sicText \n" +
                        "WHERE { ?x a :SICCode; :sicText ?sicTex          String sparqlQuery8 =\n" +
                        "                \"PREFIX : <http://ex.org/Company#>  \\n\" +\n" +
                        "                        \"SELECT DISTINCT ?x ?companyName ?careOf \\n\" +\n" +
                        "                        \"WHERE { ?x a :Company; :companyName ?companyName; :registeredIn ?y. \\n\" +\n" +
                        "                        \"?y a :Address; :careOf ?careOf. }\";\nt. }";

        /* Query for company with address */
        String Address= "PREFIX : <http://ex.org/Company#>\n" +
                "SELECT DISTINCT ?x ?cn ?po ?co ?ad1 ?pt ?ct ?pc\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :registeredIn ?y. \n" +
                "?y a :Address.\n" +
                "?y :poBox ?po.\n" +
                "?y :careOf ?co. \n" +
                "?y :addressLine1 ?ad1.\n" +
                "?y :addressLine2 ?ad2.\n" +
                "?y :postTown ?pt.\n" +
                "?y :county ?ct.\n" +
                "?y :country ?cy.\n" +
                "?y :postCode ?pc\n" +
                "}\n" +
                "\n";

        /* Query for company with SIC */
        String CompanySIC =
                "PREFIX : <http://ex.org/Company#>\n" +
                        "SELECT DISTINCT ?x ?companyName ?sicText \n" +
                        "WHERE { ?x a :Company; :companyName ?companyName; :hasSICCode ?y. \n" +
                        "		 ?y a :SICCode; :sicText ?sicText}";


        /* Query for company with mortgage */
        String Mortgage =
                "PREFIX : <http://ex.org/Company#>\n" +
                        "SELECT *\n" +
                        "WHERE { \n" +
                        "?x a :Company.\n" +
                        "?x :companyName ?cn.\n" +
                        "?x :hasMortgage ?y. \n" +
                        "?y :numMortCharges ?nmc.\n" +
                        "?y :numMortOutstanding ?nos. \n" +
                        "?y :numMortPartSatisfied ?nps.\n" +
                        "?y :numMortSatisfied ?nsf\n" +
                        "}";

        /* Query for company with previousname */
        String previousname ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasPreviousName ?y. \n" +
                "?y :conDate ?cd.\n" +
                "?y :companyPreviousName ?cpn\n" +
                "}";
        /* Query for company with limitedpartership */
        String limitedpartership ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasLimitedPartnerahip ?y. \n" +
                "?y :numGenPartners ?ngp.\n" +
                "?y :numLimPartners ?np\n" +
                "}";

        /*Query for the company which registered in England*/
        String CompanyRegisteredInEngland ="PREFIX : <http://ex.org/Company#>\n" +
                "                SELECT *\n" +
                "                WHERE { \n" +
                "                ?x a :Company.\n" +
                "                ?x :companyName ?cn.\n" +
                "                ?x :registeredIn ?y.\n" +
                "                ?y a :Address.\n" +
                "                ?y :country \"England\"^^xsd:string.\n" +
                "                ?y :country ?country\n" +
                "                }";

        /*Query for the company with country, and eumembership*/
        String eumembership ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :originatedFrom ?c.\n" +
                "?c a :Country.\n" +
                "?c :countryName ?cname.\n" +
                "?c :euMember ?eu\n" +
                "}";

        /*Query for the company with continent*/
        String CompanyWithContinent ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :originatedFrom ?c.\n" +
                "?c a :Country.\n" +
                "?c :belongsTo ?cont.\n" +
                "?cont a :Continent.\n" +
                "?cont :continentName ?contnm\n" +
                "}";

        /*Query for the company with category*/
        String CompanyWithCategory ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasCategory ?c.\n" +
                "?c a :CompanyCategory.\n" +
                "?c :categoryName ?catnm\n" +
                "}";

        Map<String, String> Queries = new HashMap<String, String>();
        Queries.put("Company",Company);
        Queries.put("Continent",Continent);
        Queries.put("Account",Account);
        Queries.put("Country",Country);
        Queries.put("CEO",CEO);
        Queries.put("DepartmentCategory",DepartmentCategory);
        Queries.put("Shareholders",Shareholders);
        Queries.put("SICCODE",SICCODE);
        Queries.put("Address",Address);
        Queries.put("CompanySIC",CompanySIC);
        Queries.put("Mortgage",Mortgage);
        Queries.put("previousname",previousname);
        Queries.put("limitedpartership",limitedpartership);
        Queries.put("CompanyRegisteredInEngland",CompanyRegisteredInEngland);
        Queries.put("eumembership",eumembership);
        Queries.put("CompanyWithContinent",CompanyWithContinent);

        String  sparqlQuery ="";
        Iterator it = Queries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if (pairs.getKey().toString() == QueryName) {
                sparqlQuery = pairs.getValue().toString();
            }
        }
        System.out.println("QueryName = " + QueryName.toString()+ " \n" + "sparqlQuery = " + sparqlQuery);
        String sqlQuery="";
        final String[] sparqleQueries={};
        List<String> result = new ArrayList<String>();
        try {
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
            int columnSize = rs.getColumnCount();
            final ToStringRenderer renderer = ToStringRenderer.getInstance();
            while (rs.nextRow()) {
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    result.add(renderer.getRendering(binding) + ", ");
                    System.out.print(renderer.getRendering(binding) + ", ");
                }
                System.out.print("\n");
            }
            rs.close();


        } finally {
			/*
			 * Close connection and resources
			 */
            st.close();

            conn.close();

            reasoner.dispose();
        }
        String [] ResultInArray = result.toArray(new String[result.size()]);
        return  ResultInArray;
    }

}
