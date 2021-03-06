saisie.controller('enrolSEctrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state', function ($scope, $rootScope, $stateParams, $http,$filter,$state) {
    console.log("entrer dans enrolSEctrl");
    var ValueUrl = serverAdresse+'dataValue';
	var dataInstanceEntete = {};
    var dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
	$scope.enrolE = {};
  $scope.chargeList = true;
var enrolSectionE = ["_01_rela_sexuel","_02_ag_sexuel","_03_nbr_part_sexuel","_04_part_sexuel","_05_P1_age_parte","_06_P1_statut_part",
	"_07_P1_frequence_protect","_05_P2_age_parte","_06_P2_statut_part","_07_P2_frequence_protect",
	"_05_P3_age_parte","_06_P3_statut_part","_07_P3_frequence_protect","_08_echange_sexe"];

	mappigData();


	 function mappigData() {
	        for(var i = 0;i<enrolSectionE.length;i++){
	            var id = getElementId(enrolSectionE[i]);
	            if(id){
	                for(var j=0;j<$rootScope.enrolementData.length;j++){
	                    if(id == $rootScope.enrolementData[j].element){
	                        $scope.enrolE[enrolSectionE[i]] = $rootScope.enrolementData[j].value;
	                    }
	                }
                  if(!$scope.enrolE[enrolSectionE[i]] || $scope.enrolE[enrolSectionE[i]] == null || $scope.enrolE[enrolSectionE[i]] == ""){
                    console.error("Element sans valeur, code = ",enrolSectionE[i]);
                  }
	            }
	        }
	        console.log("mappigData() $scope.enrolE = ",$scope.enrolE);
          $scope.chargeList = false;
	    }

	$scope.savePage = function (){
		console.log("entrer dans savePage");
		console.log("$scope.enrolE = ",$scope.enrolE);
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
		$state.go('enrolSD',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});

	}

	/*function getElement() {
        console.log("getElement()");
        for(var pop in $scope.enrolE){
            if($scope.enrolE[pop] != null && $scope.enrolE[pop] && $scope.enrolE[pop] != ""){
                var id = getElementId(pop);
                if(id){
                    var data = {};
                    data.element = id;
                    data.value = $scope.enrolE[pop];
                    data.numero = 1;
                    dataInstance.dataValue.push(data);
                }else{
                    console.error("getElement(). Element non trouvé, code = ",pop);
                }
            }else {
                console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolE[pop]);
            }
        }
        console.log("dataInstance = ",dataInstance);

    }*/

    function getElement() {
          console.log("getElement()");
          for(var pop in $scope.enrolE){
            var id = getElementId(pop);
            if(id){
              if($scope.enrolE[pop] != null && $scope.enrolE[pop] && $scope.enrolE[pop] != ""){
                  var data = {};
                  data.element = id;
                  data.value = $scope.enrolE[pop];
                  data.numero = 1;
                  //dataInstance.dataValue.push(data);
                  updateEnrolData(data);
              }else {
                  console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolE[pop]);
                  deleteEnrolData(id);
              }
            }else{
                console.error("getElement(). Element non trouvé, code = ",pop);
            }
          }
          console.log("dataInstance = ",dataInstance);

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
        console.log("E updateEnrolData() dataInstance = ",dataInstance);
        console.log("E updateEnrolData() $rootScope.benefNewEnrolData = ",$rootScope.benefNewEnrolData);
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
        $state.go('enrolSF',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
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
