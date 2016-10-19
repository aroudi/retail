/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('invoiceListCtrl', function($scope, $state, $timeout, ngDialog, baseDataService, SUCCESS, FAILURE, INVOICE_ALL_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF, TXN_TYPE_INVOICE, TXN_TYPE_REFUND, INVOICE_SEARCH_URI) {

    $scope.searchForm = {};
    $scope.searchForm.clientId = -1;
    $scope.includeInvoice = true;
    $scope.includeRefund = true;
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txhdOrigTxnNr', displayName:'Sale Order No',enableCellEdit:false, width:'12.5%'},
            {field:'txhdTxnNr', displayName:'No',enableCellEdit:false, width:'12.5%'},
            {field:'invoiceTxnType.displayName', displayName:'Type',enableCellEdit:false, width:'10%'},
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-backward fa-2x" ng-click="grid.appScope.refundTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'10%' }
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
    getAllInvoice();
    function getAllInvoice() {

        baseDataService.getBaseData(TXN_TYPE_INVOICE).then(function(response){
            $scope.txnTypeInvoice = response.data;
        });
        baseDataService.getBaseData(TXN_TYPE_REFUND).then(function(response){
            $scope.txnTypeRefund = response.data;
        });

        baseDataService.getBaseData(INVOICE_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
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
            $state.go('dashboard.createSaleTransaction');
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
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
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



});
