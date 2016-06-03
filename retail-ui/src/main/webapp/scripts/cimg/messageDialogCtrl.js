/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('messageDialogCtrl', function($scope, baseDataService, message) {

    populatePageData();
    function populatePageData() {
        $scope.message = message;
    }

    $scope.submit = function () {
        if ($scope.message != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.message.action='yes';
            $scope.confirm($scope.message);
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
