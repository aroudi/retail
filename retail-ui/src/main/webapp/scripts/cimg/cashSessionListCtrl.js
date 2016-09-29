/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('cashSessionListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CASH_SESSION_ALL_CURRENT_URI, SESSION_EVENT_TYPE_FLOAT, SESSION_EVENT_TYPE_PICKUP) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'cssnOperator.usrFirstName', enableCellEdit:false, width:'20%'},
            {field:'cssnOperator.usrSurName', enableCellEdit:false, width:'25%'},
            {field:'cssnStartDate', displayName:'Created on',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'cssnStatus.displayName', displayName:'Status',enableCellEdit:false, width:'15%'},
            {field:'cssnTotalFloat', displayName:'Total Float',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'cssnTotalPickup', displayName:'Total Pickup',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Add Float" tooltip-placement="bottom" class="fa fa-calculator fa-2x" ng-click="grid.appScope.addFloat(row)"></i></a> <a href=""><i tooltip="Pickup Float" tooltip-placement="bottom" class="fa fa-dollar fa-2x" ng-click="grid.appScope.pickupFloat(row)"></i></a>', width:'10%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };
    getAllCurrentCashSessions();
    function getAllCurrentCashSessions() {
        baseDataService.getBaseData(CASH_SESSION_ALL_CURRENT_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.addFloat = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        baseDataService.getBaseData(SESSION_EVENT_TYPE_FLOAT).then(function(response){
            var data = response.data;
            var addFloatForm = {
                cashSession : row.entity,
                eventType : data,
                amount : 0.00,
                comment : ''
            }
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(addFloatForm);
            $state.go('dashboard.addFloat');
        });
    }

    $scope.pickupFloat = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        baseDataService.getBaseData(SESSION_EVENT_TYPE_PICKUP).then(function(response){
            var data = response.data;
            var addFloatForm = {
                cashSession : row.entity,
                eventType : data,
                amount : 0.00,
                comment : ''
            }
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(addFloatForm);
            $state.go('dashboard.addFloat');
        });
    }

});
