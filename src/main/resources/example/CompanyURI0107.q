[QueryItem="ddd"]
PREFIX : <http://ex.org/Company#>
                SELECT *
                WHERE { 
                ?x a :Company.
                ?x :companyName ?cn.
                ?x :registeredIn ?y.
                ?y a :Address.
                ?y :country "England"^^xsd:string
                }

[QueryItem="1111"]
PREFIX : <http://ex.org/Company#>
                SELECT *
                WHERE { 
                ?x a :Company.
                ?x :companyName ?cn.
                ?x :hasSICCode ?c.
                ?c :sicText ?st
                
                }

[QueryItem="PreviousName"]
PREFIX : <http://ex.org/Company#>
                SELECT *
                WHERE { 
                ?companyNumber a :Company.
                ?companyNumber :companyName ?companyName.
                ?companyNumber :hasPreviousName ?y. 
                ?y :conDate ?changeDate.
                ?y :companyPreviousName ?previousName
                }

[QueryItem="CompanyAddress"]
PREFIX : <http://ex.org/Company#>
                SELECT DISTINCT *
                WHERE { 
                ?x a :Company.
                ?x :companyName ?companyName.
                ?x :registeredIn ?y. 
                ?y a :Address.
                ?y :poBox ?poBox.
                ?y :careOf ?careOf. 
                ?y :addressLine1 ?addressLine1.
                ?y :addressLine2 ?addressLine2.
                ?y :postTown ?postTown.
                ?y :county ?county.
                ?y :country ?country.
                ?y :postCode ?postCode
                }
