/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('invoiceListCtrl', function($q, $scope, $state, $timeout, ngDialog, UserService, baseDataService, SUCCESS, FAILURE, INVOICE_ALL_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF, TXN_TYPE_INVOICE, TXN_TYPE_REFUND, INVOICE_SEARCH_URI, INVOICE_SEARCH_PAGING_URI, CUSTOMER_ALL_URI, DELIVERY_DOCKET_REPRINT_URI) {

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
    $scope.model={};
    //$scope.model.client ={};
    $scope.searchForm.clientId = -1;
    $scope.includeInvoice = true;
    $scope.includeRefund = true;
    $scope.searchForm.imported = false;
    initPageData();


    $scope.getPage = function(){
        $scope.searchForm.txnTypeList = [];
        if ($scope.includeInvoice) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeInvoice.id);
        }
        if ($scope.includeRefund) {
            $scope.searchForm.txnTypeList.push($scope.txnTypeRefund.id);
        }
        $scope.searchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.searchForm.pageSize = paginationOptions.pageSize;
        if ($scope.model.client != undefined) {
            $scope.searchForm.clientId = $scope.model.client.id;
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
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editTransaction(row, true)"></i></a>&nbsp;<a href=""><i tooltip="Reprint" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>&nbsp;<a href=""><i tooltip="Reprint Delivery Docket" tooltip-placement="bottom" class="fa fa-newspaper-o fa-2x" ng-show="grid.appScope.displayRefundOption(row)" ng-click="grid.appScope.reprintDeliveryDocket(row)"></i></a>&nbsp;<a href=""><i tooltip="Refund" tooltip-placement="bottom" class="fa fa-backward fa-2x" ng-show="grid.appScope.displayRefundOption(row)" ng-click="grid.appScope.refundTransaction(row)"></i></a>', width:'15%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'customer.companyName', displayName:'Client', enableCellEdit:false, width:'15%'},
            {field:'txivImported', displayName:'Source', enableCellEdit:false, width:'8%', cellFilter:'invoiceSource',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === true) {
                        return 'amber';
                    } else if (grid.getCellValue(row, col) === false) {
                        return 'green'
                    }
                }
            },
            {field:'txhdOrigTxnNr', cellTemplate: txnNumberTemplate,displayName:'Ref No',enableCellEdit:false, width:'8%'},
            {field:'txhdTxnNr', displayName:'Invoice/Refund No',enableCellEdit:false, width:'15%'},
            {field:'invoiceTxnType', displayName:'Type',enableCellEdit:false, width:'5%', cellFilter:'invoiceTxnTypeFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'7%', cellFilter:'currency'},
            {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'7%', cellFilter:'currency'},
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
    function initPageData() {
        $scope.searchForm = {};
        /*
         * by default we only wants to query the invoices for last month until user change the date
         */
        $scope.searchForm.dateFrom = new Date(
            new Date().getFullYear(),
            new Date().getMonth() - 1,
            new Date().getDate()
        );

        /*
         * promiseList for using to call search after resolving all promises (after retreiving all base data)
         */
        promiseList = [];
        promiseList.push(baseDataService.getBaseData(TXN_TYPE_INVOICE).then(function(response){
            $scope.txnTypeInvoice = response.data;
        }));
        promiseList.push(baseDataService.getBaseData(TXN_TYPE_REFUND).then(function(response){
            $scope.txnTypeRefund = response.data;
        }));
        promiseList.push(baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
            baseDataService.populateCustomerDropdownList($scope.customerSet);

            if ($scope.customerSet.length > 0) {
                var customer = {
                    "id" : -1,
                    "companyName" : "All"
                }
                $scope.customerSet.unshift(customer);
            }
            //$scope.model.client = baseDataService.populateSelectList($scope.model.client,$scope.customerSet);
        }));
        $q.all(promiseList).then(function(results) {
            $scope.getPage();
        })
    }

    $scope.editTransaction = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
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
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.refundTxn');
        });
    }
    $scope.exportToPdf = function(row) {
        var exportUrl = INVOICE_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }
    $scope.reprintDeliveryDocket = function(row) {
        console.log('reprint delivery docket called');
        var exportUrl = DELIVERY_DOCKET_REPRINT_URI + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }
    $scope.searchCustomer = function () {
        ngDialog.openConfirm({
            template:'views/pages/customerSearch.html',
            controller:'customerSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                $scope.model.client = value;
                $scope.searchForm.clientId = $scope.model.client.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.onCustomerChange = function () {
        //$scope.model.client = value;
        $scope.searchForm.clientId = $scope.model.client.id;
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
        $scope.model={};
        $scope.model.client ={};
        $scope.model.client.id = -1;
    }
    $scope.createInvoice = function () {
        //check if customer has access
        if (!UserService.checkUserHasAccess("createInvoice")) {
            baseDataService.displayMessage("info", "Access is denied!!", "You don't have access for creating Invoice");
            return;
        }
        $state.go('dashboard.createInvoice', {txnType:'txnInvoice', blankPage:'true'});

    }


});
