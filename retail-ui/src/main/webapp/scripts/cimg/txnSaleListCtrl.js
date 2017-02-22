/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('txnSaleListCtrl', function($scope, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE, TXN_ALL_URI, TXN_GET_URI, TXN_EXPORT_PDF, TXN_TYPE_QUOTE, TXN_TYPE_SALE, TXN_SEARCH_URI, QUOTE_DELETE_URI, TXN_SEARCH_PAGING_URI, CUSTOMER_ALL_URI) {

    $scope.saleOrderSearchForm = {};
    $scope.saleOrderSearchForm.clientId = -1;
    $scope.includeSaleOrder = true;
    $scope.includeQuote = true;

    $scope.getPage = function(){
        $scope.saleOrderSearchForm.txnTypeList = [];
        if ($scope.includeSaleOrder) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeSale.id);
        }
        if ($scope.includeQuote) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeQuote.id);
        }
        $scope.saleOrderSearchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.saleOrderSearchForm.pageSize = paginationOptions.pageSize;
        if ($scope.client != undefined) {
            $scope.saleOrderSearchForm.clientId = $scope.client.id;
        } else {
            $scope.saleOrderSearchForm.clientId = -1;
        }
        baseDataService.addRow($scope.saleOrderSearchForm, TXN_SEARCH_PAGING_URI).then(function(response) {
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
        columnDefs: [
            {name:'Action', enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row,false)"></i></a>&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editTransaction(row,true)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)" ></i></a> </i></a>&nbsp;<a href=""><i tooltip="Delete" ng-show="row.entity.txhdTxnType.categoryCode ===\'TXN_TYPE_QUOTE\'" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeQuote(row)" ></i></a> ', width:'10%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'20%'},
            {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'10%'},
            {field:'txhdState', displayName:'State', enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'txhdTxnType.displayName' , displayName:'Type', enableCellEdit:false, width:'10%'},
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'}
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            $scope.getPage();
        });
    };
    initPageData();
    function initPageData() {
        $scope.saleOrderSearchForm = {};
        baseDataService.getBaseData(TXN_TYPE_SALE).then(function(response){
            $scope.txnTypeSale = response.data;
            baseDataService.getBaseData(TXN_TYPE_QUOTE).then(function(response){
                $scope.txnTypeQuote = response.data;
                $scope.getPage();
            });
        });
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
            $scope.client = baseDataService.populateSelectList($scope.customer,$scope.customerSet);
            if ($scope.customerSet.length > 0) {
                var customer = {
                    "id" : -1,
                    "companyName" : "All"
                }
                $scope.customerSet.unshift(customer);
            }
        });
    }

    $scope.editTransaction = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/txnSale.html',
                    controller:'txnSaleCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true}}
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                $state.go('dashboard.createSaleTransaction');
            }
        });
    }

    $scope.refundTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            var txnDetailList = response.data.txnDetailFormList;
            for (var i = 0; i < txnDetailList.length; i++) {
                txnDetailList[i].txdeQtyBalance = txnDetailList[i].txdeQtyTotalInvoiced*1 - txnDetailList[i].txdeQtyTotalRefund*1;
            }
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.refundTxn');
        });
    }

    $scope.exportToPdf = function(row) {

        var exportUrl = TXN_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }

    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                $scope.client = value;
                $scope.saleOrderSearchForm.clientId = $scope.client.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.onCustomerChange = function () {
        $scope.saleOrderSearchForm.clientId = $scope.client.id;
    }

    $scope.search = function() {
        $scope.saleOrderSearchForm.txnTypeList = [];
        if ($scope.includeSaleOrder) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeSale.id);
        }
        if ($scope.includeQuote) {
            $scope.saleOrderSearchForm.txnTypeList.push($scope.txnTypeQuote.id);
        }
        //$scope.saleOrderSearchForm.txnTypeList = angular.copy(txnTypeList);
        baseDataService.addRow($scope.saleOrderSearchForm, TXN_SEARCH_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

    $scope.removeQuote = function(row) {

        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete quote??').then(function(result){
            if (result) {
                var deleteUrl = QUOTE_DELETE_URI + row.entity.id;
                baseDataService.getBaseData(deleteUrl).then(function(response){
                    var deleteResponse = response.data;
                    if (deleteResponse.status === SUCCESS) {
                        $scope.gridApi.core.setRowInvisible(row);
                    } else {
                        baseDataService.displayMessage('info','Error!!','Error in deleting quote.');
                    }
                });
            } else {
                return;
            }
        });
    }
    $scope.clearCustomer = function() {
        $scope.client ={};
        $scope.client.id = -1;
    }

});
