saisie.controller('enrol_List_Ctrl', ['$scope', '$rootScope', '$http', '$filter', '$stateParams','$timeout', function ($scope, $rootScope, $http, $filter, $stateParams,$timeout) {
    console.log("entrer dans enrol_List_Ctrl");
    var dataValuesUrl = serverAdresse+"dataValue/orgPro";
    var searchUrl = serverAdresse+"dataValue/search";
    var instanceUrl = serverAdresse+"instance";
    var DATE_FORMAT = 'dd-MM-yyyy HH:mm:ss';
    var prog = "prog=";
    var org = "org=";
    var listeInstance = [];
    $rootScope.leProgramme = {};
    //$rootScope.elementProgramme = [];
    var nosElements = [];
    var data = [];
    $rootScope.VadInfo = {};
    $rootScope.allData = [];
    var orgSelest = {};
    orgSelest = $stateParams.org;
    $scope.infoSearch = {};
    var config = {
        headers: {
            'Content-Type': 'application/json'
        }
    };


    //console.log("orgSelest = ",orgSelest);
    initialDom();
    if(orgSelest){
        formateMetaData();
    }else{
        console.log("orgSelest not exist");
        toastr["success"]("Aucune organisation selectionné");
    }
    function initialDom() {

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

    $rootScope.lancerVADList = function () {
        formateMetaData();
    };

    function formateMetaData() {
        for (var i = 0; i < $rootScope.lesProgrammes.length; i++) {
            if ($rootScope.lesProgrammes[i].code == "enrolement") {
                //$rootScope.VadInfo.programme = $rootScope.programme[i].id;
                $rootScope.programmeSelect = $rootScope.lesProgrammes[i];
                //console.log("$rootScope.programmeSelect = ", $rootScope.programmeSelect);
                if($rootScope.programmeSelect.elements.length == 0){
                    toastr["success"]("Aucune Element n'existe");
                    return;
                }
                break;
            }
        }
        if (!$rootScope.orgUnitSelect || !$rootScope.orgUnitSelect.id) {
            $rootScope.getOrgSelect(orgSelest);
        }
        getdata();
    }

    function getdata(page) {
        var org = "org=" + $rootScope.orgUnitSelect.id;
        var prog = "prog=" + $rootScope.programmeSelect.id;
        var ValuesUrl;
        if(page){
            ValuesUrl = dataValuesUrl + "?" + prog + "&" + org+page;
        }else{
            ValuesUrl = dataValuesUrl + "?" + prog + "&" + org;
        }

        $http.get(ValuesUrl).then(function (response) {
              //console.log("getdata() response = ", response);
              donnerRecu(response.data);
            /*data = response.data.content;
            gestPagging(response.data);
            if (data.length > 0) {
                //console.log("getdata() data = ", data);
                $rootScope.allData = data;
                getElements();
            }*/

        }, function (err) {
            console.log(err);
        });
    }

    function donnerRecu(donnee){
      data = donnee.content;
      gestPagging(donnee);
      if (data.length > 0) {
          //console.log("getdata() data = ", data);
          $rootScope.allData = data;
          getElements();
      }
    }

    function gestPagging(data) {
        $scope.infoSearch.totalPages = data.totalPages;
        $scope.infoSearch.totalElements = data.totalElements;
        $scope.infoSearch.taille = data.size;
        $scope.infoSearch.page = data.number;
        $scope.infoSearch.pagging = [];
        var laPage = 0;

        for(var i = 0 ; i < data.totalPages;i++){
            laPage += 1;
            $scope.infoSearch.pagging.push(laPage);
        }
        //console.log("getdata() infoSearch = ", $scope.infoSearch);
    }

    function getElements() {

        $rootScope.elementProgramme = [];

        for (var l = 0, k = $rootScope.programmeSelect.elements.length; l < k; l++) {

            if ($rootScope.programmeSelect.elements[l].element.code == "dat_enrol") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "id_dreams") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "nom") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "pren") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "age_enrol") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if (nosElements.length == 5) {
                break;
            }


        }
        //console.log("getElements() nosElements = ", nosElements);

        contituerListe();
    }

    function contituerListe() {
        var liste= [];
        for (var i = 0; i < data.length; i++) {
            var temp = {};
            if(data[i].instance){
                temp.instance = data[i].instance;
                for (var k = 0; k < nosElements.length; k++) {
                    for (var j = 0; j < data[i].dataValue.length; j++) {
                        var date1 = new Date(data[i].dataValue[j].dateUpdate);
                        if (temp.update && temp.update < date1) {
                            temp.update = date1;
                        } else {
                            temp.update = date1;
                        }
                        if (nosElements[k].id == data[i].dataValue[j].element) {
                            if(nosElements[k].ensembleOption && angular.isObject(nosElements[k].ensembleOption)){
                                temp[nosElements[k].code] = [];
                                temp[nosElements[k].code] = getOptions(nosElements[k].ensembleOption.options,data[i].dataValue[j].value);
                            }else{
                                temp[nosElements[k].code] = data[i].dataValue[j].value;
                            }
                        }
                    }
                }
                temp.update = $filter('date')(temp.update, DATE_FORMAT);
                liste.push(temp);
            }else{
                $scope.infoSearch.totalElements -= 1;
            }

        }

        //console.log("lesEnrol = ", liste);
        $scope.lesEnrol = liste;

    }

    $scope.deleteInstance = function (instance) {
        //console.log("instance = " + instance);
        instanceUrl = instanceUrl + "/" + instance;
        $http.delete(instanceUrl).then(function (response) {
            console.log(response);
            getdata();
        }, function (err) {
            console.log(err);
        });
    };

    function getOptions(options, value) {
        //console.log("getOptions() > options",options," // value = ",value);
        var conti = true;
        var debut = 0, point = 0;
        var list = []; var opt = "";
        while(conti){
            point = value.indexOf(" ",debut);
            if(point != -1){
                opt = value.substring(debut, point);
                list.push(getOptionValue(options, opt));
                point++;
            }else{
                opt = value.substring(debut, value.length);
                list.push(getOptionValue(options, opt));
                conti = false;
            }
            debut = point;
        }
        return list;
    }

    function getOptionValue(options, value) {
        //console.log("getOptionValue() > options",options," // value = ",value);
        for(var i = 0;i<options.length;i++){
            if(options[i].code == value){
                return options[i].name;
            }
        }
        return value;
    }

    $scope.affichePage = function (page) {
      //  console.log("affichePage() > page = ",page);
        if(page != $scope.infoSearch.page){
            var aller = "&page="+page;
            getdata(aller);
        }
    }

    $scope.searchElement = function(){
      console.log("searchElement() > $scope.search = ",$scope.search);
      console.log("searchElement() > $rootScope.programmeSelect = ",$rootScope.programmeSelect);
      console.log("searchElement() > $rootScope.orgUnitSelect = ",$rootScope.orgUnitSelect);
      $rootScope.elementProgramme = [];
      var recherche = {};
      recherche.valueSearchs = []
      for(var pop in $scope.search){
        if($scope.search[pop] && $scope.search[pop] != ""){
          console.log("element = ",pop, " // value = ",$scope.search[pop]);
          var tmp = {};
          tmp.element = getElementId(pop);
          tmp.value = $scope.search[pop];
          recherche.valueSearchs.push(tmp);
        }
      }

      recherche.organisation = $rootScope.orgUnitSelect.id;
      recherche.programme = $rootScope.programmeSelect.id;
      recherche.page = 0;
      recherche.size = 50;
      console.log("searchElement() > recherche = ",recherche);
      getSearch(recherche);

    }

    function getElementId(code){

      console.log("getElementId() > nosElements = ",nosElements);
      for(var i = 0;i<nosElements.length;i++){
        if(nosElements[i].code == code){
          return nosElements[i].id;
        }

      }
  }

  function getSearch(searchElement){

    //var dataRequest = searchUrl+"?searchElement="+searchElement.valueSearchs;
    //$http.get(dataRequest).then(function (response) {
      $http.post(searchUrl, searchElement, config).then(function(succes){
          console.log("getSearch() succes = ", succes);
          donnerRecu(succes.data);
        }, function (err) {
        console.log(err);
    });

  }

}]);
