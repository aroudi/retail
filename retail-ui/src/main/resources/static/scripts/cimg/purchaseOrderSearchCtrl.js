/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderSearchCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, searchUrl, SUCCESS, FAILURE, POH_GET_ALL_CONFIRMED_PER_SUPPLIER_URI, POH_GET_URI) {
    $scope.title = "Purchase Order List";
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        enableColumnResizing: true,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'15%'},
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency'},
            {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency'},
            {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
        });
    };
    getAllConfirmedPurchaseOrders();
    function getAllConfirmedPurchaseOrders() {
        baseDataService.getBaseData(searchUrl).then(function(response){
            if (response.data === undefined || response.data === null || response.data.length < 1) {
                $scope.confirm(undefined);
            } else {
                $scope.gridOptions.data = response.data;
            }
        });
    }

    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            var pohGetURI = POH_GET_URI +  $scope.selectedOption.id;
            baseDataService.getBaseData(pohGetURI).then(function(response){
                $scope.confirm(response.data);
            });
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
