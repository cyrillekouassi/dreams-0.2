saisie.controller('dossierBeneficiaireListCTRL',['$scope','$http','$rootScope','$stateParams','$filter',function ($scope,$http,$rootScope,$stateParams,$filter) {
    console.log("entrer dans dossierBeneficiaireListCTRL");
    var dataValuesUrl = serverAdresse+"dataValue/orgPro";
    var instanceUrl = serverAdresse+"instance";
    var searchUrl = serverAdresse+"dataValue/search";
    var DATE_FORMAT = 'dd-MM-yyyy HH:mm:ss';
    var prog = "prog=";
    var org = "org=";
    var listeInstance = [];
    var leProgramme = {};
    $rootScope.elementProgramme = [];
    var nosElements = [];
    var data = [];
    $rootScope.RefInfo = {};
    $rootScope.allData = [];
    $scope.LesDossiers = [];
    $scope.infoSearch = {};
    var orgSelest = {};
    $scope.chargeList = false;
    orgSelest = $stateParams.org;
    $scope.search = {};
    // formattage de type de donnee à recevoir
    var config = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if(orgSelest){
        formateMetaData();
    }else{
        console.log("orgSelest not exist");
        toastr["success"]("Aucune organisation selectionné");
    }

    function formateMetaData() {
      $scope.LesDossiers = [];
        for (var i = 0; i < $rootScope.lesProgrammes.length; i++) {
            if ($rootScope.lesProgrammes[i].code == "dossierBeneficiare") {
                $rootScope.programmeSelect = $rootScope.lesProgrammes[i];
                //prog = prog + $rootScope.programmeSelect.id;
                console.log("$rootScope.programmeSelect = ", $rootScope.programmeSelect);
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
      $scope.LesDossiers = [];
      $scope.chargeList = true;
        var org = "org=" + $rootScope.orgUnitSelect.id;
        var prog = "prog=" + $rootScope.programmeSelect.id;
        var dataValuesSearch;
        if(page){
            dataValuesSearch = dataValuesUrl + "?" + prog + "&" + org+page;
        }else{
            dataValuesSearch = dataValuesUrl + "?" + prog + "&" + org;
        }
        $http.get(dataValuesSearch).then(function (response) {
            //console.log("getdata() response = ", response);
            data = response.data.content;
            gestPagging(response.data);
            if (data.length > 0) {
                $rootScope.allData = data;
                getElements();
            }else {
              $scope.chargeList = false;
            }

        }, function (err) {
            console.log(err);
        });
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
        console.log("getdata() infoSearch = ", $scope.infoSearch);
    }

    function getElements() {

        //$rootScope.elementProgramme = [];

        for (var l = 0, k = $rootScope.programmeSelect.elements.length; l < k; l++) {
            if ($rootScope.programmeSelect.elements[l].element.code == "id_dreams") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "nomPrenomBenef") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if ($rootScope.programmeSelect.elements[l].element.code == "age_enrol") {
                nosElements.push($rootScope.programmeSelect.elements[l].element);
            }
            if (nosElements.length == 3) {
                break;
            }
        }
        contituerListe();
    }

    function contituerListe() {
        var listegroupe = [];
        for (var i = 0; i < data.length; i++) {
            var temp = {};
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
            listegroupe.push(temp);
        }

        console.log("listegroupe = ", listegroupe);
        $scope.LesDossiers = listegroupe;
        $scope.chargeList = false;
    }

    $scope.deleteInstance = function (instance) {
        console.log("instance = " + instance);
        var instanceDelete = instanceUrl + "/" + instance;
        $http.delete(instanceDelete).then(function (response) {
            console.log(response);
            formateMetaData();
        }, function (err) {
            console.log(err);
            formateMetaData();
        });
    };

    function getOptions(options, value) {
        console.log("getOptions() > options",options," // value = ",value);
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
        console.log("affichePage() > page = ",page);
        if(page != $scope.infoSearch.page){
            var aller = "&page="+page;
            getdata(aller);
        }
    }



// bouton search: affiche la zone de saisie
    $scope.afficheSearch = function(){
    $scope.searchForm = true; // activer l'affichage de la zone de recherche

// rafraichir la page pour activer l'affichage du calaendrier
      /*$timeout( function(){
        $scope.$apply(function() {
          initialDom();
        });
      },10);*/
    }
//Croix pour masquer la zone de recherche
    $scope.closeSearchForm = function(){
      $scope.searchForm = false;// desactiver l'affichage de la zone de recherche
      formateMetaData();// annuler les recherche effectuées
    }

// boutton rechercher: lancer la recherche
    $scope.searchElement = function(){
      console.log("searchElement() > $scope.search = ",$scope.search);
      console.log("searchElement() > $rootScope.programmeSelect = ",$rootScope.programmeSelect);
      console.log("searchElement() > $rootScope.orgUnitSelect = ",$rootScope.orgUnitSelect);
      //$rootScope.elementProgramme = [];
      var recherche = {};
      recherche.valueSearchs = []
      // recuperer les id et les valeurs des element de la zone de recheche
      for(var pop in $scope.search){
        if($scope.search[pop] && $scope.search[pop] != ""){
          console.log("element = ",pop, " // value = ",$scope.search[pop]);
          var tmp = {};
          tmp.element = getElementId(pop);
          tmp.value = $scope.search[pop];
          recherche.valueSearchs.push(tmp);
        }
      }

      // initialiser la pagination
      recherche.organisation = $rootScope.orgUnitSelect.id;
      recherche.programme = $rootScope.programmeSelect.id;
      recherche.page = 0;
      recherche.size = 50;
      console.log("searchElement() > recherche = ",recherche);
      getSearch(recherche);

    }

    function getElementId(elementCode){
      console.log("getElementId() > nosElements = ",nosElements, " // elementCode = ",elementCode);
      for (var k = 0; k < nosElements.length; k++) {
        if (nosElements[k].code == elementCode) {
          return nosElements[k].id;
        }
      }
    }

    // Envoyer les elements rechercher sur le serveur
    function getSearch(searchElement){
        $scope.chargeList = true;
        $http.post(searchUrl, searchElement, config).then(function(succes){
          // reponse du serveur avec des donnee
            console.log("getSearch() succes = ", succes);
            donnerRecu(succes.data);
          }, function (err) {
            // reponse d'erreur du serveur
          console.log(err);
      });

    }

    // Traiter les données reçu du serveur et contenant les resultats des recheches
    function donnerRecu(donnee){
      data = donnee.content;
      gestPagging(donnee); // faire la pagination
      if (data.length > 0) {
          $rootScope.allData = data;
          getElements();
      }else{
        $scope.chargeList = false;
      }
    }

}]);
