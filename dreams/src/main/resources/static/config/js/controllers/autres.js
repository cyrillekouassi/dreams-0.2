conf.controller('autresCTRL',['$scope','$http','$rootScope','$interval',function ($scope,$http,$rootScope,$interval) {
    console.log("entrer dans autresCTRL");
    var executeRapportUrl = serverAdresse+"api/executeRapport?action=execute";
    var statusRapportUrl = serverAdresse+"api/executeRapport?action=status";
    $rootScope.ongletSelect = "autres";
    $scope.resultRapport = {};
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
      }, function (err) {
          console.log(err);
      });
    }
    function statusExecuteRapport(){
      $http.get(statusRapportUrl).then(function (response) {
          console.log("executeRapport ==>",response);
          $scope.resultRapport = response.data;
      }, function (err) {
          console.log(err);
      });
    }

}]);
