saisie.controller('activiteGroupeCTRL',['$scope','$rootScope','$http','$stateParams','$state','$timeout',function ($scope,$rootScope,$http,$stateParams,$state,$timeout) {
    console.log("entrer dans activiteGroupeCTRL");
    var dataInstanceValue = serverAdresse+"dataValue/";
    var dataValueUrl = serverAdresse+'dataValue';
    var searchBeneficiaire = serverAdresse+"beneficiaire";
    //$scope.vadData = {};
    $scope.groupeSession = {};
    $scope.groupe = {};
    var modifSession = {};
    var dataInstance = {};
    var elementCode = [];
    var instanceValue = [];

    var dataInstanceEntete = {};
    var autocompleteData = [];
    var instanceValueEnrolement = [];
    var materielQte = {};
    var tempData=[];
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = dataInstanceEntete;


    var groupeTable = ["nomOrganisation","nomSafespace","nomAgentPrinc","prenomAgentPrinc","contactAgentPrinc","nomAgentSecond","prenomAgentSecond","contactAgentSecond","region","district","sousPrefect","commune","quartier","infoSession","thematique","trancheAge"];
    var sessionTable = ["dateSession","themeAborde","methodologie","outils","materielQuantite"];
    var benefTable = ["id_dreams","nomPrenomBenef","contactBenef","benefSession","benefAyantTernime","totalBenefSession","totalBenefAyantTernime"];
    var autreCibleTable = ["codeBenef","nomPrenomAutreCible","contactAutreCible","sexeAutreCible","autreCibleSession","autreCibleAyantTernimeMasc","autreCibleAyantTernimeFem","totalAutreCibleSession","totalAutreCibleAyantTernimeMasc","totalAutreCibleAyantTernimeFem"];
    var groupeElement = [];
    var sessionElement = [];
    var benefElement = [];
    var autreCibleElement = [];
    $scope.sessionData = [];
    $scope.ListBenef = [];
    $scope.TotalBenef = {};
    $scope.ListAutreCible = [];
    var benefValue = {};
    var autreCibleValue = {};
    var origin = "";

    var collecteData=[];
    var sessionValue = [];
    var benefValue = [];
    var listeBeneficiaire = [];
    var benefSelected = {};
    var listeBenefInitial = [];

    var autreValue = [];
    var listeAutreBenefCode = [];
    var autreBenefCodeSelected = {};
    var listeAutreBenefCodeInitial = [];


    initOrg();

    $scope.thematique = {
        model: 'myString',
        availableOptions: [
            {value: 'educationSexuel', name: 'Education sexuelle complète'},
            {value: 'educationFinanciere', name: 'Education financière / Business plus'},
            {value: "sasa", name: 'Sasa'},
            {value: "sinovoyu", name: 'SINOVOYU'},
            {value: "psychoSocial", name: 'Psycho-social'},
            {value: 'avec', name: 'AVEC'}
        ]
    };
    $scope.trancheAge = {
        model: 'myString',
        availableOptions: [
            {value: '10-14', name: '10-14 ans'},
            {value: '15-19', name: '15-19 ans'}
        ]
    };

    gestElements();

    function initOrg() {
        $scope.groupe.nomOrganisation = $rootScope.orgUnitSelect.organisationLocal;
        $scope.groupe.nomSafespace = $rootScope.orgUnitSelect.name;
        $scope.groupe.region = $rootScope.orgUnitSelect.region;
        $scope.groupe.sousPrefect = $rootScope.orgUnitSelect.sousPrefecture;
        $scope.groupe.commune = $rootScope.orgUnitSelect.commune;
        //$scope.groupe.district = $rootScope.orgUnitSelect.commune;
        //$scope.groupe.quartier = $rootScope.orgUnitSelect.quartier;
    }

    if(dataInstanceEntete.instance){
      //console.log("$rootScope.allData = ",$rootScope.allData);
        if($rootScope.allData){
            for(var i=0;i<$rootScope.allData.length;i++){
                if($rootScope.allData[i].instance == dataInstanceEntete.instance){
                    instanceValue = $rootScope.allData[i].dataValue;
                    console.log("instanceValue = ",angular.copy(instanceValue));
                    mappigData();
                }
            }
        }else{
            //origin = "init";
            getdataInstace(dataInstanceEntete.instance);
        }


    }

    function getdataInstace(instance){
        console.log("getdataInstace()");
         var InstanceValue = dataInstanceValue + instance;
        $http.get(InstanceValue).then(function (response) {
            console.log("getdataInstace() response",angular.copy(response));
            if (response) {
                instanceValue = response.data.dataValue;
                mappigData();
                //getRoute();
            }
        }, function (err) {
            console.log("getdataInstace() err",err);
        });
    }

    /*function getRoute() {
        if(origin == "init"){
            mappigData();
        }else if(origin == "session"){
            collecteSessionData();
        }else if(origin == "benef"){
            collecteBenefData();
        }else if(origin == "autreCible"){
            collecteAutreCibleData();
        }else if(origin == "idbenef"){
            gestComplete();
        }
    }*/

    function mappigData() {
        //console.log("mappigData");
        instanceValue = collecteGroupeData(instanceValue);
        instanceValue = collecteSessionData(instanceValue);
        instanceValue = collecteBenefData(instanceValue);
        instanceValue = collecteAutreCibleData(instanceValue);
        initOrg();
        checkBenefListe();
        //updateBeneficiaireListe();
        //console.log("$scope.sessionData = ",angular.copy($scope.sessionData));
    }

    $scope.saveMaterial = function () {
        //console.log("saveMaterial()");
        var options = [];
        options = getOption(sessionElement,"materielQuantite");
        $scope.groupeSession.materielQuantite = "";
        $scope.MqteInfo = "";
        for (var prop in $scope.materiel) {
            if($scope.materiel[prop] && materielQte[prop] != null){
                if($scope.groupeSession.materielQuantite == ""){
                    $scope.groupeSession.materielQuantite = materielQte[prop] +" "+prop;
                    $scope.MqteInfo = materielQte[prop] +" "+getOptionValue(options,prop);
                }

                else{
                    $scope.groupeSession.materielQuantite = $scope.groupeSession.materielQuantite +" "+ materielQte[prop] +" "+prop;
                    $scope.MqteInfo = $scope.MqteInfo+"; "+ materielQte[prop] +" "+getOptionValue(options,prop);
                }

            }
        }
    };

    $scope.saveQuantite = function (materiel, quantite) {
        materielQte[materiel] = quantite;
    };

    $scope.saveSession = function () {
        console.log("saveSession()");
        tempData=[];
        origin = "session";
        /*console.log("saveSession() groupe = ",$scope.groupe);
        console.log("saveSession() groupeSession = ",$scope.groupeSession);*/
        if(checkgroupeSession()){
            dataInstance = dataInstanceEntete;
            dataInstance.dateActivite = $scope.groupeSession.dateSession;
            dataInstance.dataValue = [];
            /*if(!dataInstanceEntete.instance){
                delete dataInstance.instance;
                //tempData.push($scope.groupe);
                tempData.push($scope.groupeSession);
                //dataInstance.dataValue = [];
                machting();
            }else{
                elementCode = Object.keys($scope.groupeSession);
                if(angular.isObject(modifSession) && !isEmpty(modifSession)){
                    isValeurModifier();
                }else{
                    ajoutSession();
                }
            }*/

                elementCode = Object.keys($scope.groupeSession);
                if(angular.isObject(modifSession) && !isEmpty(modifSession)){
                    isValeurModifier();
                }else{
                    ajoutSession();
                }

        }
    };

    function checkgroupeSession() {
        console.log("checkgroupeSession()");
        if(!$scope.groupeSession.dateSession)
            return false;
        if(!$scope.groupeSession.themeAborde)
            return false;
        if(!$scope.groupeSession.methodologie)
            return false;
        if(!$scope.groupeSession.materielQuantite)
            $scope.groupeSession.materielQuantite = null;
        if(!$scope.groupeSession.outils)
            $scope.groupeSession.outils = null;
        console.log("checkgroupeSession() > $scope.groupeSession = ",$scope.groupeSession);
        return true;
    }

    function isValeurModifier() {
        console.log("isValeurModifier() elementCode = ",elementCode);
        for(var i =0;i<elementCode.length;i++){
                for(var k=0;k<sessionElement.length;k++){
                    if(elementCode[i] == sessionElement[k].code){
                        var data = {};
                        data.numero = modifSession.numero;
                        data.value = $scope.groupeSession[elementCode[i]];
                        data.element = sessionElement[k].id;
                        dataInstance.dataValue.push(data);
                        break;
                    }
                }
        }
        if(dataInstance.dataValue.length != 0){
            console.log("isValeurModifier() dataInstance.dataValue = ",dataInstance);
            modifSessionListe(dataInstance.dataValue);
            //saveData();
        }
        iniSession();
    }

    function modifSessionListe(value) {
      console.log("modifSessionListe() sessionValue = ",angular.copy(sessionValue));
      console.log("modifSessionListe() value = ",angular.copy(value));
      for(var i = 0;i<value.length;i++){
        for(var j = 0;j<sessionValue.length;j++){
          if(value[i].element == sessionValue[j].element && value[i].numero == sessionValue[j].numero){
            console.log("addSessionListe() sessionData[j] = ",angular.copy(sessionValue[j])," value = ",angular.copy(value[i]));
            sessionValue[j].value = value[i].value;
            console.log("sessionData[j] = ",angular.copy(sessionValue[j]));
            break;
          }
        }
      }

      collecteSessionData(sessionValue);
    }

    function ajoutSession() {
        console.log("ajoutSession() elementCode = ",elementCode," // sessionElement = ",sessionElement);
        for(var i = 0;i<elementCode.length;i++){
            for(var k=0;k<sessionElement.length;k++){
                if(elementCode[i] == sessionElement[k].code){
                    var data = {};
                    data.element =  sessionElement[k].id;
                    data.value = $scope.groupeSession[elementCode[i]];
                    dataInstance.dataValue.push(data);
                    break;
                }
            }
        }
        if(dataInstance.dataValue.length != 0){
            //console.log("ajoutSession() dataValue exite");
            console.log("ajoutSession() dataInstance.dataValue = ",dataInstance.dataValue);
            addSessionListe(dataInstance.dataValue);
            //saveData();
        }
        iniSession();
    }

    function addSessionListe(value) {
      console.log("addSessionListe() sessionValue = ",angular.copy(sessionValue));
      console.log("addSessionListe() sessionData = ",angular.copy($scope.sessionData));
      console.log("addSessionListe() value = ",angular.copy(value));
      for(var i = 0,j=value.length;i<j;i++){
        value[i].numero = $scope.sessionData.length + 1;
        sessionValue.push(value[i]);
      }
      console.log("addSessionListe() sessionValue = ",angular.copy(sessionValue));
      collecteSessionData(sessionValue);
    }

    function iniSession() {
        modifSession = {};
        $scope.groupeSession = {};
        $scope.method = {};
        $scope.outil = {};
        for(var pop in $scope.materiel){
            $scope[pop] = null;
        }
        $scope.materiel = {};
        $scope.methodInfo = null;
        $scope.outilsInfo = null;
        $scope.MqteInfo = null;
    }

    function machting() {
        var element = {};
        for(var i = 0;i<tempData.length;i++){
            var tmp = {};
            tmp = tempData[i];
            for (var prop in tmp) {
                element[prop] = tmp[prop];
            }
        }
        console.log("machting() element = ",element);
        elementCode = Object.keys(element);
        console.log("elementCode = ",elementCode);
        getElementid(element)
    }

    function getElementid(valeur) {
        var data = {};
        for(var i=0;i<elementCode.length;i++){
            for(var j = 0;j<$rootScope.programmeSelect.elements.length;j++){

                    if($rootScope.programmeSelect.elements[j].element.code == elementCode[i]){
                        var data = {};
                        data.element = $rootScope.programmeSelect.elements[j].element.id;
                        data.value = valeur[elementCode[i]];
                        dataInstance.dataValue.push(data);
                    }

            }
        }
        console.log("dataInstance = ",dataInstance);
        if(dataInstance.dataValue.length != 0){
            //saveData();
        }

        iniSession();
    }

    function saveData() {

        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http.post(dataValueUrl, dataInstance, config).then(function(succes){
            console.log("succes = ",succes);
            //dataInstance = succes.data;
            if(succes.data.status == "ok"){
                dataInstance.instance = succes.data.id;
                dataInstanceEntete.instance = succes.data.id;
                $stateParams.inst = succes.data.id;
                getdataInstace(dataInstance.instance);
            }
        }, function(error){
            //console.log("error = ",error);
            //echecSave();
        });
    }

    $scope.saveValue = function (element,value) {
        console.log("saveValue() element =",element,"//value =",value);

        /*if(dataInstanceEntete.instance){
            origin = "autre";
            var valueTDO = {};
            valueTDO.element = getElementId(element);
            //valueTDO.instance = dataInstanceEntete.instance;
            valueTDO.numero = $scope.groupe.numero;
            valueTDO.value = value;
            dataInstance = dataInstanceEntete;
            dataInstance.dataValue = [];
            dataInstance.dataValue.push(valueTDO);
            console.log("saveValue() valueTDO = ",valueTDO);
            //saveData();
            //getdataInstace(dataInstanceEntete.instance);
            //sendValueTDO(valueTDO);
        }*/
    };

    $scope.searchId = function (valeur) {
        //console.log("searchId element = ",element," // valeur = ",valeur," //$rootScope.programmeEnrolement = ",$rootScope.programmeEnrolement);

        if(valeur.length > 2){
            console.log("searchId valeur = ",valeur);
            var org = "organisation="+dataInstanceEntete.organisation;
            var value = "idDreams="+valeur;

            var datavalueSearch = searchBeneficiaire+"?"+org+"&"+value;
            $http.get(datavalueSearch).then(function (response) {
                console.log("searchId() response",response);
                autocompleteData = response.data;
                chargeData();
            }, function (err) {
                console.log("searchId() err",err);
            });
        }

    };

    function getElementId(code) {
        //console.log("getElementId() code = ",code," // groupeElement = ",groupeElement);
        for(var j=0;j<groupeElement.length;j++){
            if(code == groupeElement[j].code){
                return groupeElement[j].id;
            }
        }
    }

    function chargeData() {

        $scope.valueComplete = [];
        for(var i = 0, j=autocompleteData.length;i<j;i++){
            $scope.valueComplete.push(autocompleteData[i].id_dreams);
        }
        console.log("chargeData() $scope.valueComplete = ", $scope.valueComplete);
    }

    $scope.choisir=function(string){
        $scope.groupeBenef.id_dreams=string;
        $scope.valueComplete=null;
        saveValueId(string);
    };

    function saveValueId(string) {

        for(var i =0,j=autocompleteData.length;i<j;i++){
            if(string == autocompleteData[i].id_dreams){
                gestComplete(autocompleteData[i]);
            }
        }
    }

    function gestComplete(benefInfo) {
        $scope.groupeBenef.nomPrenomBenef = benefInfo.name+" "+benefInfo.firstName;
        benefSelected = benefInfo;
        console.log("gestComplete() benefSelected= ",benefSelected);
    }

    function gestElements() {
        //console.log("entrer dans gestElements()");
        groupeElement = [];
        sessionElement = [];
        benefElement = [];
        autreCibleElement = [];
        for(var i =0,j=$rootScope.programmeSelect.elements.length;i<j;i++){
            var trouve = false;
            for(var l=0;l<sessionTable.length;l++){
                if(sessionTable[l] == $rootScope.programmeSelect.elements[i].element.code){
                    trouve = true;
                    sessionElement.push($rootScope.programmeSelect.elements[i].element);
                    break;
                }
            }
            if(!trouve){
                for(var l=0;l<benefTable.length;l++){
                    if(benefTable[l] == $rootScope.programmeSelect.elements[i].element.code){
                        benefElement.push($rootScope.programmeSelect.elements[i].element);
                        trouve = true;
                        break;
                    }
                }
            }

            if(!trouve){
                for(var l=0;l<autreCibleTable.length;l++){
                    if(autreCibleTable[l] == $rootScope.programmeSelect.elements[i].element.code){
                        autreCibleElement.push($rootScope.programmeSelect.elements[i].element);
                        trouve = true;
                        break;
                    }
                }
            }

            if(!trouve){
                for(var l=0;l<groupeTable.length;l++){
                    if(groupeTable[l] == $rootScope.programmeSelect.elements[i].element.code){
                        groupeElement.push($rootScope.programmeSelect.elements[i].element);
                        trouve = true;
                        break;
                    }
                }
            }


        }
        //console.log("gestElements() $rootScope.elementProgramme = ", $rootScope.elementProgramme);
        /*console.log("gestElements() groupeElement", groupeElement);
        console.log("gestElements() sessionElement", sessionElement);
        console.log("gestElements() benefElement", benefElement);
        console.log("gestElements() autreCibleElement", autreCibleElement);*/

    }

    function collecteSessionData(value) {
        console.log("collecteSessionData()");
        sessionValue = [];
        $scope.sessionData = [];
        for(var i = 0,j=sessionElement.length;i<j;i++){
          //console.log("sessionElement = ",sessionElement);
            var l=0;
            while(l<value.length){
                if(sessionElement[i].id == value[l].element && value[l].value){
                    var tmp = {};
                    var numer = value[l].numero;
                    tmp.numero = numer;
                    if(sessionElement[i].ensembleOption != null && sessionElement[i].code != "themeAborde"){
                        tmp[sessionElement[i].code] = {};
                        tmp[sessionElement[i].code] =  chargeOptionValue(sessionElement[i].ensembleOption.options,value[l].value);
                        tmp[sessionElement[i].code]["ancVal"] = value[l];
                        sessionValue.push(value[l]);
                        if(sessionElement[i].code == "materielQuantite"){
                            tmp[sessionElement[i].code] = chargeQuantite(tmp[sessionElement[i].code]);
                        }
                    }else{
                        var tp = {};
                        tp.ancVal = {};
                        tp.ancVal = value[l];
                        sessionValue.push(value[l]);
                        tp.value = value[l].value;
                        tmp[sessionElement[i].code] = tp;
                        //tmp[sessionElement[i].code] = instanceValue[l].value;
                    }
                    $scope.sessionData = eachPosition(tmp,$scope.sessionData);
                    value.splice(l,1);
                    l--;
                }
                l++;
            }
        }

        console.log("collecteSessionData() $scope.sessionData = ",$scope.sessionData);
        //console.log("collecteSessionData() $scope.groupe = ",$scope.groupe);
        console.log("collecteSessionData() sessionValue = ",angular.copy(sessionValue));
        return value;
    }

    function eachPosition(objet, tableau) {
        //console.log("eachPosition()");
        //console.log("eachPosition() > objet = ",objet," / tableau = ",tableau);
        var leng = tableau.length;
        if(leng == 0){
            tableau.push(objet);
            return tableau;
        }

        var i = 0;
        while(i<leng){
            if(objet.numero == tableau[i].numero){
                var tmp = {};
                tmp = tableau[i];
                delete objet.numero;
                for (var prop in objet) {
                    tmp[prop] = objet[prop];
                }
                tableau[i] = tmp;
                return tableau;
            }
            if(objet.numero < tableau[i].numero){
                tableau.splice(i,0,objet);
                return tableau;
            }
            i++;
        }
        tableau.push(objet);
        //console.log("eachPosition tableau = ",tableau);
        return tableau;
    }

    function collecteBenefData(value) {
        $scope.ListBenef = [];
        benefValue = [];
        listeBenefInitial = [];
        for(var i = 0,j=benefElement.length;i<j;i++){
            var l=0;
            while(l<value.length){
                if(benefElement[i].id == value[l].element){
                    var tmp = {};
                    var numer = value[l].numero;
                    tmp.numero = numer;
                    benefValue.push(value[l]);
                    if(benefElement[i].ensembleOption != null){
                        if("totalBenefSession" == benefElement[i].code){
                            tmp[benefElement[i].code] = [];
                            tmp[benefElement[i].code] = separeSpaceParseInt(value[l].value);
                        }else{
                            tmp[benefElement[i].code] = {};
                            tmp[benefElement[i].code] =  chargeOptionSet(benefElement[i].ensembleOption.options,value[l].value);
                        }

                    }else{
                        //console.log("collecteBenefData() pas de ensembleOption");
                        if("totalBenefAyantTernime" == benefElement[i].code){
                            tmp[benefElement[i].code] = parseInt(value[l].value, 10);
                        }else if("benefAyantTernime" == benefElement[i].code){
                            tmp[benefElement[i].code] = parseBoolean(value[l].value)
                        }else{
                            var tp = {};
                            tp.ancVal = {};
                            tp.ancVal = value[l];
                            tp.value = value[l].value;
                            tmp[benefElement[i].code] = tp;
                            if("id_dreams" == benefElement[i].code){
                              listeBenefInitial.push(value[l]);
                            }
                        }
                    }
                    if("totalBenefSession" != benefElement[i].code && "totalBenefAyantTernime" != benefElement[i].code){
                        $scope.ListBenef = eachPosition(tmp,$scope.ListBenef);
                    }else{
                        $scope.TotalBenef[benefElement[i].code] = tmp;
                    }
                    value.splice(l,1);
                    l--;
                }
                l++;
            }
        }
        /*console.log("collecteBenefData() CYRILEE::::::::");
        console.log("collecteBenefData() listeBenefInitial = ",angular.copy(listeBenefInitial));
        console.log("collecteBenefData() $scope.ListBenef = ",angular.copy($scope.ListBenef));
        console.log("collecteBenefData() $scope.ListBenef = ",angular.copy(benefValue));*/
        return value;
    }

    function chargeOptionSet(option,value) {
       // console.log("chargeOptionSet() > option = ",option," // value = ",value);
        var tmpIndex = {};var listOption = [];
        listOption = separeSpace(value);
        for(var i = 0;i<option.length;i++){
            var trouv = false;
            for(var j = 0; j < listOption.length;j++){
                if(option[i].code == listOption[j]){
                    tmpIndex[option[i].code] = true;
                    trouv = true;
                    break;
                }
            }
            if(!trouv){
                tmpIndex[option[i].code] = false;
            }
        }
        //console.log("chargeOptionSet() > tmpIndex = ",tmpIndex);
        return tmpIndex;
    }

    function chargeOptionName(option,value) {
        //console.log("chargeOptionName() > option = ",option," // value = ",value);
        var tmp = {};
        if(!value){
            return tmp;
        }
        for(var i = 0;i<option.length;i++){
            if(option[i].code == value){
                tmp.value = option[i].name;
                break;
            }
        }
        tmp.etat = chargeOptionSet(option,value);
        //console.log("chargeOptionSet() > tmpIndex = ",tmpIndex);
        return tmp;
    }

    function collecteAutreCibleData(value) {
        console.log("collecteAutreCibleData()");
        $scope.ListAutreCible = [];
        $scope.TotalAutreCible = {};
        autreValue = [];
        listeAutreBenefCodeInitial = [];
        for(var i = 0,j=autreCibleElement.length;i<j;i++){
            var l=0;
            while(l<value.length){
                if(autreCibleElement[i].id == value[l].element){
                    var tmp = {};
                    var numer = value[l].numero;
                    tmp.numero = numer;
                    autreValue.push(value[l]);
                    if(autreCibleElement[i].ensembleOption != null){
                        //console.log("collecteAutreCibleData() ensembleOption existe");
                        if("totalAutreCibleSession" == autreCibleElement[i].code){
                            tmp[autreCibleElement[i].code] = [];
                            tmp[autreCibleElement[i].code] = separeSpaceParseInt(value[l].value);
                        }else if("sexeAutreCible" == autreCibleElement[i].code){
                            tmp[autreCibleElement[i].code] = {};
                            tmp[autreCibleElement[i].code] = chargeOptionName(autreCibleElement[i].ensembleOption.options,value[l].value);
                        }else{
                            tmp[autreCibleElement[i].code] = {};
                            tmp[autreCibleElement[i].code] =  chargeOptionSet(autreCibleElement[i].ensembleOption.options,value[l].value);
                        }

                    }else{
                        //console.log("collecteAutreCibleData() pas de ensembleOption");
                        if("totalAutreCibleAyantTernimeMasc" == autreCibleElement[i].code || "totalAutreCibleAyantTernimeFem" == autreCibleElement[i].code){
                            tmp[autreCibleElement[i].code] = parseInt(value[l].value, 10);
                        }else if("autreCibleAyantTernimeMasc" == autreCibleElement[i].code || "autreCibleAyantTernimeFem" == autreCibleElement[i].code){
                            tmp[autreCibleElement[i].code] = parseBoolean(value[l].value)
                        }else{
                            var tp = {};
                            tp.ancVal = {};
                            tp.ancVal = value[l];
                            tp.value = value[l].value;
                            tmp[autreCibleElement[i].code] = tp;
                            if("codeBenef" == autreCibleElement[i].code){
                              listeAutreBenefCodeInitial.push(value[l]);
                            }
                        }
                    }
                    if("totalAutreCibleSession" != autreCibleElement[i].code && "totalAutreCibleAyantTernimeMasc" != autreCibleElement[i].code && "totalAutreCibleAyantTernimeFem" != autreCibleElement[i].code){
                        $scope.ListAutreCible = eachPosition(tmp,$scope.ListAutreCible);
                    }else{
                        $scope.TotalAutreCible[autreCibleElement[i].code] = tmp;
                    }
                    value.splice(l,1);
                    l--;
                }
                l++;
            }
        }
        console.log("collecteBenefData() $scope.ListAutreCible = ",$scope.ListAutreCible," // $scope.TotalAutreCible = ",$scope.TotalAutreCible);
        /*console.log("collecteBenefData() CYRILEE::::::::");
        console.log("collecteBenefData() listeAutreBenefCodeInitial = ",angular.copy(listeBenefInitial));
        console.log("collecteBenefData() $scope.ListAutreCible = ",angular.copy($scope.ListAutreCible));
        console.log("collecteBenefData() autreValue = ",angular.copy(autreValue));*/
        return value;
    }

    function collecteGroupeData(value) {
        //console.log("collecteGroupeData() groupeElement = ",angular.copy(groupeElement)," // instanceValue = ",angular.copy(instanceValue));
        $scope.groupe = {};
        for(var i = 0,j=groupeElement.length;i<j;i++){
            var l=0;
            while(l<value.length){
                if(groupeElement[i].id == value[l].element){
                    //console.log("collecteGroupeData() code = ",groupeElement[i].code," //instanceValue[l] = ",angular.copy(instanceValue[l]));
                    $scope.groupe[groupeElement[i].code] = value[l].value;
                    //$scope.groupe.numero = instanceValue[l].numero;
                    value.splice(l,1);
                    l--;
                    break;
                }
                l++;
            }
        }
      //  console.log("collecteGroupeData() $scope.groupe = ",angular.copy($scope.groupe));
        return value;
    }

    function chargeOption(ensemble) {
        //console.log("entrer dans chargeOption > ensemble = ",ensemble);
        for(var i=0, ij = $rootScope.ensembleOption.length;i<ij;i++){
            if(ensemble.id == $rootScope.ensembleOption[i].id){
                ensemble.options = $rootScope.ensembleOption[i].options;
                return ensemble;
            }
        }
    }

    function chargeOptionValue(option,data) {
        //console.log("entrer dans chargeOptionValue");
        //console.log("chargeOptionValue() > option = ",option," || data = ",data);
        var tmp = {};
        tmp.origValue = [];
        tmp.origValue = separeSpace(data);
            tmp.value = [];
        ////console.log("chargeOptionValue() > origValue = ",origValue);
        tmp.value = getValue(option,tmp.origValue);
        //console.log("chargeOptionValue() > tmp = ",tmp);
        return tmp;

    }

    function separeSpace(data) {
        var lesData = [];
        if(!data){return lesData;}
        var conti = true;
        var space = -2,initi = 0;
        while (conti){
            space = data.indexOf(" ",initi);
            if(space != -1){
                lesData.push(data.substring(initi, space));
                initi = space+1;
            }else{
                lesData.push(data.substring(initi, data.length));
                conti = false;
            }
        }
        return lesData;
    }

    function separeSpaceParseInt(data) {
        var lesData = [];
        if(!data){return lesData;}
        var conti = true;
        var space = -2,initi = 0;
        while (conti){
            space = data.indexOf(" ",initi);
            if(space != -1){
                lesData.push(parseInt(data.substring(initi, space), 10));
                initi = space+1;
            }else{
                lesData.push(parseInt(data.substring(initi, data.length), 10));
                conti = false;
            }
        }
        return lesData;
    }

    function parseBoolean(data) {
        if(data == 'true')
            return true;
        if(data == 'false')
            return false;
        return null;
    }

    function getValue(option,origValue) {
        //console.log("entrer dans getValue");
        //console.log("getValue() > option = ",option," origValue = ",origValue);
        var value = [];
        for(var i = 0;i<origValue.length;i++){
            var trouv = false;
            for(var k=0;k<option.length;k++){
                if(option[k].code == origValue[i]){
                    trouv = true;
                    value.push(option[k].name);
                    break;
                }
            }
            if(!trouv){
                value.push(origValue[i]);
            }

        }
        return value;
    }

    function chargeQuantite(tmp) {
        //console.log("chargeQuantite() enter tmp = ",angular.copy(tmp));
        var a = 0; var tab = [];
        while(a < tmp.value.length){
            var val = "";
            val = tmp.value[a]+" "+tmp.value[a + 1];
            tab.push(val);
            a += 2;
        }
        tmp.value = tab;
        //console.log("chargeQuantite() sortie tmp = ",angular.copy(tmp));
        return tmp;
    }

    $scope.MQfocus = function () {
        //console.log("MQfocus()");
        $(function () {
            // wait till load event fires so all resources are available
            $('#modalLoginForm').modal('show');
        });
    };

    $scope.outilfocus = function () {
        //console.log("outilfocus()");
        $(function () {
            // wait till load event fires so all resources are available
            $('#modalOutil').modal('show');
        });
    };

    $scope.methodfocus = function () {
        //console.log("methodfocus()");
        $(function () {
            // wait till load event fires so all resources are available
            $('#modalMethod').modal('show');
        });
    };

    $scope.saveOutils = function () {
        //console.log("saveOutils () $scope.outils",$scope.outil);
        //console.log("saveOutils () sessionElement",sessionElement);
        var options = [];
        options = getOption(sessionElement,"outils");
        $scope.groupeSession.outils = "";
        $scope.outilsInfo = "";
        for (var prop in $scope.outil) {
            if($scope.outil[prop]){
                if($scope.groupeSession.outils == ""){
                    $scope.groupeSession.outils = prop;
                    $scope.outilsInfo = getOptionValue(options,prop);
                }
                else{
                    $scope.groupeSession.outils = $scope.groupeSession.outils +" "+prop;
                    $scope.outilsInfo = $scope.outilsInfo+"; "+getOptionValue(options,prop);
                }
            }
        }
    };

    function getOptionValue(option,value) {
        //console.log("entrer dans getOptionValue");
            for(var k=0;k<option.length;k++){
                if(option[k].code == value){
                    return option[k].name;
                }
        }
        return value;
    }

    function getOption(element,code) {
        //console.log("entrer dans getOptionValue");
        for(var k=0;k<element.length;k++){
            if(element[k].code == code){
                return element[k].ensembleOption.options;
            }
        }
        return value;
    }

    $scope.saveMethode = function () {
        //console.log("saveMethode () $scope.method",$scope.method);
        //console.log("saveMethode () sessionElement",sessionElement);
        var options = [];
        options = getOption(sessionElement,"methodologie");
        $scope.groupeSession.methodologie = "";
        $scope.methodInfo = "";
        for (var prop in $scope.method) {
            if($scope.method[prop]){
                if($scope.groupeSession.methodologie == ""){
                    $scope.groupeSession.methodologie = prop;
                    $scope.methodInfo = getOptionValue(options,prop);
                }
                else{
                    $scope.groupeSession.methodologie = $scope.groupeSession.methodologie +" "+prop;
                    $scope.methodInfo = $scope.methodInfo+"; "+getOptionValue(options,prop);
                }
            }
        }
    };

    $scope.modifierSession = function (session) {
        //console.log("modifierSession() > session = ",session);
        iniSession();
        modifSession = session;
        $scope.groupeSession.dateSession = session.dateSession.value;
         $scope.groupeSession.themeAborde = session.themeAborde.value;
        MethoModif(session.methodologie.origValue);
        if(session.outils){
            OutilsModif(session.outils.origValue);
        }
        if(session.materielQuantite){
            MaterielQuantiteModif(session.materielQuantite.origValue);
        }

         /*$scope.groupeSession.methodologie = [];
         $scope.groupeSession.methodologie = session.methodologie.origValue;
         $scope.groupeSession.outils = [];
         $scope.groupeSession.outils = session.outils.origValue;*/
    };

    function MethoModif(origine) {
        $scope.method = {};
        for(var i=0;i<origine.length;i++){
            $scope.method[origine[i]] = true;
        }
        $scope.saveMethode();
    }

    function OutilsModif(origine) {
        $scope.outil = {};
        for(var i=0;i<origine.length;i++){
            $scope.outil[origine[i]] = true;
        }
        $scope.saveOutils();
    }

    function MaterielQuantiteModif(origine) {
        $scope.materiel = {};
        for(var i=0;i<origine.length;i+=2){
            //console.log('i = ',i,"origine.length = ",origine.length);
            $scope.materiel[origine[i+1]] = true;
            $scope[origine[i+1]] = origine[i];
            materielQte[origine[i+1]] = origine[i];
        }
        $scope.saveMaterial();
    }

    function isEmpty(obj) {
        for(var prop in obj) {
            if(obj.hasOwnProperty(prop))
                return false;
        }
        return true;
    }

    $scope.saveBenef = function () {
        /*console.log("saveBenef()");
        console.log("saveBenef() groupeBenef = ",$scope.groupeBenef);
        console.log("saveBenef() benefElement = ",benefElement);*/
        console.log("saveBenef() benefValue = ",angular.copy(benefValue));
        //if(checkbenef() && dataInstance.instance){
        if(checkbenef()){
            //origin = "benef";
            //console.log("saveBenef() $scope.sessionData = ",$scope.sessionData);
            //dataInstance.dreamsId = $scope.groupeBenef.id_dreams;
            listeBeneficiaire.push(benefSelected);
            dataInstance.dateActivite = $scope.sessionData[0].dateSession.value;
            dataInstance.order = 1;
            var code = Object.keys($scope.groupeBenef);
            dataInstance.dataValue = [];
            for(var pop in $scope.groupeBenef){
                if(!benefValue[pop]){
                    benefValue[pop] = getBenfElementId(pop);
                }
                var data = {};
                //data.numero = modifSession.numero;
                data.value = $scope.groupeBenef[pop];
                data.element = benefValue[pop];
                dataInstance.dataValue.push(data);
            }
            if(dataInstance.dataValue.length != 0){
                //console.log("saveBenef() dataInstance = ",dataInstance);
                addListeBenef(dataInstance.dataValue);
              //  saveData();
            }
        }
        initBenef();
    };

    function addListeBenef(value) {
      //console.log("addListeBenef() value = ",angular.copy(value)," // benefValue = ",angular.copy(benefValue));
      for(var i = 0;i<value.length;i++){
          value[i].numero = $scope.ListBenef.length +1;
          benefValue.push(value[i]);
      }
      //console.log("addListeBenef() benefValue = ",angular.copy(benefValue));
      collecteBenefData(benefValue);
    }

    function checkbenef(benef) {
        if(!$scope.groupeBenef.id_dreams || isExiste($scope.groupeBenef.id_dreams)){
            return false;
        }
        if(!$scope.groupeBenef.nomPrenomBenef){
            return false;
        }
        if(!$scope.groupeBenef.contactBenef){
            $scope.groupeBenef.contactBenef = null;
        }
        $scope.groupeBenef.benefSession = null;
        $scope.groupeBenef.benefAyantTernime = false;

        if($scope.ListBenef.length == 0){
            $scope.groupeBenef.totalBenefSession = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
            $scope.groupeBenef.totalBenefAyantTernime = 0;
        }
        console.log("checkbenef() > $scope.groupeBenef = ",$scope.groupeBenef);
        return true;
    }

    function initBenef() {
        $scope.groupeBenef = {}
    }

    function isExiste(benefID) {
        for(var i = 0,j=listeBeneficiaire.length;i<j;i++){
            if(benefID == listeBeneficiaire[i].id_dreams){
                return true;
            }
        }
        return false;
    }

    function getBenfElementId(code) {
        //console.log("getElementId() code = ",code," // groupeElement = ",groupeElement);
        for(var j=0;j<benefElement.length;j++){
            if(code == benefElement[j].code){
                return benefElement[j].id;
            }
        }
    }

    function getAutreCibleElementId(code) {
        //console.log("getElementId() code = ",code," // groupeElement = ",groupeElement);
        for(var j=0;j<autreCibleElement.length;j++){
            if(code == autreCibleElement[j].code){
                return autreCibleElement[j].id;
            }
        }
    }

    $scope.changeSessionBenef = function (benef,key,value,index) {
        //console.log("entrer dans changeSessionBenef() > benef= ",angular.copy(benef),"// key = ",key," // value = ",value," // index = ",index);

        //console.log("changeSessionBenef() > benef= ",angular.copy(benef));
        //console.log("changeSessionBenef() > $scope.ListBenef= ",angular.copy($scope.ListBenef)," // $scope.TotalBenef = ",$scope.TotalBenef);
        //if(dataInstance.instance && index < $scope.sessionData.length){
        if(index < $scope.sessionData.length){
          benef.benefSession[key] = value;
            origin = "benef";
          //  console.log("changeSessionBenef() $scope.sessionData = ",angular.copy($scope.sessionData));
            dataInstance.dreamsId = benef.id_dreams.value;
            dataInstance.dateActivite = $scope.sessionData[index].dateSession.value;
            dataInstance.order = index + 1;
            dataInstance.codeId = null;
            /*if(value){
                dataInstance.organisation = "ajouter";
            }else{
                dataInstance.organisation = "delete";
            }*/
            dataInstance.dataValue = [];
            var benefSes = "";
            for(var pop in benef.benefSession){
                if(benef.benefSession[pop]){
                    if(benefSes == ""){
                        benefSes = pop;
                    }else{
                        benefSes += " "+ pop;
                    }
                }
            }
            var data = {};
            data.numero = benef.numero;
            data.value = benefSes;
            data.element = getBenfElementId("benefSession");
            dataInstance.dataValue.push(data);

            if(value){
                $scope.TotalBenef.totalBenefSession.totalBenefSession[index] += 1;
            }else{
                $scope.TotalBenef.totalBenefSession.totalBenefSession[index] -= 1;
            }
            var totalBenefSes = "";
            for(var i =0;i<$scope.TotalBenef.totalBenefSession.totalBenefSession.length;i++){
                if(totalBenefSes == ""){
                    totalBenefSes = ""+$scope.TotalBenef.totalBenefSession.totalBenefSession[i];
                }else{
                    totalBenefSes += " "+ $scope.TotalBenef.totalBenefSession.totalBenefSession[i];
                }
            }
            var data = {};
            data.numero = $scope.TotalBenef.totalBenefSession.numero;
            data.value = totalBenefSes;
            data.element = getBenfElementId("totalBenefSession");
            dataInstance.dataValue.push(data);

            if(dataInstance.dataValue.length != 0){
                //console.log("changeSessionBenef() dataInstance = ",angular.copy(dataInstance));
                ajustebenefSession(dataInstance.dataValue);
                //saveData();
            }
        }
    };

    function ajustebenefSession(value) {
      console.log("ajustebenefSession()");
      /*console.log("ajustebenefSession() benefValue = ",angular.copy(benefValue));
      console.log("ajustebenefSession() value = ",angular.copy(value));*/

      for(var i = 0;i<value.length;i++){
        for(var j = 0;j<benefValue.length;j++){
          if(value[i].element == benefValue[j].element && value[i].numero == benefValue[j].numero){
            //console.log("value = ",angular.copy(value[i]),"benefValue = ",angular.copy(benefValue[j]));
            benefValue[j].value = value[i].value;
            break;
          }
        }
      }
      //console.log("ajustebenefSession() fin benefValue = ",angular.copy(benefValue));
      //collecteBenefData(benefValue);
    }

    $scope.changeSessionBenefTermine = function (benef,value) {
      console.log("entrer dans changeSessionBenefTermine() > benef= ",angular.copy(benef)," // value = ",value);
        //if(dataInstance.instance){
            origin = "benef";
            dataInstance.dataValue = [];
            var data = {};
            data.numero = benef.numero;
            data.value = ""+benef.benefAyantTernime;
            data.element = getBenfElementId("benefAyantTernime");
            dataInstance.dataValue.push(data);

            //dataInstance.dreamsId = benef.id_dreams.value;
            dataInstance.dateActivite = $scope.sessionData[0].dateSession.value;
            dataInstance.order = 1;
            dataInstance.codeId = data.element;

          /*  if(value){
                dataInstance.organisation = "ajouter";
            }else{
                dataInstance.organisation = "delete";
            }*/

            if(value){
                $scope.TotalBenef.totalBenefAyantTernime.totalBenefAyantTernime += 1;
            }else{
                $scope.TotalBenef.totalBenefAyantTernime.totalBenefAyantTernime -= 1;
            }
           var data = {};
            data.numero = $scope.TotalBenef.totalBenefAyantTernime.numero;
            data.value = $scope.TotalBenef.totalBenefAyantTernime.totalBenefAyantTernime;
            data.element = getBenfElementId("totalBenefAyantTernime");
            dataInstance.dataValue.push(data);

            if(dataInstance.dataValue.length != 0){
                console.log("changeSessionBenef() dataInstance = ",dataInstance);
                ajustebenefSession(dataInstance.dataValue);
              //  saveData();
            }
      //  }
    };

    $scope.searchCode = function (valeur) {
        //console.log("searchCode valeur = ",valeur);
        if(valeur.length > 2){
          //  console.log("searchId valeur = ",valeur);
            var org = "organisation="+dataInstanceEntete.organisation;
            var value = "idDreams="+valeur;
            var datavalueSearch = searchBeneficiaire+"?"+org+"&"+value;
            $http.get(datavalueSearch).then(function (response) {
                console.log("searchId() response",response);
                autocompleteData = response.data;
                chargeDataCode();
            }, function (err) {
                console.log("searchId() err",err);
            });
        }

    };

    function chargeDataCode() {
        $scope.valueCompleteCode = [];
        $scope.valueCompleteCode = [];
        for(var i = 0, j=autocompleteData.length;i<j;i++){
            $scope.valueCompleteCode.push(autocompleteData[i].id_dreams);
        }
    }

    $scope.choisirCode=function(string){
        $scope.groupeAutreCible.codeBenef=string;
        $scope.valueCompleteCode=null;
    };

    $scope.saveAutreCible = function () {
        console.log("saveAutreCible()");
        /*console.log("saveAutreCible() groupeAutreCible = ",$scope.groupeAutreCible);
        console.log("saveAutreCible() autreCibleElement = ",autreCibleElement);
        console.log("saveAutreCible() dataInstance = ",dataInstance);*/
        //if(checkAutreCible() && dataInstance.instance){
        if(checkAutreCible()){
            //origin = "autreCible";
            /*dataInstance.dreamsId = $scope.groupeAutreCible.codeBenef;
            dataInstance.dateActivite = $scope.sessionData[0].dateSession.value;
            dataInstance.order = 1;*/
            dataInstance.dataValue = [];
            ajouteBenefAutreCible(autreBenefCodeSelected);
            for(var pop in $scope.groupeAutreCible){
                if(!autreCibleValue[pop]){
                    autreCibleValue[pop] = getAutreCibleElementId(pop);
                }
                var data = {};
                //data.numero = modifSession.numero;
                data.value = $scope.groupeAutreCible[pop];
                data.element = autreCibleValue[pop];
                dataInstance.dataValue.push(data);
            }
            if(dataInstance.dataValue.length != 0){
                console.log("saveAutreCible() dataInstance = ",dataInstance);
                  addListeAutreCible(dataInstance.dataValue);
                //saveData();
            }
        }
        initAutreCible();
    };

    function ajouteBenefAutreCible(benef) {
      console.log("ajouteBenefAutreCible() debut listeAutreBenefCode = ",angular.copy(listeAutreBenefCode));
      for(var i = 0,j=listeAutreBenefCode.length;i<j;i++){
          if(benef.id_dreams == listeAutreBenefCode[i].id_dreams){
              return;
          }
      }
      listeAutreBenefCode.push(benef);
      console.log("ajouteBenefAutreCible() fin listeAutreBenefCode = ",angular.copy(listeAutreBenefCode));
    }

    function addListeAutreCible(value) {
      //console.log("addListeBenef() value = ",angular.copy(value)," // benefValue = ",angular.copy(benefValue));
      for(var i = 0;i<value.length;i++){
          value[i].numero = $scope.ListAutreCible.length +1;
          autreValue.push(value[i]);
      }
      console.log("addListeAutreCible() autreValue = ",angular.copy(autreValue));
      collecteAutreCibleData(autreValue);
    }

    function checkAutreCible() {
        if(!$scope.groupeAutreCible.codeBenef || !isCodeExiste($scope.groupeAutreCible.codeBenef)){
            return false;
        }
        if(!$scope.groupeAutreCible.nomPrenomAutreCible){
            return false;
        }
        if(!$scope.groupeAutreCible.sexeAutreCible){
            return false;
        }
        if(!$scope.groupeAutreCible.contactAutreCible){
            $scope.groupeAutreCible.contactAutreCible = null;
        }


        $scope.groupeAutreCible.autreCibleSession = null;

        if($scope.groupeAutreCible.sexeAutreCible == "M"){
            $scope.groupeAutreCible.autreCibleAyantTernimeMasc = false;
            $scope.groupeAutreCible.autreCibleAyantTernimeFem = null;
        }else{
            $scope.groupeAutreCible.autreCibleAyantTernimeMasc = null;
            $scope.groupeAutreCible.autreCibleAyantTernimeFem = false;
        }

        if($scope.ListAutreCible.length == 0){
            $scope.groupeAutreCible.totalAutreCibleSession = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
            $scope.groupeAutreCible.totalAutreCibleAyantTernimeMasc = 0;
            $scope.groupeAutreCible.totalAutreCibleAyantTernimeFem = 0;
        }
        console.log("checkAutreCible() > $scope.groupeAutreCible = ",$scope.groupeAutreCible);
        return true;
    }

    function isCodeExiste(code) {
      //console.log("isCodeExiste() > autocompleteData = ",autocompleteData);
        for(var i = 0,j=autocompleteData.length;i<j;i++){
            if(code == autocompleteData[i].id_dreams){
              //console.log("isCodeExiste() > autocompleteData = ",autocompleteData[i]);
              autreBenefCodeSelected = autocompleteData[i];
                return true;
            }
        }
        return false;
    }

    function initAutreCible() {
        $scope.groupeAutreCible = {};
        $scope.groupeAutreCible.sexeAutreCible = "";
    }

    $scope.changeSessionAutre = function (autre,key,value,index) {
        console.log('changeSessionAutre() > autre = ',autre," // key = ",key," // value = ",value," // index= ",index);
        //console.log("entrer dans changeSessionBenef() > benef= ",angular.copy(benef),"// key = ",key," // value = ",value," // index = ",index);

        //if(dataInstance.instance && index < $scope.sessionData.length ){
        if(index < $scope.sessionData.length ){
          autre.autreCibleSession[key] = value;
            dataInstance.dataValue = [];
            var autreCibleSess = "";
            for(var pop in autre.autreCibleSession){
                if(autre.autreCibleSession[pop]){
                    if(autreCibleSess == ""){
                        autreCibleSess = pop;
                    }else{
                        autreCibleSess += " "+ pop;
                    }
                }
            }
            var data = {};
            data.numero = autre.numero;
            data.value = autreCibleSess;
            data.element = getAutreCibleElementId("autreCibleSession");
            dataInstance.dataValue.push(data);

            if(value){
                $scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[index] += 1;
            }else{
                $scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[index] -= 1;
            }
            var totalautreCibleSess = "";
            for(var i =0;i<$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession.length;i++){
                if(totalautreCibleSess == ""){
                    totalautreCibleSess = ""+$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[i];
                }else{
                    totalautreCibleSess += " "+$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[i];
                }
            }
            var data = {};
            data.numero = $scope.TotalAutreCible.totalAutreCibleSession.numero;
            data.value = totalautreCibleSess;
            data.element = getAutreCibleElementId("totalAutreCibleSession");
            dataInstance.dataValue.push(data);



            if(dataInstance.dataValue.length != 0){
                console.log("changeSessionBenef() dataInstance = ",dataInstance);
                ajusteSessionAutre(dataInstance.dataValue);
              //  saveData();
            }
        }

    };

    function ajusteSessionAutre(value) {
      console.log("ajusteSessionAutre()");
      /*console.log("ajustebenefSession() benefValue = ",angular.copy(benefValue));
      console.log("ajustebenefSession() value = ",angular.copy(value));*/

      for(var i = 0;i<value.length;i++){
        for(var j = 0;j<autreValue.length;j++){
          if(value[i].element == autreValue[j].element && value[i].numero == autreValue[j].numero){
            console.log("value = ",angular.copy(value[i]),"autreValue = ",angular.copy(autreValue[j]));
            autreValue[j].value = value[i].value;
            break;
          }
        }
      }
    console.log("ajusteSessionAutre() autreValue = ",angular.copy(autreValue));
      //collecteBenefData(benefValue);
    }

    $scope.changeSessionAutreTermineMasc = function (autre, value) {
        //console.log("changeSessionAutreTermineMasc() autre = ",autre," // value = ",value," // $scope.TotalAutreCible= ",$scope.TotalAutreCible);
      //  if(dataInstance.instance){
            origin = "autreCible";
            dataInstance.dataValue = [];
            var data = {};
            data.numero = autre.numero;
            data.value = ""+autre.autreCibleAyantTernimeMasc;
            data.element = getAutreCibleElementId("autreCibleAyantTernimeMasc");
            dataInstance.dataValue.push(data);

            if(value){
                $scope.TotalAutreCible.totalAutreCibleAyantTernimeMasc.totalAutreCibleAyantTernimeMasc += 1;
            }else{
                $scope.TotalAutreCible.totalAutreCibleAyantTernimeMasc.totalAutreCibleAyantTernimeMasc -= 1;
            }
            var data = {};
            data.numero = $scope.TotalAutreCible.totalAutreCibleAyantTernimeMasc.numero;
            data.value = $scope.TotalAutreCible.totalAutreCibleAyantTernimeMasc.totalAutreCibleAyantTernimeMasc;
            data.element = getAutreCibleElementId("totalAutreCibleAyantTernimeMasc");
            dataInstance.dataValue.push(data);

            if(dataInstance.dataValue.length != 0){
                //console.log("changeSessionBenef() dataInstance = ",dataInstance);
                ajusteSessionAutre(dataInstance.dataValue);
              //  saveData();
            }
        //}
    };

    $scope.changeSessionAutreTermineFem = function (autre, value) {
        //console.log("changeSessionAutreTermineMasc() autre = ",autre," // value = ",value," // $scope.TotalAutreCible= ",$scope.TotalAutreCible);
      //  if(dataInstance.instance){
            origin = "autreCible";
            dataInstance.dataValue = [];
            var data = {};
            data.numero = autre.numero;
            data.value = ""+autre.autreCibleAyantTernimeFem;
            data.element = getAutreCibleElementId("autreCibleAyantTernimeFem");
            dataInstance.dataValue.push(data);

            if(value){
                $scope.TotalAutreCible.totalAutreCibleAyantTernimeFem.totalAutreCibleAyantTernimeFem += 1;
            }else{
                $scope.TotalAutreCible.totalAutreCibleAyantTernimeFem.totalAutreCibleAyantTernimeFem -= 1;
            }
            var data = {};
            data.numero = $scope.TotalAutreCible.totalAutreCibleAyantTernimeFem.numero;
            data.value = $scope.TotalAutreCible.totalAutreCibleAyantTernimeFem.totalAutreCibleAyantTernimeFem;
            data.element = getAutreCibleElementId("totalAutreCibleAyantTernimeFem");
            dataInstance.dataValue.push(data);

            if(dataInstance.dataValue.length != 0){
                //console.log("changeSessionBenef() dataInstance = ",dataInstance);
                ajusteSessionAutre(dataInstance.dataValue);
                //saveData();
            }
      //  }
    }

    $scope.saveActiviteGroupe = function () {
      console.log("entrer dans saveActiviteGroupe()");
      dataInstance.dateActivite = $scope.sessionData[0].dateSession.value;
      dataInstance.dreamsId = [];
      dataInstance.order = 1;
      dataInstance.codeId = null;
      dataInstance.dataValue = [];

      dataInstance.dreamsId = idDreamsCollecte();

      console.log("$scope.sessionData = ",angular.copy($scope.sessionData));
      console.log("saveActiviteGroupe, sessionValue = ",angular.copy(sessionValue));
      console.log("saveActiviteGroupe, benefValue = ",angular.copy(benefValue));
      console.log("saveActiviteGroupe, autreValue = ",angular.copy(autreValue));
      collecteData = [];
      compileGroupe();
      compileData(sessionValue);
      compileData(benefValue);
      compileData(autreValue);

      /*console.log("saveActiviteGroupe, collecteData = ",angular.copy(collecteData));
      console.log("saveActiviteGroupe, dataInstance = ",angular.copy(dataInstance));*/
      dataInstance.dataValue = collecteData;
      saveData();


    }

    function compileGroupe() {
      console.log("$scope.groupe = ",$scope.groupe);
      for (var prop in $scope.groupe) {
        //element[prop] = tmp[prop];
        if($scope.groupe[prop]){
          var data = {};
          data.element = getoneElementid(prop);
          data.numero = 1;
          data.value = $scope.groupe[prop];
          collecteData.push(data);
        }
      }
      console.log("$scope.groupeSession = ",angular.copy(collecteData));
    }

    function compileData(data) {
      console.log("data = ",angular.copy(data));
      for(var j = 0;j<data.length;j++){
        collecteData.push(data[j]);
      }

    }

    function getoneElementid(valeur) {

      for(var j = 0;j<$rootScope.programmeSelect.elements.length;j++){
        if($rootScope.programmeSelect.elements[j].element.code == valeur){
          return $rootScope.programmeSelect.elements[j].element.id;
        }
      }
      return null;

    }

    function checkBenefListe() {
      if (!dataInstanceEntete.instance) {
        return;
      }
      var valuesUrl = searchBeneficiaire+"/instance/"+dataInstanceEntete.instance;
      $http.get(valuesUrl).then(function (response) {
          console.log("checkBenefListe() response = ", response);
          checkBenefExistInList(response.data);
      }, function (err) {
          console.log(err);
      });
    }

    function getAllBenefListe(tmpBenef, origin) {
      var temps = [];
      if (tmpBenef.length == 0) {
        return;
      }else{
        for (var i = 0; i < tmpBenef.length; i++) {
          temps.push(tmpBenef[i].value);
        }
      }
      //console.log("getAllBenefListe() tmpBenef = ", tmpBenef," // temps = ",temps);
      var valuesUrl = searchBeneficiaire+"/iddreams?id="+temps;
      $http.get(valuesUrl).then(function (response) {
          console.log("getAllBenefListe() response = ", response);
          constiBenefList(response.data, origin, tmpBenef);
      }, function (err) {
          console.log(err);
      });
    }

    function checkBenefExistInList(benefRecu) {
    //  console.log("checkBenefExistInList() listeBenefInitial = ",angular.copy(listeBenefInitial)," // benefRecu = ",angular.copy(benefRecu));
      listeBenefInitial = deletebenefDoublon(listeBenefInitial);
      var tmpBenef = [];
      for(var i=0;i<listeBenefInitial.length;i++){
        var trouve = false;
        for(var j=0;j<benefRecu.length;j++){
          if(listeBenefInitial[i].value == benefRecu[j].id_dreams){
            listeBeneficiaire.push(benefRecu[j]);
            trouve = true;
            break;
          }
        }
        if (!trouve) {
          tmpBenef.push(listeBenefInitial[i]);
        }
      }
      console.log("checkBenefExistInList() listeBeneficiaire = ", listeBeneficiaire," // tmpBenef = ",tmpBenef);

      console.log("checkBenefExistInList() listeBenefInitial = ",angular.copy(listeBenefInitial)," // benefRecu = ",angular.copy(benefRecu));
      listeAutreBenefCodeInitial = deletebenefDoublon(listeAutreBenefCodeInitial);
      var tmpAutre = [];
      for(var i=0;i<listeAutreBenefCodeInitial.length;i++){
        var trouve = false;
        for(var j=0;j<benefRecu.length;j++){
          if(listeAutreBenefCodeInitial[i].value == benefRecu[j].id_dreams){
            listeAutreBenefCode.push(benefRecu[j]);
            trouve = true;
            break;
          }
        }
        if (!trouve) {
          tmpAutre.push(listeAutreBenefCodeInitial[i]);
        }
      }
      console.log("checkAutreCibleExistInList() listeAutreBenefCode = ", listeAutreBenefCode," // tmpAutre = ",tmpAutre);

      getAllBenefListe(tmpBenef, "benef");
      getAllBenefListe(tmpAutre, "autre");
    }

    function constiBenefList(benef, origin, benefOrigine) {
      console.log("constiBenefList() benef = ", benef," /// origin = ",origin," /// benefOrigine = ",benefOrigine);
      var inconnu = [];
      for (var j = 0; j < benefOrigine.length; j++) {
        var trouve = false;
        for (var i = 0; i < benef.length; i++) {
          if(benefOrigine[j].value == benef[i].id_dreams){
            trouve = true;
            if(origin == "benef"){
              listeBeneficiaire.push(benef[i]);
            }
            if(origin == "autre"){
              listeAutreBenefCode.push(benef[i]);
            }
          }
        }
        if (!trouve) {
          inconnu.push(benefOrigine[j]);
        }
      }
      if(inconnu.length != 0){
        if(origin == "benef"){

        }
        if(origin == "autre"){

        }
      }

    }

    function deletebenefDoublon(benef) {
      var pas = 0;
      var i = pas + 1;
      while (i<benef.length) {
        if (benef[i].value == benef[pas].value) {
          benef.splice(i,1);
          i--;
        }
        i++;
        if(i==benef.length){
          pas++;
          if(pas<benef.length){
            i = pas+1;
          }
        }
      }
      //console.log("deletebenefDoublon() out benef = ",angular.copy(benef));
      return benef;
    }

    $scope.deleteBeneficiaire = function (benef){
      console.log("deleteBeneficiaire, benef = ",angular.copy(benef));
      var befNom = "Nom: "+benef.nomPrenomBenef.value;
      var befCode = "Code: "+benef.id_dreams.value;
      var txt = "Attention, Vous êtes sur le point de supprimer\n"+befNom+"\n"+befCode;
      if (!confirm(txt)) {
        return;
      }
      var i = 0;
      for (var sess in benef.benefSession) {
        //console.log("deleteBeneficiaire, i = ",angular.copy(i));
        if(benef.benefSession[sess]){
          $scope.changeSessionBenef(benef,sess,false,i);
        }

        i++;
      }
      if(benef.benefAyantTernime){
        $scope.changeSessionBenefTermine(benef,false);
      }



      i = 0;
      var numero = benef.numero;
      while(i<benefValue.length){
        if(benefValue[i].numero == numero){
          benefValue.splice(i, 1);
          i--;
        }
        i++;
      }
      //console.log("deleteBeneficiaire, benefValue = ",angular.copy(benefValue));
      benefValue = ajusterNumero(numero, benefValue)
      console.log("deleteBeneficiaire, after benefValue = ",angular.copy(benefValue));
      collecteBenefData(benefValue);
    }

    function ajusterNumero(numero, value) {
      for(var i = 0;i<value.length;i++){
        if(value[i].numero > numero){
          value[i].numero--;
        }
      }
      return value;
    }

    $scope.deleteAutreCible = function (autre){
      //console.log("deleteAutreCible, autreValue = ",angular.copy(autreValue));
      console.log("deleteAutreCible, autre = ",angular.copy(autre));
      //$scope.changeSessionAutre = function (autre,key,value,index)

      var befNom = "Nom: "+autre.nomPrenomAutreCible.value;
      var befCode = "Code: "+autre.codeBenef.value;
      var txt = "Attention, Vous êtes sur le point de supprimer\n"+befNom+"\n"+befCode;
      if (!confirm(txt)) {
        return;
      }

      var i = 0;
      for (var sess in autre.autreCibleSession) {
        if(autre.autreCibleSession[sess]){
          $scope.changeSessionAutre(autre,sess,false,i);
        }

        i++;
      }

      if(autre.autreCibleAyantTernimeMasc){
        $scope.changeSessionAutreTermineMasc(autre,false);
      }
      if(autre.autreCibleAyantTernimeFem){
        $scope.changeSessionAutreTermineFem(autre,false);
      }


      i = 0;
      var numero = autre.numero;
      while(i<autreValue.length){
        if(autreValue[i].numero == numero){
          autreValue.splice(i, 1);
          i--;
        }
        i++;
      }
      //console.log("deleteBeneficiaire, autreValue = ",angular.copy(autreValue));
      autreValue = ajusterNumero(numero, autreValue);
      //console.log("deleteBeneficiaire, after autreValue = ",angular.copy(autreValue));
      collecteAutreCibleData(autreValue);
    }

    $scope.deleteSession = function (session){
      console.log("deleteSession() > session = ",angular.copy(session));
      //console.log("deleteSession() > sessionValue = ",angular.copy(sessionValue));
      //console.log("deleteSession() > $scope.ListBenef = ",angular.copy($scope.ListBenef));
      //console.log("deleteSession() > $scope.benef.benefSession = ",angular.copy($scope.benef.benefSession));
      //console.log("deleteSession() > $scope.TotalBenef = ",angular.copy($scope.TotalBenef));
      //$scope.TotalBenef.totalBenefSession.totalBenefSession
      //console.log("deleteSession() > $scope.ListAutreCible = ",angular.copy($scope.ListAutreCible));
      //console.log("deleteSession() > $scope.autre.autreCibleSession = ",angular.copy($scope.autre.autreCibleSession));
      //console.log("deleteSession() > $scope.TotalAutreCible = ",angular.copy($scope.TotalAutreCible));
      //$scope.TotalBenef.totalBenefSession.totalBenefSession
      var bef = "Session du "+session.dateSession.value;
      var txt = "Attention, Vous êtes sur le point de supprimer\n"+bef
      if (!confirm(txt)) {
        return;
      }

      var i = 0;
      var numero = session.numero;
      while(i<sessionValue.length){
        if(sessionValue[i].numero == numero){
          sessionValue.splice(i, 1);
          i--;
        }
        i++;
      }
      sessionValue = ajusterNumero(numero, sessionValue);
      collecteSessionData(sessionValue);

      // supprimer colonne de la session chez les bénéficiaires

    //console.log("deleteSession() > numero = ",angular.copy(numero));

      dataInstance.dataValue = [];
      for (var i = 0; i < $scope.ListBenef.length; i++) {
        var val = "";
        var deb = [];
        for(var pop in $scope.ListBenef[i].benefSession){
          deb.push($scope.ListBenef[i].benefSession[pop]);
        }
        deb.splice(numero-1,1);
        deb.push(false);
        var b = 0;
        for(var pop in $scope.ListBenef[i].benefSession){
          $scope.ListBenef[i].benefSession[pop] = deb[b];
          b++;
        }
        for(var pop in $scope.ListBenef[i].benefSession){
            if($scope.ListBenef[i].benefSession[pop]){
                if(val == ""){
                    val = pop;
                }else{
                    val += " "+ pop;
                }
            }
        }
        var data = {};
        data.numero = $scope.ListBenef[i].numero;
        data.value = val;
        data.element = getBenfElementId("benefSession");
        dataInstance.dataValue.push(data);
      }

      $scope.TotalBenef.totalBenefSession.totalBenefSession.splice(numero-1,1);
      $scope.TotalBenef.totalBenefSession.totalBenefSession.push(0);

      var totalBenefSes = "";
      for(var i =0;i<$scope.TotalBenef.totalBenefSession.totalBenefSession.length;i++){
          if(totalBenefSes == ""){
              totalBenefSes = ""+$scope.TotalBenef.totalBenefSession.totalBenefSession[i];
          }else{
              totalBenefSes += " "+ $scope.TotalBenef.totalBenefSession.totalBenefSession[i];
          }
      }
      var data = {};
      data.numero = $scope.TotalBenef.totalBenefSession.numero;
      data.value = totalBenefSes;
      data.element = getBenfElementId("totalBenefSession");
      dataInstance.dataValue.push(data);

      ajustebenefSession(dataInstance.dataValue);


      //Supprimer colonne de la session chez les autres cibles
      dataInstance.dataValue = [];
      for (var i = 0; i < $scope.ListAutreCible.length; i++) {
        var val = "";
        var deb = [];
        for(var pop in $scope.ListAutreCible[i].autreCibleSession){
          deb.push($scope.ListAutreCible[i].autreCibleSession[pop]);
        }
        deb.splice(numero-1,1);
        deb.push(false);
        var b = 0;
        for(var pop in $scope.ListAutreCible[i].autreCibleSession){
          $scope.ListAutreCible[i].autreCibleSession[pop] = deb[b];
          b++;
        }
        for(var pop in $scope.ListAutreCible[i].autreCibleSession){
            if($scope.ListAutreCible[i].autreCibleSession[pop]){
                if(val == ""){
                    val = pop;
                }else{
                    val += " "+ pop;
                }
            }
        }
        var data = {};
        data.numero = $scope.ListAutreCible[i].numero;
        data.value = val;
        data.element = getAutreCibleElementId("autreCibleSession");
        dataInstance.dataValue.push(data);
      }


      $scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession.splice(numero-1,1);
      $scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession.push(0);

      var totalautreCibleSess = "";
      for(var i =0;i<$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession.length;i++){
          if(totalautreCibleSess == ""){
              totalautreCibleSess = ""+$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[i];
          }else{
              totalautreCibleSess += " "+$scope.TotalAutreCible.totalAutreCibleSession.totalAutreCibleSession[i];
          }
      }
      var data = {};
      data.numero = $scope.TotalAutreCible.totalAutreCibleSession.numero;
      data.value = totalautreCibleSess;
      data.element = getAutreCibleElementId("totalAutreCibleSession");
      dataInstance.dataValue.push(data);
      ajusteSessionAutre(dataInstance.dataValue);

    }

    function idDreamsCollecte() {
      console.log("idDreamsCollecte() > $scope.ListBenef = ",angular.copy($scope.ListBenef));
      console.log("idDreamsCollecte() > $scope.ListAutreCible = ",angular.copy($scope.ListAutreCible));
      var listeBenefAutre = [];
      var trouve = false;
      for (var i = 0; i < $scope.ListBenef.length; i++) {
        trouve = false;
        for (var j = 0; j < listeBenefAutre.length; j++) {
          if(listeBenefAutre[j] == $scope.ListBenef[i].id_dreams.value){
          //  console.log("idDreamsCollecte() > listeBenefAutre existe = ",angular.copy($scope.ListBenef[i].id_dreams.value));
            trouve = true;
            break;
          }
        }
        if(!trouve){
          listeBenefAutre.push($scope.ListBenef[i].id_dreams.value);
        }
      }

      for (var i = 0; i < $scope.ListAutreCible.length; i++) {
        trouve = false;
        for (var j = 0; j < listeBenefAutre.length; j++) {
          if(listeBenefAutre[j] == $scope.ListAutreCible[i].id_dreams.value){
            console.log("idDreamsCollecte() > listeBenefAutre existe = ",angular.copy($scope.ListAutreCible[i].id_dreams.value));
            trouve = true;
            break;
          }
        }
        if(!trouve){
          listeBenefAutre.push($scope.ListAutreCible[i].id_dreams.value);
        }
      }

      console.log("idDreamsCollecte() > listeBenefAutre = ",angular.copy(listeBenefAutre));
      return listeBenefAutre;
    }


}]);
