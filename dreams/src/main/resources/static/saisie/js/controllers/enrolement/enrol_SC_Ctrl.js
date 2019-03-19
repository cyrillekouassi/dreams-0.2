saisie.controller('enrolSCctrl', ['$scope', '$rootScope', '$stateParams', '$http','$filter','$state','$timeout',
function ($scope, $rootScope, $stateParams, $http,$filter,$state,$timeout) {
    console.log("entrer dans enrolSCctrl");
    var ValueUrl = serverAdresse+'dataValue';
	var dataInstanceEntete = {};
    var dataInstance = {};
    dataInstanceEntete.programme = $stateParams.prog;
    dataInstanceEntete.organisation = $stateParams.org;
    dataInstanceEntete.instance = $stateParams.inst;
    dataInstance = angular.copy(dataInstanceEntete);
	$scope.enrolC = {};
  $scope.chargeList = true;
	var enrolSectionC = ["_01_scolarise","_02_niv_scolaire","_02_a_classe","_02_a_autre_niv_scolaire",
		"_03_soutient_finance","_03_a_autre_soutient_finance","_04_pas_sco_actuel","_05_raison_non_sco",
		"_05_a_autre_raison_non_sco","_06_second_chance","_06_a_autre_second_chance","_07_source_revenu",
		"_07_a_autre_source_revenu","_08_economie","_09_gard_economie","_09_a_autre_gard_economie"];
    var primaire = [
        {value: 'CP1', name: 'CP1'},
        {value: 'CP2', name: 'CP2'},
        {value: 'CE1', name: 'CE1'},
        {value: 'CE2', name: 'CE2'},
        {value: 'CM1', name: 'CM1'},
        {value: 'CM2', name: 'CM2'}
    ];
    var secondaire = [
        {value: '6e', name: '6ème'},
        {value: '5e', name: '5ème'},
        {value: '4e', name: '4ème'},
        {value: '3e', name: '3ème'},
        {value: '2e', name: '2nd'},
        {value: '1e', name: '1ere'},
        {value: 'Tle', name: 'Terminale'}
    ];
    $scope.niveau = {
        classe: []
    };



	mappigData();
  initialClasse();
  $scope.chargeList = false;

	 function mappigData() {
	        for(var i = 0;i<enrolSectionC.length;i++){
	            var id = getElementId(enrolSectionC[i]);
	            if(id){
	                for(var j=0;j<$rootScope.enrolementData.length;j++){
	                    if(id == $rootScope.enrolementData[j].element){
	                        $scope.enrolC[enrolSectionC[i]] = $rootScope.enrolementData[j].value;
	                    }
	                }
                  if(!$scope.enrolC[enrolSectionC[i]] || $scope.enrolC[enrolSectionC[i]] == null || $scope.enrolC[enrolSectionC[i]] == ""){
                    console.error("Element sans valeur, code = ",enrolSectionC[i]);
                  }
	            }
	        }
	        console.log("mappigData() $scope.enrolC = ",$scope.enrolC);
	    }

	$scope.savePage = function (){
		console.log("entrer dans savePage");
		console.log("$scope.enrolC = ",$scope.enrolC);
		console.log("dataInstance = ",dataInstance);
		console.log("$rootScope.programmeSelect = ",$rootScope.programmeSelect);
		//dataInstance.dreamsId = $scope.enrolA.id_dreams;
        //dataInstance.dateActivite = $scope.enrolA.dat_enrol;
		dataInstance.dataValue = [];
		getElement();
		saveData();

	}

	$scope.previewPage = function (){
		console.log("entrer dans previewPage");
		$state.go('enrolSB',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});

	}

	function getElement() {
        console.log("getElement()");
        //console.log("$scope.enrolC = ",$scope.enrolC);
        for(var pop in $scope.enrolC){
            if($scope.enrolC[pop] != null && $scope.enrolC[pop] && $scope.enrolC[pop] != ""){
                var id = getElementId(pop);
                if(id){
                    var data = {};
                    data.element = id;
                    data.value = $scope.enrolC[pop];
                    data.numero = 1;
                    dataInstance.dataValue.push(data);
                }else{
                    console.error("getElement(). Element non trouvé, code = ",pop);
                }
            }else {
                console.info("getElement(). Element sans valeur, code = ",pop," // valeur = ",$scope.enrolC[pop]);
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

	function succesSave() {
    console.log("succesSave()");
        $state.go('enrolSD',{org: $rootScope.orgUnitSelect.id, prog: dataInstance.programme, inst: dataInstance.instance});
    }

$scope.changeniveau = function(value){
  console.log("changeniveau, value = ",value);
  $scope.enrolC._02_a_classe = null;
  $scope.classePRIMECOND = false;
  $timeout( function(){
    $scope.$apply(function() {
      if(value == "PRIM"){
        $scope.niveau.classe = primaire;
        $scope.classePRIMECOND = true;
      }
      if(value == "SECOND"){
        $scope.niveau.classe = secondaire;
        $scope.classePRIMECOND = true;
      }
      $(document).ready(function () {
          $('.mdb-select').material_select();
      });
    });
  },10);

}

/*function initialClasse() {
  $timeout( function(){
    $scope.$apply(function() {
      if($scope.enrolC){
        if($scope.enrolC._02_a_classe){
          if($scope.enrolC._02_a_classe != null;){
            if($scope.enrolC._02_niv_scolaire == "PRIM"){
              $scope.niveau.classe = primaire;
              $scope.classePRIMECOND = true;
            }
            if($scope.enrolC._02_niv_scolaire == "SECOND"){
              $scope.niveau.classe = secondaire;
              $scope.classePRIMECOND = true;
            }
            $(document).ready(function () {
                $('.mdb-select').material_select();
            });
          }
        }
      }

    });
  },10);
}*/


function initialClasse() {
  console.log("initialClasse()");
  $timeout( function(){
    $scope.$apply(function() {
      if($scope.enrolC){
        if($scope.enrolC._02_a_classe){
          if($scope.enrolC._02_a_classe != null){
            if($scope.enrolC._02_niv_scolaire){
              if($scope.enrolC._02_niv_scolaire == "PRIM"){
                $scope.niveau.classe = primaire;
                $scope.classePRIMECOND = true;
              }
              if($scope.enrolC._02_niv_scolaire == "SECOND"){
                $scope.niveau.classe = secondaire;
                $scope.classePRIMECOND = true;
              }
            }
          }

        }

      }



      $(document).ready(function () {
          $('.mdb-select').material_select();
      });
    });
  },10);
  }

}]);
