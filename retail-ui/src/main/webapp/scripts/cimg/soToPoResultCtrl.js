/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('soToPoResultCtrl', function($scope,purchaseOrderList, $state, $timeout, uiGridConstants) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'10%'},
            {field:'pohRevision', displayName:'Rev.',enableCellEdit:false, width:'5%'},
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'35%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
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
       $scope.gridOptions.data = purchaseOrderList;
    };
    $scope.cancel = function() {
        //$scope.$destroy();
        $scope.closeThisDialog('button');
    };
});
