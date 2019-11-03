conf.controller('autresCTRL',['$scope','$http','$rootScope','$interval',function ($scope,$http,$rootScope,$interval) {
    console.log("entrer dans autresCTRL");
    var executeRapportUrl = serverAdresse+"api/executeRapport?action=execute";
    var statusRapportUrl = serverAdresse+"api/executeRapport?action=status";
    var erasedDeletingUrl = serverAdresse+"api/erasedDeleting";
    $rootScope.ongletSelect = "autres";
    $scope.resultRapport = {};
    $scope.deletingRapport = {};
    $scope.deletingResultat = [];
    $scope.dateDerniereExecution = ".....";
    var stop;
    statusExecuteRapport();

    stop = $interval(function() {
      console.log("statusExecuteRapport ==>");
      statusExecuteRapport();
    }, 5000);

    $scope.executeRapport = function(){
      $scope.resultRapport.status = 'enCours';
      $http.get(executeRapportUrl).then(function (response) {
          console.log("executeRapport ==>",response);
          $scope.resultRapport = response.data;
          if($scope.resultRapport.id == null){
              $scope.dateDerniereExecution = "...";
          }else{
              $scope.dateDerniereExecution = $scope.resultRapport.id;
          }
      }, function (err) {
          console.log(err);
      });
    }
    function statusExecuteRapport(){
      $http.get(statusRapportUrl).then(function (response) {
          console.log("executeRapport ==>",response);
          $scope.resultRapport = response.data;
          if($scope.resultRapport.id == null){
              $scope.dateDerniereExecution = "...";
          }else{
              $scope.dateDerniereExecution = $scope.resultRapport.id;
          }

      }, function (err) {
          console.log(err);
      });
    }

    $scope.SupprimerDefinitivement = function(){
      $scope.deletingRapport.enCours = true;
      deletingResultat = [];
      $http.get(erasedDeletingUrl).then(function (response) {
          console.log("SupprimerDefinitivement ==>",response);
          $scope.deletingResultat = response.data.raisonNonImport;
          $scope.deletingRapport.enCours = false;
          $scope.deletingRapport.fini = true;
      }, function (err) {
          console.log(err);
      });
    }

}]);
