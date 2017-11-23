/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('deliveryNoteListCtrl', function($scope, $state, uiGridConstants, ngDialog, purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, DEL_NOTE_GET_ALL_URI, DEL_NOTE_GET_URI, POH_GET_URI, DEL_NOTE_SEARCH_URI, DEL_NOTE_SEARCH_PAGING_URI, SUPPLIER_ALL_URI, TAXLEGVARIANCE_ALL_URI) {

    $scope.model= {};
    //$scope.model.supplier= {};
    $scope.searchForm = {};
    $scope.searchForm.supplierId = -1;

    $scope.getPage = function(){
        $scope.searchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.searchForm.pageSize = paginationOptions.pageSize;
        if ($scope.model.supplier != undefined) {
            $scope.searchForm.supplierId = $scope.model.supplier.id;
        } else {
            $scope.searchForm.supplierId = -1;
        }
        baseDataService.addRow($scope.searchForm, DEL_NOTE_SEARCH_PAGING_URI).then(function(response) {
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
        enableSelectAll:true,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.openDeliveryNoteDetail(row, false)"></i></a>&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.openDeliveryNoteDetail(row, true)"></i></a>', width:'5%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohId', visible:false, enableCellEdit:false},
            {field:'delnNoteNumber', displayName:'Note No',enableCellEdit:false, width:'10%'},
            {field:'delnGrn', displayName:'GRN',enableCellEdit:false, width:'10%'},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'10%',
                cellTemplate:'<a href="" ng-click="grid.appScope.viewPurchaseOrderDetail(row)">{{grid.appScope.getLinkedPurchaseOrder(row)}}</a>'
            },
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'delnDeliveryDate', displayName:'Delivery Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'delnValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'delnValueNett', displayName:'Net Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'delnStatus', displayName:'status',enableCellEdit:false, width:'8%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
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

    initPageData();
    function initPageData() {
        $scope.searchForm = {};
        $scope.getPage();
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
    }

    $scope.openDeliveryNoteDetail = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var delnGetURI = DEL_NOTE_GET_URI +  row.entity.id;
        baseDataService.getBaseData(delnGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/deliveryNote.html',
                    controller:'deliveryNoteCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true},
                        taxCodeSet: function(baseDataService, TAXLEGVARIANCE_ALL_URI){
                            return baseDataService.getBaseData(TAXLEGVARIANCE_ALL_URI);
                        }
                    }
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                $state.go('dashboard.deliveryNote');
            }
        });
    }

    $scope.viewPurchaseOrderDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.pohId;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            //$state.go('dashboard.purchaseOrderDetail');
            ngDialog.openConfirm({
                template:'views/pages/purchaseOrderDetail.html',
                controller:'purchaseOrderDetailCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true},
                    taxCodeSet: function(baseDataService, TAXLEGVARIANCE_ALL_URI){
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

    $scope.getLinkedPurchaseOrder = function (row) {
        if (row.entity.pohOrderNumber ==undefined || row.entity.pohOrderNumber ==null ) {
            return '***';
        }
        return row.entity.pohOrderNumber;
    };
    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.model.supplier = value;
                $scope.searchForm.supplierId = $scope.model.supplier.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.search = function() {
        baseDataService.addRow($scope.searchForm, DEL_NOTE_SEARCH_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

});
