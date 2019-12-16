saisie.controller('enrolSIJKctrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state', function ($scope, $rootScope, $stateParams, $http,$filter,$state) {
    console.log("entrer dans enrolSIJKctrl");
    var ValueUrl = serverAdresse+'dataValue';
    var valueBeneficiaire = serverAdresse+'beneficiaire';
	var dataInstanceEntete = {};
    var dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
	$scope.enrolIJK = {};
  $scope.repas = {};
  $scope.participation = {};
  $scope.chargeList = true;
	var enrolSectionIJK = ["_01_participation_program",
		"_01_a_autre_program",
    "codeOEV",
		"_01_vie_assossiative",
		"_02_amis",
		"_03_reseau_social",
		"_04_person_confiance",
		"_05_group_epargn",
		"_06_refus_ide_group",
		"_07_trist_angoiss",
		"_01_nbre_repas",
		"_02_element_repas",
		"Comment"];

	mappingData();


	 function mappingData() {
	        for(var i = 0;i<enrolSectionIJK.length;i++){
	            var id = getElementId(enrolSectionIJK[i]);
	            if(id){
	                for(var j=0;j<$rootScope.enrolementData.length;j++){
	                    if(id == $rootScope.enrolementData[j].element){
	                        $scope.enrolIJK[enrolSectionIJK[i]] = $rootScope.enrolementData[j].value;
                          if(enrolSectionIJK[i] == "_01_participation_program"){
                            initiParticipation($rootScope.enrolementData[j].value);
                          }
                          if(enrolSectionIJK[i] == "_02_element_repas"){
                            initiRepas($rootScope.enrolementData[j].value);
                          }
	                    }
	                }
                  if(!$scope.enrolIJK[enrolSectionIJK[i]] || $scope.enrolIJK[enrolSectionIJK[i]] == null || $scope.enrolIJK[enrolSectionIJK[i]] == ""){
                    console.error("Element sans valeur, code = ",enrolSectionIJK[i]);
                  }
	            }
	        }
	        console.log("mappingData() $scope.enrolIJK = ",$scope.enrolIJK);
          $scope.chargeList = false;
	    }

	$scope.savePage = function (){
		console.log("entrer dans savePage");
		console.log("$scope.enrolIJK = ",$scope.enrolIJK);
		console.log("dataInstance = ",dataInstance);
		console.log("$rootScope.programmeSelect = ",$rootScope.programmeSelect);
		//dataInstance.dreamsId = $scope.enrolA.id_dreams;
        //dataInstance.dateActivite = $scope.enrolA.dat_enrol;
    dataInstance = $rootScope.dataInstance;
		dataInstance.dataValue = [];
		getElement();
    //updateEnrolData();
		saveBenef();

	}

	$scope.previewPage = function (){
		console.log("entrer dans previewPage");
		$state.go('enrolSH',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});

	}


    function getElement() {
          console.log("getElement()");
          $scope.enrolIJK._01_participation_program = gestparticipation();
          $scope.enrolIJK._02_element_repas = gestrepas();
          console.log("$scope.enrolH = ",$scope.enrolIJK);
          for(var pop in $scope.enrolIJK){
            var id = getElementId(pop);
            if(id){
              if($scope.enrolIJK[pop] != null && $scope.enrolIJK[pop] && $scope.enrolIJK[pop] != ""){
                      var data = {};
                      data.element = id;
                      data.value = $scope.enrolIJK[pop];
                      data.numero = 1;
                      //dataInstance.dataValue.push(data);
                      updateEnrolData(data);
              }else {
                  console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolIJK[pop]);
                  deleteEnrolData(id);
              }
            }else{
                console.error("getElement(). Element non trouvé, code = ",pop);
            }
          }
          dataInstance.dataValue = $rootScope.enrolementData;
          console.log("dataInstance = ",dataInstance);

      }

	function gestparticipation(){
		console.log("$scope.participation = ",$scope.participation);
		var participationValue=null;
		 for(var pop in $scope.participation){
			 if($scope.participation[pop]){
				if(!participationValue){
					participationValue= "" + pop;
				}
				else{
					participationValue= participationValue +" " + pop;
				}
			 }
		 }
		 console.log("participationValue = ",participationValue);
		 return participationValue;
	}

	function gestrepas(){
		console.log("$scope.repas = ",$scope.repas);
		var repasValue=null;
		 for(var pop in $scope.repas){
			 if($scope.repas[pop]){
				if(!repasValue){
					repasValue= "" + pop;
				}
				else{
					repasValue= repasValue +" " + pop;
				}
			 }
		 }
		 console.log("repasValue = ",repasValue);
		 return repasValue;
	}

	function getElementId(code) {
        for(var j = 0;j<$rootScope.programmeSelect.elements.length;j++){
            if($rootScope.programmeSelect.elements[j].element.code == code){
                return $rootScope.programmeSelect.elements[j].element.id;

            }
        }
        return null;
    }

	function saveData() {

        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http.post(ValueUrl, dataInstance, config).then(function(succes){
        //$http.post(ValueUrl, $rootScope.benefNewEnrolData, config).then(function(succes){
            console.log("saveData() succes = ",succes);
            //dataInstance.instance =
            if(succes.data.status == "ok"){
                dataInstance.instance = succes.data.id;
                //succesSave();
                executeDossierBesoins();
            }else{
                toastr["success"]("Echec d'enregistrement");
            }
        }, function(error){
            console.log("saveData() error = ",error);
            toastr["success"]("Echec d'enregistrement");
        });
    }

  function saveBenef() {

        var config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http.post(valueBeneficiaire, $rootScope.beneficiaireData, config).then(function(succes){
            console.log("saveBenef() > succes = ",succes);
            if(succes.data.status == "OK"){
                saveData();
                toastr["success"]("Bénéficiaire OK");
            }else{
                toastr["success"]("Impossible de créer ce bénéficiaire");
            }
        }, function(error){
            console.log("saveBenef() > error = ",error);
            toastr["success"]("Impossible de créer ce bénéficiaire");
        });
    }

	function succesSave() {
        $state.go('enrol_List',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
      }

  function initiParticipation(valeur){
          console.log("initiParticipation => valeur = ",valeur);
          if(!valeur || valeur == "" || valeur == " "){return;}

          var conti = true; var option = null;
          var initi = 0, space = 0;
          while (conti){
              space = valeur.indexOf(" ",initi);
              if(space != -1){
                  option = valeur.substring(initi,space);
                  initi = space+1;
              }else{
                  option = valeur.substring(initi,valeur.length);
                  conti = false;
              }
              $scope.participation[option] = true;
          }
        }

    function initiRepas(valeur){
          console.log("initiRepas => valeur = ",valeur);
          if(!valeur || valeur == "" || valeur == " "){return;}

          var conti = true; var option = null;
          var initi = 0, space = 0;
          while (conti){
              space = valeur.indexOf(" ",initi);
              if(space != -1){
                  option = valeur.substring(initi,space);
                  initi = space+1;
              }else{
                  option = valeur.substring(initi,valeur.length);
                  conti = false;
              }
              $scope.repas[option] = true;
          }
        }

    function executeDossierBesoins() {
            var instance = "instance=" + dataInstance.instance;
            var beneficiaireID = "beneficiaireID=" + getValue("id_dreams");
            var dateEnrolement = "dateEnrolement=" + getValue("dat_enrol");
            var apiDossierBesoin = serverAdresse+"api/genererBesoinAndDossier"
            var ValuesUrl = apiDossierBesoin+"?" + instance + "&" + beneficiaireID+ "&" +dateEnrolement;

            $http.get(ValuesUrl).then(function (response) {
              if(response.data.status == "ok"){
                console.log("executeDossierBesoins() response = ",response);
                  //dataInstance.instance = succes.data.id;
                  succesSave();
              }else{
                  toastr["success"]("Echec d'enregistrement");
              }

            }, function (err) {
              console.log("executeDossierBesoins() error = ",err);
              toastr["success"]("Echec d'enregistrement");
            });
        }

        function getValue(code){
          var id = getElementId(code);
          if(id){
            for(var j=0;j<$rootScope.enrolementData.length;j++){
                if(id == $rootScope.enrolementData[j].element){
                    return $rootScope.enrolementData[j].value;
                }
            }
          }
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
}]);
