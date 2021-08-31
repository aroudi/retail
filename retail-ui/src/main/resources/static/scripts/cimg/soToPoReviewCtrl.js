/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('soToPoReviewCtrl', function($scope,saleOrderList, $state, $timeout, uiGridConstants, baseDataService, SUCCESS, FAILURE, GENERATE_PO_FROM_SO_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10 %', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'30%'},
            {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'10%'},
            {field:'status', displayName:'Status', enableCellEdit:false, width:'20%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Void Item" ng-show="grid.appScope.isTxnLineVoidable(row)" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.voidItem(row)" ></i></a>&nbsp;<a href=""><i tooltip="Delete Item" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a>', width: '8%'}
        ]
    };
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };
    initPageData();
    function initPageData() {
       $scope.gridOptions.data = angular.copy(saleOrderList);
    };
    $scope.submit = function () {
        var txhdIdList = [];
        for (var i = 0; i < $scope.gridOptions.data.length; i++) {
            txhdIdList.push($scope.gridOptions.data[i].id);
        }
        baseDataService.addRow(txhdIdList, GENERATE_PO_FROM_SO_URI).then(function(response) {
            //return the generated purchase order list.
            $scope.confirm(response.data);
        });

    };
    $scope.cancel = function() {
        //$scope.$destroy();
        $scope.closeThisDialog('button');
    };
    $scope.removeItem = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    alert('item is undefined');
                    return;
                }

            } else {
                return;
            }
            $scope.gridOptions.data.splice(baseDataService.getArrIndexOf($scope.gridOptions.data, row.entity, 1));
        });
    };


});
