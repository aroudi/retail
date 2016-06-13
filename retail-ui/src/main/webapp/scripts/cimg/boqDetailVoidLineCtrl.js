/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailVoidLineCtrl', function($scope, baseDataService, boqItem, SUCCESS, FAILURE, BOQ_LINE_STATUS_VOID) {

    populatePageData();
    function populatePageData() {
        $scope.boqDetailBackup = angular.copy(boqItem);
        $scope.boqDetail = boqItem;
        baseDataService.getBaseData(BOQ_LINE_STATUS_VOID).then(function(response){
            $scope.boqDetail.bqdStatus = response.data;
        });

    }

    $scope.submit = function () {
        if ($scope.boqDetail != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.boqDetail);
        }
    }
    $scope.cancel = function() {
        $scope.boqDetail = $scope.boqDetailBackup;
        boqItem = $scope.boqDetailBackup;
        $scope.closeThisDialog('button');
    }
});
