/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('addFloatCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, CASH_SESSION_ADD_FLOAT_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        $scope.addFloatForm = angular.copy(baseDataService.getRow());
        //baseDataService.setIsPageNew(true);
        //baseDataService.setRow({});
    }

    //add float
    $scope.addFloat = function () {

        var rowObject = $scope.addFloatForm;
        baseDataService.addRow(rowObject, CASH_SESSION_ADD_FLOAT_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listCurrentCashSession');
            } else {
                alert('Not able to save data. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
