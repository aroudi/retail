/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productReservationInfoCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, searchUrl) {
    $scope.title = "Product Reservation List";
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        enableColumnResizing: true,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'txnHeaderId', visible:false, enableCellEdit:false},
            {field:'txnType', displayName:'Transaction',enableCellEdit:false, width:'15%', cellFilter:'productReservTxntypeFilter'},
            {field:'txnNumber', displayName:'Number',enableCellEdit:false, width:'15%'},
            {field:'qtyReserved', displayName:'Qty Reserved',enableCellEdit:false, width:'10%'}
        ]
    };
    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };
    getRows();
    function getRows() {
        baseDataService.getBaseData(searchUrl).then(function(response){
           $scope.gridOptions.data = response.data;
        });
    }
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
});
