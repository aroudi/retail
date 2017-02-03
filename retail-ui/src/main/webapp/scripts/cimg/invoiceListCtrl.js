/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('invoiceListCtrl', function($scope, $state, $timeout, ngDialog, baseDataService, SUCCESS, FAILURE, INVOICE_ALL_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF, TXN_TYPE_INVOICE, TXN_TYPE_REFUND, INVOICE_SEARCH_URI, INVOICE_SEARCH_PAGING_URI) {

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
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txhdOrigTxnNr', displayName:'Sale Order No',enableCellEdit:false, width:'12.5%'},
            {field:'txhdTxnNr', displayName:'No',enableCellEdit:false, width:'12.5%'},
            {field:'invoiceTxnType.displayName', displayName:'Type',enableCellEdit:false, width:'10%'},
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>&nbsp;<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Refund" tooltip-placement="bottom" class="fa fa-backward fa-2x" ng-show="grid.appScope.displayRefundOption(row)" ng-click="grid.appScope.refundTransaction(row)"></i></a>', width:'10%' }
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
    }

    $scope.editTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (row.entity.invoiceTxnType.categoryCode === 'TXN_TYPE_INVOICE') {
                $state.go('dashboard.createSaleTransaction');
            } else {
                $state.go('dashboard.refundTxn');
            }
        });
    }
    $scope.refundTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
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
