saisie.controller('enrolSGctrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state', function ($scope, $rootScope, $stateParams, $http,$filter,$state) {
    console.log("entrer dans enrolSGctrl");
    var ValueUrl = serverAdresse+'dataValue';
	var dataInstanceEntete = {};
    var dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
	$scope.enrolG = {};
  $scope.secours = {};
  $scope.aupres = {};
  $scope.chargeList = true;
var enrolSectionG = ["_01_humil_public",
	"_01_a_frequence",
	"_02_menace_nuire",
	"_02_a_frequence",
	"_03_injure",
	"_03_a_frequence",
	"_04_menace_prive",
	"_04_a_frequence",
	"_05_porte_main",
	"_05_a_frequence",
	"_06_viol",
	"_06_a_frequence",
	"_07_abus_sexuel",
	"_07_a_frequence",
	"_08_demande_aide",
	"_09_aupris_de_qui",
	"_09_a_autre_aupris_de_qui",
	"_10_secours",
	"_10_a_autre_secours"];

	mappigData();


	 function mappigData() {
	        for(var i = 0;i<enrolSectionG.length;i++){
	            var id = getElementId(enrolSectionG[i]);
	            if(id){
	                for(var j=0;j<$rootScope.enrolementData.length;j++){
	                    if(id == $rootScope.enrolementData[j].element){
	                        $scope.enrolG[enrolSectionG[i]] = $rootScope.enrolementData[j].value;
                          if(enrolSectionG[i] == "_09_aupris_de_qui"){
                            initiAupres($rootScope.enrolementData[j].value);
                          }
                          if(enrolSectionG[i] == "_10_secours"){
                            initiSecours($rootScope.enrolementData[j].value);
                          }
	                    }

	                }
                  if(!$scope.enrolG[enrolSectionG[i]] || $scope.enrolG[enrolSectionG[i]] == null || $scope.enrolG[enrolSectionG[i]] == ""){
                    console.error("Element sans valeur, code = ",enrolSectionG[i]);
                  }
	            }
	        }
	        console.log("mappigData() $scope.enrolG = ",$scope.enrolG);
          $scope.chargeList = false;
	    }

	$scope.savePage = function (){
		console.log("entrer dans savePage");
		console.log("$scope.enrolG = ",$scope.enrolG);
		console.log("dataInstance = ",dataInstance);
		console.log("$rootScope.programmeSelect = ",$rootScope.programmeSelect);
		//dataInstance.dreamsId = $scope.enrolA.id_dreams;
        //dataInstance.dateActivite = $scope.enrolA.dat_enrol;
		dataInstance.dataValue = [];
		getElement();
		//saveData();
    //updateEnrolData();
    succesSave();
	}

	$scope.previewPage = function (){
		console.log("entrer dans previewPage");
		$state.go('enrolSF',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});

	}

	/*function getElement() {
        console.log("getElement()");
        $scope.enrolG._09_aupris_de_qui = gestaupres();
        $scope.enrolG._10_secours = gestsecours();
        console.log("$scope.enrolG = ",$scope.enrolG);
        for(var pop in $scope.enrolG){
            if($scope.enrolG[pop] != null && $scope.enrolG[pop] && $scope.enrolG[pop] != ""){
                var id = getElementId(pop);
                if(id){
                    var data = {};
                    data.element = id;
                    data.value = $scope.enrolG[pop];
                    data.numero = 1;
                    dataInstance.dataValue.push(data);
                }else{
                    console.error("getElement(). Element non trouvé, code = ",pop);
                }
            }else {
                console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolG[pop]);
            }
        }
        console.log("dataInstance = ",dataInstance);

    }*/

    function getElement() {
          console.log("getElement()");
          $scope.enrolG._09_aupris_de_qui = gestaupres();
          $scope.enrolG._10_secours = gestsecours();
          console.log("$scope.enrolG = ",$scope.enrolG);
          for(var pop in $scope.enrolG){
            var id = getElementId(pop);
            if(id){
              if($scope.enrolG[pop] != null && $scope.enrolG[pop] && $scope.enrolG[pop] != ""){
                      var data = {};
                      data.element = id;
                      data.value = $scope.enrolG[pop];
                      data.numero = 1;
                      //dataInstance.dataValue.push(data);
                      updateEnrolData(data);
              }else {
                  console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolG[pop]);
                  deleteEnrolData(id);
              }
            }else{
                console.error("getElement(). Element non trouvé, code = ",pop);
            }
          }
      }

	function gestaupres(){
		console.log("$scope.aupres = ",$scope.aupres);
		var aupresValue=null;
		 for(var pop in $scope.aupres){
			 if($scope.aupres[pop]){
				if(!aupresValue){
					aupresValue= "" + pop;
				}
				else{
					aupresValue= aupresValue +" " + pop;
				}
			 }
		 }
		 console.log("aupresValue = ",aupresValue);
		 return aupresValue;
	}

	function gestsecours(){
		console.log("$scope.secours = ",$scope.secours);
		var secoursValue=null;
		 for(var pop in $scope.secours){
			 if($scope.secours[pop]){
				if(!secoursValue){
					secoursValue= "" + pop;
				}
				else{
					secoursValue= secoursValue +" " + pop;
				}
			 }
		 }
		 console.log("secoursValue = ",secoursValue);
		 return secoursValue;
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
            console.log("saveData() succes = ",succes);
            //dataInstance.instance =
            if(succes.data.status == "ok"){
                //dataInstance.instance = succes.data.id;
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
        $state.go('enrolSH',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
  }

  /*function updateEnrolData() {
      console.log("G updateEnrolData() dataInstance = ",dataInstance);
      console.log("G updateEnrolData() $rootScope.benefNewEnrolData = ",$rootScope.benefNewEnrolData);
      for (var i = 0; i < dataInstance.dataValue.length; i++) {
        var trouve = false;
        for (var j = 0; j < $rootScope.benefNewEnrolData.dataValue.length; j++) {
          if($rootScope.benefNewEnrolData.dataValue[j].element == dataInstance.dataValue[i].element){
            $rootScope.benefNewEnrolData.dataValue[j].value == dataInstance.dataValue[i].value;
            touve = true;
          }
        }
        if (!trouve) {
          $rootScope.benefNewEnrolData.dataValue.push(dataInstance.dataValue[i]);
        }
      }
  }*/

    function initiAupres(valeur){
      console.log("initiAupres => valeur = ",valeur);
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
          $scope.aupres[option] = true;
      }
    }

    function initiSecours(valeur){
      console.log("initiSecours => valeur = ",valeur);
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
          $scope.secours[option] = true;
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
