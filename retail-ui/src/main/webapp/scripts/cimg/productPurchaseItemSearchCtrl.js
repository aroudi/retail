/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productPurchaseItemSearchCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, supplier, SUCCESS, FAILURE, GET_PURCHASE_ITEMS_PER_SUPPLIER_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'solId', visible:false, enableCellEdit:false},
            {field:'prodId', visible:false, enableCellEdit:false},
            {field:'catalogueNo', enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.catalogueNo
                }
            },
            {field:'partNo', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.partNo
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size',enableCellEdit:false, width:'10%'},
            {field:'price', displayName:'price',enableCellEdit:false, width:'10%'},
            {field:'bulkPrice', displayName:'bulkPrice',enableCellEdit:false, width:'10%'},
            {field:'bulkQty', displayName:'bulkQty',enableCellEdit:false, width:'10%'},
            {field:'sprcMinOrdQty', displayName:'Min Order',enableCellEdit:false, width:'10%'}
        ]
    }
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
            gridApi.grid.clearAllFilters();
        });
    };
    getAllProductPurchaseItems();
    function getAllProductPurchaseItems() {
        baseDataService.getBaseData(GET_PURCHASE_ITEMS_PER_SUPPLIER_URI+supplier.id).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.gridApi.selection.getSelectedRows());
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
