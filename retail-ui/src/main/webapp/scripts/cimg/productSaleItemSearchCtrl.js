/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productSaleItemSearchCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, SUCCESS, FAILURE, PRODUCT_SALE_ITEM_ALL_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'prodSku', enableCellEdit:false, width:'15%',
                cellTooltip: function(row,col) {
                    return row.entity.prodSku
                }
            },
            {field:'reference', enableCellEdit:false, width:'15%',
                cellTooltip: function(row,col) {
                    return row.entity.reference
                }
            },
            {field:'prodName', displayName:'Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.prodName
                }
            },
            {field: 'prodDesc', displayName: 'Description', enableCellEdit: false, width: '20%',
                cellTooltip: function (row, col) {
                    return row.entity.prodDesc
                }
            },
            {field:'prodBrand', displayName:'Brand',enableCellEdit:false, width:'10%'},
            {field:'prodClass', displayName:'Class',enableCellEdit:false, width:'10%'}
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
    getAllProductSaleItems();
    function getAllProductSaleItems() {
        baseDataService.getBaseData(PRODUCT_SALE_ITEM_ALL_URI).then(function(response){
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
