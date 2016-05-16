/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailCtrl', function($scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'25%'},
            {field:'purchaseItem.partNo', displayName:'Part No', enableCellEdit:false, width:'25%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'10%'},
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polQtyOrdered', displayName:'Qty',enableCellEdit:false, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
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

    initPageData();
    function initPageData() {
        $scope.purchaseOrderHeader = angular.copy(baseDataService.getRow());
        $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
        baseDataService.setRow({});
    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
