/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, PRODUCT_ALL_URI, PRODUCT_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'prodSku', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.prodSku
                }
            },
            /*
            {field:'reference', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.reference
                }
            },
            */
            {field:'prodName', displayName:'Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.prodName
                }
            },
            {field:'prodLocation', displayName:'Stock Location',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.prodLocation
                }
            },
            {field:'stockQty', displayName:'Available Qty',enableCellEdit:false, width:'10%'},
            {field:'reservedQty', displayName:'Reserved Qty',enableCellEdit:false, width:'10%'},
            {field:'sellPrice.prcePrice', displayName:'Cost',enableCellEdit:false, cellFilter: 'currency', width:'10%'},
            {field:'prodOrguLink.status.displayName', displayName:'Status',enableCellEdit:false, width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'IMPORTED') {
                        return 'amber';
                    } else if (grid.getCellValue(row, col) === 'LIVE') {
                        return 'green'
                    }
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editProduct(row)"></i></a>', width:'5%' }
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
    getAllProducts();
    function getAllProducts() {
        baseDataService.getBaseData(PRODUCT_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createProduct');
        });
    }
});
