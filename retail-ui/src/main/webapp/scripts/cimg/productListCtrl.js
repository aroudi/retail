/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productListCtrl', function($scope, $state, $timeout,ngDialog, UserService, baseDataService,uiGridConstants, SUCCESS, FAILURE, PRODUCT_ALL_URI, PRODUCT_GET_URI, PRODUCT_ALL_PAGING_URI, PRODUCT_SEARCH_PAGING_URI, SUPPLIER_ALL_URI, PRODUCT_TYPE_URI, TAXLEGVARIANCE_ALL_URI, PRODUCT_RESERVATION_INFO_URI, PRODUCT_LOGICAL_DELETE_URI, PRODUCT_STATUS_URI, PRODUCT_FINALISE_URI) {

    $scope.getPage = function(){
        $scope.productSearchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.productSearchForm.pageSize = paginationOptions.pageSize;
        if ($scope.model.supplier != undefined) {
            $scope.productSearchForm.supplierId = $scope.model.supplier.id;
        } else {
            $scope.productSearchForm.supplierId = -1;
        }
        if ($scope.productType != undefined) {
            $scope.productSearchForm.prodTypeId = $scope.productType.id;
        } else {
            $scope.productSearchForm.prodTypeId = -1;
        }
        if ($scope.productStatus != undefined) {
            $scope.productSearchForm.prodStatusId = $scope.productStatus.id;
        } else {
            $scope.productSearchForm.prodStatusId = -1;
        }
        baseDataService.addRow($scope.productSearchForm, PRODUCT_SEARCH_PAGING_URI).then(function(response) {
            var result = angular.copy(response.data);
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    };

    var paginationOptions = {
        pageNumber:1,
        pageSize:200,
        sort:null
    };

    $scope.gridOptions = {
        paginationPageSizes : [200,250,300,350],
        paginationPageSize:200,
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
            {field:'prodName', displayName:'Description',enableCellEdit:false, width:'25%',
                cellTooltip: function(row,col) {
                    return row.entity.prodName
                }
            },
            //{field:'department', displayName:'Dept',enableCellEdit:false, width:'10%'},
            //{field:'category1', displayName:'Cat1',enableCellEdit:false, width:'10%'},
            //{field:'category2', displayName:'Cat2',enableCellEdit:false, width:'10%'},
            //{field:'category3', displayName:'Cat3',enableCellEdit:false, width:'10%'},
            {field:'stockQty', displayName:'In Stock',enableCellEdit:false, width:'5%'},
            {field:'prodCost', displayName:'Cost',enableCellEdit:false, width:'7%', cellFilter:'currency'},
            {field:'rrp', displayName:'RRP',enableCellEdit:false, width:'8%', cellFilter:'currency'},
            {field:'defaultPrice', displayName:'Grade Def',enableCellEdit:false, width:'7%', cellFilter:'currency'},
            {field:'gradeAPrice', displayName:'Grade A',enableCellEdit:false, width:'7%',cellFilter:'currency'},
            {field:'gradeBPrice', displayName:'Grade B',enableCellEdit:false, width:'7%',cellFilter:'currency'},
            {field:'gradeCPrice', displayName:'Grade C',enableCellEdit:false, width:'7%',cellFilter:'currency'},
            {field:'gradeDPrice', displayName:'Grade D',enableCellEdit:false, width:'7%',cellFilter:'currency'}
        ]
    };
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    $scope.gridOptions.noUnselect= false;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
        /*
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            if (sortColumns.length == 0) {
                paginationOptions.sort = null;
            } else {
                paginationOptions.sort = sortColumns[0].sort.direction;
            }
            $scope.getPage();
        });
        */
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
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createProduct');
        });
    };

    $scope.viewProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            //$state.go('dashboard.createProduct');
            ngDialog.openConfirm({
                template:'views/pages/product.html',
                controller:'productCtrl2',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true},
                    taxCodeSet: function(baseDataService, TAXLEGVARIANCE_ALL_URI){
                    console.log('TAXLEGVARIANCE_ALL_URI = ' + TAXLEGVARIANCE_ALL_URI);
                    return baseDataService.getBaseData(TAXLEGVARIANCE_ALL_URI);
                }
                }
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
        $scope.model = {};
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
        baseDataService.getBaseData(PRODUCT_STATUS_URI).then(function(response){
            $scope.productStatusSet = response.data;
            if ($scope.productStatusSet.length > 0) {
                var prodStatus = {
                    "id" : -1,
                    "displayName" : "All",
                    "description" : "All"
                }
                $scope.productStatusSet.unshift(prodStatus);
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
            //$scope.model.supplier = baseDataService.populateSelectList($scope.model.supplier,$scope.supplierSet);
        });
        var pageNo = paginationOptions.pageNumber*1 ;
        var pagingUrl = PRODUCT_ALL_PAGING_URI + '/' + pageNo + '/' + paginationOptions.pageSize;
        //console.log('callback with pageNo =' + pageNo + ' paginationOptions.pageSize ='+ paginationOptions.pageSize);
        baseDataService.getBaseData(pagingUrl).then(function(response){
            var result = response.data;
            $scope.gridOptions.totalItems = result.totalRecords;
            $scope.gridOptions.data = result.result;
        });
    }
    $scope.displayReservationInfo = function (product) {
        if (product == undefined) {
            return;
        }
        //check if this supplier has outstanding purchase order.
        ngDialog.openConfirm({
            template:'views/pages/generalPopUpList.html',
            controller:'productReservationInfoCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return PRODUCT_RESERVATION_INFO_URI + product.id}}
        }).then (function (selectedItem){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.searchByEnter = function(keyEvent) {
        if (keyEvent.which != 13) {
            return
        }
        $scope.getPage();
    }

    $scope.isRowSelected = function(){
        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
            return true;
        }
        return false;
    }

    $scope.deleteProductLogically = function () {

        //check if customer has access
        if (!UserService.checkUserHasAccess("deleteProduct")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have delete access");
            return;
        }

        var selectedProductIdList = [];
        if ($scope.gridApi.selection.getSelectedRows() === undefined || $scope.gridApi.selection.getSelectedRows().length < 1){
            baseDataService.displayMessage('info', 'rows not selected', 'please select item/s to delete');
            return;
        }
        baseDataService.displayMessage('yesNo','Confirmation required!!','Do you want to delete selected rows?').then(function(result){
            if (result) {
                for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                    selectedProductIdList.push($scope.gridApi.selection.getSelectedRows()[i].id);
                }
                baseDataService.addRow(selectedProductIdList, PRODUCT_LOGICAL_DELETE_URI).then(function(response) {
                    var res = response.data;
                    if (res.status != SUCCESS ) {
                        baseDataService.displayMessage('info', 'warning', 'Not able to delete product. message:' + res.message);
                    } else {
                        for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                            var itemIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, $scope.gridApi.selection.getSelectedRows()[i]);
                            console.log('index= ' + itemIndex);
                            if (itemIndex > -1) {
                                $scope.gridOptions.data.splice(itemIndex, 1);
                            }
                            $scope.gridApi.selection.unSelectRow($scope.gridApi.selection.getSelectedRows()[i]);
                            //$scope.gridApi.core.setRowInvisible($scope.gridApi.selection.getSelectedRows()[i]);
                        }
                    }
                });
            } else {
                return;
            }
        });
    }

    $scope.finaliseProduct = function () {


        var selectedProductIdList = [];
        if ($scope.gridApi.selection.getSelectedRows() === undefined || $scope.gridApi.selection.getSelectedRows().length < 1){
            baseDataService.displayMessage('info', 'rows not selected', 'please select item/s ');
            return;
        }
        baseDataService.displayMessage('yesNo','Confirmation required!!','Do you want to finalise selected products?').then(function(result){
            if (result) {
                for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                    selectedProductIdList.push($scope.gridApi.selection.getSelectedRows()[i].id);
                }
                baseDataService.addRow(selectedProductIdList, PRODUCT_FINALISE_URI).then(function(response) {
                    var res = response.data;
                    if (res.status != SUCCESS ) {
                        baseDataService.displayMessage('info', 'warning', 'Not able to finalise product/s. message:' + res.message);
                    } else {
                        baseDataService.displayMessage('info', 'info', 'products status changed to finalised');
                        /*
                        for (var i = 0; i < $scope.gridApi.selection.getSelectedRows().length; i++) {
                            var itemIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, $scope.gridApi.selection.getSelectedRows()[i]);
                            console.log('index= ' + itemIndex);
                            if (itemIndex > -1) {
                                $scope.gridOptions.data.splice(itemIndex, 1);
                            }
                            $scope.gridApi.selection.unSelectRow($scope.gridApi.selection.getSelectedRows()[i]);
                            //$scope.gridApi.core.setRowInvisible($scope.gridApi.selection.getSelectedRows()[i]);
                        }
                        */
                    }
                });
            } else {
                return;
            }
        });
    }


    $scope.createProduct = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("createProduct")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have access for creating product");
            return;
        }
        $state.go('dashboard.createProduct', {blankPage:true});

    }

});
