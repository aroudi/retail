/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productListCtrl', function($scope, $state, $timeout,baseDataService,uiGridConstants, SUCCESS, FAILURE, PRODUCT_ALL_URI, PRODUCT_GET_URI, PRODUCT_ALL_PAGING_URI) {
    var paginationOptions = {
        pageNumber:1,
        pageSize:25,
        sort:null
    };

    $scope.gridOptions = {
        paginationPageSizes : [25,50,75,100],
        paginationPageSize:25,
        useExternalPagination: true,
        useExternalSorting:true,
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
            {field:'prodName', displayName:'Article Code',enableCellEdit:false, width:'30%',
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
            //{field:'sellPrice.prcePrice', displayName:'Cost',enableCellEdit:false, cellFilter: 'currency', width:'10%'},
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
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            if (sortColumns.length == 0) {
                paginationOptions.sort = null;
            } else {
                paginationOptions.sort = sortColumns[0].sort.direction;
            }
            getPage();
        });
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            getPage();
        });
    };
    //getAllProducts();
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

    var getPage = function(){
        var pageNo = paginationOptions.pageNumber*1 ;
        var pagingUrl = PRODUCT_ALL_PAGING_URI + pageNo + '/' + paginationOptions.pageSize;
        console.log('callback with pageNo =' + pageNo + ' paginationOptions.pageSize ='+ paginationOptions.pageSize);
        baseDataService.getBaseData(pagingUrl).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.totalItems = 26892;
            $scope.gridOptions.data = data;
        });
    }
    getPage();
});
