/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productListCtrl', function($scope, $state, $timeout,ngDialog,baseDataService,uiGridConstants, SUCCESS, FAILURE, PRODUCT_ALL_URI, PRODUCT_GET_URI, PRODUCT_ALL_PAGING_URI, PRODUCT_SEARCH_PAGING_URI, SUPPLIER_ALL_URI, PRODUCT_TYPE_URI) {

    $scope.getPage = function(){
        $scope.productSearchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.productSearchForm.pageSize = paginationOptions.pageSize;
        if ($scope.supplier != undefined) {
            $scope.productSearchForm.supplierId = $scope.supplier.id;
        } else {
            $scope.productSearchForm.supplierId = -1;
        }
        if ($scope.productType != undefined) {
            $scope.productSearchForm.prodTypeId = $scope.productType.id;
        } else {
            $scope.productSearchForm.prodTypeId = -1;
        }
        baseDataService.addRow($scope.productSearchForm, PRODUCT_SEARCH_PAGING_URI).then(function(response) {
            var result = angular.copy(response.data);
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    }

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
            {name:'Action', enableFiltering:false,cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editProduct(row)"></i></a><a href="">&nbsp;&nbsp;<i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.viewProduct(row)"></i></a>', width:'5%' },
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
            }
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
            $scope.getPage();
        });
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            $scope.getPage();
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

    $scope.viewProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            //$state.go('dashboard.createProduct');
            ngDialog.openConfirm({
                template:'views/pages/product.html',
                controller:'productCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true}}
            }).then (function (){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
        });
    }

    //getPage();
    initPageData();
    function initPageData() {
        $scope.productSearchForm = {};
        baseDataService.getBaseData(PRODUCT_TYPE_URI).then(function(response){
            $scope.productTypeSet = response.data;
            if ($scope.productTypeSet.length > 0) {
                var prodType = {
                    "id" : -1,
                    "displayName" : "All",
                    "description" : "All"
                }
                $scope.productTypeSet.unshift(prodType);
            }
            //$scope.productForm.prodType = baseDataService.populateSelectList($scope.productForm.prodType,$scope.productTypeSet);
        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            if ($scope.supplierSet.length > 0) {
                var supplier = {
                   "id" : -1,
                    "supplierName" : "All"
                }
                $scope.supplierSet.unshift(supplier);
            }
        });
        var pageNo = paginationOptions.pageNumber*1 ;
        var pagingUrl = PRODUCT_ALL_PAGING_URI + '/' + pageNo + '/' + paginationOptions.pageSize;
        //console.log('callback with pageNo =' + pageNo + ' paginationOptions.pageSize ='+ paginationOptions.pageSize);
        baseDataService.getBaseData(pagingUrl).then(function(response){
            var result = angular.copy(response.data);
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    }

});
