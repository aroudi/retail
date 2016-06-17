/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('addContactCtrl', function($scope, baseDataService, contactObject, SUCCESS, FAILURE, CONTACT_TYPE_URI) {

    populatePageData();
    function populatePageData() {
        $scope.contactBackup = angular.copy(contactObject);
        $scope.contact = contactObject;
        baseDataService.getBaseData(CONTACT_TYPE_URI).then(function(response){
            $scope.contactTypeSet = response.data;
            $scope.contact.contactType = baseDataService.populateSelectList($scope.contact.contactType,$scope.contactTypeSet);
        });

    }

    $scope.submit = function () {
        if ($scope.contact != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.contact);
        }
    }
    $scope.cancel = function() {
        contactObject = $scope.boqDetailBackup;
        $scope.closeThisDialog('button');
    }
});
