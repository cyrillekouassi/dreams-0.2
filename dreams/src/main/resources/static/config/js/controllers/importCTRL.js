conf.controller('importCTRL',['$scope','$http','$rootScope',function ($scope,$http,$rootScope) {
    console.log("entrer dans importCTRL");
    $rootScope.ongletSelect = "import";
    var ElementfileUrl = serverAdresse+"api/uploadElementfile";
    var EnrolementDatafileUrl = serverAdresse+"api/uploadEnrolDatafile";
    var EligibiliteDatafileUrl = serverAdresse+"api/uploadEligDatafile";
    $scope.chargeData = false;
    $scope.chargeElement = false;

    var data = new FormData();
    var config = {
        transformRequest: angular.identity,
        transformResponse: angular.identity,
        headers : {
            'Content-Type': undefined
        }
    };


    $scope.doUploadMetaFile = function(){
        console.log("uploadMetaFileController $scope.doUploadFile");
        $scope.chargeElement = true;
        var file = $scope.uploadedFileMeta;

        //var data = new FormData();
        data.append('uploadfile', file);

        /*var config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers : {
                'Content-Type': undefined
            }
        };*/

        $http.post(ElementfileUrl, data, config).then(function (response) {
            $scope.uploadResult=response.data;
            $scope.FileMeta = null;
            $scope.chargeElement = false;
            $scope.uploadedFileMeta = null;
        }, function (error) {
            $scope.uploadResult=error.data;
            $scope.FileMeta = null;
            $scope.chargeElement = false;
            $scope.uploadedFileMeta = null;
        });
    };


    $scope.doUploadDataFile = function(){
        console.log("uploadDataFileController doUploadFile");
        //$scope.chargeData = true;
        var file = $scope.uploadedFileData;


        //var data = new FormData();
        data.append('uploadfile', file);

        /*var config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers : {
                'Content-Type': undefined
            }
        };*/

        if($scope.typeData == "eligibilite"){
          eligibilite();
        }
        if($scope.typeData == "enrolement"){
          enrolement();
        }

    };

    function enrolement(){
      $scope.chargeData = true;
      $http.post(EnrolDatafileUrl, data, config).then(function (response) {
          console.log("doUploadDataFile() > response = ",response);
          $scope.uploadResult=response.data;
          $scope.chargeData = false;
          $scope.FileData = null;
          $scope.uploadedFileData = null;
      }, function (error) {
          console.log("doUploadDataFile() > error = ",error);
          $scope.uploadResult=error.data;
          $scope.chargeData = false;
          $scope.FileData = null;
          $scope.uploadedFileData = null;
      });
    }

    function eligibilite(){
      $scope.chargeData = true;
      $http.post(EligibiliteDatafileUrl, data, config).then(function (response) {
          console.log("doUploadDataFile() > response = ",response);
          $scope.uploadResult=response.data;
          $scope.chargeData = false;
          $scope.FileData = null;
          $scope.uploadedFileData = null;
      }, function (error) {
          console.log("doUploadDataFile() > error = ",error);
          $scope.uploadResult=error.data;
          $scope.chargeData = false;
          $scope.FileData = null;
          $scope.uploadedFileData = null;
      });
    }


}]);
