/*
 * #%L
 * ontop-quest-owlapi3
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
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
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class QuestOWLExample {

    /*
     * Use the sample database using H2 from
     * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
     *
     * Please use the pre-bundled H2 server from the above link
     *
     */
    final String owlfile = "src/main/resources/example/CompanyURI0107.owl";
    final String obdafile = "src/main/resources/example/CompanyURI0107.obda";

    public void runQuery() throws Exception {

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
        /*String sparqlQuery =
                "PREFIX : <http://meraka/moss/exampleBooks.owl#> \n" +
                        "SELECT DISTINCT ?x ?title ?author ?genre ?edition \n" +
                        "WHERE { ?x a :Book; :title ?title; :genre ?genre; :writtenBy ?y; :hasEdition ?z. \n" +
                        "		 ?y a :Author; :name ?author. \n" +
                        "		 ?z a :Edition; :editionNumber ?edition }";*/

        /* Query for company */
        String sparqlQuery =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?companyName ?companyStatus ?incorporationDate ?dissolutionDate ?companyRevenue ?companyURI \n" +
                        "WHERE { ?x a :Company; :companyName ?companyName; :companyStatus ?companyStatus; \n" +
                                     ":companyURI ?companyURI.\n"+
                        "OPTIONAL { ?x :companyRevenue ?companyRevenue; :incorporationDate ?incorporationDate; :dissolutionDate ?dissolutionDate } .  }";

        /* Query for account */
        String sparqlQuery1 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?accountRefDay ?accountRefMonth ?accountNextDueDate ?accountLastMadeUpDate ?accountCategory \n" +
                        "WHERE { ?x a :Account; :accountRefDay ?accountRefDay; :accountRefMonth ?accountRefMonth; \n" +
                        ":accountCategory ?accountCategory. \n"+
                         "OPTIONAL { ?x :accountNextDueDate ?accountNextDueDate; :accountLastMadeUpDate ?accountLastMadeUpDate } .  }";

        /* Query for continent */
        String sparqlQuery2 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?continentName \n" +
                        "WHERE { ?x a :Continent; :continentName ?continentName. }";
        /* Query for country */
        String sparqlQuery3 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?countryName ?population ?euMember \n" +
                        "WHERE { ?x a :Country; :countryName ?countryName; :population ?population. \n" +
                        "OPTIONAL { ?x :euMember ?euMember } .  }";
        /* Query for CEO */
        String sparqlQuery4 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?ceoName ?ceoGender ?ceoNationality ?ceoDOB \n" +
                        "WHERE { ?x a :CEO; :ceoName ?ceoName; :ceoGender ?ceoGender; :ceoNationality ?ceoNationality. \n" +
                        "OPTIONAL { ?x :ceoDOB ?ceoDOB }.  }";

        /* Query for DepartmentCategory */
        String sparqlQuery5 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?departmentName \n" +
                        "WHERE { ?x a :DepartmentCategory; :departmentName ?departmentName. }";

        /*Query for Shareholders */
        String sparqlQuery6 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?shareHolderName ?shareHolderDOB ?shareHolderGender ?shareHolderNationality\n" +
                        "WHERE { ?x a :ShareHolder; :shareHolderName ?shareHolderName; :shareHolderDOB ?shareHolderDOB; \n"+
                         ":shareHolderGender ?shareHolderGender; :shareHolderNationality ?shareHolderNationality. }";

        /* Query for SICCODE */
        String sparqlQuery7 =
                "PREFIX : <http://ex.org/Company#> \n" +
                        "SELECT DISTINCT ?x ?sicText \n" +
                        "WHERE { ?x a :SICCode; :sicText ?sicTex          String sparqlQuery8 =\n" +
                        "                \"PREFIX : <http://ex.org/Company#>  \\n\" +\n" +
                        "                        \"SELECT DISTINCT ?x ?companyName ?careOf \\n\" +\n" +
                        "                        \"WHERE { ?x a :Company; :companyName ?companyName; :registeredIn ?y. \\n\" +\n" +
                        "                        \"?y a :Address; :careOf ?careOf. }\";\nt. }";

        /* Query for company with address */
       String sparqlQuery8= "PREFIX : <http://ex.org/Company#>\n" +
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
         String sparqlQuery9 =
                "PREFIX : <http://ex.org/Company#>\n" +
                        "SELECT DISTINCT ?x ?companyName ?sicText \n" +
                        "WHERE { ?x a :Company; :companyName ?companyName; :hasSICCode ?y. \n" +
                        "		 ?y a :SICCode; :sicText ?sicText}";


        /* Query for company with mortgage */
        String sparqlQuery10 =
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
        String sparqlQuery11 ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasPreviousName ?y. \n" +
                "?y :conDate ?cd.\n" +
                "?y :companyPreviousName ?cpn\n" +
                "}";
        /* Query for company with limitedpartership */
        String sparqlQuery12 ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasLimitedPartnerahip ?y. \n" +
                "?y :numGenPartners ?ngp.\n" +
                "?y :numLimPartners ?np\n" +
                "}";

        /*Query for the company which registered in England*/
        String sparqlQuery13 ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :registeredIn ?y. \n" +
                "?y a :Address.\n" +
                "?y :country \"England\"^^xsd:string\n" +
                "}";

        /*Query for the company with country, and eumembership*/
        String sparqlQuery14 ="PREFIX : <http://ex.org/Company#>\n" +
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
        String sparqlQuery15 ="PREFIX : <http://ex.org/Company#>\n" +
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
        String sparqlQuery16 ="PREFIX : <http://ex.org/Company#>\n" +
                "SELECT *\n" +
                "WHERE { \n" +
                "?x a :Company.\n" +
                "?x :companyName ?cn.\n" +
                "?x :hasCategory ?c.\n" +
                "?c a :CompanyCategory.\n" +
                "?c :categoryName ?catnm\n" +
                "}";


        System.out.println("=================Result=====================");

        try {
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery10);
            int columnSize = rs.getColumnCount();
            final ToStringRenderer renderer = ToStringRenderer.getInstance();
            while (rs.nextRow()) {
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    System.out.print(renderer.getRendering(binding) + ", ");
                }
                System.out.print("\n");
            }
            rs.close();

			/*
			 * Print the query summary
			 */
            String sqlQuery = st.getUnfolding(sparqlQuery10);

            System.out.println();
            System.out.println("The input SPARQL query:");
            System.out.println("=======================");
            System.out.println(sparqlQuery8);
            System.out.println();

            System.out.println("The output SQL query:");
            System.out.println("=====================");
            System.out.println(sparqlQuery10);

        } finally {
			/*
			 * Close connection and resources
			 */
            st.close();

            conn.close();

            reasoner.dispose();
        }
    }

    /**
     * Main client program
     */
    public static void main(String[] args) {
        try {
            QuestOWLExample example = new QuestOWLExample();
            example.runQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
