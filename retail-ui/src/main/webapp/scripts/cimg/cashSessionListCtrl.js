/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('cashSessionListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, CASH_SESSION_ALL_CURRENT_URI, SESSION_EVENT_TYPE_FLOAT,
                                                   SESSION_EVENT_TYPE_PICKUP, CASH_SESSION_FETCH_DATA_FOR_RECONCILIATION_URI, CASH_SESSION_END_URI, CASH_SESSION_STATE_ENDED) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Add Float" tooltip-placement="bottom" class="fa fa-calculator fa-2x" ng-show="grid.appScope.isSessionActive(row)" ng-click="grid.appScope.addFloat(row)"></i></a> <a href=""><i tooltip="Pickup Float" tooltip-placement="bottom" class="fa fa-dollar fa-2x" ng-show="grid.appScope.isSessionActive(row)" ng-click="grid.appScope.pickupFloat(row)"></i></a> <a href=""><i tooltip="End Session" ng-show="grid.appScope.isSessionActive(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.endSession(row)"></i></a> <a href=""><i tooltip="Reconcile Session" tooltip-placement="bottom" class="fa fa-bank fa-2x" ng-click="grid.appScope.reconcileSession(row)"></i></a>', width:'15%' },
            {field:'id', displayName:'Cash Session#', enableCellEdit:false, width:'10%'},
            //{field:'cssnOperator.usrFirstName', displayName:'First Name', enableCellEdit:false, width:'15%'},
            //{field:'cssnOperator.usrSurName', displayName:'SurName',enableCellEdit:false, width:'20%'},
            {field:'cssnStartDate', displayName:'Created on',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'cssnStatus', displayName:'Status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'cssnTotalFloat', displayName:'Total Float',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'cssnTotalPickup', displayName:'Total Pickup',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency'}
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
            //baseDataService.setIsPageNew(false);
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
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(addFloatForm);
            $state.go('dashboard.addFloat');
        });
    }

    $scope.reconcileSession = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var url = CASH_SESSION_FETCH_DATA_FOR_RECONCILIATION_URI + row.entity.id;
        baseDataService.getBaseData(url).then(function(response){
            var data = response.data;
            var reconciliationForm = {
                cashSession : row.entity,
                comment : '',
                sessionEventDetailList : data
            }
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(reconciliationForm);
            $state.go('dashboard.reconcileSession');
        });
    }

    $scope.endSession = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        baseDataService.addRow(row.entity, CASH_SESSION_END_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                baseDataService.getBaseData(CASH_SESSION_STATE_ENDED).then(function(response){
                    var data = response.data;
                    row.entity.cssnStatus = data;
                });
            } else {
                alert('Not able to end session. ' + addResponse.message);
            }
        });
    }

    $scope.isSessionActive = function(row) {
        if (row.entity.cssnStatus.categoryCode == 'SESSION_STATE_OPEN' ||  row.entity.cssnStatus.categoryCode == 'SESSION_STATE_CLOSED') {
            return true;
        }
        return false;
    }

});
