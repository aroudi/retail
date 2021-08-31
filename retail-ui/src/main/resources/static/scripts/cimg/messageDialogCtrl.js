/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('messageDialogCtrl', function($scope, baseDataService, message) {

    populatePageData();
    function populatePageData() {
        $scope.message = message;
        $scope.yesButtonLabel = 'Yes';
        if ($scope.message.type=='info') {
            $scope.yesButtonLabel = 'Ok';
        } else if ($scope.message.type=='yesNo') {
            $scope.yesButtonLabel = 'Yes';
        }
    }

    $scope.yes = function () {
        if ($scope.message != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.message.action='yes';
            $scope.confirm($scope.message);
        }
    }
    $scope.no = function () {
        if ($scope.message != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.message.action='no';
            $scope.confirm($scope.message);
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
