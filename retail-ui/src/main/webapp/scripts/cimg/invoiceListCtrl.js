/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('invoiceListCtrl', function($scope, $state, $timeout, ngDialog, baseDataService, SUCCESS, FAILURE, INVOICE_ALL_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF, TXN_TYPE_INVOICE, TXN_TYPE_REFUND, INVOICE_SEARCH_URI, INVOICE_SEARCH_PAGING_URI, CUSTOMER_ALL_URI) {

    $scope.showRefNo = function(row) {
        if (row.txhdOrigTxnNr == undefined || row.txhdOrigTxnNr == null) {
            return '';
        }
        if (row.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
            return 'S.O: ' + row.txhdOrigTxnNr;
        } else {
            return 'Inv: ' + row.txhdOrigTxnNr;
        }
    }

    var txnNumberTemplate = '<div>{{grid.appScope.showRefNo(row.entity)}}</div>';

    $scope.searchForm = {};
    $scope.searchForm.clientId = -1;
    $scope.includeInvoice = true;
    $scope.includeRefund = true;

    $scope.getPage = function(){
        console.log('get page called');
        $scope.searchForm.txnTypeList = [];
        if ($scope.includeInvoice) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeInvoice.id);
        }
        if ($scope.includeRefund) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeRefund.id);
        }
        $scope.searchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.searchForm.pageSize = paginationOptions.pageSize;
        if ($scope.client != undefined) {
            $scope.searchForm.clientId = $scope.client.id;
        } else {
            $scope.searchForm.clientId = -1;
        }
        baseDataService.addRow($scope.searchForm, INVOICE_SEARCH_PAGING_URI).then(function(response) {
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
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row, false)"></i></a>&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editTransaction(row, true)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>&nbsp;<a href=""><i tooltip="Refund" tooltip-placement="bottom" class="fa fa-backward fa-2x" ng-show="grid.appScope.displayRefundOption(row)" ng-click="grid.appScope.refundTransaction(row)"></i></a>', width:'10%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txhdOrigTxnNr', cellTemplate: txnNumberTemplate,displayName:'Ref No',enableCellEdit:false, width:'10%'},
            {field:'txhdTxnNr', displayName:'Invoice/Refund No',enableCellEdit:false, width:'15%'},
            {field:'invoiceTxnType.displayName', displayName:'Type',enableCellEdit:false, width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'INVOICE') {
                        return 'green';
                    } else if (grid.getCellValue(row, col) === 'REFUND') {
                        return 'red'
                    } else{
                        return 'amber'
                    }
                }
            },
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

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
        baseDataService.getBaseData(TXN_TYPE_INVOICE).then(function(response){
            $scope.txnTypeInvoice = response.data;
            baseDataService.getBaseData(TXN_TYPE_REFUND).then(function(response){
                $scope.txnTypeRefund = response.data;
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
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode)
            {
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
                if (row.entity.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
                    $state.go('dashboard.createSaleTransaction');
                } else {
                    $state.go('dashboard.refundTxn');
                }
            }
        });
    }
    $scope.refundTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        if (row.entity.txivFullyRefunded == 1) {
            baseDataService.displayMessage('info','Warning!!','this invoice had been fully refunded.');
            return;
        }
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
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
        var exportUrl = INVOICE_EXPORT_PDF + row.entity.id;
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
                $scope.searchForm.clientId = $scope.client.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.onCustomerChange = function () {
        //$scope.client = value;
        $scope.searchForm.clientId = $scope.client.id;
    }

    $scope.search = function() {
        $scope.searchForm.txnTypeList = [];
        if ($scope.includeInvoice) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeInvoice.id);
        }
        if ($scope.includeRefund) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeRefund.id);
        }
        //$scope.saleOrderSearchForm.txnTypeList = angular.copy(txnTypeList);
        baseDataService.addRow($scope.searchForm, INVOICE_SEARCH_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

    $scope.displayRefundOption = function(row) {
        if (row.entity.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
            return true;
        }
        return false;
    }

    $scope.clearCustomer = function() {
        $scope.client ={};
        $scope.client.id = -1;
    }


});
