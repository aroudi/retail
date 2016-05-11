/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailListCtrl', function($scope,uiGridConstants, $state, $timeout,baseDataService, SUCCESS, FAILURE,  PRODUCT_GET_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplier.supplierName', displayName:'Supplier', enableCellEdit:false, width:'20%'},
            {field:'product.prodName', displayName:'Product',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.product.prodName
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'5%'},
            {field:'quantity', displayName:'Qty',enableCellEdit:false, width:'10%', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'cost', displayName:'Cost',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'margin', displayName:'Margin',enableCellEdit:false, width:'8%'},
            {field:'sellPrice', displayName:'Price',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'prodIsNew', displayName:'New Item',enableCellEdit:false, width:'8%', cellFilter:'booleanFilter',
                cellClass:
                    function(grid, row, col, rowRenderIndex, colRenderIndex) {
                        if (grid.getCellValue(row, col) === true) {
                            return 'red';
                        } else {
                            return 'green'
                        }
                    }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit Product" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editProduct(row)"></i></a>', width:'5%' }

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
        $scope.billOfQuantity = angular.copy(baseDataService.getRow());
        $scope.gridOptions.data = angular.copy($scope.billOfQuantity.lines);
        baseDataService.setRow({});
    }

    $scope.editProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.product.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createProduct');
        });
    }
});
