saisie.controller('enrolSDctrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state', function ($scope, $rootScope, $stateParams, $http,$filter,$state) {
    console.log("entrer dans enrolSDctrl");
    var ValueUrl = serverAdresse+'dataValue';
	var dataInstanceEntete = {};
    var dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
	$scope.enrolD = {};
  $scope.testvih = {};
  $scope.chargeList = true;
var enrolSectionD = ["_01_resultat_algo","_01_test_vih","_02_dernier_test","_03_prkoi_jam_teste","_03_a_autre_prkoi_jam_teste","_04_lieu_test_vih"];

	mappigData();


	 function mappigData() {
	        for(var i = 0;i<enrolSectionD.length;i++){
	            var id = getElementId(enrolSectionD[i]);
	            if(id){
	                for(var j=0;j<$rootScope.enrolementData.length;j++){
	                    if(id == $rootScope.enrolementData[j].element){
	                        $scope.enrolD[enrolSectionD[i]] = $rootScope.enrolementData[j].value;
                          if(enrolSectionD[i] == "_03_prkoi_jam_teste"){
                            initiTest($rootScope.enrolementData[j].value);
                          }
	                    }
	                }
                  if(!$scope.enrolD[enrolSectionD[i]] || $scope.enrolD[enrolSectionD[i]] == null || $scope.enrolD[enrolSectionD[i]] == ""){
                    console.error("Element sans valeur, code = ",enrolSectionD[i]);
                  }
	            }
	        }
	        console.log("mappigData() $scope.enrolD = ",$scope.enrolD);
          $scope.chargeList = false;
	    }

	$scope.savePage = function (){
		console.log("entrer dans savePage");
		console.log("$scope.enrolD = ",$scope.enrolD);
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
		$state.go('enrolSC',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});

	}

	/*function getElement() {
        console.log("getElement()");
        $scope.enrolD._03_prkoi_jam_teste = gesttestVih();
        console.log("$scope.enrolD = ",$scope.enrolD);
        for(var pop in $scope.enrolD){
            if($scope.enrolD[pop] != null && $scope.enrolD[pop] && $scope.enrolD[pop] != ""){
                var id = getElementId(pop);
                if(id){
                    var data = {};
                    data.element = id;
                    data.value = $scope.enrolD[pop];
                    data.numero = 1;
                    dataInstance.dataValue.push(data);
                }else{
                    console.error("getElement(). Element non trouvé, code = ",pop);
                }
            }else {
                console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolD[pop]);
            }
        }
        console.log("dataInstance = ",dataInstance);

    }*/

    function getElement() {
          console.log("getElement()");
          $scope.enrolD._03_prkoi_jam_teste = gesttestVih();
          console.log("$scope.enrolD = ",$scope.enrolD);
          for(var pop in $scope.enrolD){
            var id = getElementId(pop);
            if(id){
              if($scope.enrolD[pop] != null && $scope.enrolD[pop] && $scope.enrolD[pop] != ""){
                      var data = {};
                      data.element = id;
                      data.value = $scope.enrolD[pop];
                      data.numero = 1;
                      //dataInstance.dataValue.push(data);
                      updateEnrolData(data);

              }else {
                  console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolD[pop]);
                  deleteEnrolData(id);
              }
            }else{
                console.error("getElement(). Element non trouvé, code = ",pop);
            }
          }

      }

	function gesttestVih(){
		console.log("$scope.testvih = ",$scope.testvih);
		var testvihValue=null;
		 for(var pop in $scope.testvih){
			 if($scope.testvih[pop]){
				if(!testvihValue){
					testvihValue= "" + pop;
				}
				else{
					testvihValue= testvihValue +" " + pop;
				}
			 }
		 }
		 console.log("testvihValue = ",testvihValue);
		 return testvihValue;
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

    /*function updateEnrolData() {
        console.log("D updateEnrolData() dataInstance = ",dataInstance);
        console.log("D updateEnrolData() $rootScope.benefNewEnrolData = ",$rootScope.benefNewEnrolData);
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

	function succesSave() {
        $state.go('enrolSE',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
    }

    function initiTest(valeur){
      console.log("initiTest => valeur = ",valeur);
      if(!valeur || valeur == "" || valeur == " "){return;}
      $scope.testvih = {};
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
          $scope.testvih[option] = true;
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
