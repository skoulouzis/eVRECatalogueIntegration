/* cerif-prov.js
 * Provides a functions for converting a simple JSON description of available provenance logs into CERIF RDF data.
 * Paul Martin 1/12/18
*/

/* Convert provenance log JSON data to CERIF RDF data. Returns the RDF as a string.
 *   data : the JSON to convert. */
function convertProvJsonToCerifRdf(data) {
    let cerif = `@prefix vre:   <http://www.vre4eic.eu#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xml:   <http://www.w3.org/XML/1998/namespace> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
@prefix cerif: <http://eurocris.org/ontology/cerif#> .`;

    let workflowContext = data['workflowContext'],
        systemContext  = data['systemContext'];

    let xIds = [];
    for (let i = 0; i < 5; i++) xIds[i] = guid();

    cerif += `

vre:${guid()} a cerif:Event ;
    cerif:is_source_of vre:${xIds[0]} , vre:${xIds[1]} , vre:${xIds[2]} , vre:${xIds[3]}`;

    let linkIds = [], sysIds = [];
    for (let i = 0; i < systemContext.length; i++) {
        linkIds[i] = guid();
        sysIds[i]  = guid();
        cerif += ` , <vre:${linkIds[i]}>`;
    }

    cerif += ` .

vre:${xIds[0]} a cerif:SimpleLinkEntity ;
    cerif:has_classification vre:WorkflowExecutionRun .

vre:${xIds[1]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:provenanceTrace ;
    cerif:has_destination vre:${workflowContext['provenance']['id']} .

vre:${workflowContext['provenance']['id']} a cerif:Dataset ;
    cerif:has_URI <${workflowContext['provenance']['location']}> .

vre:${xIds[2]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:workflowModel ;
    cerif:has_destination vre:${workflowContext['workflow']['id']} .

vre:${workflowContext['workflow']['id']} a cerif:Dataset ;
    cerif:has_URI <${workflowContext['workflow']['location']}> .

vre:${xIds[3]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:executionLog ;
    cerif:has_destination vre:${xIds[4]} .

vre:${xIds[4]} a cerif:Dataset ;
    cerif:has_URI <${workflowContext['logs']['location']}> .`;

    for (let i = 0; i < systemContext.length; i++) {
        let host = systemContext[i], services = host['services'], slIds = [];

        for (let i = 0; i < 3; i++) slIds[i] = guid();

        cerif += `

vre:${linkIds[i]} a cerif:FullLinkEntity ;
    cerif:has_classification <vre:usesServiceHost> ;
    cerif:has_destination vre:${sysIds[i]} .

vre:${sysIds[i]} a cerif:Equipment ;
    cerif:is_source_of vre:${slIds[0]} , vre:${slIds[1]}`;

        let sLinkIds = [], sslIds = [], sflIds = [], sxlIds = [];
        for (let j = 0; j < services.length; j++) {
            sLinkIds[j] = guid();
            sslIds[j]   = guid();
            sflIds[j]   = guid();
            sxlIds[j]   = guid();
            cerif += ` , vre:${sLinkIds[j]}`;
        }

        cerif += ` .

vre:${slIds[0]} a cerif:SimpleLinkEntity ;
    cerif:has_classification vre:ServiceHost .

vre:${slIds[1]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:executionLog ;
    cerif:has_destination vre:${slIds[2]} .

vre:${slIds[2]} a cerif:Dataset ;
    cerif:has_URI <${host['logs']['location']}> .`;

        for (let j = 0; j < services.length; j++) cerif += `

vre:${sLinkIds[j]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:hostsService ;
    cerif:has_destination vre:${services[j]['id']} .

vre:${services[j]['id']} a cerif:WebService ;
    cerif:is_source_of vre:${sslIds[j]} , vre:${sflIds[j]} .

vre:${sslIds[j]} a cerif:SimpleLinkEntity ;
    cerif:has_classification vre:TavernaWebService .

vre:${sflIds[j]} a cerif:FullLinkEntity ;
    cerif:has_classification vre:executionLog ;
    cerif:has_destination vre:${sxlIds[j]} .

vre:${sxlIds[j]} a cerif:Dataset ;
    cerif:has_URI <${services[j]['logs']['location']}> .`;

    }

    return cerif;
}


/* Convenience function for invoking the conversion function and writing directly to a textarea element.
 *   source : the JSON (as string) to convert.
 *   target : the HTML textarea to write the results to. */
function writeCERIFProv(source, target) {
    try {
        let data = JSON.parse(source);
        document.getElementById(target).value = convertProvJsonToCerifRdf(data);
    } catch(exception) {
        alert("Error parsing the source JSON: " + exception.message);
    }
}


function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}
