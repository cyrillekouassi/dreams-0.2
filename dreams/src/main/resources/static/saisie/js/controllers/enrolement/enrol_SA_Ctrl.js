saisie.controller('enrolSActrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state', function ($scope, $rootScope, $stateParams, $http,$filter,$state) {
    console.log("entrer dans enrolSActrl");
    var ValueUrl = serverAdresse+'dataValue';
    var valueBeneficiaire = serverAdresse+'beneficiaire';
    var dataInstanceEntete = {};
    var dataInstance = {};
    $rootScope.dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
    $scope.enrolA = {};
    //$rootScope.enrolementData = [];
    //$rootScope.benefNewEnrolData = {};
    $rootScope.beneficiaireData = {};
    var centreSocial = {};
    var plateForme = {};
    var beneficiaire = {};
    $scope.chargeList = true;
    $scope.suivantDisabled = false;
    $scope.styleIdDreams = {};
    $scope.idDreamsInfo = "";
    var backgroundRed = {'background-color': 'red'};
    var colorRed = {'color': 'red'};
    var noStyle = {};
    //$scope.styleIdDreams = colorRed;

    var enrolSectionA = ["district","plateform_cod","centre_social","ong","codeSafespace","nom_enqueteur","num_fiche",
        "nom","pren","dat_nais","dat_enrol","age_enrol","porte_entre_bene","autre_porte_entre_bene","statut_mat_bene",
        "ocup","oc_aut","tel","region","departement","sousPrefect","village","repere","no_benef","id_dreams",
        "nom_pren_charg_benef","relation","tel_charg_benef","_01_b_num_doc","_01_doc_att_age","_01_a_type_doc",
        "_02_accord_parent","_03_acord_bene"];

    initial();
    initialDom();
    lancerAffichage();
    function lancerAffichage(){
      console.log("lancerAffichage() l'enrolement selectionné = ",angular.copy($rootScope.enrolementData));
      if(dataInstance.instance && $rootScope.enrolementData.length == 0){
          if($rootScope.allData && $rootScope.allData.length != 0){
              for(var i=0;i<$rootScope.allData.length;i++){
                  if($rootScope.allData[i].instance == dataInstanceEntete.instance){
                      $rootScope.enrolementData = $rootScope.allData[i].dataValue;
                      console.log("l'enrolement selectionné = ",$rootScope.enrolementData);
                  }
              }
          }
      }
      mappigData();
      initial();
      gestionDate();
      $scope.chargeList = false;
    }

    function initial() {
      //  console.log("entrer dans initial, $rootScope.orgUnitSelect", $rootScope.orgUnitSelect);

        $scope.enrolA.district = $rootScope.orgUnitSelect.districtSanitaire;
        centreSocial = getInfoOrganisation($rootScope.orgUnitSelect.parent.id);
        plateForme = getInfoOrganisation(centreSocial.parent.id);
        $scope.enrolA.plateform_cod = plateForme.name;
        $scope.enrolA.centre_social = centreSocial.name;
        $scope.enrolA.ong = $rootScope.orgUnitSelect.organisationLocal;
        $scope.enrolA.codeSafespace = $rootScope.orgUnitSelect.code;
        $scope.enrolA.region = $rootScope.orgUnitSelect.region;
        $scope.enrolA.departement = $rootScope.orgUnitSelect.departement;
        $scope.enrolA.sousPrefect = $rootScope.orgUnitSelect.sousPrefecture;
        $scope.enrolA.villageCommune = $rootScope.orgUnitSelect.commune;
        $scope.enrolA.villageCommune = $rootScope.orgUnitSelect.commune;

    }

    function mappigData() {
      console.error("$rootScope.enrolementData = ",$rootScope.enrolementData);
        for(var i = 0;i<enrolSectionA.length;i++){
            var id = getElementId(enrolSectionA[i]);
            if(id){
                for(var j=0;j<$rootScope.enrolementData.length;j++){
                    if(id == $rootScope.enrolementData[j].element){
                        $scope.enrolA[enrolSectionA[i]] = $rootScope.enrolementData[j].value;
                    }
                }
                if(!$scope.enrolA[enrolSectionA[i]] || $scope.enrolA[enrolSectionA[i]] == null || $scope.enrolA[enrolSectionA[i]] == ""){
                  console.error("Element sans valeur, code = ",enrolSectionA[i]);
                }
            }
        }
        console.log("mappigData() $scope.enrolA = ",$scope.enrolA);
        //getBeneficiare($scope.enrolA.id_dreams);
        if(dataInstanceEntete.instance){
          getBeneficiareInstance(dataInstanceEntete.instance);
        }
    }

    function getBeneficiareInstance(instance){
      var url = valueBeneficiaire+"/instance/"+instance;
      console.log("getBeneficiareInstance() url = ", url);
      $http.get(url).then(function (response) {
            console.log("getBeneficiareInstance() response = ", response);
            if(response.data.length == 1){
              $rootScope.beneficiaireSelect = response.data[0];
              console.log("$rootScope.beneficiaireSelect = ",$rootScope.beneficiaireSelect);
            }else {
              console.log(err);
            }


      }, function (err) {
          console.log(err);
      });

    }

    function getBeneficiare(idDreams){
      var url = valueBeneficiaire+"/id?idDreams="+idDreams;
      console.log("getBeneficiare() url = ", url);
      $http.get(url).then(function (response) {
            console.log("getdata() response = ", response);
              if(response.data.id){
                $scope.suivantDisabled = true;
                $scope.styleIdDreams = colorRed;
                $scope.idDreamsInfo = "existe"
                console.log("getdata() Controle = ");
              }

      }, function (err) {
          console.log(err);
      });

    }

    function initialDom() {
        $('.datepicker').pickadate({
            closeOnSelect: false,
            closeOnClear: false
        });
        $(document).ready(function () {
            $('.mdb-select').material_select();
        });
        $(function () {
            // Tooltip Initialization

            $('.dates input').datepicker({
                format: "dd-mm-yyyy",
                weekStart: 1,
                todayBtn: "linked",
                clearBtn: true,
                language: "fr",
                daysOfWeekHighlighted: "0,6",
                autoclose: true,
                todayHighlight: true
            });
        });
    }

    function getInfoOrganisation(id) {
        //console.log("entrer dans getInfoOrganisation, id = ",id," // $rootScope.organisation", $rootScope.organisation);
        for(var i = 0;i<$rootScope.organisation.length;i++){
            if(id == $rootScope.organisation[i].id){
                return $rootScope.organisation[i];
            }
        }
    }

    $scope.getBenefId = function() {
        //console.log("entrer dans getBenefId()");
        var mois = $scope.enrolA.dat_enrol.substring(3,5);
        var annee = $scope.enrolA.dat_enrol.substring(8,10);
        $scope.enrolA.id_dreams = "D/"+plateForme.code+"/"+centreSocial.code+"/"+annee+"/"+mois+"/"+$scope.enrolA.codeSafespace+"/"+$scope.enrolA.no_benef;
        var id = $scope.enrolA.id_dreams;
        $scope.styleIdDreams = noStyle;
        $scope.idDreamsInfo = "";
        if(id.length != 22){
          console.log("$scope.enrolA.id_dreams != 22");
          $scope.styleIdDreams = colorRed;
          $scope.idDreamsInfo = "incorrect"
          $scope.suivantDisabled = true;
          return;
        }
        $scope.suivantDisabled = false;
        console.log("$scope.enrolA.id_dreams == 22");
        if(id != $rootScope.beneficiaireSelect.id_dreams){
          getBeneficiare($scope.enrolA.id_dreams);
        }

    };

    $scope.savePage = function () {
        //console.log("entrer dans savePage()");
        //console.log("entrer dans savePage(),$scope.enrolA = ",$scope.enrolA);
        //console.log("entrer dans savePage(),dataInstance = ",dataInstance);
        dataInstance.dataValue = [];
        dataInstance.dreamsId = [];
        //dataInstance.dreamsId = $scope.enrolA.id_dreams;
        dataInstance.dreamsId.push($scope.enrolA.id_dreams);
        dataInstance.dateActivite = $scope.enrolA.dat_enrol;
        dataInstance.order = 1;
        if(!dataInstance.instance){
            delete dataInstance.instance;
        }
        $rootScope.dataInstance = dataInstance;
        getElementid();
        createBeneficiaire();
        succesSave();
        //saveData();

    };

    /*function getElementid() {
        //console.log("getElementid()");
        for(var pop in $scope.enrolA){
            if($scope.enrolA[pop] != null && $scope.enrolA[pop] && $scope.enrolA[pop] != ""){
                //var id = getElementId(pop);
                if(id){
                    var data = {};
                    data.element = id;
                    data.value = $scope.enrolA[pop];
                    data.numero = 1;
                    dataInstance.dataValue.push(data);
                }
            }else {
                console.info("getElementid(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolA[pop]);
            }
        }
        console.log("dataInstance = ",dataInstance);
        $rootScope.benefNewEnrolData = dataInstance;

    }*/

    function getElementid() {
        //console.log("getElementid()");
        for(var pop in $scope.enrolA){
          var id = getElementId(pop);
          if(id){
              if($scope.enrolA[pop] != null && $scope.enrolA[pop] && $scope.enrolA[pop] != ""){

                      var data = {};
                      data.element = id;
                      data.value = $scope.enrolA[pop];
                      data.numero = 1;
                      //dataInstance.dataValue.push(data);
                      updateEnrolData(data);
              }else {
                  console.info("getElementid(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolA[pop]);
                  deleteEnrolData(id);
              }
          }
        }
        console.log("dataInstance = ",dataInstance);
      //  $rootScope.benefNewEnrolData = dataInstance;

    }

    function updateEnrolData(data) {
      var trouve = false;
      for(var j = 0;j<$rootScope.enrolementData.length;j++){
        if($rootScope.enrolementData[j].element == data.element){
          $rootScope.enrolementData[j] = data;
          trouve = true;
          break;
        }
      }
      if(!trouve){
        $rootScope.enrolementData.push(data);
      }
    }

    function deleteEnrolData(element) {
      var a = 0;
      while(a<$rootScope.enrolementData.length){
        if($rootScope.enrolementData[a].element == element){
          $rootScope.enrolementData.splice(a, 1);
          trouve = true;
          break;
        }
        a++;
      }
    }


    function getElementId(code) {
        for(var j = 0;j<$rootScope.programmeSelect.elements.length;j++){
            if($rootScope.programmeSelect.elements[j].element.code == code){
                return $rootScope.programmeSelect.elements[j].element.id;
            }
        }
        console.error("getElementid(). Element non trouvé, code = ",code);
        return null;
    }

    function saveData() {

        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http.post(ValueUrl, dataInstance, config).then(function(succes){
            console.log("saveData() succes = ",succes);
            //dataInstance.instance =
            if(succes.data.status == "ok"){
                dataInstance.instance = succes.data.id;
                succesSave();
            }else{
                toastr["success"]("Echec d'enregistrement");
            }
        }, function(error){
            console.log("saveData() error = ",error);
            toastr["success"]("Echec d'enregistrement");
        });
    }

    function succesSave() {
      console.log("succesSave() $rootScope.enrolementData = ",$rootScope.enrolementData);
      console.log("succesSave() $rootScope.beneficiaireData = ",$rootScope.beneficiaireData);
      $state.go('enrolSB',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
    }

    function createBeneficiaire() {
        beneficiaire = {};
        beneficiaire.name = $scope.enrolA.nom;
        beneficiaire.firstName = $scope.enrolA.pren;
        beneficiaire.id_dreams = $scope.enrolA.id_dreams;
        beneficiaire.telephone = $scope.enrolA.tel;
        beneficiaire.dateNaissance = $scope.enrolA.dat_nais;
        beneficiaire.ageEnrolement = $scope.enrolA.age_enrol;
        beneficiaire.dateEnrolement = $scope.enrolA.dat_enrol;
        beneficiaire.organisation = {};
        beneficiaire.organisation.id = dataInstance.organisation;
        if($rootScope.beneficiaireSelect.id){
          beneficiaire.id = $rootScope.beneficiaireSelect.id;
        }
        $rootScope.beneficiaireData = beneficiaire;
        //saveBenef();
    }

    function saveBenef() {

        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http.post(valueBeneficiaire, beneficiaire, config).then(function(succes){
            console.log("saveBenef() > succes = ",succes);
            if(succes.data.status == "OK"){
                saveData();
                toastr["success"]("Bénéficiaire créé");
            }else{
                toastr["success"]("Impossible de créer ce bénéficiaire");
            }
        }, function(error){
            console.log("saveBenef() > error = ",error);
            toastr["success"]("Impossible de créer ce bénéficiaire");
        });
    }

    function gestionDate(){

      if($scope.enrolA.dat_nais != null){
        //console.log("$scope.enrolA.dat_nais existe")
        $scope.enrolA.dat_nais = inFormatDate($scope.enrolA.dat_nais);

      }

      if($scope.enrolA.dat_enrol != null){
        //console.log("$scope.enrolA.dat_enrol existe");
        $scope.enrolA.dat_enrol = inFormatDate($scope.enrolA.dat_enrol);
      }
    }

// yyyy-MM-dd
    function inFormatDate(date){
      var annee = "";
      var mois = "";
      var jour = "";
      var indice = date.indexOf("-");
      if(indice == 2){
         return date;
      }
      annee = date.substring(0, 4);
      mois = date.substring(5, 7);
      jour = date.substring(8, 10);

      //console.log("ladate = ",jour,"-",mois,"-",annee);
      return jour+"-"+mois+"-"+annee
    }

}]);
