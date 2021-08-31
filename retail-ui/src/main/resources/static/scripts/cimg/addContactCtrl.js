/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('addContactCtrl', function($scope, baseDataService, contactObject, SUCCESS, FAILURE, CONTACT_TYPE_URI, ALL_CATEGORY_OFTYPE_URI, COUNTRY_LIST_URI) {

    populatePageData();
    function populatePageData() {
        $scope.contactBackup = angular.copy(contactObject);
        $scope.contact = contactObject;
        baseDataService.getBaseData(CONTACT_TYPE_URI).then(function(response){
            $scope.contactTypeSet = response.data;
            $scope.contact.contactType = baseDataService.populateSelectList($scope.contact.contactType,$scope.contactTypeSet);
        });
        baseDataService.getBaseData(COUNTRY_LIST_URI).then(function(response){
            $scope.countrySet = response.data;
            $scope.country = baseDataService.populateCategoryPerDisplayName($scope.contact.country,$scope.countrySet);
            $scope.contact.country = $scope.country.displayName;
            populateStateList();
        });

    }
    $scope.onCountryChange = function () {
        $scope.contact.country = $scope.country.displayName;
        populateStateList();
    };

    $scope.onStateChange = function () {
        $scope.contact.state = $scope.state.displayName;
    };

    function populateStateList() {
        baseDataService.getBaseData(ALL_CATEGORY_OFTYPE_URI + $scope.country.categoryCode).then(function(response){
            $scope.stateSet = response.data;
            $scope.state = baseDataService.populateCategoryPerDisplayName($scope.contact.state,$scope.stateSet);
        });
    }

    $scope.submit = function () {
        if ($scope.contact != undefined) {
            //$scope.confirm($scope.selectedOption);
            if ($scope.state !== undefined) {
                $scope.contact.state = $scope.state.displayName;
            }
            if ($scope.country !== undefined) {
                $scope.contact.country = $scope.country.displayName;
            }

            $scope.confirm($scope.contact);
        }
    }
    $scope.cancel = function() {
        contactObject = $scope.contactBackup;
        $scope.closeThisDialog('button');
    }
});
